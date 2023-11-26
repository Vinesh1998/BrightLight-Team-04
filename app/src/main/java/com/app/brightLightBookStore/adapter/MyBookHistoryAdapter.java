package com.app.brightLightBookStore.adapter;

import static com.app.brightLightBookStore.adapter.CartBookAdapter.df;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.activities.User.BooksCart;
import com.app.brightLightBookStore.model.HistoryBook;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class MyBookHistoryAdapter extends RecyclerView.Adapter<MyBookHistoryAdapter.MyBookHistory> {
    List<HistoryBook> list = Collections.emptyList();
    Context mContext;

    public MyBookHistoryAdapter(List<HistoryBook> list, Context context){
        this.list = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyBookHistory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_book_history_item, parent, false);
        return new MyBookHistoryAdapter.MyBookHistory(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBookHistory holder, int position) {
        HistoryBook book = list.get(position);
        holder.tvAuth.setText(book.getAuth_name());
        holder.tvBookName.setText(book.getName());
        holder.tvDate.setText(book.getPurchase_date());
        holder.tvBookName.setText(book.getName());
        Picasso.get().load(book.getImage()).into(holder.imgBook);

        if(book.getPurchase_amt() > 0){
            holder.tvType.setText("Purchase");
            holder.tvType.setTextColor(mContext.getResources().getColor(R.color.primary));
//            holder.tvRate.setText(df.format(book.getPurchase_amt()+""));
            holder.tvRate.setText(book.getPurchase_amt()+"");
        }else if(book.getRental_amt() > 0){
            holder.tvDays.setVisibility(View.VISIBLE);
            holder.tvDays.setText(book.getDays() + "days * " + book.getRental_amt());
            holder.tvType.setText("Rental");
            holder.tvType.setTextColor(mContext.getResources().getColor(R.color.red));
            Double rate = (book.getDays() * book.getRental_amt());
            holder.tvRate.setText(df.format(rate));
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    static class MyBookHistory extends RecyclerView.ViewHolder{
        // Here we hold the MyDoctorItems
        TextView tvAuth,tvBookName,tvRate,tvType,tvDays,tvDate;
        ImageView imgBook;
        CardView bookCard;
        public MyBookHistory(@NonNull View itemView) {
            super(itemView);
            bookCard = itemView.findViewById(R.id.bookCard);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            imgBook = itemView.findViewById(R.id.bookImg);
            tvType = itemView.findViewById(R.id.tvType);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvAuth = itemView.findViewById(R.id.tvAuth);
            bookCard = itemView.findViewById(R.id.bookCard);
            tvDays = itemView.findViewById(R.id.tvDays);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
