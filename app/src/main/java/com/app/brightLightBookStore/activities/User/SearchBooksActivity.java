package com.app.brightLightBookStore.activities.User;

import static com.app.brightLightBookStore.helpers.common_helper.getDays;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.adapter.SearchBooksAdapter;
import com.app.brightLightBookStore.model.Book;
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

public class SearchBooksActivity extends AppCompatActivity {
    ImageView ivBack, ivHistory;
    private FirebaseAuth mAuth;
    SearchBooksAdapter searchBooksAdapter;
    RecyclerView recycler;
    List<Book> books;
    EditText etSearch;
    DatabaseReference databaseReference;
    TextView tvBookGenre;
    String genre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_books);
        ivBack = findViewById(R.id.ivBack);
        recycler = findViewById(R.id.recycler);
        ivHistory = findViewById(R.id.ivHistory);
        etSearch = findViewById(R.id.etSearch);
        tvBookGenre = findViewById(R.id.tvBookGenre);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        ivHistory.setVisibility(View.GONE);
        Intent intent = getIntent();

        if (null != intent) {
            //Null Checking
            genre = intent.getStringExtra("genre");
            if (genre.isEmpty()) {
                Toast.makeText(this, "invalid genre!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), SearchGenreBooks.class));
            }else
                setUpRecyclerView(genre);
        }

        ivBack.setOnClickListener(view->{
            super.onBackPressed();
        });
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

    public void setUpRecyclerView(String st_genre){
        tvBookGenre.setText(st_genre);
        //popular
        books = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("books");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                books = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String uKey = ds.child("id").getValue(String.class);
                        String book_name = ds.child("book_name").getValue(String.class);
                        String auth_name = ds.child("auth_name").getValue(String.class);
                        String genre = ds.child("genre").getValue(String.class);

                        int count = ds.child("count").getValue(Integer.class);
                        int likes = ds.child("likes").getValue(Integer.class);
                        int days = 0;
                        String shortDescription = ds.child("shortDescription").getValue(String.class);
                        String rating = ds.child("rating").getValue(String.class);
                        String image = ds.child("image").getValue(String.class);
                        String published = ds.child("published").getValue(String.class);
                        Double purchase_amt = ds.child("purchase_amt").getValue(Double.class);
                        Double rental_amt = ds.child("rental_amt").getValue(Double.class);
                        String created_date = ds.child("created_date").getValue(String.class);

                        try {
                            if(created_date != null){
                                String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
                                days = getDays(created_date, date);
                                if(st_genre.equals("New collection") && days < 3)
                                    books.add(new Book(uKey, book_name, auth_name, genre, image, purchase_amt, rental_amt, shortDescription,
                                            rating, published, count, likes, created_date));
                                else if(genre.equals(st_genre))
                                    books.add(new Book(uKey, book_name, auth_name, genre, image, purchase_amt, rental_amt, shortDescription,
                                            rating, published, count, likes, created_date));
                            }else if(genre.equals(st_genre))
                                    books.add(new Book(uKey, book_name, auth_name, genre, image, purchase_amt, rental_amt, shortDescription,
                                            rating, published, count, likes, created_date));
                        }catch (Exception exception){
                            exception.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                searchBooksAdapter = new SearchBooksAdapter(books, getApplicationContext());
                recycler.setAdapter(searchBooksAdapter);
                recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

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
//This functionality is about searching/finding a book
