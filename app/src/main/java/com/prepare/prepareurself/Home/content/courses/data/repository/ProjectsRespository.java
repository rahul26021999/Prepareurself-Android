package com.prepare.prepareurself.Home.content.courses.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;
import com.prepare.prepareurself.Apiservice.YoutubeApiInterface;
import com.prepare.prepareurself.Home.content.courses.data.db.repository.ProjectsDbRepository;
import com.prepare.prepareurself.Home.content.courses.data.model.GetProjectResponse;
import com.prepare.prepareurself.Home.content.courses.data.model.ProjectResponse;
import com.prepare.prepareurself.Home.content.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.youtubeplaylistapi.db.PlaylistVideosDbRepository;
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.VideoItemWrapper;
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.YoutubePlaylistResponseModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectsRespository {

    private ApiInterface apiInterface;
    private ProjectsDbRepository projectsDbRepository;
    private YoutubeApiInterface youtubeApiInterface;
    private PlaylistVideosDbRepository playlistVideosDbRepository;

    public ProjectsRespository(Application application){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        youtubeApiInterface = ApiClient.getYoutubeApiClient().create(YoutubeApiInterface.class);
        projectsDbRepository = new ProjectsDbRepository(application);
        playlistVideosDbRepository = new PlaylistVideosDbRepository(application);
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

    public LiveData<ProjectResponse> getProjects(String token, int courseId, String level, int count, int page){

        final MutableLiveData<ProjectResponse> data = new MutableLiveData<>();

        apiInterface.getProjects(token,courseId,count, page).enqueue(new Callback<GetProjectResponse>() {
            @Override
            public void onResponse(Call<GetProjectResponse> call, Response<GetProjectResponse> response) {

                Log.d("response_debug","hjere "+ response);

                GetProjectResponse getProjectResponse =  response.body();

                Log.d("response_debug",getProjectResponse+"");

                if (getProjectResponse!=null){
                    if (getProjectResponse.getError_code() == 0){
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

}
