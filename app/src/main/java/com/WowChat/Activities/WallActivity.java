package com.WowChat.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.WowChat.Adapters.MembersAdapter;
import com.WowChat.Adapters.SelectedFriendsAdapter;
import com.WowChat.R;
import com.WowChat.Retrofit.GroupRead;
import com.WowChat.Retrofit.RetrofitClient;
import com.WowChat.Retrofit.User;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WallActivity extends AppCompatActivity {
    String groupId,groupName,groupImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

        groupId=getIntent().getStringExtra("group_id");
        groupName=getIntent().getStringExtra("group_name");
        groupImage=getIntent().getStringExtra("group_image");

        Toolbar toolbar=findViewById(R.id.wall_toolbar);
        toolbar.setTitle(groupName);
        setSupportActionBar(toolbar);



        fetchGroupDetails();

    }

    public void fetchGroupDetails(){
        RetrofitClient client=new RetrofitClient();
        Call<GroupRead> call=client.jsonPlaceHolderApi.getGroupDetails(groupId);
        call.enqueue(new Callback<GroupRead>() {
            @Override
            public void onResponse(Call<GroupRead> call, Response<GroupRead> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(WallActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }

                GroupRead group=response.body();
                setUpRecycleView(group.getMembers());
            }

            @Override
            public void onFailure(Call<GroupRead> call, Throwable t) {
                Toast.makeText(WallActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setUpRecycleView(ArrayList<User> members){
        RecyclerView recyclerView=findViewById(R.id.wall_members_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setHasFixedSize(true);
        MembersAdapter adapter=new MembersAdapter(this,members);
        recyclerView.setAdapter(adapter);
    }
}
