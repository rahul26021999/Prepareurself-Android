package com.example.prepareurself.authentication.registration.view;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.prepareurself.Apiservice.ApiRepository;
import com.example.prepareurself.R;
import com.example.prepareurself.authentication.registration.model.CheckUsernameResponse;
import com.example.prepareurself.authentication.registration.model.RegisterResponseModel;
import com.example.prepareurself.authentication.registration.presenter.RegisterPresenter;
import com.example.prepareurself.utils.Constants;
import com.example.prepareurself.utils.PrefManager;
import com.example.prepareurself.utils.Utility;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment implements View.OnClickListener, RegisterViewAction {
    private EditText etFullname, etEmail, etPassword, etRetypePassword, etUsername;
    private Button btnRegister;
    private String TAG="onclick of register button";
    private RegisterPresenter presenter;
    private ProgressDialog dialog;
    private PrefManager prefManager;
    public RegisterFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v=inflater.inflate(R.layout.fragment_register, container, false);

        etFullname =v.findViewById(R.id.et_name);
        etEmail =v.findViewById(R.id.et_email);
        etPassword =v.findViewById(R.id.et_password);
        etRetypePassword =v.findViewById(R.id.et_repassword);
        btnRegister =v.findViewById(R.id.btn_register);
        etUsername = v.findViewById(R.id.et_username);
        btnRegister.setOnClickListener(this);

        prefManager = new PrefManager(this.getActivity());
        dialog = new ProgressDialog(this.getActivity());
        presenter = new RegisterPresenter(this.getActivity(),this,new ApiRepository());

        return v;

    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_register ){

            String str_password= etPassword.getText().toString().trim();
            String str_retype_password= etRetypePassword.getText().toString().trim();
            String str_email= etEmail.getText().toString().trim();

            if(TextUtils.isEmpty(etFullname.getText().toString().trim())){
                //Toast.makeText(this.getActivity(),"you did not enter value",Toast.LENGTH_SHORT).show();
                etFullname.setError(Constants.CANNOTBEEMPTY);
                return;
            }
            if(TextUtils.isEmpty(etUsername.getText().toString().trim())){
                //Toast.makeText(this.getActivity(),"you did not enter value",Toast.LENGTH_SHORT).show();
                etUsername.setError(Constants.CANNOTBEEMPTY);
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

            String username = etUsername.getText().toString().trim();

            presenter.checkUsernameAndRegister(firstName,lastName,username,str_password,str_email);
        }

    }


    @Override
    public void onRegistrationSuccess(RegisterResponseModel registerResponseModel) {
        prefManager.saveBoolean(Constants.ISLOGGEDIN, true);
        // save the user data to prefmanager also

        prefManager.saveString(Constants.USERFIRSTNAME,registerResponseModel.user_data.first_name);
        prefManager.saveString(Constants.USERLASTNAME,registerResponseModel.user_data.last_name);
        prefManager.saveString(Constants.USEREMAIL,registerResponseModel.user_data.email);
        prefManager.saveString(Constants.USERPASSWORD,registerResponseModel.user_data.password);
        prefManager.saveString(Constants.USER_USERNAME,registerResponseModel.user_data.username);
        prefManager.saveInteger(Constants.USERID,registerResponseModel.user_data.id);

        Utility.showToast(this.getActivity(),"Registration done!");

    }

    @Override
    public void onFailure(String errorMsg) {
        Utility.showToast(this.getActivity(),errorMsg);
    }

    @Override
    public void showLoader() {
        if (dialog!=null && !dialog.isShowing()){
            dialog.setMessage("User is being created...");
            dialog.show();
        }
    }

    @Override
    public void hideLoader() {
        if (dialog!=null && dialog.isShowing()){
            dialog.hide();
        }
    }

}
