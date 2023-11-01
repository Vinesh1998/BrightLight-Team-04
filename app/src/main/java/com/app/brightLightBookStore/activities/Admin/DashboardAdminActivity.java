package com.app.brightLightBookStore.activities.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.activities.LoginActivity;
import com.app.brightLightBookStore.adapter.MyBooksAdapter;
import com.app.brightLightBookStore.adapter.PopularBooksAdapter;
import com.app.brightLightBookStore.model.Book;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class DashboardAdminActivity extends AppCompatActivity {
    ImageView ivCart,ivChat;
    private FirebaseAuth mAuth;
    PopularBooksAdapter popularAdapter;
    MyBooksAdapter myBooksAdapter;
    RecyclerView popular_recycler,my_recycler;
    List<Book> books,my_books;
    EditText etSearch;
    Button btnBook,btnAuthors,btnPurchases,btnRentals,btnConversations,btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        btnBook = findViewById(R.id.btnBook);
        btnPurchases = findViewById(R.id.btnPurchases);
        btnRentals = findViewById(R.id.btnRentals);
        btnConversations = findViewById(R.id.btnConversations);
        btnLogout = findViewById(R.id.btnLogout);

        btnBook.setOnClickListener(view->{
            startActivity(new Intent(getApplicationContext(),BooksManagementActivity.class));
        });
        btnPurchases.setOnClickListener(view->{
            startActivity(new Intent(getApplicationContext(),UserRentals.class));
        });
        btnRentals.setOnClickListener(view->{
            startActivity(new Intent(getApplicationContext(),UserRentals.class));
        });
        btnConversations.setOnClickListener(view->{
            startActivity(new Intent(getApplicationContext(), AdminChats.class));
        });
        btnLogout.setOnClickListener(view->{
            mAuth.signOut();
            Toast.makeText(getApplicationContext(), "Good bye!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

}
