package com.app.brightLightBookStore.activities.User;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.app.brightLightBookStore.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;

public class PaymentActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    CardView cardCard,cardPaypal;
    Double amount;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_page);
        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
        cardPaypal = findViewById(R.id.cardPaypal);
        cardCard = findViewById(R.id.cardCard);
        findViewById(R.id.ivBack).setOnClickListener(view->{
            super.onBackPressed();
        });
        cardPaypal.setOnClickListener(v -> {
            showPaypal();
        });
        cardCard.setOnClickListener(v -> {
            showCard();
        });

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
            }else
                proceedPay();
        });
    }

    void showCard(){
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.show_card);
        bottomSheetDialog.show();
        EditText etHolderName = bottomSheetDialog.findViewById(R.id.etHolderName);
        EditText etCardNumber = bottomSheetDialog.findViewById(R.id.etCardNumber);
        EditText etCvv = bottomSheetDialog.findViewById(R.id.etCvv);
        Button proceed = bottomSheetDialog.findViewById(R.id.PayBtn);

        proceed.setText("Proceed to Pay  $ "+amount);
        proceed.setOnClickListener(v -> {
            if(etHolderName.getText().toString().isEmpty() && etHolderName.getText().toString().length() < 5){
                etHolderName.setError("invalid name!!!");
            }
            else if(etCardNumber.getText().toString().isEmpty() && etCardNumber.getText().toString().length() < 5){
                etHolderName.setError("invalid card number!!!");
            }
            else if(etCvv.getText().toString().isEmpty() && etCvv.getText().toString().length() < 2){
                etHolderName.setError("invalid CVV!!!");
            }else
               proceedPay();
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
