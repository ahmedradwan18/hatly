package com.el_asdka2.hatly;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import java.util.ArrayList;
import java.util.HashMap;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<request> requestList;
    private Activity activity;
    Button Send_Rate;
    SmileRating smileRating;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Requests");
    DatabaseReference RateRef = database.getReference("Rates");
    DatabaseReference OrderRef = database.getReference("DeliveryOrders");
    DatabaseReference AcceptedRateRef = database.getReference("AcceptedRate");
    Dialog mydialog,RateDialog;
    String DeliveryID;

    public RecyclerViewAdapter(ArrayList<request> movieList, Context context) {
        this.requestList = movieList;
        this.context = context;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mydialog = new Dialog(context);
        mydialog.setContentView(R.layout.edit_request_dialog);

        RateDialog = new Dialog(context);
        RateDialog.setContentView(R.layout.rate_delivery_dialog);


        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        final MyViewHolder viewHolder=new MyViewHolder(view);
        return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final request request = requestList.get(position);
        holder.description_edt.setText(request.getDescription());
        holder.price_edt.setText(request.getPrice() + "");
        final String id = request.getId();
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ref.child(id).removeValue();
                requestList.remove(position);
                notifyDataSetChanged();
            }
        });
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText update_desc=mydialog.findViewById(R.id.editDescription);
                final EditText update_price=mydialog.findViewById(R.id.editPrice);
                final Button update=mydialog.findViewById(R.id.doneEdit);
                update_desc.setText(requestList.get(position).getDescription());
                update_price.setText(requestList.get(position).getPrice());
                update.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ref.child(id).child("request-description").setValue(update_desc.getText().toString());
                        ref.child(id).child("price").setValue(update_price.getText().toString());
                        requestList.get(position).setPrice(update_price.getText().toString());
                        requestList.get(position).setDescription(update_desc.getText().toString());
                        notifyDataSetChanged();
                    }
                });
                mydialog.show();
            }
        });
        holder.done.setOnClickListener(v -> {
            Send_Rate = RateDialog.findViewById(R.id.send_rate_btn);
            smileRating = RateDialog.findViewById(R.id.smile_rating);


            Send_Rate.setOnClickListener(v1 -> {

                @BaseRating.Smiley int smiley = smileRating.getSelectedSmile();
                switch (smiley){
                    case SmileRating.BAD :

                        Query query = AcceptedRateRef.orderByChild("Order_desc").equalTo(request.getDescription());
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                                        String DeliveryID = ds.child("DeliveryID").getValue(String.class);
                                        HashMap<String,Object> RateMapBad = new HashMap<>();
                                        RateMapBad.put("Rate","Bad");
                                        RateMapBad.put("DeliveryID",DeliveryID);
                                        RateRef.push().setValue(RateMapBad);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        RateDialog.dismiss();
                        break;

                    case SmileRating.TERRIBLE :

                        Query query1 = AcceptedRateRef.orderByChild("Order_desc").equalTo(request.getDescription());
                        query1.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                                        String DeliveryID = ds.child("DeliveryID").getValue(String.class);
                                        HashMap<String,Object> RateMapTerrible = new HashMap<>();
                                        RateMapTerrible.put("Rate","Terrible");
                                        RateMapTerrible.put("DeliveryID",DeliveryID);
                                        RateRef.push().setValue(RateMapTerrible);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        RateDialog.dismiss();
                        break;
                    case SmileRating.GOOD :
                        Query query2 = AcceptedRateRef.orderByChild("Order_desc").equalTo(request.getDescription());
                        query2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                                        String DeliveryID = ds.child("DeliveryID").getValue(String.class);
                                        HashMap<String,Object> RateMapGood = new HashMap<>();
                                        RateMapGood.put("Rate","GOOD");
                                        RateMapGood.put("DeliveryID",DeliveryID);
                                        RateRef.push().setValue(RateMapGood);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        RateDialog.dismiss();
                        break;
                    case SmileRating.GREAT :
                        Query query3 = AcceptedRateRef.orderByChild("Order_desc").equalTo(request.getDescription());
                        query3.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                                        String DeliveryID = ds.child("DeliveryID").getValue(String.class);
                                        HashMap<String,Object> RateMapGreat = new HashMap<>();
                                        RateMapGreat.put("Rate","GREAT");
                                        RateMapGreat.put("DeliveryID",DeliveryID);
                                        RateRef.push().setValue(RateMapGreat);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        RateDialog.dismiss();
                        break;
                    case SmileRating.OKAY :
                        Query query4 = AcceptedRateRef.orderByChild("Order_desc").equalTo(request.getDescription());
                        query4.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){
                                    for (DataSnapshot ds : dataSnapshot.getChildren()){
                                        String DeliveryID = ds.child("DeliveryID").getValue(String.class);
                                        HashMap<String,Object> RateMapOkay = new HashMap<>();
                                        RateMapOkay.put("Rate","OKAY");
                                        RateMapOkay.put("DeliveryID",DeliveryID);
                                        RateRef.push().setValue(RateMapOkay);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        RateDialog.dismiss();
                        break;
                }
                Toast.makeText(context.getApplicationContext(),"Thanks For Your Rate",Toast.LENGTH_SHORT).show();
            });

            RateDialog.show();
        });
    }

    @Override
    public int getItemCount() {
        if (requestList == null) return 0;
        return requestList.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        Button delete;
        Button edit;
        Button done;
        TextView description_edt, price_edt;

        public MyViewHolder(View itemView) {
            super(itemView);
            description_edt = itemView.findViewById(R.id.des_txt);
            price_edt = itemView.findViewById(R.id.price_txt);
            delete = itemView.findViewById(R.id.delBtn);
            edit = itemView.findViewById(R.id.edtbtn);
            done = itemView.findViewById(R.id.doneBtn);

        }
    }
}