package com.el_asdka2.hatly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class paymentsAdapter extends RecyclerView.Adapter<paymentsAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<payments> paymentslist;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference ref = database.getReference("Requests");

    public paymentsAdapter(ArrayList<payments> paymentslist, Context context) {
        this.paymentslist = paymentslist;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_payments, parent, false);
        final MyViewHolder viewHolder=new MyViewHolder(view);
        return new paymentsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final payments payments = paymentslist.get(position);
        holder.descP.setText(payments.getDescription());
        holder.priceP.setText(payments.getPrice());
        holder.fareP.setText(payments.getFare());
        holder.date.setText(payments.getDate());
    }

    @Override
    public int getItemCount() {
        if (paymentslist == null) return 0;
        return paymentslist.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
      TextView descP,priceP,fareP,date;
        public MyViewHolder(View itemView) {
            super(itemView);
            descP = (TextView) itemView.findViewById(R.id.desc_payments);
            priceP = (TextView) itemView.findViewById(R.id.price_payments);
            fareP = itemView.findViewById(R.id.fare_payments);
            date = itemView.findViewById(R.id.date_payments);

        }
    }
}
