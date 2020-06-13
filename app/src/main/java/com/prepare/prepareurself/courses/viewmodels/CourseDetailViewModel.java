package com.prepare.prepareurself.courses.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.prepare.prepareurself.courses.data.model.AddToUserPrefResponseModel;
import com.prepare.prepareurself.courses.data.model.CourseDetailReponseModel;
import com.prepare.prepareurself.courses.data.model.RateCourseResponseModel;
import com.prepare.prepareurself.courses.data.repository.CourseDetailRepository;
import com.prepare.prepareurself.preferences.data.PrefDbRepository;
import com.prepare.prepareurself.preferences.data.PrefRepository;
import com.prepare.prepareurself.preferences.data.PreferencesModel;
import com.prepare.prepareurself.preferences.data.PrefernceResponseModel;

import java.util.ArrayList;
import java.util.List;

public class CourseDetailViewModel extends AndroidViewModel {

    private CourseDetailRepository courseDetailRepository;
    public LiveData<CourseDetailReponseModel> courseDetailReponseModelLiveData = new MutableLiveData<>();
    public LiveData<RateCourseResponseModel> rateCourseResponseModelLiveData=new MutableLiveData<>();
    public  LiveData<AddToUserPrefResponseModel> addToUserPrefResponseModelLiveData= new MutableLiveData<>();
    private PrefRepository prefRepository;
    public LiveData<PrefernceResponseModel> prefernceResponseModelLiveData= new MutableLiveData<>();

    public CourseDetailViewModel(@NonNull Application application) {
        super(application);
        courseDetailRepository = new CourseDetailRepository();
        prefRepository= new PrefRepository(application);
    }

    public void fetchCourseDetails(String token, int courseId){
        courseDetailReponseModelLiveData = courseDetailRepository.fetchCourseDetails(token, courseId);
    }
    public void fetchRateCourse(String token, int courseId, int rating){
        rateCourseResponseModelLiveData=courseDetailRepository.fetchRateCourse(token,courseId,rating);
    }
    public  void  fetchAddUserPref(String  token, int course_id, int type){
        addToUserPrefResponseModelLiveData=courseDetailRepository.fetchAddUserPref(token,course_id,type);
    }

    public  void  fetchremotePref(String token ){
        prefernceResponseModelLiveData=prefRepository.getUserPreferences(token);
    }


}
//error_code:Int?=-1,
//        var preferences