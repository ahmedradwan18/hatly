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

public class Recycler extends AppCompatActivity {
    ArrayList<request> requestList;
    RecyclerViewAdapter requestArrayAdapter;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Requests");
        requestList = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler);
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
                    String id_order = dataSnapshot1.getKey();

                    request req = new request();
                    req.setDescription(description);
                    req.setPrice(price);
                    req.setId(id_order);

                    requestList.add(req);
                }
                requestArrayAdapter = new RecyclerViewAdapter(requestList, Recycler.this);
                recyclerView.setAdapter(requestArrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}