package com.app.brightLightBookStore.activities.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.activities.Admin.AdminChats;
import com.app.brightLightBookStore.activities.LoginActivity;
import com.app.brightLightBookStore.adapter.AdminChatsAdapter;
import com.app.brightLightBookStore.adapter.MyBooksAdapter;
import com.app.brightLightBookStore.adapter.PopularBooksAdapter;
import com.app.brightLightBookStore.adapter.SearchBooksGenreAdapter;
import com.app.brightLightBookStore.model.Book;
import com.app.brightLightBookStore.model.Chat;
import com.app.brightLightBookStore.model.ChatUsers;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DashboardUserActivity extends AppCompatActivity {
    ImageView ivCart,ivChat;
    private FirebaseAuth mAuth;
    PopularBooksAdapter popularAdapter;
    MyBooksAdapter myBooksAdapter;
    RecyclerView popular_recycler,my_recycler;
    List<Book> books,my_books;
    private DatabaseReference mDatabase;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_dashboard);
        ivCart = findViewById(R.id.ivCart);
        ivChat = findViewById(R.id.ivChat);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        ivCart.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), BooksCart.class));
        });
        ivChat.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), ChatConversation.class));
        });
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.nav_home:
                    startActivity(new Intent(getApplicationContext(), DashboardUserActivity.class));
                    break;
                case R.id.nav_books:
                    startActivity(new Intent(getApplicationContext(), SearchGenreBooks.class));
                    break;
                case R.id.nav_notifications:
                    startActivity(new Intent(getApplicationContext(), Notifications.class));
                    break;
                case R.id.nav_profile:
                    startActivity(new Intent(getApplicationContext(), UserProfile.class));
                    break;
                case R.id.logout:
                    mAuth.signOut();
                    Toast.makeText(getApplicationContext(), "Good bye!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
            return true;
        });
        setUpRecyclerView();
    }
    public void setUpRecyclerView(){
        //popular
        getBooks();
        // my purchases
        getMyBooks();

        getChats();
    }
    private void getBooks(){
        databaseReference = FirebaseDatabase.getInstance().getReference("books");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flag =false;
                books = new ArrayList<>();
                popular_recycler = findViewById(R.id.popular_recycler);
                popularAdapter = new PopularBooksAdapter(books, getApplicationContext());

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String uKey = ds.child("id").getValue(String.class);
                        flag = true;
                        String book_name = ds.child("book_name").getValue(String.class);
                        String auth_name = ds.child("auth_name").getValue(String.class);
                        String genre = ds.child("genre").getValue(String.class);
                        String shortDescription = ds.child("shortDescription").getValue(String.class);
                        String rating = ds.child("rating").getValue(String.class);
                        String image = ds.child("image").getValue(String.class);
                        String published = ds.child("published").getValue(String.class);
                        String created_date = ds.child("created_date").getValue(String.class);
                        Double purchase_amt = ds.child("purchase_amt").getValue(Double.class);
                        Double rental_amt = ds.child("rental_amt").getValue(Double.class);
                        int count = ds.child("count").getValue(Integer.class);
                        int likes = ds.child("likes").getValue(Integer.class);

                        books.add(new Book(uKey,book_name,auth_name,genre,image, purchase_amt, rental_amt,shortDescription,
                                rating,published,count,likes,created_date));

                        popular_recycler.setAdapter(popularAdapter);
                        popularAdapter.notifyDataSetChanged();
                        popular_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, true));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if(!flag) {
                    Toast.makeText(getApplicationContext(), "empty books!", Toast.LENGTH_SHORT).show();
                }
            }  @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
    private void getMyBooks(){
        my_books = new ArrayList<>();
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
                        if(user_id.equals(mAuth.getUid())) {
                            String id = ds.child("id").getValue(String.class);
                            String book_id = ds.child("book_id").getValue(String.class);
                            String genre = ds.child("genre").getValue(String.class);
                            String auth_name = ds.child("auth_name").getValue(String.class);
                            String image = ds.child("image").getValue(String.class);
                            String book_name = ds.child("name").getValue(String.class);
                            String purchase_date = ds.child("purchase_date").getValue(String.class);
                            int cart_qty = ds.child("cart_qty").getValue(Integer.class);
                            Double purchase_amt = ds.child("purchase_amt").getValue(Double.class);
                            Double rental_amt = ds.child("rental_amt").getValue(Double.class);
                            int days = ds.child("days").getValue(Integer.class);

                            my_books.add(new Book(book_id,book_name,auth_name,genre,image, purchase_amt, rental_amt,"",
                                    "","",0,0,""));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    my_recycler = findViewById(R.id.my_recycler);
                    myBooksAdapter = new MyBooksAdapter(my_books, getApplicationContext());
                    my_recycler.setAdapter(myBooksAdapter);
                    my_recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.HORIZONTAL, true));
                }
                if(!flag) {
                    Toast.makeText(getApplicationContext(), "empty books!", Toast.LENGTH_SHORT).show();
                }
            }  @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    private void getChats(){
        List<Chat> chats = new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference("chats");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        boolean flag =true;
                        String receiver = ds.child("receiver_id").getValue(String.class);
                        String receiver_name = ds.child("receiver_name").getValue(String.class);
                        String id = ds.child("id").getValue(String.class);
                        String msg = ds.child("msg").getValue(String.class);
                        String date = ds.child("date").getValue(String.class);
                        String time = ds.child("time").getValue(String.class);
                        boolean flag_status = false;
                        try {
                            flag_status = ds.child("status").getValue(boolean.class);
                            if(receiver.equals(mAuth.getUid()) && !flag_status) {
                                ivChat.setImageDrawable(getResources().getDrawable(R.drawable.baseline_mark_unread_chat_alt_24));
                                ivChat.setColorFilter(getResources().getColor(R.color.red), android.graphics.PorterDuff.Mode.SRC_IN);
                            }
                        }catch (Exception ex){
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }  @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
//This fuctionality is about the user activity dashboard