package com.app.brightLightBookStore.adapter;

import static com.app.brightLightBookStore.adapter.CartBookAdapter.df;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.app.brightLightBookStore.activities.Admin.UserRentals;
import com.app.brightLightBookStore.activities.User.UserProfile;
import com.app.brightLightBookStore.model.HistoryBook;
import com.app.brightLightBookStore.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AdminBookHistoryAdapter extends RecyclerView.Adapter<AdminBookHistoryAdapter.MyBookHistory> {
    List<HistoryBook> list = Collections.emptyList();
    Context mContext;
    private DatabaseReference mDatabase;

    public AdminBookHistoryAdapter(List<HistoryBook> list, Context context){
        this.list = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyBookHistory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_book_history_item, parent, false);
        return new AdminBookHistoryAdapter.MyBookHistory(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyBookHistory holder, int position) {
        HistoryBook book = list.get(position);
        holder.tvAuth.setText(book.getAuth_name());
        holder.tvBookName.setText(book.getName());
        holder.tvDate.setText(book.getPurchase_date());
        holder.tvDateCollect.setText(book.getPurchase_date());
        holder.tvBookName.setText(book.getName());
        Picasso.get().load(book.getImage()).into(holder.imgBook);

        holder.bookCard.setOnClickListener(view->{
            if(!book.isFlag_return()) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());
                alertDialogBuilder.setMessage("Are you collecting?");
                alertDialogBuilder.setPositiveButton("Yes",
                        (arg0, arg1) -> {
                            //update record
                            mDatabase = FirebaseDatabase.getInstance().getReference("history");
                            book.setFlag_return(true);
                            mDatabase.child(book.getId()).setValue(book);
                            Intent myIntent = new Intent(view.getContext(), UserRentals.class);
                            view.getContext().startActivity(myIntent);
                            Toast.makeText(mContext, "Collected!", Toast.LENGTH_SHORT).show();
                        });
                alertDialogBuilder.setNegativeButton("No",
                        (arg0, arg1) -> {
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }else{
                Toast.makeText(mContext, "Book already collected!", Toast.LENGTH_SHORT).show();
            }
        });

        if(book.getPurchase_amt() > 0){
            holder.tvDateCollect.setVisibility(View.GONE);
            holder.ivStatus.setVisibility(View.GONE);
            holder.tvType.setText("Purchase");
            holder.tvType.setTextColor(mContext.getResources().getColor(R.color.primary));
            holder.tvRate.setText(book.getPurchase_amt()+"");
        }else if(book.getRental_amt() > 0){
            if(book.isFlag_return()) {
                holder.ivStatus.setVisibility(View.VISIBLE);
            }
            holder.tvDateCollect.setVisibility(View.VISIBLE);
            String stringDate=book.getPurchase_date();
            Date date1= null;
            try {
                date1 = new SimpleDateFormat("MM-dd-yyyy").parse(stringDate);
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(date1);

            cal.add(Calendar.DATE, book.getDays());
            Date new_date = cal.getTime();
            String date = new SimpleDateFormat("MM-dd-yyyy", Locale.getDefault()).format(new_date);
            holder.tvDateCollect.setText(date);
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
        TextView tvAuth,tvBookName,tvRate,tvType,tvDays,tvDate,tvDateCollect;
        ImageView imgBook, ivStatus;
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
            ivStatus = itemView.findViewById(R.id.ivStatus);
            tvDateCollect = itemView.findViewById(R.id.tvDateCollect);
            tvDate = itemView.findViewById(R.id.tvDate);
        }
    }
}
