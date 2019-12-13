package com.WowChat.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.WowChat.Adapters.MembersAdapter;
import com.WowChat.Adapters.SelectedFriendsAdapter;
import com.WowChat.ModalBottomSheet.AddMemberBottomSheetDialog;
import com.WowChat.ModalBottomSheet.EditGroupBottomSheetDialog;
import com.WowChat.R;
import com.WowChat.Repository.GroupRepository;
import com.WowChat.Retrofit.GroupRead;
import com.WowChat.Retrofit.RetrofitClient;
import com.WowChat.Retrofit.User;
import com.WowChat.Room.Entities.GroupTable;
import com.WowChat.Room.Entities.UserInfoTable;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class WallActivity extends AppCompatActivity {
    String groupId;
    ImageView groupDp;
    ConstraintLayout container;
    GifImageView load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wall);

        groupId=getIntent().getStringExtra("group_id");
        //groupName=getIntent().getStringExtra("group_name");
        //groupImage=getIntent().getStringExtra("group_image");
        GroupRepository repository=new GroupRepository(getApplication());
        repository.getGroupDetail(groupId).observe(this, new Observer<GroupTable>() {
            @Override
            public void onChanged(GroupTable groupTable) {
                setBasicUI(groupTable.getName(),groupTable.getImage());
            }
        });
        container=findViewById(R.id.wall_container);
        load=findViewById(R.id.wall_load);
        hideUI();
        fetchGroupDetails();
    }
    public void setBasicUI(String groupName,String groupImage){
        Toolbar toolbar=findViewById(R.id.wall_toolbar);
        toolbar.setTitle(groupName);
        setSupportActionBar(toolbar);
        groupDp=findViewById(R.id.wall_dp);
        if(groupImage!=null){
            Glide.with(this).load(groupImage).into(groupDp);
        }
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

                group=response.body();
                members=group.getMembers();
                setUpRecycleView(members);
                revealUI();
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

    public void editGroup(View view){
        EditGroupBottomSheetDialog dialog=new EditGroupBottomSheetDialog(groupId);
        dialog.show(getSupportFragmentManager(),"editGroupBottomSheetDialog");
    }

    public void addMember(View view){

        AddMemberBottomSheetDialog dialog = new AddMemberBottomSheetDialog(groupId, group.getMembers());
        dialog.show(getSupportFragmentManager(), "addMemberBottomSheetDialog");
        dialog.setOnMembersAddedListener(new AddMemberBottomSheetDialog.OnMembersAddedListener() {
            @Override
            public void onGroupMembersAdded(ArrayList<UserInfoTable> newMembers) {
                for(UserInfoTable newMember : newMembers){
                    members.add(new User(Integer.parseInt(newMember.getPersonId()),newMember.getPersonUsername(),
                            newMember.getPersonFirstName(),newMember.getPersonLastName(),newMember.getPersonImage()));
                }
                setUpRecycleView(members);
            }
        });

    }

}
