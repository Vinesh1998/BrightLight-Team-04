package com.app.brightLightBookStore.activities.Admin;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.adapter.MyBookHistoryAdapter;
import com.app.brightLightBookStore.model.HistoryBook;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class UserRentals extends AppCompatActivity {
    ImageView ivBack;
    private FirebaseAuth mAuth;
    MyBookHistoryAdapter myBookHistoryAdapter;
    RecyclerView recycler;
    List<HistoryBook> bookHistories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_rentals);
        ivBack = findViewById(R.id.ivBack);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        ivBack.setOnClickListener(view->{
            super.onBackPressed();
        });
        setUpRecyclerView();
    }

    public void setUpRecyclerView(){
        //popular
        bookHistories = new ArrayList<>();
//        bookHistories.add(new BookHistory("1","001","READ TO SUCCESS","","Chandrika",13.45,"23-08-2023"));
//        bookHistories.add(new BookHistory("2","002","THE POWER OF READING","","Lakshi",25.00,"05-09-2023"));
//        bookHistories.add(new BookHistory("3","003","READ TO SUCCESS","","Vineetha",25.00,"12-09-2023"));
        recycler = findViewById(R.id.recycler);
        myBookHistoryAdapter = new MyBookHistoryAdapter(bookHistories, getApplicationContext());
        recycler.setAdapter(myBookHistoryAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }
}
