package com.WowChat.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.WowChat.R;
import com.bumptech.glide.Glide;
import com.google.android.material.appbar.CollapsingToolbarLayout;

import de.hdodenhof.circleimageview.CircleImageView;

public class OtherProfileActivity extends AppCompatActivity {
    CircleImageView imageView;
    TextView name,username,bio;
    String image,first_name,last_name,_username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_profile);


        image=getIntent().getStringExtra("image");
        first_name=getIntent().getStringExtra("first_name");
        last_name=getIntent().getStringExtra("last_name");
        _username=getIntent().getStringExtra("username");
        imageView=findViewById(R.id.o_profile_image);
        name=findViewById(R.id.o_profile_name);
        username=findViewById(R.id.o_profile_username);
        Toolbar toolbar=findViewById(R.id.o_toolbar);
        toolbar.setTitle(_username);
        setSupportActionBar(toolbar);
       // bio=findViewById(R.id.o_profile_bio);
        setUI();


    }
    public void setUI(){
        Glide.with(this).load(image).placeholder(R.drawable.user_img).into(imageView);
        username.setText("@"+_username);
        name.setText(first_name+" "+last_name);
    }

    public void seeDP(View view){
        Intent intent=new Intent(getApplicationContext(),ImageViewerActivity.class);
        intent.putExtra("uri",image);
        startActivity(intent);
    }
}
