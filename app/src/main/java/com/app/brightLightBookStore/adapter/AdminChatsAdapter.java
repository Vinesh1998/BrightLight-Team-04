package com.app.brightLightBookStore.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.activities.Admin.AdminChatConversation;
import com.app.brightLightBookStore.activities.User.BookDetails;
import com.app.brightLightBookStore.model.Chat;
import com.app.brightLightBookStore.model.ChatUsers;

import java.util.Collections;
import java.util.List;

public class AdminChatsAdapter extends RecyclerView.Adapter<AdminChatsAdapter.MyChat> {
    List<ChatUsers> list = Collections.emptyList();
    Context mContext;
    public AdminChatsAdapter(List<ChatUsers> list, Context context){
        this.list = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_chat_card, parent, false);
        return new AdminChatsAdapter.MyChat(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyChat holder, int position) {
          ChatUsers chat = list.get(position);
          holder.tvMsg.setText(chat.getMsg());
          holder.tvDateTime.setText(chat.getDate()+" "+chat.getTime());
          holder.tvUsr.setText(chat.getUser_name());

          if(chat.isStatus())  holder.ivStatus.setVisibility(View.GONE);
          else holder.ivStatus.setVisibility(View.VISIBLE);

          holder.chat_layout.setOnClickListener(view -> {
              //update to saw
              Intent intent = new Intent(view.getContext(), AdminChatConversation.class);
              Bundle extras = new Bundle();
              extras.putString("chat_id",chat.getId());
              intent.putExtras(extras);
              view.getContext().startActivity(intent);
          });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    static class MyChat extends RecyclerView.ViewHolder{
        // Here we hold the MyDoctorItems
        TextView tvMsg,tvDateTime,tvUsr;
        LinearLayout chat_layout;
        ImageView ivStatus;
        public MyChat(@NonNull View itemView) {
            super(itemView);
            tvMsg = itemView.findViewById(R.id.tvMsg);
            tvUsr = itemView.findViewById(R.id.tvUsr);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            chat_layout = itemView.findViewById(R.id.chat_layout);
            ivStatus = itemView.findViewById(R.id.ivStatus);
        }
    }
}
