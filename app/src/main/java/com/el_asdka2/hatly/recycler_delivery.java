package com.el_asdka2.hatly;

import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class recycler_delivery extends AppCompatActivity {
    ArrayList<request_delivery> requestDeliveryList;
    RecyclerViewAdapterDelivery requestArrayAdapter;
    private RecyclerView recyclerView;
    Button button;

    boolean doubleBackToExitPressedOnce = false;

    String latitude ;
    String longitude ;
    String Cust_Name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_delivery);
       /* final Navigation o = new Navigation();
        Cust_Name = o.sname;*/
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Requests");
        requestDeliveryList = new ArrayList<>();
        button = findViewById(R.id.test);
        recyclerView=findViewById(R.id.recycler_delivery);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    String price = dataSnapshot1.child("price").getValue().toString();
                    String fare = dataSnapshot1.child("fare").getValue().toString();
                    String description = dataSnapshot1.child("request-description").getValue().toString();
                    String id = dataSnapshot1.child("id_email").getValue().toString();
                    latitude=dataSnapshot1.child("Latitude").getValue().toString();
                    longitude=dataSnapshot1.child("Longitude").getValue().toString();

                    request_delivery Request = new request_delivery();
                   // Request.setCustomer_name(o.sname);
                    Request.setDescription(description);
                    Request.setPrice(price);
                    Request.setFare(fare);
                    Request.setId(id);
                    Request.setLatitude(latitude);
                    Request.setLongitude(longitude);
                    requestDeliveryList.add(Request);
                }
                requestArrayAdapter = new RecyclerViewAdapterDelivery(requestDeliveryList, recycler_delivery.this);
                recyclerView.setAdapter(requestArrayAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            moveTaskToBack(true);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;

            }
        }, 2000);
    }

}