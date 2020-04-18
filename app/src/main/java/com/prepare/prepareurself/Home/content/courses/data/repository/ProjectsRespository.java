package com.prepare.prepareurself.Home.content.courses.data.repository;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Apiservice.ApiClient;
import com.prepare.prepareurself.Apiservice.ApiInterface;
import com.prepare.prepareurself.Home.content.courses.data.db.repository.ProjectsDbRepository;
import com.prepare.prepareurself.Home.content.courses.data.model.GetProjectResponse;
import com.prepare.prepareurself.Home.content.courses.data.model.ProjectResponse;
import com.prepare.prepareurself.Home.content.courses.data.model.ProjectsModel;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProjectsRespository {

    private ApiInterface apiInterface;
    private ProjectsDbRepository projectsDbRepository;

    public ProjectsRespository(Application application){
        apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        projectsDbRepository = new ProjectsDbRepository(application);
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
