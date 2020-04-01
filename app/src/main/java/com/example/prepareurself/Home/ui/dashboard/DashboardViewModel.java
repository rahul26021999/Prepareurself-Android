package com.example.prepareurself.Home.ui.dashboard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prepareurself.Home.model.DashboardRecyclerviewModel;
import com.example.prepareurself.R;

import java.util.ArrayList;
import java.util.List;

import static com.example.prepareurself.utils.Constants.ADDVIEWTYPE;
import static com.example.prepareurself.utils.Constants.COURSEVIEWTYPE;

public class DashboardViewModel extends ViewModel {

    MutableLiveData<List<DashboardRecyclerviewModel>> listMutableLiveData = new MutableLiveData<>();
    MutableLiveData<ArrayList<String>> courseNames = new MutableLiveData<>();
    MutableLiveData<ArrayList<String>> quizeNames = new MutableLiveData<>();

    public MutableLiveData<List<DashboardRecyclerviewModel>> getModelList(){
        List<DashboardRecyclerviewModel> modelList = new ArrayList<>();

        modelList.add(new DashboardRecyclerviewModel(COURSEVIEWTYPE, R.mipmap.ic_launcher,getCourseNames().getValue()));
        modelList.add(new DashboardRecyclerviewModel(COURSEVIEWTYPE, R.mipmap.ic_launcher,getQizzes().getValue()));
        modelList.add(new DashboardRecyclerviewModel(ADDVIEWTYPE,"This is add 1"));
        modelList.add(new DashboardRecyclerviewModel(COURSEVIEWTYPE, R.mipmap.ic_launcher,getCourseNames().getValue()));
        modelList.add(new DashboardRecyclerviewModel(ADDVIEWTYPE,"This is add 2"));
        modelList.add(new DashboardRecyclerviewModel(COURSEVIEWTYPE, R.mipmap.ic_launcher,getCourseNames().getValue()));

        listMutableLiveData.setValue(modelList);

        return listMutableLiveData;

    }

    private MutableLiveData<ArrayList<String>> getCourseNames(){
        ArrayList<String> data = new ArrayList<>();
        data.add("Android");
        data.add("NodeJS");
        data.add("AngularJs");
        data.add("ReactJs");
        data.add("Laravel");
        data.add("Machine Learning");
        data.add("Cloud Computing");

        courseNames.setValue(data);

        return courseNames;
    }

    private MutableLiveData<ArrayList<String>> getQizzes(){
        ArrayList<String> data = new ArrayList<>();
        data.add("Quiz 1");
        data.add("Quiz 2");
        data.add("Quiz 3");
        data.add("Quiz 4");
        data.add("Quiz 5");
        data.add("Quiz 5");
        data.add("Quiz 6");

        courseNames.setValue(data);

        return courseNames;
    }

}
