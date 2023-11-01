package com.app.brightLightBookStore.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.model.Chat;
import java.util.Collections;
import java.util.List;

public class MyChatsAdapter extends RecyclerView.Adapter<MyChatsAdapter.MyChat> {
    List<Chat> list = Collections.emptyList();
    Context mContext;
    String uid;
    int VIEW_TYPE_MESSAGE_SENT = 1, VIEW_TYPE_MESSAGE_RECEIVED = 2;
    public MyChatsAdapter(List<Chat> list, Context context, String uid){
        this.list = list;
        this.mContext = context;
        this.uid = uid;
    }
    @Override
    public int getItemViewType(int position) {
        Chat message = (Chat) list.get(position);

        if(uid.equals(message.getSender_id()))
            return VIEW_TYPE_MESSAGE_SENT;
        else
            return VIEW_TYPE_MESSAGE_RECEIVED;
    }
    @NonNull
    @Override
    public MyChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate((viewType == 1) ?
                R.layout.chat_card_sent : R.layout.chat_card_received, parent, false);
        return new MyChatsAdapter.MyChat(v);
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
              holder.tvUsr.setText("Admin: ");
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
        CardView cardLayout;
        public MyChat(@NonNull View itemView) {
            super(itemView);
            tvMsg = itemView.findViewById(R.id.tvMsg);
            tvUsr = itemView.findViewById(R.id.tvUsr);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            cardLayout = itemView.findViewById(R.id.cardLayout);
            chat_layout = itemView.findViewById(R.id.chat_layout);
        }
    }
}
