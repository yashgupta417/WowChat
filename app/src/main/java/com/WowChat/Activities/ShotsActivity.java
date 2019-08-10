package com.WowChat.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.WowChat.Adapters.ShotAdapter;
import com.WowChat.Retrofit.RetrofitClient;
import com.WowChat.Retrofit.Shot;
import com.WowChat.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShotsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    com.WowChat.Adapters.ShotAdapter adapter;
    String friend_id;
    String me_id;
    TextView noOfShots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shots);
        friend_id=getIntent().getStringExtra("friend_id");
        me_id=getIntent().getStringExtra("me_id");
        recyclerView = findViewById(R.id.shots_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,true));
        recyclerView.setHasFixedSize(true);
        adapter = new ShotAdapter(this);
        recyclerView.setAdapter(adapter);
        noOfShots=findViewById(R.id.no_of_shots);
        getShots(me_id,friend_id);


    }

    public void getShots(String me_id,String friend_id){
        com.WowChat.Retrofit.RetrofitClient retrofitClient=new RetrofitClient();
        Call<List<com.WowChat.Retrofit.Shot>> call=retrofitClient.jsonPlaceHolderApi.getShots(me_id,friend_id);
        call.enqueue(new Callback<List<com.WowChat.Retrofit.Shot>>() {
            @Override
            public void onResponse(Call<List<com.WowChat.Retrofit.Shot>> call, Response<List<com.WowChat.Retrofit.Shot>> response) {
                    if(!response.isSuccessful()){
                        Toast.makeText(ShotsActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    List<com.WowChat.Retrofit.Shot> shots=response.body();
                    adapter.submitList(shots);
                Toast.makeText(ShotsActivity.this, "done", Toast.LENGTH_SHORT).show();
                noOfShots.setText(Integer.toString(shots.size())+" Shots");
            }

            @Override
            public void onFailure(Call<List<Shot>> call, Throwable t) {
                Log.i("****",t.getLocalizedMessage());
                Log.i("****",t.getMessage());

                Toast.makeText(ShotsActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void goToShotActivity(View view){
        Intent intent=new Intent(getApplicationContext(), UploadShotActivity.class);
        intent.putExtra("me_id",me_id);
        intent.putExtra("friend_id",friend_id);
        startActivity(intent);
    }
}
