package com.app.brightLightBookStore.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app.brightLightBookStore.R;

import org.w3c.dom.Text;

public class CreateAccountActivity extends AppCompatActivity implements View.OnClickListener {
    TextView tvSignIn;
    Button RegisterBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        tvSignIn = findViewById(R.id.tvSignIn);
        RegisterBtn = findViewById(R.id.RegisterBtn);
        tvSignIn.setOnClickListener(this);
        RegisterBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.LoginBtn:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
            case R.id.CreateAccount:
                Toast.makeText(this, "Coming soon!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
            default:
                break;
        }
    }
}