package com.prepare.prepareurself.utils;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;

import com.prepare.prepareurself.R;


public class BaseActivity extends AppCompatActivity{

    public PrefManager prefManager;
    public AlertDialog.Builder alertDialog;
    public AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alertDialog=new AlertDialog.Builder(BaseActivity.this);
        alertDialog.setView(LayoutInflater.from(BaseActivity.this).inflate(R.layout.custom_no_internet_dialog_layout,null));
        alertDialog.setCancelable(false);

        alertDialog.setNegativeButton("Exit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                finish();
            }
        });
        alertDialog.setPositiveButton("Go To Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Settings.ACTION_SETTINGS);
                startActivity(intent);
                dialogInterface.dismiss();
            }
        });

        dialog=alertDialog.create();

        new NetwrokUtils().getNetworkLiveData(getApplicationContext())
                .observe(this, new Observer<Boolean>() {
                    @Override
                    public void onChanged(Boolean aBoolean) {
                        if (!aBoolean){
                            dialog.show();
                        }else{
                            if (dialog.isShowing()){
                                dialog.cancel();
                            }
                        }
                    }
                });
    }
}
