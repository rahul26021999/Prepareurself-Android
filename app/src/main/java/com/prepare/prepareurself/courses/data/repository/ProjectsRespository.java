package com.prepare.prepareurself.courses.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;
import com.prepare.prepareurself.Apiservice.YoutubeApiInterface;
import com.prepare.prepareurself.courses.data.db.repository.ProjectsDbRepository;
import com.prepare.prepareurself.courses.data.model.GetProjectResponse;
import com.prepare.prepareurself.courses.data.model.ProjectResponse;
import com.prepare.prepareurself.courses.data.model.ProjectResponseModel;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.resources.data.model.ResourceLikesResponse;
import com.prepare.prepareurself.resources.data.model.ResourceViewsResponse;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.db.PlaylistVideosDbRepository;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.db.SingleVideoItemWrapperRespository;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.SingleVIdeoItemWrapper;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.VideoItemWrapper;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.YoutubePlaylistResponseModel;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.YoutubeSingleVideoResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectsRespository {

    private ApiInterface apiInterface;
    private ProjectsDbRepository projectsDbRepository;
    private YoutubeApiInterface youtubeApiInterface;
    private PlaylistVideosDbRepository playlistVideosDbRepository;
    private SingleVideoItemWrapperRespository singleVideoItemWrapperRespository;

    public ProjectsRespository(Application application){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        youtubeApiInterface = ApiClient.getYoutubeApiClient().create(YoutubeApiInterface.class);
        projectsDbRepository = new ProjectsDbRepository(application);
        playlistVideosDbRepository = new PlaylistVideosDbRepository(application);
        singleVideoItemWrapperRespository = new SingleVideoItemWrapperRespository(application);
    }

  //  CAUQAA CAoQAA CA8QAA CBQQAA CBkQAA

    public LiveData<YoutubePlaylistResponseModel> getVideosFromPlaylist(String pageToken, String playlistId){

        final MutableLiveData<YoutubePlaylistResponseModel> data = new MutableLiveData<>();
        youtubeApiInterface.getPlaylist("contentDetails,id,snippet",pageToken,playlistId, Constants.YOUTUBE_PLAYER_API_KEY)
                .enqueue(new Callback<YoutubePlaylistResponseModel>() {
                    @Override
                    public void onResponse(Call<YoutubePlaylistResponseModel> call, Response<YoutubePlaylistResponseModel> response) {
                        YoutubePlaylistResponseModel responseModel = response.body();
                        if (responseModel!=null){
                            if (responseModel.getItems()!=null){
                                for (VideoItemWrapper videoItemWrapper : responseModel.getItems()){
                                    playlistVideosDbRepository.insertVideoContentDetail(videoItemWrapper);
                                }
                            }
                            data.setValue(responseModel);
                        }else{
                            data.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<YoutubePlaylistResponseModel> call, Throwable t) {
                        data.setValue(null);
                        Log.d("youtube_api_debug",t.getLocalizedMessage()+"");
                    }
                });



        return data;
    }

    public LiveData<ProjectResponse> getProjects(String token, int courseId, String level, int count, final int page){

        final MutableLiveData<ProjectResponse> data = new MutableLiveData<>();

        apiInterface.getProjects(token,courseId,count, page).enqueue(new Callback<GetProjectResponse>() {
            @Override
            public void onResponse(Call<GetProjectResponse> call, Response<GetProjectResponse> response) {

                Log.d("response_debug","hjere "+ response);

                GetProjectResponse getProjectResponse =  response.body();

                Log.d("response_debug",getProjectResponse+"");

                if (getProjectResponse!=null){
                    if (getProjectResponse.getError_code() == 0){

                        if (page == 1){
                            projectsDbRepository.deleteAllProjects();
                        }

                        for (ProjectsModel projectsModel : getProjectResponse.getProject().getData()){
                            projectsDbRepository.insertProject(projectsModel);
                        }
                        data.setValue(getProjectResponse.getProject());
                    }else{
                        data.setValue(null);
                    }
                }
            }

            @Override
            public void onFailure(Call<GetProjectResponse> call, Throwable t) {
                Log.d("response_debug",t.getLocalizedMessage()+"");
                data.setValue(null);
            }
        });

        return data;

    }

    public LiveData<SingleVIdeoItemWrapper> getVideosDetails(String videoCode) {

        final MutableLiveData<SingleVIdeoItemWrapper> data = new MutableLiveData<>();

        Log.d("course_api_debug",youtubeApiInterface.getVideoDeatils("contentDetails,id,snippet",videoCode,Constants.YOUTUBE_PLAYER_API_KEY).request().url().toString());

        youtubeApiInterface.getVideoDeatils("contentDetails,id,snippet",videoCode,Constants.YOUTUBE_PLAYER_API_KEY)
                .enqueue(new Callback<YoutubeSingleVideoResponseModel>() {
                    @Override
                    public void onResponse(Call<YoutubeSingleVideoResponseModel> call, Response<YoutubeSingleVideoResponseModel> response) {
                        YoutubeSingleVideoResponseModel responseModel = response.body();
                        if (responseModel!=null){
                            singleVideoItemWrapperRespository.insertVideoContentDetail(
                                responseModel.getItems().get(0)
                            );
                            data.setValue(responseModel.getItems().get(0));
                        }else{
                            data.setValue(null);
                        }
                    }

                    @Override
                    public void onFailure(Call<YoutubeSingleVideoResponseModel> call, Throwable t) {
                        data.setValue(null);
                    }
                });

        return data;
    }

    public LiveData<ResourceLikesResponse> likeProject(String token, int projectId, int like){

        final MutableLiveData<ResourceLikesResponse> data = new MutableLiveData<>();

        apiInterface.likeProject(token, projectId, like).enqueue(new Callback<ResourceLikesResponse>() {
            @Override
            public void onResponse(Call<ResourceLikesResponse> call, Response<ResourceLikesResponse> response) {
                ResourceLikesResponse responseModel = response.body();
                if (responseModel!=null){
                    data.setValue(responseModel);
                }else{
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResourceLikesResponse> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;

    }

    public LiveData<ResourceViewsResponse> viewProject(String token,int id) {

        final MutableLiveData<ResourceViewsResponse> data = new MutableLiveData<>();

        apiInterface.projectViewed(token, id).enqueue(new Callback<ResourceViewsResponse>() {
            @Override
            public void onResponse(Call<ResourceViewsResponse> call, Response<ResourceViewsResponse> response) {
                ResourceViewsResponse resourceViewsResponse = response.body();
                if (resourceViewsResponse!=null){
                    Log.d("project_viewed",resourceViewsResponse.getMessage());
                    data.setValue(resourceViewsResponse);
                }else{
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ResourceViewsResponse> call, Throwable t) {
                Log.d("project_viewed",t.getLocalizedMessage()+"");
                data.setValue(null);
            }
        });

        return data;
    }

    public LiveData<ProjectResponseModel> getProjectById(String token, int projectId) {
        final MutableLiveData<ProjectResponseModel> data = new MutableLiveData<>();
        apiInterface.getProjectById(token,projectId).enqueue(new Callback<ProjectResponseModel>() {
            @Override
            public void onResponse(Call<ProjectResponseModel> call, Response<ProjectResponseModel> response) {
                ProjectResponseModel projectResponseModel = response.body();
                if (projectResponseModel!=null){
                    data.setValue(projectResponseModel);
                }else{
                    data.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<ProjectResponseModel> call, Throwable t) {
                data.setValue(null);
            }
        });

        return data;
    }
}
