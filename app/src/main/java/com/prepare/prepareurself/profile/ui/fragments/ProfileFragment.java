package com.prepare.prepareurself.profile.ui.fragments;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.prepare.prepareurself.Home.ui.HomeActivity;
import com.prepare.prepareurself.authentication.ui.AuthenticationActivity;
import com.prepare.prepareurself.profile.data.model.PreferredTechStack;
import com.prepare.prepareurself.profile.data.model.UpdatePreferenceResponseModel;
import com.prepare.prepareurself.profile.ui.EditPreferenceActivity;
import com.prepare.prepareurself.profile.ui.activity.UpdatePasswordActivity;
import com.prepare.prepareurself.profile.ui.adapter.UserPrefernceAdapter;
import com.prepare.prepareurself.profile.viewmodel.ProfileViewModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.authentication.data.model.UserModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private ProfileViewModel mViewModel;

    TextView tv_email_profile;
    private PrefManager prefManager;

    private TextView tabPreference, tabUserInfo;

    private static final int INTENT_REQUEST_CODE = 100;

    private ImageView userImageView;
    private UserModel mUserModel;
    private TextView title;
    private ImageView backBtn , editimg;
    private LinearLayout prefrences,userInfo;

    private DatePickerDialog datePickerDialog;
    private TextView tvName;
    private TextView tvDob, tvContact,btnLogout,btnUpdatePass;
    private EditText etName, etDob, etContact;
    private TextView tvEditAboutMe, tvCancelAboutMe;
    private Button btnAboutMeSave;

    private RecyclerView recyclerView;
    private List<String> preferences=new ArrayList<>();
    private TextView tvPreferenceEdit;
    UserPrefernceAdapter adapter;


    String userDob = "", userName = "", userContact = "";
    private SimpleDateFormat dateFormatter;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        tv_email_profile = view.findViewById(R.id.tv_email_profile);
        userImageView = view.findViewById(R.id.user_image);
        title=view.findViewById(R.id.title);
        backBtn=view.findViewById(R.id.backBtn);
        editimg=view.findViewById(R.id.edit_image_vector);
        prefrences=view.findViewById(R.id.prefrences);
        userInfo=view.findViewById(R.id.userInfo);
        tvName = view.findViewById(R.id.tv_name);
        tvDob = view.findViewById(R.id.tv_dob);
        tvContact = view.findViewById(R.id.tv_call);
        etName = view.findViewById(R.id.et_name);
        etDob = view.findViewById(R.id.et_dob);
        etContact = view.findViewById(R.id.et_call);
        tvEditAboutMe = view.findViewById(R.id.tv_aboutme_edit);
        tvCancelAboutMe= view.findViewById(R.id.tv_aboutme_cancel);
        btnAboutMeSave = view.findViewById(R.id.btn_aboutme_save);
        btnUpdatePass = view.findViewById(R.id.btn_update_password);
        btnLogout = view.findViewById(R.id.btn_logout);
        tabPreference = view.findViewById(R.id.tabPreference);
        tabUserInfo = view.findViewById(R.id.tabUserInfo);
        recyclerView = view.findViewById(R.id.rv_user_preference);
        tvPreferenceEdit = view.findViewById(R.id.tv_preference_edit);

        backBtn.setOnClickListener(this);
        title.setText("My Profile");
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);


        adapter = new UserPrefernceAdapter(getActivity());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        tvPreferenceEdit.setOnClickListener(this);

        if (HomeActivity.gotoPrefFromBanner){
            tabPreference.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            tabUserInfo.setTextColor(getResources().getColor(R.color.dark_grey));
            userInfo.setVisibility(View.GONE);
            prefrences.setVisibility(View.VISIBLE);
            HomeActivity.gotoPrefFromBanner = false;
        }

        hideAboutEditTexts();
        showAboutTextViews();

        prefManager = new PrefManager(getActivity());

        dateFormatter = new SimpleDateFormat("MM/dd/yyyy", Locale.US);


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

        tvEditAboutMe.setOnClickListener(this);
        tvCancelAboutMe.setOnClickListener(this);
        btnAboutMeSave.setOnClickListener(this);
        editimg.setOnClickListener(this);
        userImageView.setOnClickListener(this);
        etDob.setOnClickListener(this);
        tabPreference.setOnClickListener(this);
        tabUserInfo.setOnClickListener(this);

        if(getActivity()!=null)
        {
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
                            if (userDob.contains("T")){
                                tvDob.setText(userDob.split("T")[0]);
                            } else{
                                tvDob.setText(userDob.split(" ")[0]);
                            }
                        }
                        if (TextUtils.isEmpty(userContact)){
                            tvContact.setHint("Click Edit to update contact");
                        }else{
                            tvContact.setText(userContact);
                        }
                        if(userModel.getProfile_image()==null)
                            editimg.setImageResource(R.drawable.ic_add_a_photo_black_24dp);
                        else
                            editimg.setImageResource(R.drawable.ic_iconfinder_circle_edit_pen_pencil_glyph_763463);


                        mUserModel = userModel;
                        tv_email_profile.setText(userModel.getEmail());

                        if (getActivity()!=null){
                            Glide.with(getActivity())
                                    .load(Constants.USERIMAGEBASEURL + userModel.getProfile_image())
                                    .override(500,500)
                                    .placeholder(R.drawable.person_placeholder)
                                    .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                                    .into(userImageView);
                        }

                        if(userModel.getPreferences()!=null) {
                            preferences.clear();
                            preferences.addAll(Arrays.asList(mUserModel.getPreferences().split(",")));
                            adapter.setPreferredTechStacks(preferences);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }

    }

    private void setDataOnTextViews(UserModel userModel) {
        tvName.setText(userModel.getFirst_name() + " " + userModel.getLast_name());
        if (!TextUtils.isEmpty(userModel.getDob())){
            if (userModel.getDob().contains("T")){
                tvDob.setText(userModel.getDob().split("T")[0]);
            }else{
                tvDob.setText(userModel.getDob().split(" ")[0]);
            }

        }else {
            tvDob.setText("Click Edit to update dob");
        }

        if (userModel.getPhone_number()!=null){
            tvContact.setText(userModel.getPhone_number());
        }else{
            tvContact.setText("Click Edit to update contact");
        }
    }

    private void uploadImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/jpeg");

        try {
            startActivityForResult(intent, INTENT_REQUEST_CODE);

        } catch (ActivityNotFoundException e) {

            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode ==Activity.RESULT_OK) {
                Uri resultUri = result.getUri();
                try {
                    InputStream is = null;
                    is = getActivity().getContentResolver().openInputStream(resultUri);
                    if (is!=null)
                        uploadImageToServer(getBytes(is));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }

        }
        if (requestCode == INTENT_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
                Uri uri= data.getData();
                CropImage.activity(uri)
                        .setAspectRatio(1,1)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(getContext(), this);
            }
        }

    }

    private byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream byteBuff = new ByteArrayOutputStream();

        int buffSize = 1024;
        byte[] buff = new byte[buffSize];

        int len = 0;
        while ((len = is.read(buff)) != -1) {
            byteBuff.write(buff, 0, len);
        }

        return byteBuff.toByteArray();
    }


    private void uploadImageToServer(byte[] imageBytes){
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/jpeg"), imageBytes);
        MultipartBody.Part body = MultipartBody.Part.createFormData("profile_image", "image.jpg", requestFile);
       // RequestBody name =  RequestBody.create(MediaType.parse("text/html"), "image.jpg");
        mViewModel.uploadImage(prefManager.getString(Constants.JWTTOKEN),body)
                .observe(getActivity(), new Observer<UpdatePreferenceResponseModel>() {
                    @Override
                    public void onChanged(UpdatePreferenceResponseModel updatePreferenceResponseModel) {
                        if (updatePreferenceResponseModel!=null){
                            if (updatePreferenceResponseModel.getError_code() == 0){
                                Glide.with(getActivity())
                                        .load(
                                        Constants.USERIMAGEBASEURL + updatePreferenceResponseModel.getUser_data()
                                                .getProfile_image())
                                        .placeholder(R.drawable.person_placeholder)
                                        .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                                        .into(userImageView);

                                if (mUserModel!=null){
                                    mUserModel.setProfile_image(updatePreferenceResponseModel.getUser_data().getProfile_image());
                                }

                                mViewModel.saveMyPreference(mUserModel);
                            }else{
                                Utility.showToast(getActivity(), "Unable to update profile picture at the moment");
                            }
                        }else{
                            Utility.showToast(getActivity(), Constants.SOMETHINGWENTWRONG);
                        }

//                        progressBar.setVisibility(View.GONE);
                    }
                });
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
        btnAboutMeSave.setVisibility(View.GONE);
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
        btnAboutMeSave.setVisibility(View.VISIBLE);
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
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis()-1000);
        datePickerDialog.show();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.backBtn:
                    Objects.requireNonNull(getActivity()).onBackPressed();
                break;
            case R.id.tabPreference:
                tabPreference.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                tabUserInfo.setTextColor(getResources().getColor(R.color.dark_grey));
                userInfo.setVisibility(View.GONE);
                prefrences.setVisibility(View.VISIBLE);
                break;
            case R.id.tabUserInfo:
                tabPreference.setTextColor(getResources().getColor(R.color.dark_grey));
                tabUserInfo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                userInfo.setVisibility(View.VISIBLE);
                prefrences.setVisibility(View.GONE);

                break;
            case R.id.et_dob:
                    setdatepicker();
                break;
            case R.id.tv_aboutme_edit:
                hideAboutTextViews();
                showAboutEditTexts();
                //change
                tvCancelAboutMe.setVisibility(View.VISIBLE);
                tvEditAboutMe.setVisibility(View.GONE);

                etName.setText(userName);
                if (TextUtils.isEmpty(userDob)){
                    etDob.setHint("Tap to update dob");
                }else{
                    if (userDob.contains("T")){
                        etDob.setText(userDob.split("T")[0]);
                    }else{
                        etDob.setText(userDob.split(" ")[0]);
                    }

                }

                if (TextUtils.isEmpty(userContact)){
                    etContact.setHint("Phone Number");

                }

                else{
                    etContact.setText(userContact);
                }
                break;
            case R.id.tv_aboutme_cancel:
                showAboutTextViews();
                hideAboutEditTexts();
                btnAboutMeSave.setVisibility(View.GONE);
                tvEditAboutMe.setVisibility(View.VISIBLE);
                tvCancelAboutMe.setVisibility(View.GONE);
                break;
            case R.id.tv_preference_edit:
                startActivity(new Intent(getActivity(), EditPreferenceActivity.class));
                getActivity().finish();
                break;
            case R.id.edit_image_vector:
                uploadImage();
                break;
            case R.id.btn_aboutme_save:

                tvCancelAboutMe.setVisibility(View.GONE);
                tvEditAboutMe.setVisibility(View.VISIBLE);

                String dob = etDob.getText().toString();
                String fullName =  etName.getText().toString();
                String contact = etContact.getText().toString().trim();
                Log.d("DOB0",userDob+"");
                //int phnumber= Integer.parseInt(etContact.getText().toString());
                //valdatn
                if ((contact.length()>11 || contact.length()<10)){
                    etContact.setError("enter valid phone number");
                    return;
                }
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
                break;

        }
    }

}
