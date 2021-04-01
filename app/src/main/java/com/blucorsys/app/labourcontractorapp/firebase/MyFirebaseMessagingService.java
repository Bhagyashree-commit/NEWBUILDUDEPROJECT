package com.blucorsys.app.labourcontractorapp.firebase;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.blucorsys.app.labourcontractorapp.LoginActivity;
import com.blucorsys.app.labourcontractorapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
  /**
   * Called when message is received.
   *
   *
   * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
   */




  private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

     // PrefManager pref;
  @Override
  public void onMessageReceived(RemoteMessage remoteMessage) {
    super.onMessageReceived(remoteMessage);
    // TODO(developer): Handle FCM messages here.
    // If the application is in the foreground handle both data and notification messages here.
    // Also if you intend on generating your own notifications as a result of a received FCM
    // message, here is where that should be initiated. See sendNotification method below.
    RemoteMessage.Notification notification = remoteMessage.getNotification();
    Map<String, String> data = remoteMessage.getData();
    //  pref=new PrefManager(this);
    // Check if message contains a notification payload.
    if (remoteMessage.getNotification() != null)
    {
      Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
       sendNotification(notification, data);
    }
    // Check if message contains a data payload.
    if (remoteMessage.getData().size() > 0) {
      Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
      try {


        JSONObject jsonObject = new JSONObject(remoteMessage.getData());
        JSONObject json = new JSONObject(remoteMessage.getData().toString());
        handleDataMessage(json);
      } catch (Exception e) {
        Log.e(TAG, "ExceptionJson: " + e.getMessage());
      }
    }

  }


  private void handleDataMessage(JSONObject json) throws JSONException {
    Log.e(TAG, "push json: " + json.toString());

    JSONObject obj=new JSONObject(json.toString().replace("\\"," "));

    try {
      JSONObject data = obj.getJSONObject("data");

     // result.replaceAll("\\","");

      String title = data.getString("title");

      String message = data.getString("message");
//      boolean isBackground = data.getBoolean("is_background");
      String imageUrl = data.getString("image");
      String timestamp = data.getString("timestamp");

     // JSONObject payload = data.getJSONObject("payload");

      Log.e(TAG, "title: " + title);
      Log.e(TAG, "message: " + message);
//      Log.e(TAG, "isBackground: " + isBackground);
    //  Log.e(TAG, "payload: " + payload.toString());
      Log.e(TAG, "imageUrl: " + imageUrl);
      Log.e(TAG, "timestamp: " + timestamp);


      if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
        // app is in foreground, broadcast the push message


        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
        pushNotification.putExtra("message", message);
        LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

        Intent intent = null;
        intent=new Intent(getApplicationContext(), LoginActivity.class);

          intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);



        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        play_notification(title,message,imageUrl,intent);

      } else {
        // app is in background, show the notification in notification tray
        Intent intent = null;


        intent=new Intent(getApplicationContext(), LoginActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        play_notification(title,message,imageUrl,intent);

        // check for image attachment
//        if (TextUtils.isEmpty(imageUrl)) {
//          showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
//        } else {
//          // image is present, show notification with image
//          showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
//        }
      }
    } catch (JSONException e) {
      Log.e(TAG, "Json Exception: " + e.getMessage());
    } catch (Exception e) {
      Log.e(TAG, "Exception: " + e.getMessage());
    }
  }

  private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
   NotificationUtils notificationUtils = new NotificationUtils(context);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
  }
  /**
   * Showing notification with text and image
   */
  private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
    NotificationUtils notificationUtils = new NotificationUtils(context);
    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
    notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
  }
  /**
   * Create and show a custom notification containing the received FCM message.
   *
   * @param notification FCM notification payload received.
   * @param data FCM data payload received.
   */
  private void sendNotification(RemoteMessage.Notification notification, Map<String, String> data) {
    Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

    Intent intent = new Intent(this, LoginActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    final Uri NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
            + "://" +getApplicationContext() .getPackageName() + "/raw/tone");
    //final Uri NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.tone);
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
            .setContentTitle(notification.getTitle())
            .setContentText(notification.getBody())
            .setAutoCancel(true)
            .setSound(NOTIFICATION_SOUND_URI)
         //   .setContentIntent(pendingIntent)
            .setContentInfo(notification.getTitle())
            .setLargeIcon(icon)
            .setOngoing(true)
            .setColor(Color.RED)
            .setLights(Color.RED, 1000, 300)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setSmallIcon(R.mipmap.ic_launcher);

    try {
      String picture_url = data.get("picture_url");
      if (picture_url != null && !"".equals(picture_url)) {
        URL url = new URL(picture_url);
        Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        notificationBuilder.setStyle(
                new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(notification.getBody())
        );
      }
    } catch (IOException e) {
      e.printStackTrace();
    }


    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    // Notification Channel is required for Android O and above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
      NotificationChannel channel = new NotificationChannel(
              "channel_id", "channel_name", NotificationManager.IMPORTANCE_DEFAULT
      );
      channel.setDescription("channel description");
      channel.setShowBadge(true);
      channel.canShowBadge();
      channel.enableLights(true);
      channel.setLightColor(Color.RED);
      channel.enableVibration(true);
      //channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
      notificationManager.createNotificationChannel(channel);
    }

    notificationManager.notify(0, notificationBuilder.build());
  }

  public void play_notification(String title,String message,String imageUrl,Intent intent)
  {
    Bitmap icon = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);

    PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
  //  final Uri NOTIFICATION_SOUND_URI = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + BuildConfig.APPLICATION_ID + "/" + R.raw.tone);
    NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "channel_id")
            .setContentTitle(title)
            .setContentText(message)
            .setAutoCancel(true)

           // .setSound(NOTIFICATION_SOUND_URI)
            .setContentIntent(pendingIntent)
            .setContentInfo(title)
            .setLargeIcon(icon)
            .setColor(Color.RED)
            .setLights(Color.RED, 1000, 300)
            .setDefaults(Notification.DEFAULT_VIBRATE)
            .setSmallIcon(R.mipmap.ic_launcher);

    try {
      //  String picture_url = data.get("picture_url");
      if (imageUrl != null && !"".equals(imageUrl)) {
        URL url = new URL(imageUrl);
        Bitmap bigPicture = BitmapFactory.decodeStream(url.openConnection().getInputStream());
        notificationBuilder.setStyle(
                new NotificationCompat.BigPictureStyle().bigPicture(bigPicture).setSummaryText(title)
        );
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

    // Notification Channel is required for Android O and above
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
   //   Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.tone);

      AudioAttributes attributes = new AudioAttributes.Builder()
              .setUsage(AudioAttributes.USAGE_NOTIFICATION)
              .build();


      NotificationChannel channel = new NotificationChannel(
              "channel_id", "channel_name", NotificationManager.IMPORTANCE_HIGH
      );
      channel.setDescription("channel description");

      channel.setShowBadge(true);

      channel.canShowBadge();
      channel.enableLights(true);
      channel.setLightColor(Color.RED);
      channel.enableVibration(true);
    //  channel.setSound(sound, attributes);
      channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500});
      if (notificationManager != null)
      notificationManager.createNotificationChannel(channel);
    }

    Notification note = notificationBuilder.build();
    //here
    note.flags = Notification.FLAG_INSISTENT;
    note.flags |= Notification.FLAG_ONGOING_EVENT;
    note.flags |= Notification.FLAG_AUTO_CANCEL;

    NotificationUtils.clearNotifications(getApplicationContext());

    notificationManager.notify(0, note);

    // play notification sound
    NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
    notificationUtils.playNotificationSound();
  }
}