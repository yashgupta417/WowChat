package com.WowChat.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.WowChat.Repository.GroupRepository;
import com.WowChat.ViewModel.MainAcitivityViewModel;
import com.bumptech.glide.Glide;
import com.WowChat.R;
import com.WowChat.Retrofit.RetrofitClient;
import com.WowChat.Retrofit.User;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.WowChat.Activities.MainActivity.viewModel;

public class ProfileActivity extends AppCompatActivity {
    Uri image;
    String imageURL;
    public CircleImageView circleImageView;
    public TextView name;
    public TextView username;
    public GifImageView load;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initializeUIElements();
        setUIValues();
    }
    public void initializeUIElements(){
        Toolbar toolbar=findViewById(R.id.p_toolbar);
        setSupportActionBar(toolbar);
        circleImageView=findViewById(R.id.profile_image);
        name=findViewById(R.id.profile_name);
        username=findViewById(R.id.profile_username);
        load=findViewById(R.id.dp_upload_gif);
    }

    public void setUIValues(){
        SharedPreferences sharedPreferences=this.getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String fName=sharedPreferences.getString("first_name","");
        String lName=sharedPreferences.getString("last_name","");
        String uName=sharedPreferences.getString("username","");
        imageURL=sharedPreferences.getString("image","");

        name.setText(fName+" "+lName);
        username.setText("@"+uName);
        if(!imageURL.equals("")){
            Glide.with(this).load(imageURL).placeholder(R.drawable.loadingc).into(circleImageView);
        }
        else {
            Glide.with(this).load(R.drawable.user_img).into(circleImageView);
        }


    }
    public void seemyDP(View view){
        Intent intent=new Intent(getApplicationContext(),ImageViewerActivity.class);
        intent.putExtra("uri",imageURL);
        startActivity(intent);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==2){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(getApplicationContext(),GalleryActivity.class);
                startActivityForResult(intent,1);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null){
            image=data.getData();

            if(image!=null){
                uploadPost();
            }
        }
    }
    public void chooseImage(View view){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
                Log.i("me","inside if");

            }else{
                Log.i("me","inside else");
                Intent intent=new Intent(getApplicationContext(),GalleryActivity.class);
                startActivityForResult(intent,1);

            }
        }
    }

    public void uploadPost(){
        load.setVisibility(View.VISIBLE);
        File file = new File(image.getPath());
        File compressimagefile=null;
        try {
            compressimagefile =new Compressor(this).compressToFile(file);
        } catch (IOException e) {
            compressimagefile=file;
            e.printStackTrace();
        }

        final RequestBody requestBody = RequestBody.create( compressimagefile,MediaType.parse("multipart/form-data"));
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image",file.getName(),requestBody);

        RetrofitClient retrofitClient=new RetrofitClient();
        SharedPreferences sharedPreferences=this.getSharedPreferences(getPackageName(),Context.MODE_PRIVATE);
        String username=sharedPreferences.getString("username","");
        Call<User> call=retrofitClient.jsonPlaceHolderApi.updateProfileImage(username,imagePart);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                User user=response.body();
                SharedPreferences sharedPreferences= getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
                sharedPreferences.edit().putString("image",user.getImage()).apply();
                load.setVisibility(View.INVISIBLE);
                setUIValues();

            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(com.WowChat.Activities.ProfileActivity.this, "Check your Connection", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public void showAlertDialog(View view){
        new AlertDialog.Builder(this)
                .setTitle("LOGOUT")
                .setMessage("This will delete all your chats in your phone.Are you Sure?")
                .setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        logout();
                    }
                })
                .setNegativeButton("No",null)
                .show();
    }
    public void logout(){
        final SharedPreferences sharedPreferences=this.getSharedPreferences(getPackageName(),Context.MODE_PRIVATE);
        RetrofitClient retrofitClient=new RetrofitClient();
        String id =sharedPreferences.getString("id",null);
        Call<User> call=retrofitClient.jsonPlaceHolderApi.logout(id);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(com.WowChat.Activities.ProfileActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                User user=response.body();
                if(user.getToken()==null){
                    sharedPreferences.edit().putString("token",null).apply();

                    viewModel.deleteMessagesAndUserInfo();
                    GroupRepository groupRepository=new GroupRepository(getApplication());
                    groupRepository.deleteAllGroupsAndMessages();
                    Toast.makeText(com.WowChat.Activities.ProfileActivity.this, "Logged out Successfully", Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(getApplicationContext(),LoginActivity.class);
                    startActivity(intent);
                }


            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(com.WowChat.Activities.ProfileActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
