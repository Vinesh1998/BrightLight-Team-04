package com.app.brightLightBookStore.activities;

import static android.content.ContentValues.TAG;
import static com.app.brightLightBookStore.helpers.common_helper.getAdminLogin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.activities.Admin.DashboardAdminActivity;
import com.app.brightLightBookStore.activities.User.DashboardUserActivity;
import com.app.brightLightBookStore.helpers.BaseActivity;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    Button btnLogin;
    TextView tvCreateAccount, forgotPassword, tvPassError;
    EditText etEmail, etPassword;
    CheckBox chBoxPass;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        chBoxPass = findViewById(R.id.chBoxPass);
        btnLogin = findViewById(R.id.LoginBtn);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        tvPassError = findViewById(R.id.tvPassError);
        tvCreateAccount = findViewById(R.id.CreateAccount);
        forgotPassword = findViewById(R.id.forgotPassword);
        mAuth = FirebaseAuth.getInstance();
        btnLogin.setOnClickListener(this);
        tvCreateAccount.setOnClickListener(this);
        forgotPassword.setOnClickListener(this);

        chBoxPass.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!isChecked){
                chBoxPass.setText("Show Password");
                etPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }else{
                chBoxPass.setText("Hide Password");
                etPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.LoginBtn:
                String stEmail = etEmail.getText().toString();
                String stPassword = etPassword.getText().toString();
                tvPassError.setVisibility(View.GONE);

                if(getAdminLogin(stEmail,stPassword))
                    startActivity(new Intent(getApplicationContext(), DashboardAdminActivity.class));

                if(stEmail.isEmpty()) etEmail.setError("Required");
                else if(!android.util.Patterns.EMAIL_ADDRESS.matcher(etEmail.getText().toString()).matches())
                    etEmail.setError("invalid email!");
                else if(stPassword.isEmpty()) etEmail.setError("Required");

                String passwordInput = stPassword;
                if (!passwordInput.matches(".*[0-9].*")) {
                    etPassword.setError("Password should contain at least 1 digit");
                    tvPassError.setVisibility(View.VISIBLE);
                }
                else if (!passwordInput.matches(".*[a-z].*")) {
                    etPassword.setError("Password should contain at least 1 lower case letter");
                    tvPassError.setVisibility(View.VISIBLE);
                }
                else if (!passwordInput.matches(".*[A-Z].*")) {
                    etPassword.setError("Password should contain at least 1 upper case letter");
                    tvPassError.setVisibility(View.VISIBLE);
                }
                else if (!passwordInput.matches(".*[a-zA-Z].*")) {
                    etPassword.setError("Password should contain a letter");
                    tvPassError.setVisibility(View.VISIBLE);
                } else if (!passwordInput.matches(".*[!@#$%&*_].*")) {
                    etPassword.setError("Password should contain a special char");
                    tvPassError.setVisibility(View.VISIBLE);
                }
                else if (!passwordInput.matches( ".{8,}")) {
                    etPassword.setError("Password should contain 8 characters!");
                    tvPassError.setVisibility(View.VISIBLE);
                }else{
                       signIn(etEmail.getText().toString().trim(), etPassword.getText().toString().trim(), v);
                }
                break;
            case R.id.CreateAccount:
                startActivity(new Intent(getApplicationContext(), CreateAccountActivity.class));
                break;
                case R.id.forgotPassword:
                showPopUp(v);
                break;
            default:
                break;
        }
    }
    private void signIn(String email, String password,View view) {
        Log.d(TAG, "signIn:" + email);
        showProgressDialog();
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
                    // If sign in fails, display a message to the user. If sign in succeeds
                    // the auth state listener will be notified and logic to handle the
                    // signed in user can be handled in the listener.
                    if (!task.isSuccessful()) {
                        Log.w(TAG, "signInWithEmail", task.getException());
                        Snackbar.make(view, "Invalid Credentials!!", Snackbar.LENGTH_SHORT)
                                .setAction("OK", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view1) {
                                    }
                                })
                                .setActionTextColor(getResources().getColor(R.color.red))
                                .show();
                    }
                    else{
                        startActivity(new Intent(getApplicationContext(),DashboardUserActivity.class));
                    }

                    // [START_EXCLUDE]
                    hideProgressDialog();
                    // [END_EXCLUDE]
                });
        // [END sign_in_with_email]
    }
    public void showPopUp(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater layoutInflater = getLayoutInflater();

        //this is custom dialog
        //custom_popup_dialog contains textview only
        View customView = layoutInflater.inflate(R.layout.activity_forget_password, null);
        // reference the textview of custom_popup_dialog
        Button sendBtn = customView.findViewById(R.id.sendBtn);
        EditText etEmail = customView.findViewById(R.id.etForgetEmail);

        sendBtn.setOnClickListener(v->{
            String stEmail = etEmail.getText().toString();

            if(stEmail.isEmpty()){
                etEmail.setError("Required");
            }else {
                mAuth.sendPasswordResetEmail(stEmail).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginActivity.this, "Link sent", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    } else {
                        etEmail.setError("User not available!");
                        Toast.makeText(LoginActivity.this, "User not available!", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(e -> Toast.makeText(LoginActivity.this, "Error Failed", Toast.LENGTH_LONG).show());
            }
        });
        builder.setView(customView);
        builder.create();
        builder.show();
    }
}
//This is the code for loginactivity functionality