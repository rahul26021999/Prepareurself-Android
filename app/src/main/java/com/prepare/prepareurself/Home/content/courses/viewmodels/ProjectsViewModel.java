package com.prepare.prepareurself.Home.content.courses.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Home.content.courses.data.db.repository.ProjectsDbRepository;
import com.prepare.prepareurself.Home.content.courses.data.model.ProjectResponse;
import com.prepare.prepareurself.Home.content.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.Home.content.courses.data.repository.ProjectsRespository;
import com.prepare.prepareurself.utils.youtubeplaylistapi.db.PlaylistVideosDbRepository;
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.VideoContentDetails;
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.VideoItemWrapper;
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.YoutubePlaylistResponseModel;

import java.util.List;

public class ProjectsViewModel extends AndroidViewModel {

    private ProjectsRespository respository;
    private ProjectsDbRepository dbRepository;
    private PlaylistVideosDbRepository playlistVideosDbRepository;

    private LiveData<ProjectResponse> projectResponseMutableLiveData = new MutableLiveData<>();
    private LiveData<List<ProjectsModel>> listLiveData = new MutableLiveData<>();
    private LiveData<YoutubePlaylistResponseModel> youtubePlaylistResponseModelLiveData = new MutableLiveData<>();
    private LiveData<List<VideoItemWrapper>> videoContentsLiveData = new MutableLiveData<>();

    public ProjectsViewModel(@NonNull Application application) {
        super(application);
        respository = new ProjectsRespository(application);
        dbRepository = new ProjectsDbRepository(application);
        playlistVideosDbRepository = new PlaylistVideosDbRepository(application);
    }

    public void fetchProjects(String token, int courseId, String level, int count, int page){
        projectResponseMutableLiveData = respository.getProjects(token,courseId,level,count,page);
    }

    public LiveData<ProjectResponse> getProjectResponseMutableLiveData() {
        return projectResponseMutableLiveData;
    }

    public LiveData<List<ProjectsModel>> getListLiveData() {

        listLiveData = dbRepository.getAllProjects();
        return listLiveData;
    }

    public LiveData<ProjectsModel> getProjectById(int id){
        return dbRepository.getProjectsById(id);
    }

    public LiveData<YoutubePlaylistResponseModel> fetchVideosFromPlaylist(String pageToken, String playlistId){
        youtubePlaylistResponseModelLiveData =  respository.getVideosFromPlaylist(pageToken,playlistId);
        return youtubePlaylistResponseModelLiveData;
    }

    public LiveData<List<VideoItemWrapper>> getVideoContentsLiveData(String playlistId){
        videoContentsLiveData = playlistVideosDbRepository.getVideoItemWrapperByPlaylistId(playlistId);
        return videoContentsLiveData;
    }

}
