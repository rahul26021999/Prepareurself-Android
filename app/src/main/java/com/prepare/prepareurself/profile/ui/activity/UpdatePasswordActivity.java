package com.prepare.prepareurself.profile.ui.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.prepare.prepareurself.R;
import com.prepare.prepareurself.profile.data.model.UpdatePasswordResponseModel;
import com.prepare.prepareurself.profile.viewmodel.ProfileViewModel;
import com.prepare.prepareurself.utils.Constants;
import com.prepare.prepareurself.utils.PrefManager;
import com.prepare.prepareurself.utils.Utility;

public class UpdatePasswordActivity extends AppCompatActivity {

    private EditText etOldPassword, etNewPassword, etConfirmPassword;

    TextView btn_backtoLogin;
    private Button btnUpdate;
    private ProfileViewModel viewModel;

    String oldPass = "", newPass = "", confirmPass = "";

    private PrefManager prefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        viewModel = new ViewModelProvider(this).get(ProfileViewModel.class);

        prefManager = new PrefManager(this);

        etOldPassword = findViewById(R.id.et_old_password);
        etNewPassword = findViewById(R.id.et_new_password);
        etConfirmPassword = findViewById(R.id.et_confrm_password);
        btnUpdate = findViewById(R.id.btn_update);
        btn_backtoLogin = findViewById(R.id.btn_back);

//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
//        getSupportActionBar().setTitle("Update Password");
        btn_backtoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oldPass = etOldPassword.getText().toString().trim();
                newPass = etNewPassword.getText().toString().trim();
                confirmPass = etConfirmPassword.getText().toString().trim();

                if (TextUtils.isEmpty(oldPass)){
                    etOldPassword.setError(Constants.CANNOTBEEMPTY);
                    return;
                }

                if (TextUtils.isEmpty(newPass)){
                    etNewPassword.setError(Constants.CANNOTBEEMPTY);
                    return;
                }

                if (TextUtils.isEmpty(confirmPass)){
                    etConfirmPassword.setError(Constants.CANNOTBEEMPTY);
                    return;
                }

                if (newPass.length()<8){
                    etOldPassword.setError(null);
                    etNewPassword.setError(null);
                    etConfirmPassword.setError(null);
                    etNewPassword.setError(Constants.PASSWORDMUSTBEATLEAST8CHARACTERS);
                }


                if (!confirmPass.equals(newPass)){
                    etOldPassword.setError(null);
                    etNewPassword.setError(null);
                    etConfirmPassword.setError(null);
                    etConfirmPassword.setError(Constants.PASSWORDNOTMATCHED);

                    return;
                }

                btnUpdate.setText("Updating");

                viewModel.updatePassword(prefManager.getString(Constants.JWTTOKEN),oldPass, newPass)
                        .observe(UpdatePasswordActivity.this, new Observer<UpdatePasswordResponseModel>() {
                            @Override
                            public void onChanged(UpdatePasswordResponseModel updatePasswordResponseModel) {
                                btnUpdate.setText("Update Password");
                                if (updatePasswordResponseModel!=null){
                                    if (updatePasswordResponseModel.getError_code() == 0){
                                        prefManager.saveString(Constants.JWTTOKEN,updatePasswordResponseModel.getToken());
                                        Utility.showToast(UpdatePasswordActivity.this,"Password updates successfully");
                                        finish();
                                    }else{
                                        Utility.showToast(UpdatePasswordActivity.this,updatePasswordResponseModel.getMessage());
                                    }
                                }else{
                                    Utility.showToast(UpdatePasswordActivity.this, "Unable to update at the moment");
                                }
                            }
                        });


            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
