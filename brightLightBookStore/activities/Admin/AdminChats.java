package com.app.brightLightBookStore.activities.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
            startActivity(new Intent(this, DashboardAdminActivity.class));
            ;
        });

        setUpRecyclerView();
    }
    int count;
    public void setUpRecyclerView(){
        count = 0;
        //popular
        chats = new ArrayList<>();
        recycler = findViewById(R.id.recycler);
        myChatsAdapter = new AdminChatsAdapter(chats, this);
        recycler.setAdapter(myChatsAdapter);
        mDatabase = FirebaseDatabase.getInstance().getReference("chats");
        mDatabase.addValueEventListener(new ValueEventListener() {
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
                        boolean flag_status = false;

                        try {
                             flag_status = ds.child("flag").getValue(boolean.class);
                        }catch (Exception ex){
                        }

                        for(int i =0; i< chats.size();i++){
                            ChatUsers chat = chats.get(i);
                            if(chat.getUser_name().equals(sender_name)) {
                                flag = false;
                                if (!flag_status) {
                                    chats.get(i).setDate(date);
                                    chats.get(i).setTime(time);
                                    chats.get(i).setMsg(msg);
                                    chats.get(i).setStatus(false);
                                }
                            }else if(sender.equals("1")) flag=false;
                        }
                        if(flag) chats.add(new ChatUsers(sender,msg,date,time,sender_name,flag_status));//to identify the chat user

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Comparator<ChatUsers> reverseComparator = (c1, c2) -> {
                        Date start = null;
                        try {
                            start = new SimpleDateFormat("MM-dd-yyyy HH:mm", Locale.ENGLISH)
                                    .parse(c1.getDate() + " "+c1.getTime() );
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        Date end = null;
                        try {
                            end = new SimpleDateFormat("MM-dd-yyyy HH:mm", Locale.ENGLISH)
                                    .parse(c2.getDate()+ " "+c2.getTime());
                        } catch (ParseException e) {
                            throw new RuntimeException(e);
                        }
                        return end.compareTo(start);
                    };

                    Collections.sort(chats, reverseComparator);
                    myChatsAdapter.notifyDataSetChanged();
                    recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                }
            }  @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, DashboardAdminActivity.class));
    }

}
