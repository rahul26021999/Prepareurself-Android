package com.example.prepareurself.Home.content.courses.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.example.prepareurself.Home.content.courses.data.model.Resource;

import java.util.ArrayList;
import java.util.List;

public class ResourcesViewModel extends AndroidViewModel {

    private MutableLiveData<List<Resource>> listMutableLiveData = new MutableLiveData<>();

    public ResourcesViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<Resource>> getListMutableLiveData() {

        Resource r1 = new Resource();
        r1.setExpanded(false);
        r1.setDescription("This is the description for title 1");
        r1.setId("1");
        r1.setTitle("This is title 1");

        Resource r2 = new Resource();
        r2.setExpanded(false);
        r2.setDescription("This is the description for title 2");
        r2.setId("2");
        r2.setTitle("This is title 2");

        Resource r3 = new Resource();
        r3.setExpanded(false);
        r3.setDescription("This is the description for title 3");
        r3.setId("3");
        r3.setTitle("This is title 3");

        Resource r4 = new Resource();
        r4.setExpanded(false);
        r4.setDescription("This is the description for title 4");
        r4.setId("4");
        r4.setTitle("This is title 4");

        List<Resource> resources = new ArrayList<>();
        resources.add(r1);
        resources.add(r2);
        resources.add(r3);
        resources.add(r4);

        listMutableLiveData.setValue(resources);

        return listMutableLiveData;
    }

}
