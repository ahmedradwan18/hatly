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

public class NotificationReceiverAccept extends BroadcastReceiver {
    FirebaseUser firebaseUser;
    DatabaseReference reference,reference2;
    String CustomerID, DeliveryID,Order_desc;

    Intent intent1;
    @Override
    public void onReceive(Context context, Intent intent) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("AcceptedOrders");
        reference2 = FirebaseDatabase.getInstance().getReference("AcceptedRate");

        CustomerID = intent.getStringExtra("CustomerID_2");
        DeliveryID = intent.getStringExtra("DeliveryID_1");
        Order_desc = intent.getStringExtra("order_desc_3");

        HashMap<String,String> AcceptedMap = new HashMap<>();
       // AcceptedMap.put("to","cofmAjjK1PbnJRVUoDZAtncdO1k1");
        //AcceptedMap.put("from","1PUS2gT7taWBljn1fII2Jyn3ozL2");
        AcceptedMap.put("from",CustomerID);
        AcceptedMap.put("to",DeliveryID);
        AcceptedMap.put("message","Your Request has been Accepted");
        AcceptedMap.put("order_desc",Order_desc);
        reference.push().setValue(AcceptedMap).addOnCompleteListener(task -> {
            Toast.makeText(context,"Notification sent",Toast.LENGTH_SHORT).show();
        });

        HashMap<String,String> AcceptedRateMap = new HashMap<>();

        AcceptedRateMap.put("DeliveryID",DeliveryID);
        AcceptedRateMap.put("Order_desc",Order_desc);

        reference2.push().setValue(AcceptedRateMap);
    }


}
