package com.WowChat.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.WowChat.Adapters.MembersAdapter;
import com.WowChat.ModalBottomSheet.AddMemberBottomSheetDialog;
import com.WowChat.ModalBottomSheet.EditGroupBottomSheetDialog;
import com.WowChat.R;
import com.WowChat.Retrofit.GroupRead;
import com.WowChat.Retrofit.RetrofitClient;
import com.WowChat.Retrofit.User;
import com.WowChat.Room.Entities.UserInfoTable;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WallActivity extends AppCompatActivity {
    String groupId,groupImage,groupName;
    ImageView groupDp,addMemberButton;
    GifImageView load;
    RecyclerView recyclerView;
    Toolbar toolbar;
    TextView groupNameTextView;
    String myId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

        groupId=getIntent().getStringExtra("group_id");
        groupName=getIntent().getStringExtra("group_name");
        groupImage=getIntent().getStringExtra("group_image");
        load=findViewById(R.id.wall_load);
        addMemberButton=findViewById(R.id.add_member);
        toolbar=findViewById(R.id.wall_toolbar);
        groupNameTextView=findViewById(R.id.groupName);
        groupDp=findViewById(R.id.groupDp);

        setBasicUI();
        SharedPreferences sharedPreferences=getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        myId=sharedPreferences.getString("id","");
        fetchGroupDetails(0);
    }

    public void setBasicUI(){
        Toolbar toolbar=findViewById(R.id.wall_toolbar);
        toolbar.setTitle(groupName);
        toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbar);
        groupNameTextView.setText(groupName);
        addMemberButton.setEnabled(false);
        if(groupImage!=null){
            Glide.with(this).load(groupImage).placeholder(R.drawable.loadingc).into(groupDp);
            groupDp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(),ImageViewerActivity.class);
                    intent.putExtra("uri",groupImage);
                    startActivity(intent);
                }
            });
        }
    }
    ArrayList<User> members;
    public void fetchGroupDetails(final Integer source){
        RetrofitClient client=new RetrofitClient();
        Call<GroupRead> call=client.jsonPlaceHolderApi.getGroupDetails(groupId);
        call.enqueue(new Callback<GroupRead>() {
            @Override
            public void onResponse(Call<GroupRead> call, Response<GroupRead> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(WallActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                load.setVisibility(View.GONE);
                addMemberButton.setEnabled(true);
                addMemberButton.setAlpha(1f);

                members=response.body().getMembers();
                setUpMembersRecyclerView(members);
            }

            @Override
            public void onFailure(Call<GroupRead> call, Throwable t) {
                Toast.makeText(WallActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setUpMembersRecyclerView(final ArrayList<User> members){
        RecyclerView recyclerView=findViewById(R.id.wall_members_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
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

    public void editGroup(View view){
        EditGroupBottomSheetDialog dialog=new EditGroupBottomSheetDialog(groupId);
        dialog.show(getSupportFragmentManager(),"editGroupBottomSheetDialog");
        dialog.setOnImageUpdatedListener(new EditGroupBottomSheetDialog.OnImageUpdatedListener() {
            @Override
            public void onGroupDpUpdated(String image) {
                groupImage=image;
                Glide.with(getApplicationContext()).load(image).placeholder(R.drawable.loadingc).into(groupDp);
            }
        });
    }

    public void addMember(View view){

        AddMemberBottomSheetDialog dialog = new AddMemberBottomSheetDialog(groupId,members,myId);
        dialog.show(getSupportFragmentManager(), "addMemberBottomSheetDialog");
        dialog.setOnMembersAddedListener(new AddMemberBottomSheetDialog.OnMembersAddedListener() {
            @Override
            public void onGroupMembersAdded(ArrayList<UserInfoTable> newMembers) {
                for(UserInfoTable newMember : newMembers){
                    members.add(new User(Integer.parseInt(newMember.getPersonId()),newMember.getPersonUsername(),
                            newMember.getPersonFirstName(),newMember.getPersonLastName(),newMember.getPersonImage()));
                }
                setUpMembersRecyclerView(members);
            }
        });

    }


}
