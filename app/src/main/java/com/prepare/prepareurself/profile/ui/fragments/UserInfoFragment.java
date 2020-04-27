package com.prepare.prepareurself.profile.ui.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
import android.widget.TextView;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.authentication.data.model.UserModel;
import com.prepare.prepareurself.authentication.ui.AuthenticationActivity;
import com.prepare.prepareurself.profile.data.model.UpdatePreferenceResponseModel;
import com.prepare.prepareurself.profile.ui.activity.UpdatePasswordActivity;
import com.prepare.prepareurself.profile.viewmodel.ProfileViewModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class UserInfoFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private DatePickerDialog datePickerDialog;
    private TextView tvName;
    private TextView tvDob, tvContact;
    private EditText etName, etDob, etContact;
    private TextView tvEditAboutMe;
    private Button btnSave, btnUpdatePass, btnLogout;
    private PrefManager prefManager;

    String userDob = "", userName = "", userContact = "";
    private SimpleDateFormat dateFormatter;

    public static UserInfoFragment newInstance() {
        return new UserInfoFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.user_info_fragment, container, false);

        tvName = view.findViewById(R.id.tv_name);
        tvDob = view.findViewById(R.id.tv_dob);
        tvContact = view.findViewById(R.id.tv_call);
        etName = view.findViewById(R.id.et_name);
        etDob = view.findViewById(R.id.et_dob);
        etContact = view.findViewById(R.id.et_call);
        tvEditAboutMe = view.findViewById(R.id.tv_aboutme_edit);
        btnSave = view.findViewById(R.id.btn_aboutme_save);
        btnUpdatePass = view.findViewById(R.id.btn_update_password);
        btnLogout = view.findViewById(R.id.btn_logout);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        hideAboutEditTexts();
        showAboutTextViews();

        prefManager = new PrefManager(getActivity());

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        mViewModel.getUserModelLiveData().observe(getActivity(), new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                if (userModel!=null){
                    userName = userModel.getFirst_name() + " " + userModel.getLast_name();
                    userDob = userModel.getDob();
                    userContact = userModel.getPhone_number();
                    tvName.setText(userName);
                    if (TextUtils.isEmpty(userDob)){
                        tvDob.setText("Click Edit to update dob");
                    }else{
                        tvDob.setText(userName);
                    }

                    if (TextUtils.isEmpty(userContact)){
                        tvContact.setHint("Click Edit to update contact");
                    }else{
                        tvContact.setText(userContact);
                    }

                }
            }
        });

        tvEditAboutMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideAboutTextViews();
                showAboutEditTexts();

                etName.setText(userName);

                if (TextUtils.isEmpty(userDob)){
                    etDob.setHint("Tap to update dob");
                }else{
                    etDob.setText(userName);
                }

                if (TextUtils.isEmpty(userContact)){
                    etContact.setHint("Start typing..");
                }else{
                    etContact.setText(userContact);
                }

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dob = etDob.getText().toString();
                String fullName =  etName.getText().toString();
                String contact = etContact.getText().toString().trim();

                String firstName = "", lastName = "";
                // first convert name to first name and last name
                String[] name = Utility.splitName(getActivity(),fullName);
                if (name.length>0){
                    if (name.length == 1){
                        firstName = name[0];
                    }else if (name.length == 2){
                        firstName = name[0];
                        lastName = name[1];
                    }else{
                        firstName = name[0];
                        lastName = name[name.length-1];
                    }
                }

                mViewModel.updateUser(prefManager.getString(Constants.JWTTOKEN),
                        firstName,
                        lastName,
                        dob,
                        contact).observe(getActivity(), new Observer<UpdatePreferenceResponseModel>() {
                    @Override
                    public void onChanged(UpdatePreferenceResponseModel updatePreferenceResponseModel) {
                        if (updatePreferenceResponseModel!=null){
                            if (updatePreferenceResponseModel.getError_code() == 0){
                                setDataOnTextViews(updatePreferenceResponseModel.getUser_data());
                                hideAboutEditTexts();
                                showAboutTextViews();
                                Utility.showToast(getActivity(), "Details updated successfully");
                            }else{
                                Utility.showToast(getActivity(), updatePreferenceResponseModel.getMsg());
                            }
                        }else{
                            Utility.showToast(getActivity(), Constants.SOMETHINGWENTWRONG);
                        }
                    }
                });

            }
        });

        btnUpdatePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UpdatePasswordActivity.class));
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefManager.saveBoolean(Constants.ISLOGGEDIN, false);
                startActivity(new Intent(getActivity(), AuthenticationActivity.class));
                getActivity().finish();
            }
        });

        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setdatepicker();
            }
        });

    }

    private void setDataOnTextViews(UserModel userModel) {
        tvName.setText(userModel.getFirst_name() + " " + userModel.getLast_name());
        tvDob.setText(userModel.getDob());
        tvContact.setText(userModel.getPhone_number());
    }

    private void hideAboutTextViews() {
        tvName.setVisibility(View.GONE);
        tvDob.setVisibility(View.GONE);
        tvContact.setVisibility(View.GONE);
    }

    private void hideAboutEditTexts() {
        etName.setVisibility(View.GONE);
        etDob.setVisibility(View.GONE);
        etContact.setVisibility(View.GONE);
        btnSave.setVisibility(View.GONE);
    }

    private void showAboutTextViews() {
        tvName.setVisibility(View.VISIBLE);
        tvDob.setVisibility(View.VISIBLE);
        tvContact.setVisibility(View.VISIBLE);
    }

    private void showAboutEditTexts() {
        etName.setVisibility(View.VISIBLE);
        etDob.setVisibility(View.VISIBLE);
        etContact.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.VISIBLE);
    }

    private void setdatepicker() {
        final Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        //date picker dialog
        datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year, month, dayOfMonth);
                etDob.setText(dateFormatter.format(calendar.getTime()));
            }
        }, year, month, day);
        datePickerDialog.show();
    }

}
