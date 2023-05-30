package com.app.brightLightBookStore.activities.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.activities.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardUserActivity extends AppCompatActivity {
    ImageView ivCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        ivCart = findViewById(R.id.ivCart);
        ivCart.setOnClickListener(view->{
            Toast.makeText(this, "Cart coming soon..!", Toast.LENGTH_SHORT).show();
        });
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:
                        Toast.makeText(DashboardUserActivity.this, "home!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_books:
                        Toast.makeText(DashboardUserActivity.this, "my books coming soon!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_notifications:
                        Toast.makeText(DashboardUserActivity.this, "notifications coming soon!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_profile:
                        Toast.makeText(DashboardUserActivity.this, "profile coming soon!", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.logout:
                        Toast.makeText(DashboardUserActivity.this, "Logout success!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        break;
                    default: break;
                }
                return true;
            }
        });
    }
}
