package com.app.brightLightBookStore.activities.User;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.adapter.CartBookAdapter;
import com.app.brightLightBookStore.adapter.PopularBooksAdapter;
import com.app.brightLightBookStore.model.Book;
import com.app.brightLightBookStore.model.CartBook;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BooksCart extends AppCompatActivity {
    private FirebaseAuth mAuth;
    CartBookAdapter cartAdapter;
    List<CartBook> books;
    RecyclerView cartRecycler;
    public static int checkout = 0;
    public static TextView tvTotalCheckout,tvCheckout;
    LinearLayout btnCheckout,bottom;

    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_books_cart);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        setUpRecyclerView();

        findViewById(R.id.ivBack).setOnClickListener(view->{
            startActivity(new Intent(getApplicationContext(),DashboardUserActivity.class));
        });

        findViewById(R.id.checkout).setOnClickListener(view->{
            if(checkout > 0) {
                Intent intent = new Intent(getApplicationContext(), PaymentActivity.class);
                Bundle extras = new Bundle();
                extras.putString("amount", tvTotalCheckout.getText().toString());
                intent.putExtras(extras);
                startActivity(intent);
            }else {
                new AlertDialog.Builder(this)
                        .setTitle("Invalid!")
                        .setMessage("You must select at least 1 item from the cart!")
                        .setPositiveButton(android.R.string.ok, null)
                        .show();
            }
        });
    }

    public void setUpRecyclerView(){
        checkout= 0;
        //popular
        books = new ArrayList<>();
        cartRecycler = findViewById(R.id.cartRecycler);
        bottom = findViewById(R.id.bottom);
        bottom.setVisibility(View.GONE);
        tvTotalCheckout = findViewById(R.id.tvTotalCheckout);
        tvCheckout = findViewById(R.id.tvCheckout);
        getCartDetails();
    }
    private void getCartDetails(){
        databaseReference = FirebaseDatabase.getInstance().getReference("cart");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean flag =false;
                books = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String uKey = ds.child("id").getValue(String.class);
                        flag = true;
                        String book_name = ds.child("name").getValue(String.class);
                        String user_id = ds.child("user_id").getValue(String.class);

                        if(user_id.equals(mAuth.getUid())) {
                            String book_id = ds.child("book_id").getValue(String.class);
                            String auth_name = ds.child("auth_name").getValue(String.class);
                            int cart_qty = ds.child("cart_qty").getValue(Integer.class);
                            String image = ds.child("image").getValue(String.class);
                            Double purchase_amt = ds.child("purchase_amt").getValue(Double.class);
                            Double rental_amt = ds.child("rental_amt").getValue(Double.class);
                            boolean check = ds.child("check").getValue(Boolean.class);
                            int days = ds.child("days").getValue(Integer.class);
                            books.add(new CartBook(uKey, user_id, book_id, auth_name, book_name, image,
                                    purchase_amt, rental_amt, cart_qty, days, check));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(books.size()  > 0){
                    bottom.setVisibility(View.VISIBLE);
                }else{
                    TextView tvItem = findViewById(R.id.itemstv);
                    tvItem.setText("Cart is empty!!!");
                    tvItem.setTextSize(24);
                }

                Collections.reverse(books);
                cartAdapter = new CartBookAdapter(books, getApplicationContext());
                cartRecycler.setAdapter(cartAdapter);
                cartRecycler.addItemDecoration(new DividerItemDecoration(getApplicationContext(),
                        DividerItemDecoration.VERTICAL));
                cartRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));
                if(!flag) {
                    Toast.makeText(getApplicationContext(), "empty books!", Toast.LENGTH_SHORT).show();
                }
            } @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
}
