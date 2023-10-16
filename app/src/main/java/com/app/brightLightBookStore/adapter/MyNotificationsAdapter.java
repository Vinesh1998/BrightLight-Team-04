package com.app.brightLightBookStore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.model.Notification;

import java.util.Collections;
import java.util.List;

public class MyNotificationsAdapter extends RecyclerView.Adapter<MyNotificationsAdapter.MyNotification> {
    List<Notification> list = Collections.emptyList();
    Context mContext;
    public MyNotificationsAdapter(List<Notification> list, Context context){
        this.list = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyNotification onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new MyNotificationsAdapter.MyNotification(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyNotification holder, int position) {
        Notification book = list.get(position);
        holder.notifTitle.setText(book.getName());
        holder.tvBookName.setText(book.getBook_name());
        holder.tvPublished.setText(book.getPublished());
        holder.tvRate.setText("$ "+book.getAmt());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    static class MyNotification extends RecyclerView.ViewHolder{
        // Here we hold the MyDoctorItems
        TextView notifTitle,tvBookName,tvRate,tvPublished;
        CardView notifCard;
        public MyNotification(@NonNull View itemView) {
            super(itemView);
            notifCard = itemView.findViewById(R.id.notifCard);
            tvBookName = itemView.findViewById(R.id.book_name);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvPublished = itemView.findViewById(R.id.tvPublished);
            notifTitle = itemView.findViewById(R.id.notifTitle);
        }
    }
}
