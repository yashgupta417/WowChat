package com.WowChat.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.WowChat.Adapters.MembersAdapter;
import com.WowChat.Adapters.MemoryAdapter;
import com.WowChat.R;
import com.WowChat.Repository.GroupRepository;
import com.WowChat.Retrofit.GroupRead;
import com.WowChat.Retrofit.MemoryRead;
import com.WowChat.Retrofit.RetrofitClient;
import com.WowChat.Retrofit.User;
import com.WowChat.Room.Entities.GroupTable;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OtherGroupActivity extends AppCompatActivity {
    String groupId;
    ImageView groupDp;
    SwipeRefreshLayout container;
    GifImageView load;
    RecyclerView recyclerView;
    Toolbar toolbar;
    TextView groupName,groupCreatedBy,memberCount,memoryCount,followerCount,followText;
    ImageView followIcon;
    boolean following;
    String myId;
    int followers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_group);
        groupId=getIntent().getStringExtra("group_id");
        recyclerView=findViewById(R.id.og_memories_recycleView);
        container=findViewById(R.id.og_container);
        load=findViewById(R.id.og_load);
        toolbar=findViewById(R.id.og_toolbar);
        groupName=findViewById(R.id.og_group_name);
        groupCreatedBy=findViewById(R.id.og_group_created_by);
        memberCount=findViewById(R.id.og_members_count);
        memoryCount=findViewById(R.id.og_memories_count);
        followerCount=findViewById(R.id.og_followers_count);
        followText=findViewById(R.id.og_follow_text);
        followIcon=findViewById(R.id.og_follow_icon);
        SharedPreferences sharedPreferences=getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        myId=sharedPreferences.getString("id","");
        hideUI();
        fetchGroupDetails(0);
    }
    public void refreshContent(){
        container.setRefreshing(true);
        fetchGroupDetails(1);
    }
    public void refreshDone(){
        container.setRefreshing(false);
    }
    public void setBasicUI(GroupRead group){

        toolbar.setTitle(group.getGroupName());
        setSupportActionBar(toolbar);
        groupDp=findViewById(R.id.og_dp);
        groupName.setText(group.getGroupName());
        groupCreatedBy.setText("Created By @"+group.getPresident().getUsername());
        memberCount.setText(Integer.toString(group.getMembers().size()));
        followerCount.setText(Integer.toString(group.getFollowers().size()));
        following=false;
        followers=group.getFollowers().size();
        for(User x :group.getFollowers()){
            if(x.getId()==Integer.parseInt(myId)){
                following=true;
            }
        }
        performFollowLogic();
        if(group.getGroupImage()!=null){
            Glide.with(this).load(group.getGroupImage()).placeholder(R.drawable.loadingc).into(groupDp);
        }
        container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshContent();
            }
        });
    }
    public void performFollowLogic(){
        if(following){
            followText.setText("Following");
            //followText.setTextColor(Color.parseColor("#000000"));
            Glide.with(this).load(R.drawable.ic_check).into(followIcon);
        }else{
            followText.setText("Follow");
            //followText.setTextColor(Color.parseColor("#00BFFF"));
            Glide.with(this).load(R.drawable.ic_add_circle_blue).into(followIcon);
        }
        followerCount.setText(Integer.toString(followers));
    }
    public void hideUI(){
        container.setVisibility(View.INVISIBLE);
        container.setEnabled(false);
    }
    public void revealUI(){
        container.setVisibility(View.VISIBLE);
        container.setEnabled(true);
        load.setVisibility(View.GONE);
    }
    GroupRead group;
    ArrayList<User> members;
    public void fetchGroupDetails(final Integer source){
        RetrofitClient client=new RetrofitClient();
        Call<GroupRead> call=client.jsonPlaceHolderApi.getGroupDetails(groupId);
        call.enqueue(new Callback<GroupRead>() {
            @Override
            public void onResponse(Call<GroupRead> call, Response<GroupRead> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(OtherGroupActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }

                group=response.body();
                setBasicUI(group);
                members=group.getMembers();
                setUpMembersRecyclerView(members);
                fetchMemories(source);
            }

            @Override
            public void onFailure(Call<GroupRead> call, Throwable t) {
                Toast.makeText(OtherGroupActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setUpMembersRecyclerView(final ArrayList<User> members){
        RecyclerView recyclerView=findViewById(R.id.og_members_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerView.setHasFixedSize(true);
        final MembersAdapter adapter=new MembersAdapter(this,members);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new MembersAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {
                User member=adapter.members.get(position);
                if(member.getId()==Integer.parseInt(myId)){
                    Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
                    startActivity(intent);
                    return;
                }
                Intent intent=new Intent(getApplicationContext(),ChatActivity.class);
                intent.putExtra("username",member.getUsername());
                intent.putExtra("firstName",member.getFirstName());
                intent.putExtra("lastName",member.getLastName());
                intent.putExtra("email",member.getEmail());
                intent.putExtra("image",member.getImage());
                intent.putExtra("id",Integer.toString(member.getId()));
                startActivity(intent);
            }
        });
    }
    public void fetchMemories(final Integer source){
        RetrofitClient client=new RetrofitClient();
        Call<List<MemoryRead>> call=client.jsonPlaceHolderApi.getMemories(groupId);
        call.enqueue(new Callback<List<MemoryRead>>() {
            @Override
            public void onResponse(Call<List<MemoryRead>> call, Response<List<MemoryRead>> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(OtherGroupActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                setUpMemoriesAdapter(new ArrayList<MemoryRead>(response.body()));
                if(source==0){
                    revealUI();
                }else if(source==1){
                    refreshDone();
                }

            }

            @Override
            public void onFailure(Call<List<MemoryRead>> call, Throwable t) {

            }
        });
    }
    public void setUpMemoriesAdapter(ArrayList<MemoryRead> memories){
        memoryCount.setText(Integer.toString(memories.size()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,3));
        MemoryAdapter adapter=new MemoryAdapter(this,memories);
        recyclerView.setAdapter(adapter);
    }

    public void followWork(View view){
        following=!following;
        if(following){
            followers++;
        }else{
            followers--;
        }
        performFollowLogic();
        RetrofitClient client=new RetrofitClient();
        Call<User> call=client.jsonPlaceHolderApi.followGroup(myId,groupId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(OtherGroupActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(OtherGroupActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
