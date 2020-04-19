package com.prepare.prepareurself.Home.content.courses.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.Home.content.courses.data.db.repository.ProjectsDbRepository;
import com.prepare.prepareurself.Home.content.courses.data.model.ProjectResponse;
import com.prepare.prepareurself.Home.content.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.Home.content.courses.data.repository.ProjectsRespository;

import java.util.List;

public class ProjectsViewModel extends AndroidViewModel {

    private ProjectsRespository respository;
    private ProjectsDbRepository dbRepository;

    private LiveData<ProjectResponse> projectResponseMutableLiveData = new MutableLiveData<>();
    private LiveData<List<ProjectsModel>> listLiveData = new MutableLiveData<>();

    public ProjectsViewModel(@NonNull Application application) {
        super(application);
        respository = new ProjectsRespository(application);
        dbRepository = new ProjectsDbRepository(application);
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
}
