package com.prepare.prepareurself.Home.content.profile.ui.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.prepare.prepareurself.Home.content.profile.ui.EditPreferencesActivity;
import com.prepare.prepareurself.Home.content.profile.viewmodel.ProfileViewModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.authentication.data.model.UserModel;

import java.util.Calendar;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    /*private TextView tvName, tvEmail;
    private Button editbtn;*/
    LinearLayout l_userinfo, l_preferences;
    TextView  t_userinfo, t_preferences, tv_preference_edit, tv_aboutme_edit;
    TextView tv_dob, tv_name,tv_call;
    EditText et_dob,et_name, et_call;
    Button btn_aboutme;
    DatePickerDialog datePickerDialog;
    TextView tv_email_profile;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        l_userinfo=view.findViewById(R.id.userinfolayout);
        l_preferences=view.findViewById(R.id.preferenceslayout);
        t_userinfo=view.findViewById(R.id.userinfobtn);
        t_preferences=view.findViewById(R.id.preferencesbtn);
        tv_preference_edit=view.findViewById(R.id.tv_preference_edit);
        tv_aboutme_edit=view.findViewById(R.id.tv_aboutme_edit);
        tv_dob=view.findViewById(R.id.tv_dob);
        tv_name=view.findViewById(R.id.tv_name);
        tv_call=view.findViewById(R.id.tv_call);
        et_dob=view.findViewById(R.id.et_dob);
        et_name=view.findViewById(R.id.et_name);
        et_call=view.findViewById(R.id.et_call);
        tv_email_profile = view.findViewById(R.id.tv_email_profile);
        btn_aboutme=view.findViewById(R.id.btn_aboutme);

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
        tv_aboutme_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_dob.setVisibility(View.GONE);
                tv_name.setVisibility(View.GONE);
                tv_call.setVisibility(View.GONE);
                et_dob.setVisibility(View.VISIBLE);
                et_name.setVisibility(View.VISIBLE);
                et_call.setVisibility(View.VISIBLE);
                btn_aboutme.setVisibility(View.VISIBLE);
            }
        });

        et_dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setdatepicker();
            }
        });
        tv_preference_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(getActivity(), EditPreferencesActivity.class);
                startActivity(intent);

            }
        });
        btn_aboutme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_dob.setVisibility(View.VISIBLE);
                tv_name.setVisibility(View.VISIBLE);
                tv_call.setVisibility(View.VISIBLE);
                et_dob.setVisibility(View.GONE);
                et_name.setVisibility(View.GONE);
                et_call.setVisibility(View.GONE);
                String userdob=et_dob.getText().toString();
                String username=et_name.getText().toString();
                String userphnumber= et_call.getText().toString();
                tv_dob.setText(userdob);
                tv_name.setText(username);
                tv_call.setText(userphnumber);
                btn_aboutme.setVisibility(View.GONE);

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

    private void setdatepicker() {
        final Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        //date picker dialog
        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                et_dob.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
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
                tv_email_profile.setText(userModel.getEmail());

                tv_dob.setText(userModel.getDob());
                tv_name.setText(userModel.getFirst_name() + " " + userModel.getLast_name());
                tv_call.setText(userModel.getPhone_number());

                et_dob.setText(userModel.getDob());
                et_name.setText(userModel.getFirst_name() + " " + userModel.getLast_name());
                et_call.setText(userModel.getPhone_number());

                if (TextUtils.isEmpty(userModel.getDob())){
                    tv_dob.setText("Click Edit to update your birthday");
                    et_dob.setHint("Tap to update your birthday");
                }

                if (TextUtils.isEmpty(userModel.getPhone_number())){
                    tv_call.setText("Click Edit to update your Contact");
                    et_call.setHint("Enter your Contact Number");
                }

            }
        });


    }

}
