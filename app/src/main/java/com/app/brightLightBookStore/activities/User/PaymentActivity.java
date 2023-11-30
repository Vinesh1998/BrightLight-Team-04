package com.app.brightLightBookStore.activities.User;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.app.brightLightBookStore.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    CardView cardPaypal, paymentCards;
    Double amount;

    String cvv_num;

    int count = 4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        cardPaypal = findViewById(R.id.cardPaypal);
        paymentCards = findViewById(R.id.cardCards);
        findViewById(R.id.ivBack).setOnClickListener(view-> super.onBackPressed());

        cardPaypal.setOnClickListener(v -> showPaypal());
        paymentCards.setOnClickListener(v -> showCard());

        Intent intent = getIntent();
        if (null != intent) {
            //Null Checking
            if (intent.getStringExtra("amount").isEmpty()) {
                Toast.makeText(this, "invalid!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), DashboardUserActivity.class));
            }else{
                amount = Double.parseDouble(intent.getStringExtra("amount"));
            }
        }
    }

    void showPaypal(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.show_paypal);
        bottomSheetDialog.show();
        EditText etHolderName = bottomSheetDialog.findViewById(R.id.etHolderName);
        EditText etHolderPass = bottomSheetDialog.findViewById(R.id.etHolderPass);
        Button proceed = bottomSheetDialog.findViewById(R.id.PayBtn);
        proceed.setText("Proceed to Pay  $ "+amount);
        proceed.setOnClickListener(v -> {
            if(etHolderName.getText().toString().isEmpty() || etHolderName.getText().toString().length() < 5){
                etHolderName.setError("invalid!");
                etHolderName.setFocusable(true);
            }else if(etHolderPass.getText().toString().isEmpty() || etHolderPass.getText().toString().length() < 5){
                etHolderPass.setError("invalid!");
                etHolderPass.setFocusable(true);
            }
            else
                proceedPay();
        });
    }

    void showCard(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.show_card);
        bottomSheetDialog.show();
        Spinner spCardSelect = bottomSheetDialog.findViewById(R.id.spCardSelect);
        EditText etHolderName = bottomSheetDialog.findViewById(R.id.etHolderName);
        EditText etCardNumber = bottomSheetDialog.findViewById(R.id.etCardNumber);
        EditText etCvv = bottomSheetDialog.findViewById(R.id.etCvv);
        Button proceed = bottomSheetDialog.findViewById(R.id.PayBtn);
        ImageView ivCard = bottomSheetDialog.findViewById(R.id.ivCard);
        TextView tvCardTitle = bottomSheetDialog.findViewById(R.id.tvCardTitle);

        proceed.setText("Proceed to Pay  $ "+amount);
        tvCardTitle.setText(" Card");
        final List<String> list = new ArrayList<String>();
        list.add("American express");
        list.add("Discover Card");
        list.add("Mastercard");
        list.add("Visa Card");

        ArrayAdapter<String> adp1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, list);
        adp1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCardSelect.setAdapter(adp1);

        etCvv.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                cvv_num  = charSequence.toString();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String currentString = etCvv.getText().toString();
                    if (currentString.length() > count) {
                        etCvv.setText(cvv_num);
                        etCvv.setFocusable(true);
                    }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        proceed.setOnClickListener(v -> {
            if(etHolderName.getText().toString().isEmpty() && etHolderName.getText().toString().length() < 5){
                etHolderName.setError("invalid name!!!");
            }
            else if(etCardNumber.getText().toString().isEmpty() && etCardNumber.getText().toString().length() < 5){
                etHolderName.setError("invalid card number!!!");
            }
            else if(etCvv.getText().toString().isEmpty() || etCvv.getText().toString().length() < count){
                etCvv.setError("invalid CVV!!!");
            }
            else
               proceedPay();
        });

        spCardSelect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                count = 3;
                if(etCvv.getText().toString().length() > 0){
                        etCvv.setText("");
                        etCvv.setFocusable(true);
                        cvv_num = "";
                }
                ((TextView) view).setTextColor(Color.BLACK); //Change selected text color
                if(i == 0){
                    ivCard.setBackground(getResources().getDrawable(R.drawable.americanexpress));
                    count = 4;
                }
                else if(i == 1)
                    ivCard.setBackground(getResources().getDrawable(R.drawable.discover));
                else if(i == 2)
                    ivCard.setBackground(getResources().getDrawable(R.drawable.mastercard));
                else if(i == 3)
                    ivCard.setBackground(getResources().getDrawable(R.drawable.visa));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    private void proceedPay() {
        Intent intent = new Intent(getApplicationContext(), PaymentStatus.class);
        Bundle extras = new Bundle();
        extras.putString("amount",String.valueOf(amount));
        intent.putExtras(extras);
        startActivity(intent);
    }
}
//This functionality is about the payment activity of the application
