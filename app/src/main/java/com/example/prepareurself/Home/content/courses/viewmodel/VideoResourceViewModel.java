package com.example.prepareurself.Home.content.courses.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.prepareurself.Home.content.courses.model.VideoResources;

import java.util.ArrayList;
import java.util.List;

public class VideoResourceViewModel extends AndroidViewModel {

    private MutableLiveData<List<VideoResources>> listLiveData = new MutableLiveData<>();

    public VideoResourceViewModel(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<VideoResources>> getListLiveData() {

        VideoResources v1 = new VideoResources();
        v1.setVideoId("1");
        v1.setVideoTitle("KR$NA - UNTITLED (FULL VIDEO) | KALAMKAAR");
        v1.setVideoCode("2KsrtJ3VtZs");
        v1.setImageUrl("https://bs-uploads.toptal.io/blackfish-uploads/blog/post/seo/og_image_file/og_image/15921/secure-rest-api-in-nodejs-18f43b3033c239da5d2525cfd9fdc98f.png");

        VideoResources v2 = new VideoResources();
        v2.setVideoId("2");
        v2.setVideoCode("fHSo2cpyIas");
        v2.setVideoTitle("KR$NA - SEEDHA MAKEOVER | KALAMKAAR");
        v2.setImageUrl("https://bs-uploads.toptal.io/blackfish-uploads/blog/post/seo/og_image_file/og_image/15921/secure-rest-api-in-nodejs-18f43b3033c239da5d2525cfd9fdc98f.png");

        VideoResources v3 = new VideoResources();
        v3.setVideoId("3");
        v3.setVideoCode("KPEJDCFe3-A");
        v3.setVideoTitle("KR$NA - MAHARANI (FULL VIDEO) | KALAMKAAR");
        v3.setImageUrl("https://bs-uploads.toptal.io/blackfish-uploads/blog/post/seo/og_image_file/og_image/15921/secure-rest-api-in-nodejs-18f43b3033c239da5d2525cfd9fdc98f.png");

        VideoResources v4 = new VideoResources();
        v4.setVideoId("4");
        v4.setVideoCode("92ltAWKDAJ4");
        v4.setVideoTitle("KARMA : GODZILLA | DEEP KALSI | KALAMKAAR");
        v4.setImageUrl("https://bs-uploads.toptal.io/blackfish-uploads/blog/post/seo/og_image_file/og_image/15921/secure-rest-api-in-nodejs-18f43b3033c239da5d2525cfd9fdc98f.png");

        List<VideoResources> videoResources = new ArrayList<>();
        videoResources.add(v1);
        videoResources.add(v2);
        videoResources.add(v3);
        videoResources.add(v4);

        listLiveData.setValue(videoResources);

        return listLiveData;
    }
}
