package com.app.brightLightBookStore.activities.User;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.adapter.MyBookHistoryAdapter;
import com.app.brightLightBookStore.model.HistoryBook;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class UserHistory extends AppCompatActivity {
    ImageView ivBack;
    private FirebaseAuth mAuth;
    MyBookHistoryAdapter myBookHistoryAdapter;
    RecyclerView recycler;
    List<HistoryBook> bookHistories;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_history);
        ivBack = findViewById(R.id.ivBack);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        ivBack.setOnClickListener(view-> super.onBackPressed());
        setUpRecyclerView();
    }

    public void setUpRecyclerView(){
        //popular
        bookHistories = new ArrayList<>();
        recycler = findViewById(R.id.recycler);
        databaseReference = FirebaseDatabase.getInstance().getReference("history");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flag =false;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        flag = true;
                        String user_id = ds.child("user_id").getValue(String.class);
                        String id = ds.child("id").getValue(String.class);
                        if(user_id.equals(mAuth.getUid()))
                        {
                            String book_id = ds.child("book_id").getValue(String.class);
                            String auth_name = ds.child("auth_name").getValue(String.class);
                            String image = ds.child("image").getValue(String.class);
                            String book_name = ds.child("name").getValue(String.class);
                            String purchase_date = ds.child("purchase_date").getValue(String.class);
                            int cart_qty = ds.child("cart_qty").getValue(Integer.class);
                            Double purchase_amt = ds.child("purchase_amt").getValue(Double.class);
                            Double rental_amt = ds.child("rental_amt").getValue(Double.class);
                            int days = ds.child("days").getValue(Integer.class);

                            bookHistories.add(new HistoryBook(id,mAuth.getUid(),book_id,auth_name,book_name,
                                    image,purchase_amt,rental_amt, purchase_date,cart_qty,days, false));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                 if(!flag) {
                    Toast.makeText(getApplicationContext(), "empty books!", Toast.LENGTH_SHORT).show();
                }

                Collections.reverse(bookHistories);

                myBookHistoryAdapter = new MyBookHistoryAdapter(bookHistories, getApplicationContext());
                recycler.setAdapter(myBookHistoryAdapter);
                recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
            }  @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
