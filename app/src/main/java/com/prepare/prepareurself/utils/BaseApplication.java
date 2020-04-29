package com.prepare.prepareurself.utils;

import android.app.Application;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Update;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.onesignal.OneSignal;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.firebase.UpdateHelper;

import java.util.HashMap;
import java.util.Map;

public class BaseApplication extends Application {

    private PrefManager prefManager;
    private static BaseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        prefManager = new PrefManager(this);

        final FirebaseRemoteConfig remoteConfig = FirebaseRemoteConfig.getInstance();

        Map<String , Object> defaultValue = new HashMap<>();
        defaultValue.put(UpdateHelper.KEY_UPDATE_ENABLE, false);
        defaultValue.put(UpdateHelper.KET_UPDATE_VERSION, "1.0");
        defaultValue.put(UpdateHelper.KEY_UPDATE_URL,"https://play.google.com/store/apps/details?id=com.prepare.prepareurself");

        remoteConfig.setDefaults(defaultValue);
        remoteConfig.fetch(5)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            remoteConfig.activateFetched();
                        }
                    }
                });

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

}
