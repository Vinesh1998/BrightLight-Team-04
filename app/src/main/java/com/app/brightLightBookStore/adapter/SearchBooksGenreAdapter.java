package com.app.brightLightBookStore.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.activities.User.BookDetails;
import com.app.brightLightBookStore.activities.User.SearchBooksActivity;
import com.app.brightLightBookStore.model.Book;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchBooksGenreAdapter extends RecyclerView.Adapter<SearchBooksGenreAdapter.MyBook> {
    String[] genres;
    Context mContext;
    PopularBooksAdapter searchBooksAdapter;
    DatabaseReference databaseReference;
    public SearchBooksGenreAdapter(String[] genres, Context context){
        this.genres = genres;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.genere_recycler,
                parent, false);
        return new SearchBooksGenreAdapter.MyBook(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBook holder, int position) {
        holder.tvGenreTitle.setText(genres[position]);
        getBooks(genres[position], holder.popular_recycler, holder.tvViewAll, holder.tvError);

        holder.tvViewAll.setOnClickListener(view->{
            Intent intent = new Intent(view.getContext(), SearchBooksActivity.class);
            Bundle extras = new Bundle();
            extras.putString("genre",genres[position]);
            intent.putExtras(extras);
            view.getContext().startActivity(intent);
        });
    }

    public void getBooks(String genre, RecyclerView recycler, TextView tvViewAll, TextView tvError){
        List<Book> pop_books = new ArrayList<>();
        //  Fetch books for genre
        databaseReference = FirebaseDatabase.getInstance().getReference("books");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    try {
                        String uKey = ds.child("id").getValue(String.class);

                        String book_name = ds.child("book_name").getValue(String.class);
                        String auth_name = ds.child("auth_name").getValue(String.class);
                        String genre_name = ds.child("genre").getValue(String.class);

                        String shortDescription = ds.child("shortDescription").getValue(String.class);
                        String rating = ds.child("rating").getValue(String.class);
                        String image = ds.child("image").getValue(String.class);
                        String published = ds.child("published").getValue(String.class);
                        Double purchase_amt = ds.child("purchase_amt").getValue(Double.class);
                        Double rental_amt = ds.child("rental_amt").getValue(Double.class);
                        String created_date = ds.child("created_date").getValue(String.class);
                        int count = ds.child("count").getValue(Integer.class);
                        int likes = ds.child("likes").getValue(Integer.class);

                        if(genre.equals(genre_name)) {
                            pop_books.add(new Book(uKey, book_name, auth_name, genre, image, purchase_amt, rental_amt, shortDescription,
                                    rating, published, count, likes, created_date));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(pop_books.size()>0){
                    tvViewAll.setVisibility(View.VISIBLE);
                    tvError.setVisibility(View.GONE);
                }
                else{
                    tvViewAll.setVisibility(View.GONE);
                    tvError.setVisibility(View.VISIBLE);
                }

                searchBooksAdapter = new PopularBooksAdapter(pop_books, mContext);
                recycler.setAdapter(searchBooksAdapter);
                recycler.setLayoutManager(new LinearLayoutManager(mContext, RecyclerView.HORIZONTAL, false));
            }  @Override
            public void onCancelled(DatabaseError databaseError) {}
        });
    }
    @Override
    public int getItemCount() {
        return genres.length;
    }
    static class MyBook extends RecyclerView.ViewHolder{
        // Here we hold the MyDoctorItems
        TextView tvGenreTitle, tvError, tvViewAll;
        RecyclerView popular_recycler;
        public MyBook(@NonNull View itemView) {
            super(itemView);
            tvGenreTitle = itemView.findViewById(R.id.tvGenreTitle);
            tvError = itemView.findViewById(R.id.tvError);
            tvViewAll = itemView.findViewById(R.id.tvViewAll);
            popular_recycler = itemView.findViewById(R.id.popular_recycler);
        }
    }
}
