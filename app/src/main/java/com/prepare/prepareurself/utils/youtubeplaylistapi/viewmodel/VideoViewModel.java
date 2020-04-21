package com.prepare.prepareurself.utils.youtubeplaylistapi.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.utils.youtubeplaylistapi.db.PlaylistVideosDbRepository;
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.VideoItemWrapper;

import java.util.List;

public class VideoViewModel extends AndroidViewModel {

    private PlaylistVideosDbRepository playlistVideosDbRepository;

    private LiveData<List<VideoItemWrapper>> listLiveData = new MutableLiveData<>();

    public VideoViewModel(@NonNull Application application) {
        super(application);
        playlistVideosDbRepository = new PlaylistVideosDbRepository(application);
    }

    public LiveData<List<VideoItemWrapper>> getListLiveData(String playlistId) {

        listLiveData = playlistVideosDbRepository.getVideoItemWrapperByPlaylistId(playlistId);

        return listLiveData;
    }
}
