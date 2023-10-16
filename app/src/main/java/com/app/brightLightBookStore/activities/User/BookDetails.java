package com.app.brightLightBookStore.activities.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.model.Book;
import com.app.brightLightBookStore.model.CartBook;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BookDetails extends AppCompatActivity {
    ImageView ivBack, ivBook;
    Button btnCart, btnBuy;
    TextView tvBookName, tvAuthor, tvPublishedOn, tvShortDesc, tvCount ,tvPrice, tvRental;
    private FirebaseAuth mAuth;
    String uid;
    boolean flag;

    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        btnCart = findViewById(R.id.btnCart);
        btnBuy = findViewById(R.id.btnBuyNow);
        ivBack = findViewById(R.id.ivBack);
        ivBook = findViewById(R.id.ivBook);
        tvBookName = findViewById(R.id.tvBookName);
        tvCount = findViewById(R.id.tvCount);
        tvAuthor = findViewById(R.id.tvAuthor);
        tvPublishedOn = findViewById(R.id.tvPublishedOn);
        tvShortDesc = findViewById(R.id.tvShortDesc);
        tvPrice = findViewById(R.id.tvPrice);
        tvRental = findViewById(R.id.tvRental);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        ivBack.setOnClickListener(view->{
            startActivity(new Intent(getApplicationContext(), DashboardUserActivity.class));
        });

        Intent intent = getIntent();

        if (null != intent) {
            //Null Checking
            uid= intent.getStringExtra("book_id");
            if (uid.isEmpty()) {
                Toast.makeText(this, "invalid!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), DashboardUserActivity.class));
            }else{
                getBookDetails(uid);
            }
        }
    }

    private void getBookDetails(String strId){
        FirebaseDatabase.getInstance().getReference("books").child(strId)
        .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                try {
                    flag = true;
                    String uKey = ds.child("id").getValue(String.class);
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
                    Book book = new Book(uKey,book_name,auth_name,image, purchase_amt, rental_amt,shortDescription,
                            rating,published,count,likes);
                    displayData(book);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void displayData(Book book){
        tvBookName.setText(book.getBook_name());
        tvAuthor.setText(book.getAuth_name());
        tvAuthor.setVisibility(View.GONE);
        tvPublishedOn.setText(book.getPublished());
        tvShortDesc.setText(book.getShortDescription());
        tvCount.setText(book.getCount()+"");
        tvPrice.setText("$. "+book.getPurchase_amt());
        tvRental.setText("$. "+book.getRental_amt() +"/1day");
        Picasso.get().load(book.getImage()).into(ivBook);

        btnBuy.setOnClickListener(v -> {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
            bottomSheetDialog.setContentView(R.layout.show_rent_days_popup);
            bottomSheetDialog.show();
            EditText etNoOfDays = bottomSheetDialog.findViewById(R.id.etNoOfDays);
            Button btnProceed = bottomSheetDialog.findViewById(R.id.btnProceed);

            btnProceed.setOnClickListener(view->{
                if(etNoOfDays.getText().toString().isEmpty()){
                    etNoOfDays.setError("Required");
                    etNoOfDays.setFocusable(true);
                }else{
                    addToRentalCart(book, Integer.parseInt(etNoOfDays.getText().toString()));
                }
            });
        });
        btnCart.setOnClickListener(v -> {
            addToPurchaseCart(book);
        });
    }

    private void addToPurchaseCart(Book book){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String uKey = mDatabase.getDatabase().getReference("cart").push().getKey();
        CartBook cartBook = new CartBook(uKey,mAuth.getUid(),book.getId(),book.getAuth_name(),book.getBook_name(),
                book.getImage(), book.getPurchase_amt(),0.00,1,0);
        // insert into cart collection
        mDatabase.child("cart").child(uKey).setValue(cartBook)
                .addOnSuccessListener(command -> {
                    Toast.makeText(this,"Added to cart!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, BooksCart.class));
                })
                .addOnFailureListener(command ->
                        Toast.makeText(this, "failed to add book!", Toast.LENGTH_SHORT).show()
                );
    }

    private void addToRentalCart(Book book,int noOfDays){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String uKey = mDatabase.getDatabase().getReference("cart").push().getKey();
        CartBook cartBook = new CartBook(uKey,mAuth.getUid(),book.getId(),book.getAuth_name(),book.getBook_name(),
                book.getImage(), 0.00, book.getRental_amt(),1, noOfDays);
        // insert into cart collection
        mDatabase.child("cart").child(uKey).setValue(cartBook)
                .addOnSuccessListener(command -> {
                    Toast.makeText(this,"Added to cart!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this, BooksCart.class));
                })
                .addOnFailureListener(command ->
                        Toast.makeText(this, "failed to add cart!", Toast.LENGTH_SHORT).show()
                );
    }
}
