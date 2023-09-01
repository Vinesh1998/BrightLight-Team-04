package com.app.brightLightBookStore.activities.Admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.adapter.AdminChatsAdapter;
import com.app.brightLightBookStore.adapter.MyChatsAdapter;
import com.app.brightLightBookStore.adapter.MyNotificationsAdapter;
import com.app.brightLightBookStore.helpers.BaseActivity;
import com.app.brightLightBookStore.model.Chat;
import com.app.brightLightBookStore.model.ChatUsers;
import com.app.brightLightBookStore.model.Notification;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
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

public class AdminChats extends BaseActivity {
    ImageView ivBack;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    List<ChatUsers> chats;
    RecyclerView recycler;
    AdminChatsAdapter myChatsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_chats);
        ivBack = findViewById(R.id.ivBack);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        ivBack.setOnClickListener(view->{
            super.onBackPressed();
        });

        setUpRecyclerView();
    }
    int count;
    public void setUpRecyclerView(){
        count = 0;
        //popular
        chats = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance().getReference("chats");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        boolean flag =true;
                        String sender = ds.child("sender_id").getValue(String.class);
                        String sender_name = ds.child("sender_name").getValue(String.class);
                        String id = ds.child("id").getValue(String.class);
                        String msg = ds.child("msg").getValue(String.class);
                        String date = ds.child("date").getValue(String.class);
                        String time = ds.child("time").getValue(String.class);
                        for (ChatUsers chat: chats) {
                            if(chat.getUser_name().equals(sender_name) || sender.equals("1"))
                                flag = false;
                        }
                        if(flag) chats.add(new ChatUsers(sender,msg,date,time,sender_name,true));//to identify the chat user
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    recycler = findViewById(R.id.recycler);
                    myChatsAdapter = new AdminChatsAdapter(chats, getApplicationContext());
                    recycler.setAdapter(myChatsAdapter);
                    recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                }
            }  @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
