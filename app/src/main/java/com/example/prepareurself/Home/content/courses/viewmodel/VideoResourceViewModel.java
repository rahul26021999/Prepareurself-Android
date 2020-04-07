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
        v1.setVideoTitle("This is Video 1");
        v1.setVideoCode("X2COHLCv0eQ");
        v1.setImageUrl("https://bs-uploads.toptal.io/blackfish-uploads/blog/post/seo/og_image_file/og_image/15921/secure-rest-api-in-nodejs-18f43b3033c239da5d2525cfd9fdc98f.png");

        VideoResources v2 = new VideoResources();
        v2.setVideoId("2");
        v2.setVideoCode("X2COHLCv0eQ");
        v2.setVideoTitle("This is Video 2");
        v2.setImageUrl("https://bs-uploads.toptal.io/blackfish-uploads/blog/post/seo/og_image_file/og_image/15921/secure-rest-api-in-nodejs-18f43b3033c239da5d2525cfd9fdc98f.png");

        VideoResources v3 = new VideoResources();
        v3.setVideoId("3");
        v3.setVideoCode("X2COHLCv0eQ");
        v3.setVideoTitle("This is Video 3");
        v3.setImageUrl("https://bs-uploads.toptal.io/blackfish-uploads/blog/post/seo/og_image_file/og_image/15921/secure-rest-api-in-nodejs-18f43b3033c239da5d2525cfd9fdc98f.png");

        VideoResources v4 = new VideoResources();
        v4.setVideoId("4");
        v4.setVideoCode("X2COHLCv0eQ");
        v4.setVideoTitle("This is Video 4");
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