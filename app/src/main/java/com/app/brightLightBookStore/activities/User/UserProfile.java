package com.app.brightLightBookStore.activities.User;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.activities.LoginActivity;
import com.app.brightLightBookStore.helpers.BaseActivity;
import com.app.brightLightBookStore.model.Book;
import com.app.brightLightBookStore.model.User;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.Change;

public class UserProfile extends BaseActivity {
    EditText etName,etEmail,etMobile,etAddress,etAge,etGender;
    Button btnSave;
    ImageView ivBack;
    TextView tvChangePass;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    String address, mobile, gender;
    int age;
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
        mDatabase = FirebaseDatabase.getInstance().getReference("users");
        getUser(mAuth.getUid());
        btnSave.setOnClickListener(view->{
            validate();

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
    boolean flag = false;
    User user = null;
    public void getUser(String uid){
        mDatabase.child(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot ds) {
                        try {
                            flag = true;
                            String id = ds.child("id").getValue(String.class);
                            String name = ds.child("name").getValue(String.class);
                            String email = ds.child("email").getValue(String.class);
                            String address = ds.child("address").getValue(String.class);
                            String mobile = ds.child("mobile").getValue(String.class);
                            String gender = ds.child("gender").getValue(String.class);
                            int age = ds.child("age").getValue(Integer.class);
                            user = new User(id, name, email, mobile, gender, address, age);
                            displayData(user);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    public void validate(){
       address = etAddress.getText().toString();
       age = Integer.parseInt(etAge.getText().toString());
       mobile = etMobile.getText().toString();
       gender = etGender.getText().toString();

       if(mobile.isEmpty()) {
           etAddress.setError("Required!");
           return;
       }
       else if(age < 1 ){
           etAddress.setError("Required!");
           return;
       }
       else if(gender.isEmpty()){
           etAddress.setError("Required!");
           return;
       }
       else if(address.isEmpty()){
           etAddress.setError("Required!");
           return;
       }
       updateDetails();
    }

    private void displayData(User user){
        if(!flag){
            Toast.makeText(this, "invalid user!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }

        etName.setText(user.getName());
        etEmail.setText(user.getEmail());
        etMobile.setText(user.getMobile());
        etAge.setText(user.getAge()+"");
        etGender.setText(user.getGender());
        etAddress.setText(user.getAddress());
    }

    private void updateDetails(){
        address = etAddress.getText().toString();
        age = Integer.parseInt(etAge.getText().toString());
        mobile = etMobile.getText().toString();
        gender = etGender.getText().toString();
        user = new User(mAuth.getUid(),etName.getText().toString(),user.getEmail(),mobile,gender,address,age);
        mDatabase.child(mAuth.getUid().toString()).setValue(user);
        Toast.makeText(this, "Updated!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(getApplicationContext(), UserProfile.class));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
