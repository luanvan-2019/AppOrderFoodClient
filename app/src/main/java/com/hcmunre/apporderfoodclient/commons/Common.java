package com.hcmunre.apporderfoodclient.commons;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.hcmunre.apporderfoodclient.R;
import com.hcmunre.apporderfoodclient.models.Entity.Category;
import com.hcmunre.apporderfoodclient.models.Entity.Order;
import com.hcmunre.apporderfoodclient.models.Entity.Restaurant;
import com.hcmunre.apporderfoodclient.models.Entity.User;

public class Common {
    public static final String NOTIFI_TITLE = "title";
    public static final String NOTIFI_CONTENT = "content";
    public static final String PAYPAL_CLIENT_ID="AXI3rU3HxrVbDF_wtjdpZAXIfe6ky2q7v3XBR1oNcBiZPPqZgEJztnrtIulItcHNFJPTi3zSLptTgbYV";
    public static User currentUser;
    public static Order curentOrder;
    public static Restaurant currentRestaurant;
    public static Category currentCategory;
    public static final String KEY_USER = "data_user";
    public static final String KEY_RESTAURANT = "data_restaurant";
    public static final String REMEMBER_FBID = "FBID";
    public static final String TAG="ERROR";
    public static boolean isConnectedToInternet(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {

            NetworkInfo[] info = connectivityManager.getAllNetworkInfo();
            if (info != null) {

                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED)
                        return true;
                }
            }
        }
        return false;
    }
    public static void showNotifacation(Context context, int notiId, String title, String content, Intent intent) {
        PendingIntent pendingIntent = null;
        if (pendingIntent != null) {
            pendingIntent = PendingIntent.getActivity(context, notiId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            String NOTIFICATION_CHANNEL_ID = "app_food_Client";
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                        "Thông báo", NotificationManager.IMPORTANCE_DEFAULT);
                notificationChannel.setDescription("Ứng duụng cửa hàng");
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);
                notificationManager.createNotificationChannel(notificationChannel);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
            builder.setContentTitle(title)
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setSmallIcon(R.mipmap.ic_launcher_round)
                    .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.app_icon));
            if (pendingIntent != null) {
                builder.setContentIntent(pendingIntent);
            }
            //hiển thị thông báo
            Notification notification = builder.build();
            notificationManager.notify(notiId, notification);

        }
    }
    public static String convertStatusFoodToString(int statusFood){
        switch (statusFood){
            case 0:
                return "Hết món";
            case 1:
                return "Đang bán";
            default:
                return "Đang bán";
        }
    }
    public static void showToast(Context context,String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
    public static String getTopicChannel(int id) {
        return new StringBuilder("Restaurant").append(id).toString();
    }

    public static String createTopicSender(String topicChannel) {
        return new StringBuilder("/topics/").append(topicChannel).toString();
    }
}
