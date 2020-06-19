package net.vizja.youtube.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import net.vizja.youtube.model.VideosResponse;
import net.vizja.youtube.repository.VideosRepository;

public class VideosViewModel extends AndroidViewModel {

    public VideosViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<VideosResponse>loadVideos(String search, String pageToken){
        return VideosRepository
                .getInstance()
                .loadVideos(search,pageToken);
    }
}
