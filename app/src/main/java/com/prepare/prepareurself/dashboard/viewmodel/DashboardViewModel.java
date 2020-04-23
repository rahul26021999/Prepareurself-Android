package com.prepare.prepareurself.dashboard.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.authentication.data.db.repository.UserDBRepository;
import com.prepare.prepareurself.authentication.data.model.UserModel;
import com.prepare.prepareurself.courses.data.db.repository.ProjectsDbRepository;
import com.prepare.prepareurself.courses.data.db.repository.TopicsDbRepository;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.courses.data.model.TopicsResponseModel;
import com.prepare.prepareurself.courses.data.repository.ProjectsRespository;
import com.prepare.prepareurself.courses.data.repository.TopicsRepository;
import com.prepare.prepareurself.dashboard.data.db.repository.CourseDbRepository;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.dashboard.data.model.GetCourseResponseModel;
import com.prepare.prepareurself.dashboard.data.repository.CourseRepository;
import com.prepare.prepareurself.resources.data.db.repository.ResourcesDbRepository;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.resources.data.model.ResourcesResponse;
import com.prepare.prepareurself.resources.data.repository.ResourceRespository;
import com.prepare.prepareurself.utils.Constants;

import java.util.List;

public class DashboardViewModel extends AndroidViewModel {

    private LiveData<ResourcesResponse> resourcesLiveData = new MutableLiveData<>();
    private LiveData<List<CourseModel>> liveCourses = new MutableLiveData<>();
    private LiveData<GetCourseResponseModel> getCourseResponseModelLiveData = new MutableLiveData<>();
    CourseRepository courseRepository;
    CourseDbRepository courseDbRepository;
    UserDBRepository userDBRepository;
    TopicsDbRepository topicsDbRepository;
    TopicsRepository topicsRepository;
    private ProjectsRespository projectsRespository;
    private ProjectsDbRepository projectsDbRepository;
    public DashboardViewModel(@NonNull Application application) {
        super(application);
        courseRepository = new CourseRepository(application);
        courseDbRepository = new CourseDbRepository(application);
        userDBRepository = new UserDBRepository(application);
        topicsRepository = new TopicsRepository(application);
        topicsDbRepository = new TopicsDbRepository(application);
        projectsRespository = new ProjectsRespository(application);
        projectsDbRepository = new ProjectsDbRepository(application);
    }

    public void getCourses(String token){
        liveCourses = courseRepository.getCourses(token);
    }

    public LiveData<GetCourseResponseModel> getCourseResponseModelLiveData(){
        return getCourseResponseModelLiveData;
    }

    public LiveData<List<CourseModel>> getLiveCourses(){
        if (liveCourses.getValue()==null){
            liveCourses = courseDbRepository.getAllCourses();
        }
        return liveCourses;
    }

    public LiveData<UserModel> getUserInfo(){
        return userDBRepository.getUserInfo();
    }

    public void getFiveTopicsByCourseIdFromRemote(String token, int courseId){
        topicsRepository.getTopicsByIId(token, courseId,5,1);
    }

    public LiveData<List<TopicsModel>> getFiveTopicsByCourseIdFromDb(int courseId){
        return topicsDbRepository.getFiveResourcesByID(courseId);
    }

    public void getFiveProjectsByCourseIdFromRemote(String token, int courseId){
        projectsRespository.getProjects(token,courseId,"",5,1);
    }

    public LiveData<List<ProjectsModel>> getFiveProjectByCourseId(int courseId){
        return projectsDbRepository.getFiveProjectsByCourseId(courseId);
    }

}
