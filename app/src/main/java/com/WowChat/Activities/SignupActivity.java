package com.WowChat.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import pl.droidsonroids.gif.GifImageView;
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
    public GifImageView load;
    public Button signupButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        initializeUIElements();
    }
    public void initializeUIElements(){
        Toolbar toolbar=findViewById(R.id.signup_toolbar);
        setSupportActionBar(toolbar);
        firstName=(EditText)findViewById(R.id.first_name);
        lastName=findViewById(R.id.last_name);
        username=findViewById(R.id.username);
        email=findViewById(R.id.email);
        password=findViewById(R.id.password);
        load=findViewById(R.id.signup_load);
        signupButton=findViewById(R.id.signup_button);
    }
    static boolean isEmailValid(String email) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        return email.matches(regex);
    }
    public void signUp(View view){
        String fName=firstName.getText().toString().trim();
        String lName=lastName.getText().toString().trim();
        String uName=username.getText().toString().trim();
        String eMail=email.getText().toString().trim();
        pass=password.getText().toString().trim();

        if(fName.isEmpty()){
            Toast.makeText(this, "First Name can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        if(lName==null){
            lName="";
        }

        for(int i=0;i<uName.length();i++){
            if(!(Character.isLetterOrDigit(uName.charAt(i)) || uName.charAt(i)=='_')){
                Toast.makeText(this, "Invalid Username", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        if(!isEmailValid(eMail)){
            Toast.makeText(this, "Invalid Email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(pass.length()<8){
            Toast.makeText(this, "Password must be of at least 8 characters", Toast.LENGTH_SHORT).show();
            return;
        }

        if(pass.contains(fName)){
            Toast.makeText(this, "Password can not contain name and username", Toast.LENGTH_SHORT).show();
            return;
        }
        User user=new User(uName,eMail,fName,lName,pass);
        signUp(user);
    }
    public void signUp(User user){
        signupButton.setEnabled(false);
        signupButton.animate().alpha(0.5f);
        load.setVisibility(View.VISIBLE);
        RetrofitClient retrofitClient=new RetrofitClient();
        Call<User> call=retrofitClient.jsonPlaceHolderApi.signUp(user);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(com.WowChat.Activities.SignupActivity.this, "Something went wrong ", Toast.LENGTH_SHORT).show();
                    load.setVisibility(View.INVISIBLE);
                    signupButton.setEnabled(true);
                    signupButton.animate().alpha(1f);
                    return;
                }
                User user=response.body();
                Toast.makeText(com.WowChat.Activities.SignupActivity.this, "Account Created "+user.getFirstName(), Toast.LENGTH_SHORT).show();
                login(user.getUsername(),pass);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                load.setVisibility(View.INVISIBLE);
                signupButton.setEnabled(true);
                signupButton.animate().alpha(1f);
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
                load.setVisibility(View.INVISIBLE);
                signupButton.setEnabled(true);
                signupButton.animate().alpha(1f);
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

                //Toast.makeText(com.WowChat.Activities.SignupActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                getAndCreateFCMTokenOnServer();
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                load.setVisibility(View.INVISIBLE);
                signupButton.setEnabled(true);
                signupButton.animate().alpha(1f);
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
                        SharedPreferences sharedPreferences= getSharedPreferences(getPackageName(),Context.MODE_PRIVATE);
                        String user_id=sharedPreferences.getString("id","");
                        FCMToken fcmToken=new FCMToken(token,"FCM",user_id);
                        RetrofitClient retrofitClient=new RetrofitClient();
                        Call<FCMToken> call=retrofitClient.jsonPlaceHolderApi.createFCMToken(fcmToken);
                        call.enqueue(new Callback<FCMToken>() {
                            @Override
                            public void onResponse(Call<FCMToken> call, Response<FCMToken> response) {
                                if(!response.isSuccessful()){
                                 //   Toast.makeText(com.WowChat.Activities.SignupActivity.this, "FCM Token not updated", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                               // Toast.makeText(com.WowChat.Activities.SignupActivity.this, "FCM token uploaded", Toast.LENGTH_SHORT).show();
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
