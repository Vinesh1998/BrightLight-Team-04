package com.app.brightLightBookStore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.model.Chat;

import java.util.Collections;
import java.util.List;

public class AdminChatConversationsAdapter extends RecyclerView.Adapter<AdminChatConversationsAdapter.MyChat> {
    List<Chat> list = Collections.emptyList();
    Context mContext;
    String uid;
    public AdminChatConversationsAdapter(List<Chat> list, Context context, String uid){
        this.list = list;
        this.mContext = context;
        this.uid = uid;
    }

    @NonNull
    @Override
    public MyChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_card_sent, parent, false);
        return new AdminChatConversationsAdapter.MyChat(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyChat holder, int position) {
          Chat chat = list.get(position);
          holder.tvMsg.setText(chat.getMsg());

          holder.tvDateTime.setText(chat.getDate()+" "+chat.getTime());
          if(uid.equals(chat.getSender_id())){
                holder.chat_layout.setBackground(mContext.getDrawable(R.drawable.admin_chat_background));
                holder.tvUsr.setText("Me: ");
          }else{
              holder.chat_layout.setBackground(mContext.getDrawable(R.drawable.my_chat_background));
              holder.tvUsr.setText(chat.getSender_name()+": ");
          }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    static class MyChat extends RecyclerView.ViewHolder{
        // Here we hold the MyDoctorItems
        TextView tvMsg,tvDateTime,tvUsr;
        LinearLayout chat_layout;
        public MyChat(@NonNull View itemView) {
            super(itemView);
            tvMsg = itemView.findViewById(R.id.tvMsg);
            tvUsr = itemView.findViewById(R.id.tvUsr);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            chat_layout = itemView.findViewById(R.id.chat_layout);
        }
    }
}
