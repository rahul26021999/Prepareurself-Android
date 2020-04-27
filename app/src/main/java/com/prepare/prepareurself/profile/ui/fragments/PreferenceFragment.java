package com.prepare.prepareurself.profile.ui.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.android.material.chip.Chip;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.authentication.data.model.UserModel;
import com.prepare.prepareurself.profile.data.model.PreferredTechStack;
import com.prepare.prepareurself.profile.ui.EditPreferenceActivity;
import com.prepare.prepareurself.profile.ui.adapter.UserPrefernceAdapter;
import com.prepare.prepareurself.profile.viewmodel.ProfileViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PreferenceFragment extends Fragment {

    private ProfileViewModel profileViewModel;
    private RecyclerView recyclerView;
    private UserModel mUserModel;
    private List<String> preferences=new ArrayList<>();
    private List<String> allStackID=new ArrayList<>();
    private List<String> allStackName=new ArrayList<>();
    private List<PreferredTechStack> preferredTechStackList = new ArrayList<>();
    private TextView tvEdit;
    UserPrefernceAdapter adapter;

    public static PreferenceFragment newInstance() {
        return new PreferenceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.preference_fragment, container, false);

        recyclerView = view.findViewById(R.id.rv_user_preference);
        tvEdit = view.findViewById(R.id.tv_preference_edit);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        profileViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        adapter = new UserPrefernceAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        tvEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), EditPreferenceActivity.class));
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        profileViewModel.getUserModelLiveData().observe(getActivity(), new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                if(userModel.getPreferences()!=null) {
                    mUserModel = userModel;
                    Log.i("helloChip",userModel.getPreferences());
                    preferences.addAll(Arrays.asList(userModel.getPreferences().split(",")));
                }
                profileViewModel.getPreferencesFromDb().observe(getActivity(), new Observer<List<PreferredTechStack>>() {
                    @Override
                    public void onChanged(final List<PreferredTechStack> preferredTechStacks) {
                        int size = preferredTechStacks.size();
                        Map<String,String> allStack=new HashMap<>();
                        for (int i=0;i<size;i++){
                            allStack.put(""+preferredTechStacks.get(i).getId(),preferredTechStacks.get(i).getName());
                        }

                        adapter.setPreferredTechStacks(preferences);
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        });
    }
}
