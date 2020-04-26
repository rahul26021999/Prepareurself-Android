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
//    private TextView tvName, tvEmail;
//    private Button editbtn;
    LinearLayout l_userinfo, l_preferences;
    TextView  t_userinfo, t_preferences, tv_preference_edit, tv_aboutme_edit;
    TextView tv_dob, tv_name,tv_call;
    EditText et_dob,et_name, et_call;
    Button btn_save;
    DatePickerDialog datePickerDialog;
    TextView tv_email_profile;
    private PrefManager prefManager;
    private RecyclerView rvPreferences;
    private List<PreferredTechStack> userPrefrence = new ArrayList<>();
 //   private List<PreferredTechStack> allStack = new ArrayList<>();
    private UserPrefernceAdapter adapter;
    private TextView tvLoading;
    private Button btnUpdatePassword, btnLogout;
    private HashMap<Integer, PreferredTechStack> allPreferredStacks = new HashMap<>();

    private List<String> preferences=new ArrayList<>();
    private List<String> allStack=new ArrayList<>();



    private ViewPager viewPager;
    private TextView tvTopVideo, tvTopTheory;

    private CardView profileImageCard;

    private static final int INTENT_REQUEST_CODE = 100;

    private ProgressBar progressBar;
    private ImageView userImageView;
    private UserModel mUserModel;

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
        btn_save =view.findViewById(R.id.btn_aboutme);
        rvPreferences = view.findViewById(R.id.rv_user_preference);
        tvLoading = view.findViewById(R.id.tvLoading_preferences);
        btnLogout = view.findViewById(R.id.btn_logout);
        btnUpdatePassword = view.findViewById(R.id.btn_update_password);
        progressBar = view.findViewById(R.id.loadImage);
        userImageView = view.findViewById(R.id.user_image);



        viewPager = view.findViewById(R.id.view_pager_resources);
        tvTopVideo = view.findViewById(R.id.tv_resouce_heading_video);
        tvTopTheory = view.findViewById(R.id.tv_resouce_heading_theory);
        profileImageCard = view.findViewById(R.id.image_cardView_profile);

//        l_userinfo.setVisibility(View.VISIBLE);
//        l_preferences.setVisibility(View.GONE);

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

//        adapter = new UserPrefernceAdapter(getActivity());
//        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
//        rvPreferences.setLayoutManager(layoutManager);
//        rvPreferences.setAdapter(adapter);


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

        prefManager = new PrefManager(getActivity());

        progressBar.setVisibility(View.GONE);


        final SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(getActivity().getSupportFragmentManager());
        sectionsPagerAdapter.addFragment(UserInfoFragment.newInstance(),"User Info");
        sectionsPagerAdapter.addFragment(PreferenceFragment.newInstance(),"Preferences");
        viewPager.setAdapter(sectionsPagerAdapter);

        profileImageCard.setOnClickListener(new View.OnClickListener() {
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



//                tv_dob.setText(userModel.getDob());
//                tv_name.setText(userModel.getFirst_name() + " " + userModel.getLast_name());
//                tv_call.setText(userModel.getPhone_number());
//
//                et_dob.setText(userModel.getDob());
//                et_name.setText(userModel.getFirst_name() + " " + userModel.getLast_name());
//                et_call.setText(userModel.getPhone_number());
//
//                if (TextUtils.isEmpty(userModel.getDob())){
//                    tv_dob.setText("Click Edit to update your birthday");
//                    et_dob.setHint("Tap to update your birthday");
//                }
//
//                if (TextUtils.isEmpty(userModel.getPhone_number())){
//                    tv_call.setText("Click Edit to update your Contact");
//                    et_call.setHint("Enter your Contact Number");
//                }

            }
        });

//        btnLogout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                prefManager.saveBoolean(Constants.ISLOGGEDIN, false);
//                startActivity(new Intent(getActivity(), AuthenticationActivity.class));
//                getActivity().finish();
//            }
//        });
//
//        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(getActivity(), UpdatePasswordActivity.class));
//            }
//        });



//        t_userinfo.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                l_userinfo.setVisibility(View.VISIBLE);
//                l_preferences.setVisibility(View.GONE);
//                t_userinfo.setTextColor(getResources().getColor(R.color.blue));
//                t_preferences.setTextColor(getResources().getColor(R.color.grey));
//            }
//        });

//        t_preferences.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                l_userinfo.setVisibility(View.GONE);
//                l_preferences.setVisibility(View.VISIBLE);
//                t_userinfo.setTextColor(getResources().getColor(R.color.grey));
//                t_preferences.setTextColor(getResources().getColor(R.color.blue));
//            }
//        });
//        tv_aboutme_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                tv_dob.setVisibility(View.GONE);
//                tv_name.setVisibility(View.GONE);
//                tv_call.setVisibility(View.GONE);
//                et_dob.setVisibility(View.VISIBLE);
//                et_name.setVisibility(View.VISIBLE);
//                et_call.setVisibility(View.VISIBLE);
//                btn_save.setVisibility(View.VISIBLE);
//            }
//        });
//
//        et_dob.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setdatepicker();
//            }
//        });
//        tv_preference_edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(getActivity(), EditPreferenceActivity.class);
//                startActivity(intent);
//
//            }
//        });
//        btn_save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                btn_save.setText("Updating");
//
//
//                final String userdob=et_dob.getText().toString();
//                final String username=et_name.getText().toString();
//                final String userphnumber= et_call.getText().toString();
//
//                String firstName = "", lastName = "";
//                // first convert name to first name and last name
//                String[] name = Utility.splitName(getActivity(),username);
//                if (name.length>0){
//                    if (name.length == 1){
//                        firstName = name[0];
//                    }else if (name.length == 2){
//                        firstName = name[0];
//                        lastName = name[1];
//                    }else{
//                        firstName = name[0];
//                        lastName = name[name.length-1];
//                    }
//                }
//
//
//
//                mViewModel.updateUser(prefManager.getString(Constants.JWTTOKEN),firstName,lastName,userdob,userphnumber)
//                        .observe(getActivity(), new Observer<UpdatePreferenceResponseModel>() {
//                            @Override
//                            public void onChanged(UpdatePreferenceResponseModel updatePreferenceResponseModel) {
//                                if (updatePreferenceResponseModel!=null){
//                                    if (updatePreferenceResponseModel.getError_code() == 0){
//                                        btn_save.setText("Save");
//                                        Utility.showToast(getActivity(),"Profile updated successfully");
//                                        btn_save.setVisibility(View.GONE);
//
//                                        tv_dob.setVisibility(View.VISIBLE);
//                                        tv_name.setVisibility(View.VISIBLE);
//                                        tv_call.setVisibility(View.VISIBLE);
//                                        et_dob.setVisibility(View.GONE);
//                                        et_name.setVisibility(View.GONE);
//                                        et_call.setVisibility(View.GONE);
//
//                                        tv_dob.setText(userdob);
//                                        tv_name.setText(username);
//                                        tv_call.setText(userphnumber);
//
//
//                                    }else{
//                                        btn_save.setText("Save");
//                                        Utility.showToast(getActivity(),updatePreferenceResponseModel.getMsg());
//                                    }
//                                }else{
//                                    btn_save.setText("Save");
//                                    Utility.showToast(getActivity(),"Unable to update at the moment");
//                                }
//
//                            }
//                        });
//
//            }
//        });
//
//        mViewModel.getUserModelLiveData().observe(getActivity(), new Observer<UserModel>() {
//            @Override
//            public void onChanged(UserModel userModel) {
//                if(userModel.getPreferences()!=null) {
//                    preferences.addAll(Arrays.asList(userModel.getPreferences().split(",")));
//                }
//                mViewModel.getPreferencesFromDb().observe(getActivity(), new Observer<List<PreferredTechStack>>() {
//                    @Override
//                    public void onChanged(final List<PreferredTechStack> preferredTechStacks) {
//                        if (preferredTechStacks!=null && preferences!=null){
//                            int size = preferredTechStacks.size();
//                            Log.i("Size",""+size);
//                            for (int i = 0; i<preferences.size(); i++){
//                                for (PreferredTechStack p : preferredTechStacks){
//                                    if (p.getId() == Integer.parseInt(preferences.get(i))){
//                                        userPrefrence.add(p);
//                                    }
//                                }
//                            }
//
//                            adapter.setPreferredTechStacks(userPrefrence);
//                            adapter.notifyDataSetChanged();
//
//                        }
//                     }
//                });
//
//            }
//        });



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
                progressBar.setVisibility(View.VISIBLE);
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

                        progressBar.setVisibility(View.GONE);
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
