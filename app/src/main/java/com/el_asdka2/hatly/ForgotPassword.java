package com.el_asdka2.hatly;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

/*
import butterknife.BindView;
*/

public class ForgotPassword extends AppCompatActivity {
    //@BindView(R.id.verifiedMail)
    EditText verimail;

    //   @BindView(R.id.resetPass_btn)
    Button resetPAss;

    private FirebaseAuth firebaseAuth;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ForgotPassword.this , login.class);
        startActivity(intent);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  ButterKnife.bind(this);
        setContentView(R.layout.activity_forgot_password);

        verimail = findViewById(R.id.verifiedMail);
        resetPAss = findViewById(R.id.resetPass_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        resetPAss.setOnClickListener(view -> {
            String usermail = verimail.getText().toString().trim();
            if (usermail.equals("")) {
                Toast.makeText(ForgotPassword.this, "Please Enter Your Registered Mail ID", Toast.LENGTH_SHORT).show();
            } else {
                firebaseAuth.sendPasswordResetEmail(usermail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(ForgotPassword.this, "Password Reset Email Sent ", Toast.LENGTH_SHORT).show();
                            finish();
                            startActivity(new Intent(ForgotPassword.this, login.class));

                        } else {
                            Toast.makeText(ForgotPassword.this, "Error in Sending Password Reset Email ", Toast.LENGTH_SHORT).show();

                        }

                    }
                });
            }
        });

    }


}