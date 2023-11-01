package com.app.brightLightBookStore.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.brightLightBookStore.R;
import com.app.brightLightBookStore.activities.User.BooksCart;
import com.app.brightLightBookStore.model.Book;
import com.app.brightLightBookStore.model.CartBook;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.List;

public class CartBookAdapter extends RecyclerView.Adapter<CartBookAdapter.MyBook> {
    List<CartBook> list = Collections.emptyList();
    Context mContext;
    public static final DecimalFormat df = new DecimalFormat("0.00");
    DatabaseReference  mDatabase = FirebaseDatabase.getInstance().getReference().child("cart");
    public CartBookAdapter(List<CartBook> list, Context context){
        this.list = list;
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyBook onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_book_card,
                parent, false);
        return new CartBookAdapter.MyBook(v);
    }

    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onBindViewHolder(@NonNull MyBook holder, int position) {
        CartBook book = list.get(position);
        holder.tvBookName.setText(book.getName());
        holder.tvQty.setText(book.getCart_qty()+"");
        holder.tvAuth.setText("by "+book.getAuth_name());

        Picasso.get().load(book.getImage()).into(holder.imgBook);

        update_data(holder, book, true);

        holder.selectItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked()) {
                // checked update status
                // insert into cart collection
                mDatabase.child(book.getId()).child("check").setValue(true);
                update_data(holder, book, true);
            }else{
                mDatabase.child(book.getId()).child("check").setValue(false);
                update_data_uncheck(holder, book, false);
            }
        });
        holder.btnAdd.setOnClickListener(view->{
            if(5 > book.getCart_qty()) {
                if (holder.selectItem.isChecked()){
                    int qty = book.getCart_qty() + 1;
                    book.setCart_qty(qty);
                    holder.tvQty.setText(qty + "");
                    BooksCart.checkout += 1;
                    BooksCart.tvCheckout.setText(BooksCart.checkout + "");

                    if (book.getPurchase_amt() > 0) {
                        Double totalCheck = Double.parseDouble(BooksCart.tvTotalCheckout.getText().toString());
                        totalCheck += book.getPurchase_amt();
                        BooksCart.tvTotalCheckout.setText(df.format(totalCheck));
//                        mDatabase.getDatabase().getReference("books").child(book.getId()).child("count").setValue(book.getCount()-1);
                    } else if (book.getRental_amt() > 0) {
                        Double totalCheck = Double.parseDouble(BooksCart.tvTotalCheckout.getText().toString());
                        totalCheck += book.getRental_amt();
                        BooksCart.tvTotalCheckout.setText(df.format(totalCheck));
                    }
                }else Toast.makeText(mContext, "Book is not selected!!", Toast.LENGTH_SHORT).show();
            }else Toast.makeText(mContext, "quantity exceeded!", Toast.LENGTH_SHORT).show();
        });
        holder.btnRemove.setOnClickListener(view->{
            if(book.getCart_qty() > 0) {
                if (holder.selectItem.isChecked()) {
                    int qty = book.getCart_qty() - 1;
                    book.setCart_qty(qty);
                    holder.tvQty.setText(qty + "");
                    BooksCart.checkout  -= 1;

                    BooksCart.tvCheckout.setText(BooksCart.checkout+"");
                    if (book.getPurchase_amt() > 0) {
                        Double totalCheck = Double.parseDouble(BooksCart.tvTotalCheckout.getText().toString());
                        totalCheck -= book.getPurchase_amt();
                        BooksCart.tvTotalCheckout.setText(df.format(totalCheck));
                    } else if (book.getRental_amt() > 0) {
                        Double totalCheck = Double.parseDouble(BooksCart.tvTotalCheckout.getText().toString());
                        totalCheck -= book.getRental_amt();
                        BooksCart.tvTotalCheckout.setText(df.format(totalCheck));
                    }
                }else Toast.makeText(mContext, "Book is not selected!!", Toast.LENGTH_SHORT).show();

                if(book.getCart_qty() == 0) {
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("cart");
                    ref.child(book.getId()).removeValue().addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(mContext, "Removed!!!", Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(mContext, BooksCart.class);
                            myIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            mContext.startActivity(myIntent);
                        }
                    });
                }
            }else {
                // remove cart item
                Toast.makeText(mContext, "invalid!!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    static class MyBook extends RecyclerView.ViewHolder{
        // Here we hold the MyDoctorItems
        TextView tvAuth,tvBookName,tvRate,tvQty,tvType,tvDays;
        ImageView imgBook;
        Button btnAdd,btnRemove;
        CheckBox selectItem;
        CardView bookCard;
        public MyBook(@NonNull View itemView) {
            super(itemView);
            bookCard = itemView.findViewById(R.id.bookCard);
            tvBookName = itemView.findViewById(R.id.tvBookName);
            imgBook = itemView.findViewById(R.id.bookImg);
            tvType = itemView.findViewById(R.id.tvType);
            tvRate = itemView.findViewById(R.id.tvRate);
            tvAuth = itemView.findViewById(R.id.tvAuth);
            bookCard = itemView.findViewById(R.id.bookCard);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            tvQty = itemView.findViewById(R.id.tvQty);
            tvDays = itemView.findViewById(R.id.tvDays);
            btnRemove = itemView.findViewById(R.id.btnRemove);
            selectItem = itemView.findViewById(R.id.selectItem);
        }
    }

    private void update_data(MyBook holder, CartBook book, boolean stat) {
        if (book.getPurchase_amt() > 0) {
            holder.tvType.setText("Purchase");
            holder.tvType.setTextColor(mContext.getResources().getColor(R.color.primary));
            holder.tvRate.setText(book.getPurchase_amt() + "");
        } else if (book.getRental_amt() > 0) {
            holder.tvDays.setVisibility(View.VISIBLE);
            holder.tvDays.setText(book.getDays() + "days * " + book.getRental_amt());
            holder.tvType.setText("Rental");
            holder.tvType.setTextColor(mContext.getResources().getColor(R.color.red));
            Double rate = (book.getDays() * book.getRental_amt());
            holder.tvRate.setText(df.format(rate) + "");
        }
        if (holder.selectItem.isChecked()) {
            if (stat) BooksCart.checkout += book.getCart_qty();
            else BooksCart.checkout -= book.getCart_qty();
            if (book.getPurchase_amt() > 0) {
                Double totalCheck = Double.parseDouble(BooksCart.tvTotalCheckout.getText().toString());
                totalCheck += book.getPurchase_amt();
                BooksCart.tvTotalCheckout.setText(df.format(totalCheck));
            } else if (book.getRental_amt() > 0) {
                Double rate = (book.getDays() * book.getRental_amt());
                Double totalCheck = Double.parseDouble(BooksCart.tvTotalCheckout.getText().toString());
                totalCheck += rate;
                BooksCart.tvTotalCheckout.setText(df.format(totalCheck));
            }
            BooksCart.tvCheckout.setText(BooksCart.checkout + "");
        }
    }

    private void update_data_uncheck(MyBook holder, CartBook book, boolean stat){
            if(stat)   BooksCart.checkout  += book.getCart_qty();
            else BooksCart.checkout  -= book.getCart_qty();

            BooksCart.tvCheckout.setText(BooksCart.checkout+"");
            if (book.getPurchase_amt() > 0) {
                holder.tvType.setText("Purchase");
                holder.tvType.setTextColor(mContext.getResources().getColor(R.color.primary));
                Double totalCheck = Double.parseDouble(BooksCart.tvTotalCheckout.getText().toString());
                totalCheck -= book.getPurchase_amt();
                BooksCart.tvTotalCheckout.setText(df.format(totalCheck));
                holder.tvRate.setText(book.getPurchase_amt() + "");
            } else if (book.getRental_amt() > 0) {
                holder.tvDays.setVisibility(View.VISIBLE);
                holder.tvDays.setText(book.getDays() + "days * " + book.getRental_amt());
                holder.tvType.setText("Rental");
                holder.tvType.setTextColor(mContext.getResources().getColor(R.color.red));
                Double rate = (book.getDays() * book.getRental_amt());
                holder.tvRate.setText(rate + "");
                Double totalCheck = Double.parseDouble(BooksCart.tvTotalCheckout.getText().toString());
                totalCheck -= rate;
                BooksCart.tvTotalCheckout.setText(df.format(totalCheck));
            }
        }
    }
