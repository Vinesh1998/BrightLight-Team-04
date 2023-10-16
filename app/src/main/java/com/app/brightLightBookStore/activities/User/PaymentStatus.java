package com.app.brightLightBookStore.activities.User;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.activities.Admin.BooksManagementActivity;
import com.app.brightLightBookStore.adapter.CartBookAdapter;
import com.app.brightLightBookStore.model.Book;
import com.app.brightLightBookStore.model.CartBook;
import com.app.brightLightBookStore.model.HistoryBook;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PaymentStatus extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Double amount =0.00;
    TextView tvAmt, tvTxnDate;
    private DatabaseReference databaseReference,mDatabase;
    List<CartBook> books;
    String today_date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_status);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        tvTxnDate = findViewById(R.id.tvTxnDate);
        tvAmt = findViewById(R.id.tvAmt);
        findViewById(R.id.btnHome).setOnClickListener(view->{
            startActivity(new Intent(getApplicationContext(), DashboardUserActivity.class));
        });
        Intent intent = getIntent();
        if (null != intent) {
            //Null Checking
            if (intent.getStringExtra("amount").isEmpty()) {
                Toast.makeText(this, "invalid!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), DashboardUserActivity.class));
            }else{
                amount = Double.parseDouble(intent.getStringExtra("amount"));
                tvAmt.setText(amount+"");
                addToHistory();
            }
        }
        tvTxnDate.setText(today_date);
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.payment_success);
        mediaPlayer.start();
    }
    private void addToHistory(){
        // fetch user's cart and add all to user history and delete from cart
        databaseReference = FirebaseDatabase.getInstance().getReference("cart");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                books = new ArrayList<>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {

                        String uKey = ds.child("id").getValue(String.class);
                        String book_name = ds.child("book_name").getValue(String.class);
                        String user_id = ds.child("user_id").getValue(String.class);

                        if(user_id.equals(mAuth.getUid())) {
                            String book_id = ds.child("book_id").getValue(String.class);
                            String auth_name = ds.child("auth_name").getValue(String.class);
                            int cart_qty = ds.child("cart_qty").getValue(Integer.class);
                            String image = ds.child("image").getValue(String.class);
                            Double purchase_amt = ds.child("purchase_amt").getValue(Double.class);
                            Double rental_amt = ds.child("rental_amt").getValue(Double.class);
                            int days = ds.child("days").getValue(Integer.class);

                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            String dbKey = mDatabase.getDatabase().getReference("history").push().getKey();

                            HistoryBook historyBook = new HistoryBook(dbKey,mAuth.getUid(),book_id,book_name,
                                    auth_name,image,purchase_amt,rental_amt, today_date,cart_qty,days);

                            mDatabase.child("history").child(dbKey).setValue(historyBook)
                                    .addOnSuccessListener(command -> {
                                        ds.getRef().removeValue();
                                    })
                                    .addOnFailureListener(command ->
                                            Toast.makeText(getApplicationContext(), "something went wrong!", Toast.LENGTH_SHORT).show()
                                    );
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
