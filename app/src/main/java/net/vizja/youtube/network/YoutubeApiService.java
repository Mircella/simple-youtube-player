package net.vizja.youtube.network;

import net.vizja.youtube.network.responses.SearchResponse;
import net.vizja.youtube.network.responses.VideoDetailResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeApiService {
    @GET("search?part=snippet&maxResults=10")
    Call<SearchResponse> search(@Query("q") String s,
                                @Query("pageToken") String nextPageToken,
                                @Query("key") String apiKey);

    @GET("videos?part=contentDetails%2Cstatistics%2Csnippet")
    Call<VideoDetailResponse> getVideoDetail(@Query(value = "id", encoded = true) String idList,
                                             @Query("key") String apiKey);

}
