package com.WowChat.Services;

import android.app.ActivityManager;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioAttributes;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.Observer;

import com.WowChat.Activities.MainActivity;
import com.WowChat.Repository.MyRepository;
import com.WowChat.Retrofit.FCMToken;
import com.WowChat.Retrofit.Message;
import com.WowChat.Retrofit.RetrofitClient;
import com.WowChat.Room.Entities.MessageTable;
import com.WowChat.Room.Entities.UserInfoTable;
import com.WowChat.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.WowChat.MyApp.CHANNEL_1_ID;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        updateFCMTokenOnServer(s);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String sender_id=remoteMessage.getData().get("sender_id");
        String recipient_id=remoteMessage.getData().get("recipient_id");
        String username=remoteMessage.getData().get("s_username");
        String firstName=remoteMessage.getData().get("s_first_name");
        String lastName=remoteMessage.getData().get("s_last_name");
        String email=remoteMessage.getData().get("s_email");
        String messsage_id=remoteMessage.getData().get("id");
        String text=remoteMessage.getData().get("text");
        String date=remoteMessage.getData().get("date");
        String time=remoteMessage.getData().get("time");
        String status=remoteMessage.getData().get("status");
        String image;
        if(text==null){
            MyRepository myRepository=new MyRepository(getApplication());
            myRepository.updateMessageStatus(messsage_id,status);
        }else{
            if(remoteMessage.getData().get("s_image")==null){
                image="";
            }else {
                image=remoteMessage.getData().get("s_image");
            }

            MessageTable messageTable=new MessageTable(messsage_id,text,sender_id,recipient_id,date,time);
            messageTable.setStatus("Sent");
            UserInfoTable userInfoTable=new UserInfoTable(username,firstName,lastName,email,image,sender_id,text,time,date);
            MyRepository repository=new MyRepository(this.getApplication());
            repository.insertMessage(messageTable);
            repository.updateOrCreateUserInfo(userInfoTable);
            repository.setUnseenCount(sender_id);
//            repository.incrementUnseenCount(username);
            showNotification(firstName,text);
            RetrofitClient retrofitClient=new RetrofitClient();
            Call<Message> call=retrofitClient.jsonPlaceHolderApi.updateMessageStatus(messsage_id,"Sent");
            call.enqueue(new Callback<Message>() {
                @Override
                public void onResponse(Call<Message> call, Response<Message> response) {

                }

                @Override
                public void onFailure(Call<Message> call, Throwable t) {

                }
            });
        }

    }
    public void showNotification(String title,String message){
        boolean result=shouldShowNotification(this);

        if(result){
            Log.i("****","true");
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            Bitmap icon = BitmapFactory.decodeResource(getResources(),
                    R.drawable.wow);

            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.wow)
                    .setLargeIcon(icon)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setAutoCancel(true)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setContentIntent(pendingIntent)
                    .build();

            notificationManager.notify(1, notification);
        }else{
            Log.i("****","false");
        }
    }

    public void updateFCMTokenOnServer(String token){

        SharedPreferences sharedPreferences= this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String user_id=sharedPreferences.getString("id","");
         RetrofitClient retrofitClient=new RetrofitClient();
        Call<FCMToken> call=retrofitClient.jsonPlaceHolderApi.updateFCMToken(token,user_id);
        call.enqueue(new Callback<FCMToken>() {
            @Override
            public void onResponse(Call<FCMToken> call, Response<FCMToken> response) {
                if(!response.isSuccessful()){
                   Log.i("****fcm","new token couldn't uploaded");
                }
                Log.i("****fcm","new token uploaded successfully");
            }
            @Override
            public void onFailure(Call<FCMToken> call, Throwable t) {
                Log.i("****fcm","network issues while uploading new token ");
            }
        });

    }
    public boolean shouldShowNotification(Context context) {
        ActivityManager.RunningAppProcessInfo myProcess = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(myProcess);
        if (myProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
            return true;

        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        // app is in foreground, but if screen is locked show notification anyway
        return km.inKeyguardRestrictedInputMode();
    }
}


