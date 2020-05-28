package com.prepare.prepareurself.courses.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.courses.data.db.repository.ProjectsDbRepository;
import com.prepare.prepareurself.courses.data.model.ProjectResponse;
import com.prepare.prepareurself.courses.data.model.ProjectResponseModel;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.repository.ProjectsRespository;
import com.prepare.prepareurself.dashboard.data.db.repository.CourseDbRepository;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.favourites.data.db.repository.LikedProjectRepository;
import com.prepare.prepareurself.favourites.data.model.LikedProjectsModel;
import com.prepare.prepareurself.resources.data.model.ResourceLikesResponse;
import com.prepare.prepareurself.resources.data.model.ResourceViewsResponse;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.db.PlaylistVideosDbRepository;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.db.SingleVideoItemWrapperRespository;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.SingleVIdeoItemWrapper;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.VideoItemWrapper;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.YoutubePlaylistResponseModel;

import java.util.List;

public class ProjectsViewModel extends AndroidViewModel {

    private ProjectsRespository respository;
    private ProjectsDbRepository dbRepository;
    private PlaylistVideosDbRepository playlistVideosDbRepository;
    private SingleVideoItemWrapperRespository singleVideoItemWrapperRespository;
    private CourseDbRepository courseDbRepository;
    private LikedProjectRepository likedProjectRepository;

    private LiveData<SingleVIdeoItemWrapper> singleVIdeoItemWrapperLiveData = new MutableLiveData<>();
    private LiveData<ProjectResponse> projectResponseMutableLiveData = new MutableLiveData<>();
    private LiveData<List<ProjectsModel>> listLiveData = new MutableLiveData<>();
    private LiveData<YoutubePlaylistResponseModel> youtubePlaylistResponseModelLiveData = new MutableLiveData<>();
    private LiveData<List<VideoItemWrapper>> videoContentsLiveData = new MutableLiveData<>();

    public ProjectsViewModel(@NonNull Application application) {
        super(application);
        respository = new ProjectsRespository(application);
        dbRepository = new ProjectsDbRepository(application);
        playlistVideosDbRepository = new PlaylistVideosDbRepository(application);
        singleVideoItemWrapperRespository = new SingleVideoItemWrapperRespository(application);
        courseDbRepository = new CourseDbRepository(application);
        likedProjectRepository = new LikedProjectRepository(application);
    }

    public LiveData<CourseModel> getCourseById(int id){
        return courseDbRepository.getCourseById(id);
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

    public LiveData<List<ProjectsModel>> getProjectByCourseId(int courseId){
        return dbRepository.getProjectsByCourseId(courseId);
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

    public LiveData<SingleVIdeoItemWrapper> fetchVideoDetails(String videoCode) {
       singleVIdeoItemWrapperLiveData =  respository.getVideosDetails(videoCode);
       if (singleVIdeoItemWrapperLiveData.getValue() == null){
           singleVIdeoItemWrapperLiveData = singleVideoItemWrapperRespository.getVideoContentDetails(videoCode);
       }

       return singleVIdeoItemWrapperLiveData;
    }

    public LiveData<ResourceLikesResponse> likeProject(String token, int id, int liked) {
        return respository.likeProject(token,id,liked);
    }

    public LiveData<ResourceViewsResponse> viewProject(String token, int id) {
        return respository.viewProject(token,id);
    }

    public void getProjectByIdFromRemote(String token,int projectId) {
        respository.getProjectById(token, projectId);
    }

    public void saveProject(ProjectsModel project) {
        dbRepository.insertProject(project);
    }

    public void insertLikedProject(LikedProjectsModel likedProjectsModel){
        likedProjectRepository.insertProject(likedProjectsModel);
    }

    public void deleteLikedProject(LikedProjectsModel likedProjectsModel){
        likedProjectRepository.deleteLikedProject(likedProjectsModel);
    }
}
