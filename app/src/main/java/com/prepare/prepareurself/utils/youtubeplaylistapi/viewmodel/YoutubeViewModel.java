package com.prepare.prepareurself.utils.youtubeplaylistapi.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.prepare.prepareurself.resources.data.db.repository.ResourcesDbRepository;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.utils.youtubeplaylistapi.db.PlaylistVideosDbRepository;
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.VideoItemWrapper;

public class YoutubeViewModel extends AndroidViewModel {

    private PlaylistVideosDbRepository playlistVideosDbRepository;
    private ResourcesDbRepository resourcesDbRepository;

    public YoutubeViewModel(@NonNull Application application) {
        super(application);
        playlistVideosDbRepository = new PlaylistVideosDbRepository(application);
        resourcesDbRepository = new ResourcesDbRepository(application);
    }

    public LiveData<VideoItemWrapper> getVideoItemWrapperLiveData(String videoCode, String playlistId){
        return playlistVideosDbRepository.getVideoItemWrapperByVideoIdAndPlaylistId(videoCode, playlistId);
    }

    public LiveData<ResourceModel> getResourceByResourceId(int resourceId, String type){
        return resourcesDbRepository.getResourceByResourceId(resourceId,type);
    }

}
