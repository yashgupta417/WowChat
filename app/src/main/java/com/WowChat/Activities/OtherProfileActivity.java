package com.WowChat.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.WowChat.R;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class OtherProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);
        CollapsingToolbarLayout collapsingToolbarLayout=findViewById(R.id.toolbar_layout);
        String image=getIntent().getStringExtra("image");
        String first_name=getIntent().getStringExtra("first_name");
        String last_name=getIntent().getStringExtra("last_name");
        ImageView imageView=findViewById(R.id.toolbar_image);
        Glide.with(this).load(image).placeholder(R.drawable.userimgother).into(imageView);
        collapsingToolbarLayout.setTitle(first_name+" "+last_name);

    }
}
