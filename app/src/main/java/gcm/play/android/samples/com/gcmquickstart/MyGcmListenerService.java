/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gcm.play.android.samples.com.gcmquickstart;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.gongdol.http.Contants;
import com.gongdol.http.RequestInterface;
import com.gongdol.http.vo.AckResponse;
import com.google.android.gms.gcm.GcmListenerService;

import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class MyGcmListenerService extends GcmListenerService {

    private static final String TAG = "MyGcmListenerService";

    /**
     * Called when message is received.
     *
     * @param from SenderID of the sender.
     * @param data Data bundle containing message data as key/value pairs.
     *             For Set of keys use data.keySet().
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(String from, Bundle data) {
        String title = data.getString("title");
        String body = data.getString("body");
        Log.d(TAG, "From: " + from);
        Log.d(TAG, "title: " + title);
        Log.d(TAG, "body: " + body);

        if (from.startsWith("/topics/")) {
            body = "[ Push By Subscription Topic ]" + body;
        } else {
            // normal downstream message.
        }


        PowerManager pm = (PowerManager) getApplicationContext().getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl = pm.newWakeLock(
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                        PowerManager.PARTIAL_WAKE_LOCK |
                        PowerManager.ON_AFTER_RELEASE, "TAG");
        wl.acquire();
        wl.release();


        // [START_EXCLUDE]
        /**
         * Production applications would usually process the message here.
         * Eg: - Syncing with server.
         *     - Store message in local database.
         *     - Update UI.
         */

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        sendNotification(title,body);
        // [END_EXCLUDE]
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received GCM message.
     * @param title
     * @param body
     */
    private void sendNotification(String title,String body) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* RequestInterface code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());

        final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            // notify user you are online
            Log.e(TAG,"NetworkInfo.State.CONNECTING OR NetworkInfo.State.CONNECTED");


            logMode();

            RestAdapter tokenRegRestAdapter = new RestAdapter.Builder()
                    .setLogLevel(RestAdapter.LogLevel.FULL)
                    .setEndpoint(Contants.SERVER_URL).build();

            tokenRegRestAdapter.create(RequestInterface.class)
                    .GcmAck("TestUserID", new Callback<AckResponse>() {
                        @Override
                        public void success(AckResponse ackResponse, Response response) {
                            Log.e(TAG, ackResponse.getMsg());
                        }

                        @Override
                        public void failure(RetrofitError error) {
                            Log.e(TAG, error.toString());
                        }
                    });

        } else {
            // notify user you are not online
            Log.e(TAG,"NetworkInfo.State.DISCONNECTED");
        }

    }

    @TargetApi(23)
    private void logMode() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if(android.os.Build.VERSION.SDK_INT >= (Build.VERSION_CODES.M)) {
            if (pm.isDeviceIdleMode()) {
                Log.e(TAG, "DOZE MODE");
            } else {
                Log.e(TAG, "ACTIVE MODE");
            }
        }else{
            Log.e(TAG, "Must ACTIVE MODE");
        }
    }


}
