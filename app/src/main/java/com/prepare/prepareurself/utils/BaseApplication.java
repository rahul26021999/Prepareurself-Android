package com.prepare.prepareurself.utils;

import android.app.Application;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.onesignal.OneSignal;
import com.prepare.prepareurself.R;

public class BaseApplication extends Application {

    private PrefManager prefManager;

    @Override
    public void onCreate() {
        super.onCreate();

        prefManager = new PrefManager(this);

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
