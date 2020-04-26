package com.prepare.prepareurself.profile.ui.fragments;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.prepare.prepareurself.authentication.ui.AuthenticationActivity;
import com.prepare.prepareurself.profile.data.model.MyPreferenceTechStack;
import com.prepare.prepareurself.profile.data.model.PreferredTechStack;
import com.prepare.prepareurself.profile.data.model.UpdatePreferenceResponseModel;
import com.prepare.prepareurself.profile.ui.EditPreferenceActivity;
import com.prepare.prepareurself.profile.ui.activity.UpdatePasswordActivity;
import com.prepare.prepareurself.profile.ui.adapter.UserPrefernceAdapter;
import com.prepare.prepareurself.profile.viewmodel.ProfileViewModel;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.authentication.data.model.UserModel;
import com.prepare.prepareurself.resources.ui.adapter.SectionsPagerAdapter;
import com.prepare.prepareurself.resources.ui.fragments.TheoryResourceFragment;
import com.prepare.prepareurself.resources.ui.fragments.VideoResourceFragment;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private ProfileViewModel mViewModel;

    TextView tv_email_profile;
    private PrefManager prefManager;


    private ViewPager viewPager;
    private TextView tvTopVideo, tvTopTheory;

    private static final int INTENT_REQUEST_CODE = 100;

    private ImageView userImageView;
    private UserModel mUserModel;

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profile_fragment, container, false);

        tv_email_profile = view.findViewById(R.id.tv_email_profile);
        userImageView = view.findViewById(R.id.user_image);



        viewPager = view.findViewById(R.id.view_pager_resources);
        tvTopVideo = view.findViewById(R.id.tv_resouce_heading_video);
        tvTopTheory = view.findViewById(R.id.tv_resouce_heading_theory);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ProfileViewModel.class);

        prefManager = new PrefManager(getActivity());


        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(UserInfoFragment.newInstance(),"User Info");
        sectionsPagerAdapter.addFragment(PreferenceFragment.newInstance(),"Preferences");
        viewPager.setAdapter(sectionsPagerAdapter);

        userImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position==0){
                    tvTopVideo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    tvTopTheory.setTextColor(getResources().getColor(R.color.lightgrey));
                }else if (position == 1){
                    tvTopVideo.setTextColor(getResources().getColor(R.color.lightgrey));
                    tvTopTheory.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }

            @Override
            public void onPageSelected(int position) {
                if (position==0){
                    tvTopVideo.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    tvTopTheory.setTextColor(getResources().getColor(R.color.lightgrey));
                }else if (position == 1){
                    tvTopVideo.setTextColor(getResources().getColor(R.color.lightgrey));
                    tvTopTheory.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tvTopVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0, true);
            }
        });


        tvTopTheory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1, true);
            }
        });

        mViewModel.getUserModelLiveData().observe(getActivity(), new Observer<UserModel>() {
            @Override
            public void onChanged(UserModel userModel) {
                String name = userModel.getFirst_name() + " " + userModel.getLast_name();
                /*tvName.setText(name);
                tvEmail.setText(userModel.getEmail());*/
                mUserModel = userModel;
                tv_email_profile.setText(userModel.getEmail());
                Glide.with(getActivity())
                        .load(
                                Constants.USERIMAGEBASEURL + userModel.getProfile_image())
                        .placeholder(R.drawable.person_placeholder)
                        .transition(GenericTransitionOptions.<Drawable>with(Utility.getAnimationObject()))
                        .into(userImageView);

            }
        });
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

        if (requestCode == INTENT_REQUEST_CODE){
            if (resultCode == Activity.RESULT_OK){
//                progressBar.setVisibility(View.VISIBLE);
                try {
                    if (getActivity()!=null && data!=null && data.getData()!=null) {
                        InputStream is = getActivity().getContentResolver().openInputStream(data.getData());
                        if (is!=null)
                            uploadImageToServer(getBytes(is));
                    }

                }catch (IOException e){
                    e.printStackTrace();
                }
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


    public void uploadImageToServer(byte[] imageBytes){
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_resouce_heading_theory:
                viewPager.setCurrentItem(1, true);
                break;
            case R.id.tv_resouce_heading_video:
                viewPager.setCurrentItem(0, true);
                break;
        }
    }

}
