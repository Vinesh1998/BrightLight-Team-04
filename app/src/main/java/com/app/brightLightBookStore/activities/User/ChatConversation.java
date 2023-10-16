package com.app.brightLightBookStore.activities.User;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.adapter.MyBooksAdapter;
import com.app.brightLightBookStore.adapter.MyChatsAdapter;
import com.app.brightLightBookStore.adapter.MyNotificationsAdapter;
import com.app.brightLightBookStore.helpers.BaseActivity;
import com.app.brightLightBookStore.model.Book;
import com.app.brightLightBookStore.model.Chat;
import com.app.brightLightBookStore.model.HistoryBook;
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
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatConversation extends BaseActivity {
    ImageView ivBack;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    List<Chat> chats;
    RecyclerView recycler;
    MyChatsAdapter myChatsAdapter;
    EditText etMsg;
    FloatingActionButton btnSend;
    private DatabaseReference databaseReference;
    String user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_conversation);
        ivBack = findViewById(R.id.ivBack);
        etMsg = findViewById(R.id.etMsg);
        btnSend = findViewById(R.id.btnSend);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        ivBack.setOnClickListener(view->{
            super.onBackPressed();
        });

        //get username
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flag =false;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        flag = true;
                        String user_id = ds.child("id").getValue(String.class);
//                        Toast.makeText(ChatConversation.this, user_id+"<<-->"+mAuth.getUid(), Toast.LENGTH_SHORT).show();
                        if(user_id.equalsIgnoreCase(mAuth.getUid()))
                             user_name = ds.child("name").getValue(String.class);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(!flag)
                    Toast.makeText(getApplicationContext(), "empty books!", Toast.LENGTH_SHORT).show();
            }  @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        setUpRecyclerView();
    }
    int count;
    public void setUpRecyclerView(){
        count = 0;
        //popular
        chats = new ArrayList<>();
        recycler = findViewById(R.id.recycler);

        databaseReference = FirebaseDatabase.getInstance().getReference("chats");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flag =false;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        flag = true;
                        String sender = ds.child("sender_id").getValue(String.class);
                        String sender_name = ds.child("sender_name").getValue(String.class);
                        String receiver = ds.child("receiver_id").getValue(String.class);
                        String receiver_name = ds.child("receiver_name").getValue(String.class);
                        if(sender.equals(mAuth.getUid()) || receiver.equals(mAuth.getUid()) ) {
                            String id = ds.child("id").getValue(String.class);
                            String msg = ds.child("msg").getValue(String.class);
                            String date = ds.child("date").getValue(String.class);
                            String time = ds.child("time").getValue(String.class);

                            chats.add(new Chat(id,msg,date,time, sender,sender_name,receiver,receiver_name,true));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    myChatsAdapter = new MyChatsAdapter(chats, getApplicationContext(),mAuth.getUid());
                    recycler.setAdapter(myChatsAdapter);
                    recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                }
                if(!flag) {
                    Toast.makeText(getApplicationContext(), "empty!", Toast.LENGTH_SHORT).show();
                }
            }  @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        btnSend.setOnClickListener(view->{
            String msg = etMsg.getText().toString();
            if(!msg.isEmpty()) {
                String now = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());

                mDatabase = FirebaseDatabase.getInstance().getReference();
                String dbKey = mDatabase.getDatabase().getReference("chats").push().getKey();

                Chat chat = new Chat(dbKey,msg , now, time, mAuth.getUid(), user_name,
                        "1", "Admin",true);
                mDatabase.child("chats").child(dbKey).setValue(chat)
                        .addOnSuccessListener(command -> {
                            chats.add(new Chat(dbKey, msg, now, time, mAuth.getUid(), user_name,
                                    "1","Admin", true));
                            recycler.setAdapter(myChatsAdapter);
                            myChatsAdapter.notifyDataSetChanged();
                        })
                        .addOnFailureListener(command ->
                                Toast.makeText(getApplicationContext(), "something went wrong!", Toast.LENGTH_SHORT).show()
                        );
                etMsg.setText("");
                etMsg.setFocusable(true);
            }else{
                etMsg.setFocusable(true);
                etMsg.setError("enter message!!!");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
