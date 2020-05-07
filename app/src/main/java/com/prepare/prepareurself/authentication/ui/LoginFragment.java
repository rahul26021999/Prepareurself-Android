package com.prepare.prepareurself.authentication.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
//import android.support.v4.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.prepare.prepareurself.Home.ui.HomeActivity;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.authentication.data.model.AuthenticationResponseModel;
import com.prepare.prepareurself.authentication.data.model.ForgotPasswordResponseModel;
import com.prepare.prepareurself.authentication.viewmodel.AuthViewModel;
import com.prepare.prepareurself.courses.ui.activity.ProjectsActivity;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.ui.VideoActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment implements View.OnClickListener{
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private String TAG="login button";
    private ProgressDialog progressDialog;
    private PrefManager prefManager;
    private TextView tvForgotPassword;
    private ImageView arrow;

    private AuthViewModel viewModel;

    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_login, container, false);

        etEmail =v.findViewById(R.id.et_email);
        etPassword =v.findViewById(R.id.et_password);
        btnLogin =v.findViewById(R.id.btn_login);
        arrow =v.findViewById(R.id.arrow);
        tvForgotPassword = v.findViewById(R.id.tv_forgot_password);
        btnLogin.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);
        progressDialog=new ProgressDialog(this.getActivity());
        prefManager=new PrefManager(this.getActivity());


        arrow.setOnClickListener(this);

        return v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        viewModel = ViewModelProviders.of(this).get(AuthViewModel.class);

    }

    @Override
    public void onClick(View v) {

        if(v.getId()==R.id.arrow){

        }
        if (v.getId() == R.id.tv_forgot_password){
            Intent i = new Intent(getActivity(), forgetPassword.class);
            startActivity(i);
            //changeFragment();
            /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setTitle("Reset your Password");
            final View customLayout = getLayoutInflater().inflate(R.layout.forgot_password_dialog_layout, null);
            alertDialog.setView(customLayout);

            final Button button = customLayout.findViewById(R.id.btn_submit_forgot_pass);
            final EditText editText = customLayout.findViewById(R.id.et_forgot_password_email);
            final TextView textView = customLayout.findViewById(R.id.tv_forgot_pass_info);
            textView.setVisibility(View.GONE);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String email = editText.getText().toString().trim();

                    if (TextUtils.isEmpty(email)){
                        editText.setError(Constants.CANNOTBEEMPTY);
                        return;
                    }

                    if (!Utility.isValidEmail(email)){
                        editText.setError(Constants.INVALIDEMAIL);
                        return;
                    }

                    button.setText("Submitting");

                    editText.setError(null);
                    //
                    viewModel.forgotPassword(email).observe(getActivity(), new Observer<ForgotPasswordResponseModel>() {
                        @Override
                        public void onChanged(ForgotPasswordResponseModel forgotPasswordResponseModel) {
                            if (forgotPasswordResponseModel!=null){
                                if (forgotPasswordResponseModel.getError_code() == 0){
                                    textView.setVisibility(View.VISIBLE);
                                    textView.setText("Please go and check your email");
                                }else{
                                    textView.setVisibility(View.VISIBLE);
                                    textView.setText(forgotPasswordResponseModel.getMessage());
                                }
                            }else{
                                textView.setVisibility(View.GONE);
                                Utility.showToast(getActivity(),Constants.SOMETHINGWENTWRONG);
                            }
                            button.setText("Submit");
                        }

                    });
                }
            });

            AlertDialog alert = alertDialog.create();
            alert.setCanceledOnTouchOutside(true);
            alert.show();*/
        }

        if (v.getId()==R.id.btn_login){
            String strEmail= etEmail.getText().toString().trim();
            String strPassword= etPassword.getText().toString().trim();
            if(TextUtils.isEmpty(strEmail)){
                etEmail.setError(Constants.CANNOTBEEMPTY);
                return;
            }
            if(!isValidEmail(strEmail)){
                etEmail.setError(Constants.INVALIDEMAIL);
                return;
            }
            if(TextUtils.isEmpty(strPassword)){
                etPassword.setError(Constants.CANNOTBEEMPTY);
                return;
            }

//            String androidToken = prefManager.getString(Constants.ANDROIDTOKEN);

            String androidToken = Utility.getOneSignalId();

            prefManager.saveString(Constants.ANDROIDTOKEN, androidToken);

            if (TextUtils.isEmpty(androidToken)){
                return;
            }

            viewModel.login(strEmail,strPassword, androidToken);

            showLoader();


            viewModel.getAuthenticationResponseModelMutableLiveData().observe(getActivity(), new Observer<AuthenticationResponseModel>() {
                @Override
                public void onChanged(AuthenticationResponseModel authenticationResponseModel) {
                    if (authenticationResponseModel!=null){
                        if (authenticationResponseModel.getError_code()==0){
                            prefManager.saveBoolean(Constants.ISLOGGEDIN, true);
                            prefManager.saveString(Constants.JWTTOKEN,authenticationResponseModel.getToken());
                            Utility.showToast(getActivity(),"Login done!");

                            if (AuthenticationActivity.resourceId!=-1){
                                Intent intent=new Intent(getActivity(), VideoActivity.class);
                                intent.putExtra(Constants.DEEPSHAREVIDEOAFTERLOGIN,true);
                                intent.putExtra(Constants.RESOURCEID, AuthenticationActivity.resourceId);
                                startActivity(intent);
                                getActivity().finish();
                            }else if (AuthenticationActivity.projectId!=-1){
                                Intent intent=new Intent(getActivity(), ProjectsActivity.class);
                                intent.putExtra(Constants.DEEPSHAREPROECTAFTERLOGIN,true);
                                intent.putExtra(Constants.PROJECTID, AuthenticationActivity.projectId);
                                startActivity(intent);
                                getActivity().finish();
                            } else if (AuthenticationActivity.FEEDBACKSHARE){

                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                intent.putExtra(Constants.FEEDBACKSHAREINTENT, true);
                                startActivity(intent);
                                getActivity().finish();

                            }else if (AuthenticationActivity.PROFILESHARE){

                                Intent intent = new Intent(getActivity(), HomeActivity.class);
                                intent.putExtra(Constants.PROFILESHAREINTENT, true);
                                startActivity(intent);
                                getActivity().finish();

                            }
                            else{
                                Intent intent=new Intent(getActivity(), HomeActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        }else{
                            Utility.showToast(getActivity(),authenticationResponseModel.getMessage());
                        }
                    }else{
                        Utility.showToast(getActivity(),Constants.SOMETHINGWENTWRONG);
                    }

                    hideLoader();
                }
            });
        }

}
    private boolean isValidEmail(CharSequence str_email){
        return(Patterns.EMAIL_ADDRESS.matcher(str_email).matches());
    }

    private void showLoader(){
        if (progressDialog!=null && !progressDialog.isShowing()){
            progressDialog.setMessage("Logging In...");
            progressDialog.show();
        }
    }

    private void hideLoader(){
        if (progressDialog!=null &&  progressDialog.isShowing()){
            progressDialog.hide();
        }
    }
   /* private void changeFragment(){
        fragement_forgetpassword newfragment = new fragement_forgetpassword();
        FragmentTransaction fragmentTransaction=getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainlayout,newfragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        Log.d("fraggg","show");
    }*/
}
