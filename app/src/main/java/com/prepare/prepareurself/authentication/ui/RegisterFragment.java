package com.prepare.prepareurself.authentication.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.prepare.prepareurself.Home.ui.HomeActivity;
import com.prepare.prepareurself.R;
import com.prepare.prepareurself.authentication.data.model.AuthenticationResponseModel;
import com.prepare.prepareurself.authentication.data.model.Error;
import com.prepare.prepareurself.authentication.data.model.RegisterResponseModel;
import com.prepare.prepareurself.authentication.viewmodel.AuthViewModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    private EditText etFullname, etEmail, etPassword, etRetypePassword;
    private MaterialButton btnRegister;
    private String TAG="onclick of register button";
    private ProgressDialog dialog;
    private PrefManager prefManager;
    private AuthViewModel viewModel;
    private ImageView arrow;
    private TextView termsAndConditions;
    private RegisterFragmentListener listener;

    private String TermsUrl = "http://prepareurself.in/terms-and-conditions";

    public RegisterFragment() {
        // Required empty public constructor
    }

    public interface RegisterFragmentListener{
        void onRegisterArrowClicked();
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_register, container, false);

        arrow =v.findViewById(R.id.arrow);
        etFullname =v.findViewById(R.id.et_name);
        etEmail =v.findViewById(R.id.et_email);
        etPassword =v.findViewById(R.id.et_password);
        etRetypePassword =v.findViewById(R.id.et_repassword);
        btnRegister =v.findViewById(R.id.btn_register);
        termsAndConditions=v.findViewById(R.id.termsAndConditions);

        btnRegister.setOnClickListener(this);
        termsAndConditions.setOnClickListener(this);
        arrow.setOnClickListener(this);

        prefManager = new PrefManager(this.getActivity());
        dialog = new ProgressDialog(this.getActivity());
     
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
            //Toast.makeText(getContext(),"arrow clicked",Toast.LENGTH_SHORT).show();
            listener.onRegisterArrowClicked();
        }
        else if(v.getId()==R.id.termsAndConditions){
//            Toast.makeText(getContext(),"TermsAndConditionClicked",Toast.LENGTH_SHORT).show();
            Utility.redirectUsingCustomTab(getActivity(),TermsUrl);

        }
        else if (v.getId() == R.id.btn_register ){

            String str_password= etPassword.getText().toString().trim();
            String str_retype_password= etRetypePassword.getText().toString().trim();
            final String str_email= etEmail.getText().toString().trim();

            if(TextUtils.isEmpty(etFullname.getText().toString().trim())){
                //Toast.makeText(this.getActivity(),"you did not enter value",Toast.LENGTH_SHORT).show();
                etFullname.setError(Constants.CANNOTBEEMPTY);
                return;
            }
            if(TextUtils.isEmpty(etEmail.getText().toString().trim())){
                etEmail.setError(Constants.CANNOTBEEMPTY);
                return;
            }
            if(!Utility.isValidEmail(str_email)){
                etEmail.setError(Constants.INVALIDEMAIL);
                return;
            }
            if(TextUtils.isEmpty(etPassword.getText().toString())){
                etPassword.setError(Constants.CANNOTBEEMPTY);
                return;
            }
            if(TextUtils.isEmpty(etRetypePassword.getText().toString())){
                etRetypePassword.setError(Constants.CANNOTBEEMPTY);
                return;
            }
            if (!TextUtils.isEmpty(etPassword.getText().toString().trim()) && !TextUtils.isEmpty(str_retype_password.trim())){
                if(etPassword.getText().toString().length()<8) {
                    etPassword.setError(Constants.PASSWORDMUSTBEATLEAST8CHARACTERS);
                    return;
                }
                if (!str_password.equals(str_retype_password)){ ///use .equals functon rather than comparing
                    etRetypePassword.setError(Constants.PASSWORDNOTMATCHED);
                    return;
                }
            }

            // here we will have to check for the uniqueness of username, which will be done in next api integration

            String fullname = etFullname.getText().toString().trim();
            String firstName = "", lastName = "";
            // first convert name to first name and last name
            String[] name = Utility.splitName(this.getActivity(),fullname);
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

            Log.d("name_debug",firstName + " "+ lastName);

            String androidToken = prefManager.getString(Constants.ANDROIDTOKEN);

            viewModel.register(firstName, lastName, str_email, str_password, prefManager.getString(Constants.ANDROIDTOKEN));


            showLoader();

            viewModel.getRegisterResponseModelLiveData().observe(getActivity(), new Observer<RegisterResponseModel>() {
                @Override
                public void onChanged(RegisterResponseModel authenticationResponseModel) {
                    if (authenticationResponseModel!=null){
                        if (authenticationResponseModel.isSuccess()){
                            Utility.showToast(getActivity(),"Registration done!");
                            Intent intent = new Intent(getActivity(), VerifyEmailActivity.class);
                            intent.putExtra(Constants.USEREMAIL,str_email);
                            startActivity(intent);
                            getActivity().finish();
                        }else{
                            Error error  = authenticationResponseModel.getErrors();
                            if (error!=null){
                                if (error.getPassword()!=null){
                                    Utility.showToast(getActivity(),error.getPassword().get(0));
                                }

                                if (error.getEmail()!=null){
                                    etFullname.setError(null);
                                    etPassword.setError(null);
                                    etRetypePassword.setError(null);
                                    etEmail.setError(error.getEmail().get(0));
                                }

                                if (error.getFirst_name()!=null){
                                    Utility.showToast(getActivity(),error.getFirst_name().get(0));
                                }
                            }else{
                                Utility.showToast(getActivity(),Constants.SOMETHINGWENTWRONG);
                            }

                        }
                    }else{
                        Utility.showToast(getActivity(),Constants.SOMETHINGWENTWRONG);
                    }

                    hideLoader();
                }
            });

        }

    }
    

    private void showLoader(){
        if (dialog!=null && !dialog.isShowing()){
            dialog.setMessage("User is being created...");
            dialog.show();
        }
    }

    private void hideLoader(){
        if (dialog!=null &&  dialog.isShowing()){
            dialog.hide();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (RegisterFragmentListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement RegisterFragmentListener");
        }

    }

}
