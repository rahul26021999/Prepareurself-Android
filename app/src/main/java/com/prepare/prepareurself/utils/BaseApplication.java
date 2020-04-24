package com.prepare.prepareurself.utils;

import android.app.Application;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;

import com.onesignal.OneSignal;

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

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }
}
