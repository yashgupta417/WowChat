package com.WowChat;

import android.app.ActivityManager;
import android.app.Application;
import android.app.KeyguardManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import com.WowChat.Retrofit.RetrofitClient;
import com.WowChat.Retrofit.User;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyApp extends Application {
    public static final String CHANNEL_GEN_ID = "channel2";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
        final Handler handler = new Handler();

        final Runnable r = new Runnable() {
            public void run() {
                if(!shouldNotUpdateLastSeen(getApplicationContext())){
                    updateLastSeen();
                }

                handler.postDelayed(this, 2000);
            }
        };
        handler.postDelayed(r,2000);

    }

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            NotificationChannel channelGen = new NotificationChannel(
                    CHANNEL_GEN_ID,
                    "Channel General",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
           /*Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.stairs);
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                    .build();*/
            channelGen.setDescription("This is Channel General");
           // channelGen.setSound(sound,attributes);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channelGen);
        }
    }

    private void updateLastSeen(){

        SharedPreferences sharedPreferences=this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String token=sharedPreferences.getString("token","");
        if(token!=""){
            Log.i("****updatingLastSeen","#####################");
            Calendar c = Calendar.getInstance();

            SimpleDateFormat timePreciseformat = new SimpleDateFormat("hh:mm:ss a");
            SimpleDateFormat dateformat = new SimpleDateFormat("d MMM yyyy");

            String timePrecise=timePreciseformat.format(c.getTime());
            String date=dateformat.format(c.getTime());
            User user=new User();
            String username=sharedPreferences.getString("username","");
            user.setLastSeenDate(date);
            user.setLastSeenTime(timePrecise);
            RetrofitClient retrofitClient=new RetrofitClient();
            Call<User> call=retrofitClient.jsonPlaceHolderApi.updateLastSeen(user,username);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {

                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {

                }
            });
        }

    }
    public boolean shouldNotUpdateLastSeen(Context context) {
        ActivityManager.RunningAppProcessInfo myProcess = new ActivityManager.RunningAppProcessInfo();
        ActivityManager.getMyMemoryState(myProcess);
        if (myProcess.importance != ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
            return true;

        KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
        // app is in foreground, but if screen is locked show notification anyway
        return km.inKeyguardRestrictedInputMode();
    }
}
