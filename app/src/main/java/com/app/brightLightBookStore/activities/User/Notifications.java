package com.app.brightLightBookStore.activities.User;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.adapter.MyBookHistoryAdapter;
import com.app.brightLightBookStore.adapter.MyNotificationsAdapter;
import com.app.brightLightBookStore.adapter.SearchBooksAdapter;
import com.app.brightLightBookStore.model.Book;
import com.app.brightLightBookStore.model.HistoryBook;
import com.app.brightLightBookStore.model.Notification;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Notifications extends AppCompatActivity {
    ImageView ivBack;
    private FirebaseAuth mAuth;
    MyNotificationsAdapter notificationsAdapter;
    RecyclerView recycler;
    List<Notification> notif;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
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
        notif = new ArrayList<>();
        recycler = findViewById(R.id.recycler);
        getNewBooks();
        getHistory();
    }

    private void getHistory(){

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
//                        if(user_id.equals(mAuth.getUid())) {
                        {
                            String id = ds.child("id").getValue(String.class);
                            String book_name = ds.child("name").getValue(String.class);
                            String purchase_date = ds.child("purchase_date").getValue(String.class);
                            Double purchase_amt = ds.child("purchase_amt").getValue(Double.class);
                            Double rental_amt = ds.child("rental_amt").getValue(Double.class);

                            //  check date with current date
                            if(purchase_amt>0) {
                                notif.add(new Notification(id, "You've purchased a book", book_name, purchase_amt,
                                        purchase_date, true));
                            }else{
                                notif.add(new Notification(id, "You've rented a book", book_name, rental_amt,
                                        purchase_date, true));
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
//
                notificationsAdapter = new MyNotificationsAdapter(notif, getApplicationContext());
                recycler.setAdapter(notificationsAdapter);
                recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, true));
                if(!flag) {
                    Toast.makeText(getApplicationContext(), "empty books!", Toast.LENGTH_SHORT).show();
                }
            }  @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void getNewBooks(){
        databaseReference = FirebaseDatabase.getInstance().getReference("books");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String created_date = ds.child("created_date").getValue(String.class);
                        String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
//                        try {
                            if (created_date != null ? created_date.equals(date) : false) {
                                String uKey = ds.child("id").getValue(String.class);
                                String book_name = ds.child("book_name").getValue(String.class);
                                String auth_name = ds.child("auth_name").getValue(String.class);
                                String genre = ds.child("genre").getValue(String.class);
                                String shortDescription = ds.child("shortDescription").getValue(String.class);
                                String rating = ds.child("rating").getValue(String.class);
                                String image = ds.child("image").getValue(String.class);
                                String published = ds.child("published").getValue(String.class);
                                Double purchase_amt = ds.child("purchase_amt").getValue(Double.class);
                                Double rental_amt = ds.child("rental_amt").getValue(Double.class);
                                int count = ds.child("count").getValue(Integer.class);
                                int likes = ds.child("likes").getValue(Integer.class);

                                notif.add(new Notification(uKey, "New book added!", book_name, purchase_amt,
                                        created_date, true));
                            }
//                        }catch (Exception ex){
//                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                notificationsAdapter = new MyNotificationsAdapter(notif, getApplicationContext());
                recycler.setAdapter(notificationsAdapter);
                recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, true));
            }  @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
