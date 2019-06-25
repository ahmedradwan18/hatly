package com.el_asdka2.hatly;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class NotificationReceiverDecline extends BroadcastReceiver {
    FirebaseUser firebaseUser;
    DatabaseReference reference;
    String CustomerID, DeliveryID;
    Intent intent;
    @Override
    public void onReceive(Context context, Intent intent) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("RejectedOrders");

        CustomerID = intent.getStringExtra("CustomerID");
        DeliveryID = intent.getStringExtra("DeliveryID");

        HashMap<String,String> RejectedMap = new HashMap<>();
        RejectedMap.put("to","cofmAjjK1PbnJRVUoDZAtncdO1k1");
        RejectedMap.put("from","1PUS2gT7taWBljn1fII2Jyn3ozL2");
        RejectedMap.put("message","Sorry , Your Request has been rejected");
        reference.push().setValue(RejectedMap).addOnCompleteListener(task -> {
            Toast.makeText(context,"Notification sent",Toast.LENGTH_SHORT).show();
        });

    }
}
