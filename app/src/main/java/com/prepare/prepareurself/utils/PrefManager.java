package com.prepare.prepareurself.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    Context context;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    int PRIVATE_MODE=0;

    public PrefManager(Context context) {
        this.context = context;
        pref=context.getSharedPreferences(Constants.PREF_NAME,PRIVATE_MODE);
        editor=pref.edit();
    }

    public void saveString(String key, String data)
    {
        editor.putString(key,data);
        editor.commit();
    }

    public void saveInteger(String key, int data)
    {
        editor.putInt(key,data);
        editor.commit();
    }

    public void saveBoolean(String key, Boolean val)
    {
        editor.putBoolean(key,val);
        editor.commit();
    }

    public Boolean getBoolean(String key)
    {
        return pref.getBoolean(key,false);
    }

    public String getString(String key)
    {
        return pref.getString(key,null);
    }

    public int getInt(String key)
    {
        return pref.getInt(key,0);
    }

}