package com.app.brightLightBookStore.activities.User;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.helpers.BaseActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.view.Change;

public class UserProfile extends BaseActivity {
    EditText etName,etEmail,etMobile,etAddress,etAge,etGender;
    Button btnSave;
    ImageView ivBack;
    TextView tvChangePass;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etMobile = findViewById(R.id.etMobile);
        etAddress = findViewById(R.id.etAddress);
        etAge = findViewById(R.id.etAge);
        etGender = findViewById(R.id.etGender);
        btnSave = findViewById(R.id.btnSave);
        ivBack = findViewById(R.id.ivBack);
        tvChangePass = findViewById(R.id.tvChangePass);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnSave.setOnClickListener(view->{
            Toast.makeText(this, "Details Updated!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), DashboardUserActivity.class));
        });

        ivBack.setOnClickListener(view->{
            super.onBackPressed();
        });

        tvChangePass.setOnClickListener(view->{
            startActivity(new Intent(getApplicationContext(), ChangePassword.class));
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
