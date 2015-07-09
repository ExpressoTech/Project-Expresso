package com.expresso.gcm;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Akshay on 18/6/15.
 */
public class GcmIntentService extends IntentService {
    private Handler handler;
    public GcmIntentService() {
        super("GcmIntentService");
        handler=new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle bundle=intent.getExtras();
        GoogleCloudMessaging gcm=GoogleCloudMessaging.getInstance(this);
        final String messageType = gcm.getMessageType(intent);
        handler.post(new Runnable() {
            public void run() {
                Toast toast = Toast.makeText(GcmIntentService.this, messageType, Toast.LENGTH_LONG);
                toast.show();
            }
        });

        if (!bundle.isEmpty())
        {
            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_DELETED.equals(messageType)) {
                // If it's a regular GCM message, do some work.
            } else if (GoogleCloudMessaging.
                    MESSAGE_TYPE_MESSAGE.equals(messageType)) {
            }
        }


    }




    private void generateLoginNotification(Context context, String type, String message) {
        /*Intent notificationIntent = null;
        String title;
        long when = System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if(type.equalsIgnoreCase(context.getResources().getString(R.string.no_alarm_set_type))) {
            title="No Alarm Set";
            notificationIntent = new Intent(context, HomeActivity.class);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            title="Not LoggedIn";
            notificationIntent = new Intent(context, HomeActivity.class);
            notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        PendingIntent pendingIntent = null;
        pendingIntent = PendingIntent.getActivity(context, AlarmConfig.LOGIN_NOTIFICATION_ID, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentText(message);
        builder.setContentIntent(pendingIntent);

        builder.setContentTitle(title);
        builder.setSmallIcon(R.drawable.splash_applogo);

        Notification notification = builder.build();
        notification.when = when;
        notification.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(AlarmConfig.LOGIN_NOTIFICATION_ID,notification);*/

    }

}
