package com.el_asdka2.hatly;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class paymentsRecycler extends AppCompatActivity {
    ArrayList<payments> paymentslist;
    paymentsAdapter paymentsAdapter;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments_recycler);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Requests");
        paymentslist = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerPayments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        String currentUserUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref.orderByChild("id_email").equalTo(currentUserUid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    String price = (String) dataSnapshot1.child("price").getValue();
                    String description = (String) dataSnapshot1.child("request-description").getValue();
                    String fare = (String) dataSnapshot1.child("fare").getValue();
                    String date = (String) dataSnapshot1.child("date").getValue();
                    payments payments = new payments();
                    payments.setDescription(description);
                    payments.setPrice("price : "+price);
                    payments.setDate(date);
                    payments.setFare("fare : "+fare);
                    paymentslist.add(payments);
                }
                paymentsAdapter = new paymentsAdapter(paymentslist, paymentsRecycler.this);
                recyclerView.setAdapter(paymentsAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
