package com.app.brightLightBookStore.activities.Admin;

import static com.app.brightLightBookStore.helpers.common_helper.getAdminGenres;
import static com.app.brightLightBookStore.helpers.common_helper.getGenres;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.adapter.AdminBooksAdapter;
import com.app.brightLightBookStore.model.Book;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class BooksManagementActivity extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener {
    ImageView ivBack, ivAdd, ivBookImage;
    private FirebaseAuth mAuth;
    AdminBooksAdapter adminBooksAdapter;
    RecyclerView recycler;
    List<Book> books;
    EditText etSearch;
    private DatabaseReference mDatabase;
    private DatabaseReference databaseReference;
    EditText bookName,etAuthorName,etDescription,etAvailableBook,etPublishedDate,etPurchaseAmt,etRentalAmt,etRating;
    String stBookName, stAuthorName, stShortDesc,stBookCount,stPublished,stPurchaseAmt,stRentalAmt,stRating;
    Button btnAdd, buttonChoose;
    private static final int PICK_IMAGE_REQUEST = 234;

    public static final String STORAGE_PATH_UPLOADS = "uploads/";
    public static final String DATABASE_PATH_UPLOADS = "uploads";
    private Uri filePath, downloadUrl;

    //firebase objects
    private StorageReference storageReference;
    private  Spinner spin;
    private String[] genres;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_books_management);
        ivBack = findViewById(R.id.ivBack);
        ivAdd = findViewById(R.id.ivAdd);
        etSearch = findViewById(R.id.etSearch);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        ivBack.setOnClickListener(view->{
            super.onBackPressed();
        });
        ivAdd.setOnClickListener(view->{
            addBook();
        });
        getBooks();
    }

    private void getBooks(){
        databaseReference = FirebaseDatabase.getInstance().getReference("books");
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

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                recycler = findViewById(R.id.popular_recycler);
                adminBooksAdapter = new AdminBooksAdapter(books, getApplicationContext());
                recycler.setAdapter(adminBooksAdapter);
                recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL, false));

                if(!flag) {
                    Toast.makeText(getApplicationContext(), "empty books!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),BooksManagementActivity.class));
                }
            }  @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }

    String stGenres = "";
    void addBook(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.show_add_book_popup);
        bottomSheetDialog.show();

        bookName = bottomSheetDialog.findViewById(R.id.etBookName);
        etAuthorName = bottomSheetDialog.findViewById(R.id.etAuthorName);
        etDescription = bottomSheetDialog.findViewById(R.id.etDescription);
        etAvailableBook = bottomSheetDialog.findViewById(R.id.etAvailableBook);
        etPurchaseAmt = bottomSheetDialog.findViewById(R.id.etPurchaseAmt);
        etRentalAmt = bottomSheetDialog.findViewById(R.id.etRentalAmt);
        etRating = bottomSheetDialog.findViewById(R.id.etRating);
        etPublishedDate = bottomSheetDialog.findViewById(R.id.etPublishedDate);
        btnAdd = bottomSheetDialog.findViewById(R.id.btnAdd);
        buttonChoose = bottomSheetDialog.findViewById(R.id.buttonChoose);
        ivBookImage = bottomSheetDialog.findViewById(R.id.ivBookImage);

        spin =  bottomSheetDialog.findViewById(R.id.spGenres);
        spin.setOnItemSelectedListener(this);
        genres = getAdminGenres();

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, genres);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);

        storageReference = FirebaseStorage.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference(DATABASE_PATH_UPLOADS);

        buttonChoose.setOnClickListener(v -> showFileChooser());
        btnAdd.setOnClickListener(v -> validateBookDetails());
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        ((TextView) arg0.getChildAt(0)).setTextColor(Color.BLACK);
        stGenres = genres[position];
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                ivBookImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    void validateBookDetails(){
        boolean flag=false;
        stBookName = bookName.getText().toString();
        stBookCount = etAvailableBook.getText().toString();
        stAuthorName = etAuthorName.getText().toString();
        stShortDesc = etDescription.getText().toString();
        stPurchaseAmt = etPurchaseAmt.getText().toString();
        stRentalAmt = etRentalAmt.getText().toString();
        stRating = etRating.getText().toString();
        stPublished = etPublishedDate.getText().toString();

        if(stBookName.isEmpty()) bookName.setError("required");
        else if(stAuthorName.isEmpty()) etAuthorName.setError("required");
        else if(stBookCount.isEmpty()) etAvailableBook.setError("required");
        else if(stShortDesc.isEmpty()) etDescription.setError("required");
        else if(stPurchaseAmt.isEmpty()) etPurchaseAmt.setError("required");
        else if(stRentalAmt.isEmpty()) etRentalAmt.setError("required");
        else if(stRating.isEmpty()) etRating.setError("required");

        else if(stPublished.isEmpty())
            Toast.makeText(this, "Genre can't be empty!", Toast.LENGTH_SHORT).show();

        else if(stGenres.isEmpty()) etPublishedDate.setError("required");
        else if (filePath == null)
            Toast.makeText(this, "please, provide an image!!!", Toast.LENGTH_SHORT).show();

        else flag = true;

        if(flag)  uploadFile();
        else  Toast.makeText(this, "false..!", Toast.LENGTH_SHORT).show();
    }
    void addBookDetails(String auth_name, String book_name, String stGenres, Double purchase_amt, Double rental_amt, String shortDescription,
                        String rating, String published, int count){
        String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new Date());
        mDatabase = FirebaseDatabase.getInstance().getReference();
        String uKey = mDatabase.getDatabase().getReference("books").push().getKey();
        Book book = new Book(uKey,book_name,auth_name,stGenres,downloadUrl.toString(), purchase_amt, rental_amt,shortDescription,
                rating,published,count,0, date);
        mDatabase.child("books").child(uKey).setValue(book)
                .addOnSuccessListener(command -> {
                    Toast.makeText(this,"Added!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(this,BooksManagementActivity.class));
                })
                .addOnFailureListener(command ->
                        Toast.makeText(this, "failed to add book!", Toast.LENGTH_SHORT).show()
                );
    }
    public String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    @SuppressLint("RestrictedApi")
    private void uploadFile() {
        //checking if file is available
        if (filePath != null) {
            //displaying progress dialog while image is uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            //getting the storage reference
            StorageReference sRef = storageReference.child(STORAGE_PATH_UPLOADS + System.currentTimeMillis() + "." + getFileExtension(filePath));

            //adding the file to reference
            sRef.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        sRef.getDownloadUrl().addOnSuccessListener(uri -> {
                             downloadUrl = uri;
                            addBookDetails(stAuthorName,stBookName,stGenres,Double.parseDouble(stPurchaseAmt),Double.parseDouble(stRentalAmt),
                                    stShortDesc,stRating,stPublished,Integer.parseInt(stBookCount));
                            //Do what you want with the url
                        });
                        progressDialog.dismiss();
                    })
                    .addOnFailureListener(exception -> {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
                    })
                    .addOnCompleteListener((OnCompleteListener<UploadTask.TaskSnapshot>) task -> {
                        if (task.isSuccessful()) {
                            task.getResult().getMetadata().getName();
                        }
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        //displaying the upload progress
                        double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                        progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                    });
        } else {
            Toast.makeText(this, "invalid image!!!", Toast.LENGTH_SHORT).show();
        }
    }
}
//This functionality is for the books management activity
