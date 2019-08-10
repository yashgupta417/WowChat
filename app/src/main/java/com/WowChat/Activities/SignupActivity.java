package com.WowChat.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.WowChat.R;
import com.WowChat.Retrofit.FCMToken;
import com.WowChat.Retrofit.RetrofitClient;
import com.WowChat.Retrofit.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends AppCompatActivity {
    public EditText firstName;
    public EditText lastName;
    public EditText username;
    public EditText email;
    public EditText password;
    public String pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        firstName=(EditText)findViewById(R.id.first_name);
        lastName=findViewById(R.id.last_name);
        username=findViewById(R.id.username);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);

    }

    public void signUp(View view){
        String fName=firstName.getText().toString();
        String lName=lastName.getText().toString();
        String uName=username.getText().toString();
        String eMail=email.getText().toString();
         pass=password.getText().toString();

        if(fName.trim().isEmpty() || lName.trim().isEmpty() || uName.trim().isEmpty() || eMail.trim().isEmpty()){
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(pass.length()<8){
            Toast.makeText(this, "password must be of atleast 8 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        User user=new User(uName,eMail,fName,lName,pass);
        signUp(user);
    }
    public void signUp(User user){
        RetrofitClient retrofitClient=new RetrofitClient();
        Call<User> call=retrofitClient.jsonPlaceHolderApi.signUp(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(com.WowChat.Activities.SignupActivity.this, "Something went wrong ", Toast.LENGTH_SHORT).show();
                    return;
                }
                User user=response.body();
                Toast.makeText(com.WowChat.Activities.SignupActivity.this, "Account Created "+user.getFirstName(), Toast.LENGTH_SHORT).show();
//                Intent intent=new Intent(getApplicationContext().getApplicationContext(),LoginActivity.class);
//                startActivity(intent);
//                finish();
                login(user.getUsername(),pass);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(com.WowChat.Activities.SignupActivity.this, "check your connection", Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void login(String username, String password){
        RetrofitClient retrofitClient=new RetrofitClient();

        final SharedPreferences sharedPreferences=this.getSharedPreferences(getPackageName(),Context.MODE_PRIVATE);
        User user=new User(username,password);
        Call<User> call=retrofitClient.jsonPlaceHolderApi.loginAndGetToken(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(com.WowChat.Activities.SignupActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                User loggedInUser=response.body();
                if (loggedInUser.getToken()==null){
                    Toast.makeText(com.WowChat.Activities.SignupActivity.this, "It seems like you have already logged in from some other device", Toast.LENGTH_SHORT).show();
                    return;
                }

                sharedPreferences.edit().putString("token",loggedInUser.getLastName()).apply();
                sharedPreferences.edit().putString("id",Integer.toString(loggedInUser.getId())).apply();
                sharedPreferences.edit().putString("username",loggedInUser.getUsername()).apply();
                sharedPreferences.edit().putString("first_name",loggedInUser.getFirstName()).apply();
                sharedPreferences.edit().putString("last_name",loggedInUser.getLastName()).apply();
                sharedPreferences.edit().putString("email",loggedInUser.getEmail()).apply();
                if(loggedInUser.getImage()!=null){
                    sharedPreferences.edit().putString("image",loggedInUser.getImage().toString()).apply();
                }else{
                    sharedPreferences.edit().putString("image","").apply();
                }

                Toast.makeText(com.WowChat.Activities.SignupActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                getAndCreateFCMTokenOnServer();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(com.WowChat.Activities.SignupActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getAndCreateFCMTokenOnServer(){
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("****", "getInstanceId failed", task.getException());
                            return;
                        }

                        String token = task.getResult().getToken();
                        SharedPreferences sharedPreferences= com.WowChat.Activities.SignupActivity.this.getSharedPreferences(getPackageName(),Context.MODE_PRIVATE);
                        String user_id=sharedPreferences.getString("id","");
                        FCMToken fcmToken=new FCMToken(token,"FCM",user_id);
                        RetrofitClient retrofitClient=new RetrofitClient();
                        Call<FCMToken> call=retrofitClient.jsonPlaceHolderApi.createFCMToken(fcmToken);
                        call.enqueue(new Callback<FCMToken>() {
                            @Override
                            public void onResponse(Call<FCMToken> call, Response<FCMToken> response) {
                                if(!response.isSuccessful()){
                                    Toast.makeText(com.WowChat.Activities.SignupActivity.this, "FCM Token not updated", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                Toast.makeText(com.WowChat.Activities.SignupActivity.this, "FCM token uploaded", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(getApplicationContext().getApplicationContext(), MainActivity.class);
                                startActivity(intent);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<FCMToken> call, Throwable t) {
                                Toast.makeText(com.WowChat.Activities.SignupActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
    }

}
