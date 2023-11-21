package com.example.ecommerceapp.utils.notifictionsServices

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.os.Bundle
import android.os.Looper
import android.text.TextUtils
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.example.ecommerceapp.R
import com.example.ecommerceapp.view.activity.BaseActivity
import com.example.ecommerceapp.view.activity.HomeCycleActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import com.google.gson.Gson
import org.json.JSONException
import org.json.JSONObject

class MyFirebaseMessagingService2 : FirebaseMessagingService() {
    private var notificationUtils: NotificationUtils? = null
    var mBuilder: NotificationCompat.Builder? = null
    var nmc: NotificationManagerCompat? = null
    var manager: NotificationManager? = null
    private var baseActivity: BaseActivity? = null
    var bundle2 = Bundle()
    var bundle3 = Bundle()
    private val notificationId2: String? = null
    private val type2: String? = null
    private val itemId2: String? = null
    private val postId2: String? = null
    private val jsonObject: JSONObject? = null
    private val coursesData2: JSONObject? = null

    //    private UserDataMain clientData;
    private fun showNotification(
        id: Int,
        title: String,
        message: String,
        remoteMessage: RemoteMessage
    ) {
        val chanelId = "chanelId"
        val chanelName = "chanelName"
        val resultIntent = Intent(this, HomeCycleActivity::class.java)
        //        resultIntent.putExtra("message", message);
        resultIntent.putExtra("notificationId", title)
        bundle3.putSerializable("menuFragment", title)
        if (remoteMessage.getData().size > 0) {
//            notificationId = remoteMessage.getData().get("id")
//            type = remoteMessage.getData().get("type")
//            itemId = remoteMessage.getData().get("item_id")
//            postId = remoteMessage.getData().get("subitem_id")
            //            bundle3.putString("ItemId", itemId2);
//            bundle3.putString("notificationId", notificationId2);
//            bundle3.putString("type", type2);
//            bundle3.putString("postId", postId2);
            val gson = Gson()
            //            bundle3.putSerializable("firebaseNotification", gson.toJson(remoteMessage.getData()));
            val `object`: Any = gson.toJson(remoteMessage.getData())
            try {

//                coursesData = Gson().fromJson(
//                    java.lang.String.valueOf(remoteMessage.getData().get("item")),
//                    CoursesDatum::class.java
//                )
//                coursesData.setCreator(
//                    Creator(
//                        coursesData.getCreator_id(),
//                        coursesData.getCreator_name()
//                    )
//                )
//                coursesData.setId(itemId)
//                Log.e(
//                    "Tag",
//                    """
//                        firebaseNotification: ${gson.toJson(remoteMessage.getData())}
//                        $jsonObject
//                        $coursesData2
//                        ${coursesData.getCreator_id()}${coursesData.getCreator_name()}
//                        """.trimIndent()
//                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
            //
        }
        resultIntent.putExtras(bundle3)
        val pendingIntent: PendingIntent
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.getActivity(
                this, 1, resultIntent,
                PendingIntent.FLAG_IMMUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this, 1, resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        nmc = NotificationManagerCompat.from(this)
        manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(chanelId, chanelName, NotificationManager.IMPORTANCE_DEFAULT)
            manager!!.createNotificationChannel(channel)
            mBuilder = NotificationCompat.Builder(this, chanelId)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.logo)
                .setContentText(message)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            manager!!.notify(id, mBuilder!!.build())
        } else {
            mBuilder = NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setSmallIcon(R.drawable.logo)
                .setContentText(message)
                .setPriority(Notification.PRIORITY_DEFAULT)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
            nmc = NotificationManagerCompat.from(this)
            manager!!.notify(id, mBuilder!!.build())
        }
    }

    //FirebaseInstanceId.getInstance().getInstanceId().addOnSuccessListener(new OnSuccessListener<InstanceIdResult>() {
    //        @Override
    //        public void onSuccess(InstanceIdResult instanceIdResult) {
    //            String token = instanceIdResult.getToken();
    //            // send it to server
    //        }
    //    });

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.e(TAG, "From: " + remoteMessage.getFrom())
        if (remoteMessage == null) return

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification()!!.getBody())
            handleNotification(
                remoteMessage,
                remoteMessage.getNotification()!!.getBody()!!,
                remoteMessage.getNotification()!!.getTitle()!!
            )
        }
        // Check if message contains a data payload.
        if (remoteMessage.getData().size > 0) {

//                notificationId=remoteMessage.getData().get("item_id");
//            notificationId = remoteMessage.getData().get("id")
//            type = remoteMessage.getData().get("type")
//            itemId = remoteMessage.getData().get("item_id")
//            postId = remoteMessage.getData().get("subitem_id")
//            showAds = true
            try {
//                coursesData = Gson().fromJson(
//                    java.lang.String.valueOf(remoteMessage.getData().get("item")),
//                    CoursesDatum::class.java
//                )
//                coursesData.setCreator(
//                    Creator(
//                        coursesData.getCreator_id(),
//                        coursesData.getCreator_name()
//                    )
//                )
//                coursesData.setId(itemId)
//                Log.e(
//                    "Tag",
//                    """
//                        firebaseNotification: ${Gson().toJson(remoteMessage.getData())}
//                        $jsonObject
//                        $coursesData2
//                        ${coursesData.getCreator_id()}${coursesData.getCreator_name()}
//                        """.trimIndent()
//                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
//            Log.e(
//                TAG,
//                "OrderData Payload: " + itemId.toString() + "   " + notificationId.toString() + "   " + type
//            )
            Log.e(TAG, "OrderData Payload: " + remoteMessage.getData().toString())

//            if (/* Check if data needs to be processed by long running job */ true) {
//                // For long-running tasks (10 seconds or more) use WorkManager.
//                scheduleJob();
//            } else {
//                // Handle message within 10 seconds
//                handleNow();
//            }
            try {
                val json = JSONObject(remoteMessage.getData().toString())
                handleDataMessage(json)
            } catch (e: Exception) {
                Log.e(TAG, "Exception: " + e.message)
            }
        }
    }


    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    @RequiresApi(api = Build.VERSION_CODES.Q)
    override fun onNewToken(token: String) {
        Log.d(
            TAG,
            "Refreshed token: $token"
        )

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        if (Looper.myLooper() == null) {
            Looper.prepare()
        }
        try {
            baseActivity = BaseActivity()
            baseActivity?.getUserData(token)
        } catch (e: Exception) {
            Log.d(
                TAG,
                "errrrrrrrrrrrrrrrrrrrorrrrrrrrrrrrrrrrn $token"
            )
        }
        //        if (clientData != null ) {


//        }
//        else {
//            showToast((Activity) getApplicationContext(), "you must Login first" );
//
//        }
    }
    // [END on_new_token]
    /**
     * Schedule async work using WorkManager.
     */
    private fun scheduleJob() {
        // [START dispatch_job]
        val work = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .build()
        WorkManager.getInstance().beginWith(work).enqueue()
        // [END dispatch_job]
    }

    private fun handleNotification(remoteMessage: RemoteMessage, message: String, title: String) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            val pushNotification = Intent(Config.PUSH_NOTIFICATION)
            bundle2.putSerializable("message", message)
            bundle2.putSerializable("menuFragment", title)
            pushNotification.putExtras(bundle2)
            //            pushNotification.putExtra("message", message);
//            pushNotification.putExtra("menuFragment", title);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)
            // play notification sound
            val notificationUtils = NotificationUtils(this)
            notificationUtils.playNotificationSound()
            showNotification(1, title, message, remoteMessage)
        } else {
            showNotification(1, title, message, remoteMessage)

            // If the app is in background, firebase itself handles the notification
        }
    }

    private fun handleDataMessage(json: JSONObject) {
        Log.e(
            TAG,
            "push json: $json"
        )
        try {
            val data = json.getJSONObject("data")
            val title = data.getString("title")
            val message = data.getString("message")
            //            boolean isBackground = data.getBoolean("is_background");
//            String imageUrl = data.getString("image");
            val timestamp = data.getString("timestamp")
            //            JSONObject payload = data.getJSONObject("payload");

//            Log.e(TAG, "title: " + title);
//            Log.e(TAG, "message: " + message);
//            Log.e(TAG, "isBackground: " + isBackground);
//            Log.e(TAG, "payload: " + payload.toString());
//            Log.e(TAG, "imageUrl: " + imageUrl);
//            Log.e(TAG, "timestamp: " + timestamp);
            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                val pushNotification = Intent(Config.PUSH_NOTIFICATION)
                //                bundle2.putSerializable("message", message);
                pushNotification.putExtras(bundle2)
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)

                // play notification sound
                val notificationUtils = NotificationUtils(this)
                notificationUtils.playNotificationSound()
            } else {
                // app is in background, show the notification in notification tray
                val resultIntent = Intent(getApplicationContext(), HomeCycleActivity::class.java)
                //                bundle2.putSerializable("message", message);
                resultIntent.putExtras(bundle2)

                // check for image attachment
                if (TextUtils.isEmpty("imageUrl")) {
                    showNotificationMessage(this, title, message, timestamp, resultIntent)
                } else {
                    // image is present, show notification with image
                    showNotificationMessageWithBigImage(
                        this,
                        title,
                        message,
                        timestamp,
                        resultIntent,
                        "imageUrl"
                    )
                }
            }
        } catch (e: JSONException) {
            Log.e(TAG, "Json Exception: " + e.message)
        } catch (e: Exception) {
            Log.e(TAG, "Exception: " + e.message)
        }
    }

    /**
     * Showing notification with text only
     */
    private fun showNotificationMessage(
        context: Context,
        title: String,
        message: String,
        timeStamp: String,
        intent: Intent
    ) {
        notificationUtils = NotificationUtils(this)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        notificationUtils!!.showNotificationMessage(title, message, timeStamp, intent)
    }

    /**
     * Showing notification with text and image
     */
    private fun showNotificationMessageWithBigImage(
        context: Context,
        title: String,
        message: String,
        timeStamp: String,
        intent: Intent,
        imageUrl: String
    ) {
        notificationUtils = NotificationUtils(this)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        notificationUtils!!.showNotificationMessage(title, message, timeStamp, intent, imageUrl)
    }

    companion object {
        private val TAG = MyFirebaseMessagingService2::class.java.simpleName
    }

    override fun startForegroundService(service: Intent?): ComponentName? {
        return super.startForegroundService(service)
    }
}