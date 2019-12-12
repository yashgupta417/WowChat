package com.WowChat.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.WowChat.Adapters.ImageAdapter;
import com.WowChat.R;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        RecyclerView recyclerView=findViewById(R.id.gallery_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        recyclerView.setHasFixedSize(true);

        final ImageAdapter adapter=new ImageAdapter(this);
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ImageAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent data=new Intent();
                data.setData(Uri.parse(adapter.images.get(position)));
                setResult(RESULT_OK,data);
                finish();
            }
        });
    }
}
