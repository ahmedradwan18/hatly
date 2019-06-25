package com.el_asdka2.hatly;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.RemoteMessage;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    NotificationManagerCompat notificationManager;
    public int NotificationID;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if(remoteMessage.getData().size() > 0 ){
            String message = remoteMessage.getData().get("message");
            String order_desc = remoteMessage.getData().get("order_desc");
            String dataFrom = remoteMessage.getData().get("from_user_id");
            String CustomerID = remoteMessage.getData().get("user_id");


            Intent AcceptClickIntent = new Intent(this, NotificationReceiverAccept.class);

            AcceptClickIntent.putExtra("DeliveryID_1",dataFrom);
            AcceptClickIntent.putExtra("CustomerID_2",CustomerID);
            AcceptClickIntent.putExtra("order_desc_3",order_desc);


            PendingIntent AcceptClickPendingIntent = PendingIntent.getBroadcast(this,
                    0, AcceptClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            Intent DeclineClickIntent = new Intent(this, NotificationReceiverDecline.class);
            DeclineClickIntent.putExtra("DeliveryID",dataFrom);
            DeclineClickIntent.putExtra("CustomerID",CustomerID);

            PendingIntent DeclineClickPendingIntent = PendingIntent.getBroadcast(this,
                    0, DeclineClickIntent, 0);


            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {


                String CHANNEL_ID = "my_channel_01";
                CharSequence name = "my_channel";
                String Description = "This is my channel";
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                mChannel.setDescription(Description);
                mChannel.enableLights(true);
                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                mChannel.setShowBadge(false);
                notificationManager.createNotificationChannel(mChannel);
            }else {

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this, "01")
                        .setSmallIcon(R.drawable.ic_icon)
                        .addAction(R.drawable.ic_icon, "Accept", AcceptClickPendingIntent)
                        .addAction(R.drawable.ic_icon, "Decline", DeclineClickPendingIntent)
                        .setVibrate(new long[]{1000, 1000})
                        .setContentTitle("New Order Request !")
                        .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                        .setContentText(message)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setWhen(0);


                NotificationID = (int) System.currentTimeMillis();
                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                manager.notify(NotificationID, mBuilder.build());
            }

        }else Toast.makeText(getApplicationContext(),"Empty Data",Toast.LENGTH_LONG).show();



    }


}
