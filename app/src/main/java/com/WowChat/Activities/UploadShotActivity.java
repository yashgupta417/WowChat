package com.WowChat.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.WowChat.R;
import com.WowChat.Retrofit.RetrofitClient;
import com.WowChat.Retrofit.Shot_Write;

import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadShotActivity extends AppCompatActivity {
    Uri image;
    EditText title;
    EditText text;
    String friend_id;
    String me_id;
    ImageView imageView;
    ProgressBar progressBar;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==2){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
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
//                File file=new File(getRealPathFromURI(image));
                Glide.with(this).load(image).into(imageView);
            }

            if(image!=null){
                Toast.makeText(this, "image taken", Toast.LENGTH_SHORT).show();
            }
        }
    }
    public void chooseImageForShot(View view){
        Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
            Log.i("me","inside if");

        }else{
            Log.i("me","inside else");
            Intent intent=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent,1);

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_shot);
        title=findViewById(R.id.upload_shot_title);
        text=findViewById(R.id.upload_shot_text);
        imageView=findViewById(R.id.upload_shot_image);
        progressBar=findViewById(R.id.upload_shot_progress_bar);
        friend_id=getIntent().getStringExtra("friend_id");
        me_id=getIntent().getStringExtra("me_id");
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }
    public void  uploadShot(View view){
        String title_=title.getText().toString();
        String text_=text.getText().toString();
        if(image==null){
            Toast.makeText(this, "Choose a Shot", Toast.LENGTH_SHORT).show();
            return;
        }
        if(title_.trim().isEmpty() || text_.trim().isEmpty()){
            Toast.makeText(this, "Title and Text are Required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(title_.length()>20){
            Toast.makeText(this, "Title can not me more than 20 characters", Toast.LENGTH_SHORT).show();
            return;
        }
        uploadShot(title_,text_,me_id,friend_id,image);
    }
    public void uploadShot(String title,String text,String me_id,String friend_id,Uri image){
        progressBar.setVisibility(View.VISIBLE);
        File file = new File(getRealPathFromURI(image));
        File compressimagefile=null;
        try {
            compressimagefile =new Compressor(this).compressToFile(file);
        } catch (IOException e) {
            compressimagefile=file;
            e.printStackTrace();
        }

        final RequestBody requestBody = RequestBody.create( compressimagefile, MediaType.parse("multipart/form-data"));
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image",file.getName(),requestBody);
        RequestBody titlePart=RequestBody.create(title, MediaType.parse("multipart/form-data"));
        RequestBody textPart=RequestBody.create(text, MediaType.parse("multipart/form-data"));

        RequestBody byPart=RequestBody.create(me_id, MediaType.parse("multipart/form-data"));
        RequestBody toPart=RequestBody.create(friend_id, MediaType.parse("multipart/form-data"));
        RetrofitClient retrofitClient=new RetrofitClient();

        Call<Shot_Write> call=retrofitClient.jsonPlaceHolderApi.uploadShot(titlePart,textPart,byPart,toPart,imagePart);
        call.enqueue(new Callback<Shot_Write>() {
            @Override
            public void onResponse(Call<Shot_Write> call, Response<Shot_Write> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(com.WowChat.Activities.UploadShotActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                progressBar.setVisibility(View.GONE);
                Toast.makeText(com.WowChat.Activities.UploadShotActivity.this, "Shot uploaded", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onFailure(Call<Shot_Write> call, Throwable t) {
                Log.i("****",t.getLocalizedMessage());
                Log.i("****",t.getMessage());
                progressBar.setVisibility(View.GONE);
                Toast.makeText(com.WowChat.Activities.UploadShotActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
