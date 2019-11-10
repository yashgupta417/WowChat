package com.WowChat.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.WowChat.R;
import com.bumptech.glide.Glide;

public class ImageViewerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        String uri=getIntent().getStringExtra("uri");
        ImageView imageView=findViewById(R.id.iv_image);
        if(uri!=null && uri.trim().length()>0) {
            Glide.with(this).load(uri).placeholder(R.drawable.placeholder).into(imageView);
        }else {
            Glide.with(this).load(R.drawable.user_img).into(imageView);
        }
    }
}
