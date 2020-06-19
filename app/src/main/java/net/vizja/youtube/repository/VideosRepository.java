package net.vizja.youtube.repository;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import net.vizja.youtube.model.Video;
import net.vizja.youtube.model.VideosResponse;
import net.vizja.youtube.network.YoutubeApi;
import net.vizja.youtube.network.responses.SearchItemResponse;
import net.vizja.youtube.network.responses.SearchResponse;
import net.vizja.youtube.network.responses.VideoDetailResponse;
import net.vizja.youtube.utils.Common;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VideosRepository {
    private static VideosRepository repository = new VideosRepository();
    private MutableLiveData<VideosResponse> videosLiveData;

    public static VideosRepository getInstance() {
        return repository;
    }

    private VideosRepository() {

    }


    public LiveData<VideosResponse> loadVideos(String search, String pageToken) {
        videosLiveData = new MutableLiveData<>();
        YoutubeApi.getService()
                .search(search, pageToken, Common.API_KEY)
                .enqueue(new Callback<SearchResponse>() {
                    @Override
                    public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {

                        if (response.isSuccessful()) {
                            SearchResponse searchResponse = response.body();
                            List<SearchItemResponse> itemResponses = searchResponse.getItems();
                            getVideoDetails(itemResponses, searchResponse.getNextPageToken());


                        } else
                            videosLiveData.postValue(new VideosResponse(response.code()));
                    }

                    @Override
                    public void onFailure(Call<SearchResponse> call, Throwable t) {
                        videosLiveData.postValue(new VideosResponse(t));
                    }
                });

        return videosLiveData;
    }

    private void getVideoDetails(List<SearchItemResponse> itemResponses, String nextPageToken) {
        List<String> idList = new ArrayList<>();
        for (SearchItemResponse item : itemResponses)
            idList.add(item.getId().getVideoId());

        List<Video> videoList = new ArrayList<>();
        YoutubeApi.getService().getVideoDetail(TextUtils.join(",", idList), Common.API_KEY)
                .enqueue(new Callback<VideoDetailResponse>() {
                    @Override
                    public void onResponse(Call<VideoDetailResponse> call, Response<VideoDetailResponse> response) {
                        if (response.isSuccessful()) {
                            VideoDetailResponse videoDetailResponse = response.body();
                            for (VideoDetailResponse.Item item : videoDetailResponse.getItems()) {

                                Video video = new Video(item.getId(),
                                        item.getSnippet().getTitle(),
                                        item.getSnippet().getChannelTitle(),
                                        item.getSnippet().getPublishedAt(),
                                        item.getStatistics().getViewCount(),
                                        item.getSnippet().getThumbnails().getMedium().getUrl());
                                videoList.add(video);

                            }
                            videosLiveData.postValue(new VideosResponse(videoList, nextPageToken));


                        } else
                            videosLiveData.postValue(new VideosResponse(response.code()));

                    }

                    @Override
                    public void onFailure(Call<VideoDetailResponse> call, Throwable t) {
                        videosLiveData.postValue(new VideosResponse(t));
                    }
                });


    }

}
