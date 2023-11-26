package com.app.brightLightBookStore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.model.Book;

import java.util.Collections;
import java.util.List;
import com.squareup.picasso.Picasso;

public class AdminBooksAdapter extends RecyclerView.Adapter<AdminBooksAdapter.MyBook> {
    List<Book> list = Collections.emptyList();
    Context mContext;
    public AdminBooksAdapter(List<Book> list, Context context){
        this.list = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.full_single_book_card,
                parent, false);
        return new AdminBooksAdapter.MyBook(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBook holder, int position) {
        Book book = list.get(position);
        holder.tvBookName.setText(book.getBook_name());
        holder.author_name.setText(book.getAuth_name());
        holder.tvPublished.setText(book.getPublished());
        holder.tvRate.setText("$ "+book.getPurchase_amt());
        Picasso.get().load(book.getImage()).into(holder.bookImgs);
        holder.bookCard.setOnClickListener(view->{
            Toast.makeText(mContext, "coming soon!!", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    static class MyBook extends RecyclerView.ViewHolder{
        // Here we hold the MyDoctorItems
        TextView tvAuth,tvBookName,tvRate,author_name,tvPublished;
        ImageView bookImgs;
        CardView bookCard;
        public MyBook(@NonNull View itemView) {
            super(itemView);
            bookImgs = itemView.findViewById(R.id.bookImgs);
            tvBookName = itemView.findViewById(R.id.book_name);
            author_name = itemView.findViewById(R.id.author_name);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvAuth = itemView.findViewById(R.id.tvAuth);
            tvPublished = itemView.findViewById(R.id.tvPublished);
            bookCard = itemView.findViewById(R.id.bookCard);
        }
    }
    public void updateList(List<Book> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
