package com.el_asdka2.hatly;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RecyclerViewAdapterDelivery extends RecyclerView.Adapter<RecyclerViewAdapterDelivery.MyViewHolder> {
    private ArrayList<request_delivery> requestDeliveryList;
    Context context;
    Dialog mydialog;
    Dialog dialogProf;
    String snav_img;
    String userName;
    String email;
    String phone;
    String address;
    String national;
    String image;
    String ToUserID;
    String FromUserID;
    String Delivery_Name;
    FirebaseAuth NotifAuth;
    DatabaseReference databaseReference;
    String CustomerName;
    String Delivery;
    DatabaseReference myRef;
    int Badctr,Terriblectr,Okayctr,Goodctr,Greatctr;
    Intent intent;
    public RecyclerViewAdapterDelivery(ArrayList<request_delivery> requestDeliveryList, Context context) {
        this.requestDeliveryList = requestDeliveryList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_delivery, parent, false);
        final MyViewHolder viewHolder = new MyViewHolder(view);
        mydialog = new Dialog(context);
        mydialog.setContentView(R.layout.order_dialog);



        mydialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Terriblectr = 0;
        Badctr = 0;
        Okayctr = 0;
        Goodctr = 0;
        Greatctr = 0;

        return new MyViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onBindViewHolder(@NonNull final RecyclerViewAdapterDelivery.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final request_delivery request = requestDeliveryList.get(position);
        holder.description.setText(request.getDescription());
        holder.customer_rate.setText(request.getPrice());
        final String x = request.getLatitude();
        final String y = request.getLongitude();
        final String id = request.getId();
        FirebaseDatabase database;
        FirebaseAuth mAuth;
        FromUserID = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();

        DatabaseReference DeliveryRef = FirebaseDatabase.getInstance().getReference("Users");
        DeliveryRef.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Delivery_Name = dataSnapshot.child("UserName").getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        NotifAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Notifications");
        ToUserID = id;

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
        myRef.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                snav_img = dataSnapshot.child("imgURI").getValue(String.class);
                String suserName = dataSnapshot.child("UserName").getValue(String.class);
                Glide.with(context.getApplicationContext()).load(snav_img).into(holder.customer_img);
                holder.customer_name.setText(suserName);

            }


            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        holder.customer_img.setOnClickListener(view -> {


            myRef.child(id).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userName = dataSnapshot.child("UserName").getValue(String.class);
                    email = dataSnapshot.child("e-mail").getValue(String.class);
                    phone = dataSnapshot.child("Phone").getValue(String.class);
                    address = dataSnapshot.child("address").getValue(String.class);
                    national = dataSnapshot.child("national-number").getValue(String.class);
                    image = dataSnapshot.child("imgURI").getValue(String.class);

                    intent = new Intent(context,ViewProfile.class);
                    intent.putExtra("username",userName);
                    intent.putExtra("useraddress",address);
                    intent.putExtra("userphone",phone);
                    intent.putExtra("usernational",national);
                    intent.putExtra("useremail",email);
                    intent.putExtra("userimage",image);
                    context.startActivity(intent);

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });



        });

        holder.order_info.setOnClickListener(v -> {
            Toast.makeText(context, x + " && " + y, Toast.LENGTH_LONG).show();
            TextView dialog_description_txt = mydialog.findViewById(R.id.dialogue_desc_txt);
            TextView dialog_price_txt = mydialog.findViewById(R.id.dialogue_price_txt);
            TextView dialog_fare_txt = mydialog.findViewById(R.id.dialogue_fare_txt);
            Button dialog_location_btn = mydialog.findViewById(R.id.dialogue_location_btn);
            Button dialog_request_btn = mydialog.findViewById(R.id.dialogue_request_btn);

            dialog_description_txt.setText(requestDeliveryList.get(position).getDescription());
            dialog_price_txt.setText(requestDeliveryList.get(position).getPrice());
            dialog_fare_txt.setText(requestDeliveryList.get(position).getFare());


            dialog_location_btn.setOnClickListener(v1 -> {

                Intent intent = new Intent(context, MapsActivity.class);
                intent.putExtra("latitude", x);
                intent.putExtra("longitude", y);
                intent.putExtra("customer_name", holder.customer_name.getText().toString());
                CustomerName = holder.customer_name.getText().toString();
                context.startActivity(intent);

            });
            dialog_request_btn.setOnClickListener(v12 -> {

                String messageText = "Hi " + holder.customer_name.getText().toString() + " , " + Delivery_Name + " Want To Apply Your Order !";
                Map <String,String> NotificationMap = new HashMap<>();
                NotificationMap.put("message",messageText);
                NotificationMap.put("to",request.getId());
                NotificationMap.put("order",request.getDescription());
                NotificationMap.put("from",FromUserID);
                databaseReference.push().setValue(NotificationMap).addOnSuccessListener(aVoid -> {
                    Toast.makeText(context, "Notification Sent", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(context, "Error in Sending Notification", Toast.LENGTH_SHORT).show();
                });

            });

            mydialog.show();
        });


    }


    @Override
    public int getItemCount() {
        if (requestDeliveryList == null) return 0;
        return requestDeliveryList.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        Button order_info; // Karim
        TextView description, customer_name, customer_rate; // Karim
        ImageView customer_img; // Karim

        public MyViewHolder(View itemView) {
            super(itemView);
            description = itemView.findViewById(R.id.desc_txt); // Karim
            customer_name = itemView.findViewById(R.id.cust_name); // Karim
            customer_rate = itemView.findViewById(R.id.cust_rate); // Karim
            order_info = itemView.findViewById(R.id.btn_info); // Karim
            customer_img = itemView.findViewById(R.id.cust_img); // Karim


        }
    }
}