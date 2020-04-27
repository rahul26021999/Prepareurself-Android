package com.prepare.prepareurself.profile.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.authentication.data.model.UserModel;
import com.prepare.prepareurself.profile.data.model.MyPreferenceTechStack;
import com.prepare.prepareurself.profile.data.model.PreferredTechStack;
import com.prepare.prepareurself.profile.data.model.UpdatePreferenceResponseModel;
import com.prepare.prepareurself.profile.viewmodel.ProfileViewModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class EditPreferenceActivity extends AppCompatActivity {

    private ProfileViewModel profileViewModel;
    private PrefManager prefManager;
    private ChipGroup chip_gp;
    private List<String> preferences=new ArrayList<>();
    private List<String> allStackID=new ArrayList<>();
    private MaterialButton save;
    private UserModel mUserModel;
    private List<String> allStackName=new ArrayList<>();
    private HashMap<String,String> allStack=new HashMap<>();
    private ProgressDialog dialog;
    private HashMap<Integer, PreferredTechStack> preferredTechStackHashMap = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_preference);

        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);
        prefManager = new PrefManager(this);
        profileViewModel.getAllPreferences(prefManager.getString(Constants.JWTTOKEN));

        chip_gp=findViewById(R.id.chip_gp);
        save=findViewById(R.id.save);

        dialog = new ProgressDialog(this);

        profileViewModel.getUserModelLiveData().observe(this, new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                if(userModel.getPreferences()!=null) {
                    mUserModel = userModel;
                    Log.i("helloChip",userModel.getPreferences());
                    preferences.clear();
                    preferences.addAll(Arrays.asList(userModel.getPreferences().split(",")));
                }
                profileViewModel.getPreferencesFromDb().observe(EditPreferenceActivity.this, new Observer<List<PreferredTechStack>>() {
                    @Override
                    public void onChanged(final List<PreferredTechStack> preferredTechStacks) {
                        int size = preferredTechStacks.size();
                        Log.i("Size",""+size);
                        chip_gp.removeAllViews();
                        for (int i=0;i<size;i++){
                            allStackID.add(""+preferredTechStacks.get(i).getId());
                            allStackName.add(preferredTechStacks.get(i).getName());
                            final Chip chip = new Chip(EditPreferenceActivity.this);
                            chip.setText(preferredTechStacks.get(i).getName());
                            chip.setTextAppearanceResource(R.style.EditPrefrenceChip);
                            chip.setChipBackgroundColorResource(R.color.colorPrimaryDark);
                            chip.setCheckable(true);
                            if(preferences.contains(allStackID.get(i))) {
                                chip.setChecked(true);
                               preferredTechStackHashMap.put(preferredTechStacks.get(i).getId(),preferredTechStacks.get(i));
                            }
                            chip.setCloseIconVisible(false);
                            final int finalI = i;
                            chip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if(isChecked)
                                    {
                                        preferences.add(allStackID.get(allStackName.indexOf(chip.getText().toString())));
                                        Log.i("helloChip",buttonView.getText().toString());
                                        preferredTechStackHashMap.put(preferredTechStacks.get(finalI).getId(),preferredTechStacks.get(finalI));
                                    }
                                    else{
                                        preferences.remove(allStackID.get(allStackName.indexOf(chip.getText().toString())));
                                        Log.i("helloChip",buttonView.getText().toString() +" removed");
                                        preferredTechStackHashMap.remove(preferredTechStacks.get(finalI).getId());
                                    }
                                }
                            });
                            chip_gp.addView(chip);
                        }
                    }
                });

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog!=null && !dialog.isShowing()){
                    dialog.setMessage("Updating preferences");
                    dialog.show();
                }
                List<Integer> list = new ArrayList<>();
                for (String pref : preferences){
                    list.add(Integer.parseInt(pref));
                }
                profileViewModel.updatePrefernces(prefManager.getString(Constants.JWTTOKEN),list)
                        .observe(EditPreferenceActivity.this, new Observer<UpdatePreferenceResponseModel>() {
                            @Override
                            public void onChanged(UpdatePreferenceResponseModel updatePreferenceResponseModel) {
                                if (updatePreferenceResponseModel!=null){
                                    if (updatePreferenceResponseModel.getError_code() == 0){
                                        if (mUserModel!=null){
                                            String p = TextUtils.join(",",preferences);
                                            mUserModel.setPreferences(p);
                                            profileViewModel.saveMyPreference(mUserModel);
                                            Utility.showToast(EditPreferenceActivity.this, "Updation successful");
                                        }
                                    }else{
                                        Utility.showToast(EditPreferenceActivity.this,updatePreferenceResponseModel.getMsg());
                                    }
                                }else{
                                    Utility.showToast(EditPreferenceActivity.this,Constants.SOMETHINGWENTWRONG);
                                }
                                if (dialog!=null && dialog.isShowing()){
                                    dialog.hide();
                                }
                            }
                        });
            }
        });
    }
}
