package com.app.brightLightBookStore.activities.User;

import static com.app.brightLightBookStore.helpers.common_helper.getGenres;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.adapter.SearchBooksAdapter;
import com.app.brightLightBookStore.adapter.SearchBooksGenreAdapter;
import com.app.brightLightBookStore.model.Book;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

public class SearchGenreBooks extends AppCompatActivity {
    ImageView ivBack, ivHistory;
    private FirebaseAuth mAuth;
    SearchBooksGenreAdapter searchBooksAdapter;
    RecyclerView recycler;
    List<Book> books;
    EditText etSearch;
    DatabaseReference databaseReference;
    FrameLayout frameLayout;

    String[] genres;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_books);
        ivBack = findViewById(R.id.ivBack);
        ivHistory = findViewById(R.id.ivHistory);
        etSearch = findViewById(R.id.etSearch);
        recycler = findViewById(R.id.recycler);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        etSearch.setVisibility(View.GONE);
        ivBack.setOnClickListener(view-> super.onBackPressed());
        ivHistory.setOnClickListener(view-> startActivity(new Intent(getApplicationContext(),UserHistory.class)));
        genres = getGenres();

        searchBooksAdapter = new SearchBooksGenreAdapter(genres, this);
        recycler.setAdapter(searchBooksAdapter);
        recycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
    }
}
