package com.app.brightLightBookStore.activities.Admin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.adapter.AdminChatConversationsAdapter;
import com.app.brightLightBookStore.adapter.AdminChatsAdapter;
import com.app.brightLightBookStore.adapter.MyChatsAdapter;
import com.app.brightLightBookStore.helpers.BaseActivity;
import com.app.brightLightBookStore.model.Chat;
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

public class AdminChatConversation extends BaseActivity {
    ImageView ivBack;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    List<Chat> chats;
    RecyclerView recycler;
    AdminChatConversationsAdapter myChatsAdapter;
    EditText etMsg;
    FloatingActionButton btnSend;
    TextView tvTitle;
    String msg_sender_name,msg_sender_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_conversation);
        ivBack = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);
        etMsg = findViewById(R.id.etMsg);
        btnSend = findViewById(R.id.btnSend);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        ivBack.setOnClickListener(view -> {
            super.onBackPressed();
        });

        Intent intent = getIntent();

        if (null != intent) {
            //Null Checking
            String strId = intent.getStringExtra("chat_id"); // ujserId
            if (!strId.isEmpty()) {
                setUpRecyclerView(strId);
            } else {
                Toast.makeText(this, "invalid!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), AdminChats.class));
            }
        }
    }
    int count;
    public void setUpRecyclerView(String user){
        //popular
        chats = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("chats");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
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
                        if(sender.equals(user) || receiver.equals(user) ) {
                            String id = ds.child("id").getValue(String.class);
                            String msg = ds.child("msg").getValue(String.class);
                            String date = ds.child("date").getValue(String.class);
                            String time = ds.child("time").getValue(String.class);
                            tvTitle.setText(sender_name+"'s Chat");
                            msg_sender_name = sender_name;
                            if(!sender.equals("1"))
                                msg_sender_id = sender;
                            chats.add(new Chat(id,msg,date,time, sender,sender_name,receiver,receiver_name,true));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    myChatsAdapter = new AdminChatConversationsAdapter(chats, getApplicationContext(),"1");
                    recycler.setAdapter(myChatsAdapter);
                    recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                    myChatsAdapter.notifyDataSetChanged();
                }
                if(!flag) {
                    Toast.makeText(getApplicationContext(), "empty!", Toast.LENGTH_SHORT).show();
                }
            }  @Override
            public void onCancelled(DatabaseError databaseError) {}
        });

        recycler = findViewById(R.id.recycler);
        myChatsAdapter = new AdminChatConversationsAdapter(chats, getApplicationContext(),"1");
        recycler.setAdapter(myChatsAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));

        btnSend.setOnClickListener(view->{
            String msg = etMsg.getText().toString();
            if(!msg.isEmpty()) {
                String now = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
                String time = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date());
                mDatabase = FirebaseDatabase.getInstance().getReference();
                String dbKey = mDatabase.getDatabase().getReference("chats").push().getKey();

                Chat chat = new Chat(dbKey,msg , now, time, "1", "Admin", msg_sender_id,msg_sender_name,true);
                mDatabase.child("chats").child(dbKey).setValue(chat)
                        .addOnSuccessListener(command -> {
                            chats.add(chat);
                            recycler.setAdapter(myChatsAdapter);
                            myChatsAdapter.notifyDataSetChanged();
                        })
                        .addOnFailureListener(command ->
                                Toast.makeText(getApplicationContext(), "something went wrong!", Toast.LENGTH_SHORT).show()
                        );
                etMsg.setText("");
                etMsg.setFocusable(true);
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
