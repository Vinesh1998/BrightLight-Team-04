package com.app.brightLightBookStore.adapter;

import static com.app.brightLightBookStore.adapter.CartBookAdapter.df;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.activities.User.BookDetails;
import com.app.brightLightBookStore.activities.User.DashboardUserActivity;
import com.app.brightLightBookStore.model.Book;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class PopularBooksAdapter extends RecyclerView.Adapter<PopularBooksAdapter.MyBook> {
    List<Book> list = Collections.emptyList();
    Context mContext;
    public PopularBooksAdapter(List<Book> list, Context context){
        this.list = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_book_card, parent, false);
        return new PopularBooksAdapter.MyBook(v);
    }
    @Override
    public void onBindViewHolder(@NonNull MyBook holder, int position) {
        Book book = list.get(position);
        holder.tvBookName.setText(book.getBook_name());
        holder.tvAuth.setText(book.getAuth_name());
        if(book.getPurchase_amt()  > 0) {
            holder.tvRate.setText("$ " + df.format(book.getPurchase_amt()));
        }else{
            holder.tvRate.setText("$ " + df.format(book.getRental_amt()));
        }

        Picasso.get().load(book.getImage()).into(holder.imgBook);
        holder.bookCard.setOnClickListener(view->{
                Intent intent = new Intent(view.getContext(), BookDetails.class);
                Bundle extras = new Bundle();
                extras.putString("book_id",book.getId());
                intent.putExtras(extras);
                view.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    static class MyBook extends RecyclerView.ViewHolder{
        // Here we hold the MyDoctorItems
        TextView tvAuth,tvBookName,tvRate;
        ImageView imgBook;
        CardView bookCard;
        public MyBook(@NonNull View itemView) {
            super(itemView);
            bookCard = itemView.findViewById(R.id.bookCard);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvAuth = itemView.findViewById(R.id.tvAuth);
            bookCard = itemView.findViewById(R.id.bookCard);
            imgBook = itemView.findViewById(R.id.bookImg);
        }
    }
    public void updateList(List<Book> list){
        this.list = list;
        notifyDataSetChanged();
    }
}
