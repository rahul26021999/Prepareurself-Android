package com.example.prepareurself.Home.content.resources.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.prepareurself.Home.content.courses.model.TheoryResources;

import java.util.ArrayList;
import java.util.List;

public class TheoryResourceViewModel extends AndroidViewModel {
    private MutableLiveData<List<TheoryResources>> listMutableLiveData=new MutableLiveData<>();

    public TheoryResourceViewModel(@NonNull Application application) {
        super(application);
    }
    // TODO: Implement the ViewModel
    public LiveData<List<TheoryResources>> getListLiveData(){
        TheoryResources t1=new TheoryResources();
        t1.setTheoryId("1");
        t1.setTheoryTitle("this is resource 1");
        t1.setImageUrl("https://pngimage.net/wp-content/uploads/2018/06/w3schools-logo-png-2.png");

        TheoryResources t2=new TheoryResources();
        t2.setTheoryId("2");
        t2.setTheoryTitle("this is resource 2");
        t2.setImageUrl("https://pngimage.net/wp-content/uploads/2018/06/w3schools-logo-png-2.png");

        TheoryResources t3=new TheoryResources();
        t3.setTheoryId("3");
        t3.setTheoryTitle("this is resource 3");
        t3.setImageUrl("https://pngimage.net/wp-content/uploads/2018/06/w3schools-logo-png-2.png");

        TheoryResources t4=new TheoryResources();
        t4.setTheoryId("4");
        t4.setTheoryTitle("this is resource 4");
        t4.setImageUrl("https://pngimage.net/wp-content/uploads/2018/06/w3schools-logo-png-2.png");

        List<TheoryResources> theoryResources=new ArrayList<>();
        theoryResources.add(t1);
        theoryResources.add(t2);
        theoryResources.add(t3);
        theoryResources.add(t4);

        listMutableLiveData.setValue(theoryResources);

        return listMutableLiveData;
    }
}
