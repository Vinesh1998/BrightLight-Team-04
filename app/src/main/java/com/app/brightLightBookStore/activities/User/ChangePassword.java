package com.app.brightLightBookStore.activities.User;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.activities.LoginActivity;
import com.app.brightLightBookStore.helpers.BaseActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ChangePassword extends BaseActivity {
    EditText etOldPass,etNewPass,etConfPass;
    Button btUpdate;
    ImageView ivBack;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    CheckBox chBoxPass;
    TextView tvPassHint;
    private FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        etOldPass = findViewById(R.id.etOldPass);
        chBoxPass = findViewById(R.id.chBoxPass);
        etNewPass = findViewById(R.id.etNewPass);
        etConfPass = findViewById(R.id.etConfPass);
        ivBack = findViewById(R.id.ivBack);
        btUpdate = findViewById(R.id.btUpdate);
        tvPassHint = findViewById(R.id.tvPassHint);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        chBoxPass.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(!isChecked){
                chBoxPass.setText("Show Password");
                etOldPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                etNewPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                etConfPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            }else{
                chBoxPass.setText("Hide Password");
                etOldPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                etNewPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                etConfPass.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            }
        });
        btUpdate.setOnClickListener(view->{
            if(etOldPass.getText().toString().isEmpty()) {
                etOldPass.setError("invalid Old-Password");
                etOldPass.setFocusable(true);
                return;
            }
            else if(etNewPass.getText().toString().isEmpty()) {
                etNewPass.setError("invalid new-Password");
                etNewPass.setFocusable(true);
                return;
            }

            String passwordInput = etNewPass.getText().toString();
            if (!passwordInput.matches(".*[0-9].*")) {
                etNewPass.setError("Password should contain at least 1 digit");
                etNewPass.setFocusable(true);
                tvPassHint.setVisibility(View.VISIBLE);
                return;
            }
            else if (!passwordInput.matches(".*[a-z].*")) {
                etNewPass.setError("Password should contain at least 1 lower case letter");
                etNewPass.setFocusable(true);
                tvPassHint.setVisibility(View.VISIBLE);
                return;
            }
            else if (!passwordInput.matches(".*[A-Z].*")) {
                etNewPass.setError("Password should contain at least 1 upper case letter");
                etNewPass.setFocusable(true);
                tvPassHint.setVisibility(View.VISIBLE);
                return;
            }
            else if (!passwordInput.matches(".*[a-zA-Z].*")) {
                etNewPass.setError("Password should contain a letter");
                etNewPass.setFocusable(true);
                tvPassHint.setVisibility(View.VISIBLE);
                return;
            } else if (!passwordInput.matches(".*[!@#$%&*_].*")) {
                etNewPass.setError("Password should contain a special char");
                etNewPass.setFocusable(true);
                tvPassHint.setVisibility(View.VISIBLE);
                return;
            }
            else if (!passwordInput.matches( ".{8,}")) {
                etNewPass.setError("Password should contain 8 characters!");
                etNewPass.setFocusable(true);
                tvPassHint.setVisibility(View.VISIBLE);
                return;
            }
            else if(etConfPass.getText().toString().isEmpty()) {
                etConfPass.setError("invalid conform password!");
                etConfPass.setFocusable(true);
                return;
            }
            else if(!etNewPass.getText().toString().equals(etConfPass.getText().toString())) {
                etConfPass.setError("password mismatch!");
                etConfPass.setFocusable(true);
                return;
            }

            user = FirebaseAuth.getInstance().getCurrentUser();
            final String email = user.getEmail();
            AuthCredential credential = EmailAuthProvider.getCredential(email,etOldPass.getText().toString());

            user.reauthenticate(credential).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    user.updatePassword(etNewPass.getText().toString()).addOnCompleteListener(task1 -> {
                        if(!task1.isSuccessful()){
                            Toast.makeText(ChangePassword.this, "unable to change the password!!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(ChangePassword.this, "Changed successfully!!!", Toast.LENGTH_SHORT).show();
                            mAuth.signOut();
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        }
                    });
                }else {
                    Toast.makeText(ChangePassword.this, "Authentication Failed!!!", Toast.LENGTH_SHORT).show();
                }
            });
        });

        ivBack.setOnClickListener(view->{
            super.onBackPressed();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
//This module deals with the change password functionality
