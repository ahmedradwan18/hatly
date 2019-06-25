package com.el_asdka2.hatly;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;

public class ViewProfile extends AppCompatActivity {
    DatabaseReference myRef;
    TextView custName, custMail, custAddress, custPhone, custNationalID;
    ImageButton sendMail, call;
    ImageView custImg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        custName = findViewById(R.id.view_profile_name);
        custName.setText(getIntent().getExtras().getString("username"));

        custMail = findViewById(R.id.view_profile_email_txt);
        custMail.setText(getIntent().getExtras().getString("useremail"));

        custAddress = findViewById(R.id.view_profile_address_txt);
        custAddress.setText(getIntent().getExtras().getString("useraddress"));

        custPhone = findViewById(R.id.view_profile_phone_txt);
        custPhone.setText(getIntent().getExtras().getString("userphone"));

        custNationalID = findViewById(R.id.view_profile_national_txt);
        custNationalID.setText(getIntent().getExtras().getString("usernational"));

        custImg = findViewById(R.id.view_profile_img);
        Glide.with(ViewProfile.this.getApplicationContext()).load(getIntent().getExtras().getString("userimage")).into(custImg);

        sendMail = findViewById(R.id.view_profile_message_btn);
        sendMail.setOnClickListener(v -> {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", getIntent().getExtras().getString("useremail"), null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "This is my subject text");
            startActivity(Intent.createChooser(emailIntent, null));
        });

        call = findViewById(R.id.view_profile_call_btn);
        call.setOnClickListener(v -> {
            Uri number = Uri.parse("tel:" + getIntent().getExtras().getString("userphone"));
            Intent dial = new Intent(Intent.ACTION_DIAL);
            dial.setData(number);
            startActivity(dial);
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        custName.setText("");
        custMail.setText("");
        custPhone.setText("");
        custAddress.setText("");
        custNationalID.setText("");
    }
}
