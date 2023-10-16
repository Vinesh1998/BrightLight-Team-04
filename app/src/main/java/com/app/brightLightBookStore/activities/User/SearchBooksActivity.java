package com.app.brightLightBookStore.activities.User;

import android.annotation.SuppressLint;
import android.content.Intent;
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
import com.app.brightLightBookStore.activities.LoginActivity;
import com.app.brightLightBookStore.adapter.MyBooksAdapter;
import com.app.brightLightBookStore.adapter.PopularBooksAdapter;
import com.app.brightLightBookStore.adapter.SearchBooksAdapter;
import com.app.brightLightBookStore.model.Book;
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

public class SearchBooksActivity extends AppCompatActivity {
    ImageView ivBack, ivHistory;
    private FirebaseAuth mAuth;
    SearchBooksAdapter searchBooksAdapter;
    RecyclerView recycler;
    List<Book> books;
    EditText etSearch;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_books);
        ivBack = findViewById(R.id.ivBack);
        ivHistory = findViewById(R.id.ivHistory);
        etSearch = findViewById(R.id.etSearch);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        ivBack.setOnClickListener(view->{
            super.onBackPressed();
        });
        ivHistory.setOnClickListener(view->{
            startActivity(new Intent(getApplicationContext(),UserHistory.class));
        });
        setUpRecyclerView();
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                filterBooks(s.toString());
            }
        });
    }

    public void setUpRecyclerView(){
        //popular
        books = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("books");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flag =false;
                books = new ArrayList<>();

                recycler = findViewById(R.id.popular_recycler);

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String uKey = ds.child("id").getValue(String.class);
                        flag = true;
                        String book_name = ds.child("book_name").getValue(String.class);
                        String auth_name = ds.child("auth_name").getValue(String.class);
                        String shortDescription = ds.child("shortDescription").getValue(String.class);
                        String rating = ds.child("rating").getValue(String.class);
                        String image = ds.child("image").getValue(String.class);
                        String published = ds.child("published").getValue(String.class);
                        Double purchase_amt = ds.child("purchase_amt").getValue(Double.class);
                        Double rental_amt = ds.child("rental_amt").getValue(Double.class);
                        int count = ds.child("count").getValue(Integer.class);
                        int likes = ds.child("likes").getValue(Integer.class);

                        books.add(new Book(uKey,book_name,auth_name,image, purchase_amt, rental_amt,shortDescription,
                                rating,published,count,likes));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                searchBooksAdapter = new SearchBooksAdapter(books, getApplicationContext());
                recycler.setAdapter(searchBooksAdapter);
                recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

                if(!flag) {
                    Toast.makeText(getApplicationContext(), "empty books!", Toast.LENGTH_SHORT).show();
                }
            }  @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    void filterBooks(String text){
        List<Book> temp = new ArrayList();
        for(Book d: books){
            if(!d.getBook_name().isEmpty() && d.getBook_name().toUpperCase().contains(text.toUpperCase())){
                temp.add(d);
            }else if(!d.getAuth_name().isEmpty() && d.getAuth_name().toUpperCase().contains(text.toUpperCase())){
                temp.add(d);
            }
        }
        //update recyclerview
        searchBooksAdapter.updateList(temp);
    }
}
