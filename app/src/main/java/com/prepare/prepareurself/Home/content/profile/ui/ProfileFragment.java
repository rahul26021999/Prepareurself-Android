package com.prepare.prepareurself.Home.content.profile.ui;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prepare.prepareurself.Home.content.edit_profile;
import com.prepare.prepareurself.Home.content.profile.viewmodel.ProfileViewModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.authentication.data.model.UserModel;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    /*private TextView tvName, tvEmail;
    private Button editbtn;*/
    LinearLayout l_userinfo, l_preferences;
    TextView  t_userinfo, t_preferences;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        l_userinfo=view.findViewById(R.id.userinfolayout);
        l_preferences=view.findViewById(R.id.preferenceslayout);
        t_userinfo=view.findViewById(R.id.userinfobtn);
        t_preferences=view.findViewById(R.id.preferencesbtn);
        l_userinfo.setVisibility(View.VISIBLE);
        l_preferences.setVisibility(View.GONE);
        t_userinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l_userinfo.setVisibility(View.VISIBLE);
                l_preferences.setVisibility(View.GONE);
                t_userinfo.setTextColor(getResources().getColor(R.color.blue));
                t_preferences.setTextColor(getResources().getColor(R.color.grey));
            }
        });

        t_preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                l_userinfo.setVisibility(View.GONE);
                l_preferences.setVisibility(View.VISIBLE);
                t_userinfo.setTextColor(getResources().getColor(R.color.grey));
                t_preferences.setTextColor(getResources().getColor(R.color.blue));
            }
        });
        /*tvName = view.findViewById(R.id.tv_name_profile);
        tvEmail = view.findViewById(R.id.tv_email_profile);
        editbtn=view.findViewById(R.id.btn_editbtn);
        editbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), edit_profile.class);
                startActivity(intent);

            }
        });*/


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        mViewModel.getUserModelLiveData().observe(getActivity(), new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                String name = userModel.getFirst_name() + " " + userModel.getLast_name();
                /*tvName.setText(name);
                tvEmail.setText(userModel.getEmail());*/
            }
        });

    }

}
