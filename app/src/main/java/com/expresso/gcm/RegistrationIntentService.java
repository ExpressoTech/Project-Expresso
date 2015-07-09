package com.expresso.gcm;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.expresso.activity.R;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

/**
 * Created by yadnyesh on 18/6/15.
 */
public class RegistrationIntentService extends IntentService {
    private static final String TAG = "RegIntentService";

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            synchronized (TAG) {
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Log.i(TAG, "GCM Registration Token: " + token);

                // TODO: Implement this method to send any registration to your app's servers.
                //     sendRegistrationToServer(token);

            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
        }
        //  Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        // LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }
}
