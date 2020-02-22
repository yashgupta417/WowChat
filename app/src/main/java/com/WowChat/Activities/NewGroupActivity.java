package com.WowChat.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.WowChat.Adapters.FriendsAdapter;
import com.WowChat.Adapters.SelectedFriendsAdapter;
import com.WowChat.LoadingDialog;
import com.WowChat.R;
import com.WowChat.Repository.GroupRepository;
import com.WowChat.Repository.MyRepository;
import com.WowChat.Retrofit.GroupRead;
import com.WowChat.Retrofit.GroupWrite;
import com.WowChat.Retrofit.RetrofitClient;
import com.WowChat.Retrofit.User;
import com.WowChat.Room.Entities.UserInfoTable;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewGroupActivity extends AppCompatActivity {
    EditText groupNameEditText;
    ArrayList<UserInfoTable> selectedFriends;
    String myId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        groupNameEditText=findViewById(R.id.groupNameEditText);
        SharedPreferences sharedPreferences=getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        myId=sharedPreferences.getString("id",null);

        final ArrayList<Boolean> selectedOrNot=new ArrayList<Boolean>();
        selectedFriends=new ArrayList<UserInfoTable>();

        final MyRepository repository=new MyRepository(getApplication());
        final ArrayList<UserInfoTable> friends=new ArrayList<UserInfoTable>(repository.getAllFriends());

        for(int i = 0; i< friends.size(); i++){
            selectedOrNot.add(false);
        }

        //********* SETTING SELECTED FRIENDS RECYLERVIEW*******
        final RecyclerView sfRecyclerView=findViewById(R.id.selected_friends_recyclerView);
        sfRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        sfRecyclerView.setHasFixedSize(true);
        final SelectedFriendsAdapter adapter1=new SelectedFriendsAdapter(this);
        sfRecyclerView.setAdapter(adapter1);
        //******************************************************

        // **********SETTING FRIENDS RECYCLERVIEW***********
        RecyclerView friendsRecyclerView=findViewById(R.id.friends_recyclerView);
        friendsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        friendsRecyclerView.setHasFixedSize(true);
        final FriendsAdapter adapter=new FriendsAdapter(this, friends,selectedOrNot);
        friendsRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new FriendsAdapter.onItemClickListener() {
            @Override
            public void onItemClick(int position) {

                adapter.selectedOrNot.set(position,!adapter.selectedOrNot.get(position));
                adapter.notifyItemChanged(position);

                if(adapter.selectedOrNot.get(position)){
                    selectedFriends.add(adapter.friends.get(position));
                }else{
                    int index=selectedFriends.indexOf(adapter.friends.get(position));
                    selectedFriends.remove(index);
                }

                sfRecyclerView.scrollToPosition(selectedFriends.size()-1);
                adapter1.submitList(selectedFriends);
                adapter1.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        super.onItemRangeInserted(positionStart, itemCount);
                        sfRecyclerView.scrollToPosition(adapter1.getItemCount()-1);
                    }
                });
            }
        });
        //*********************************

        EditText groupNameEditText=findViewById(R.id.groupNameEditText);
        final TextView groupNameTextView=findViewById(R.id.group_name);
        groupNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(s.length()>15){
                    Toast.makeText(NewGroupActivity.this, "Max 15 characters", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String g_name=s.toString();
                if(g_name.isEmpty()){
                    groupNameTextView.setText("Group Name?");
                    groupNameTextView.setTextColor(Color.parseColor("#44000000"));
                }else{
                    groupNameTextView.setText(g_name);
                    groupNameTextView.setTextColor(Color.BLACK);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    LoadingDialog dialog;
    public void createGroup(View view){
        createGroup();
    }
    public void createGroup(){
        String groupName=groupNameEditText.getText().toString();
        if(image==null){
            Toast.makeText(this, "Image Required", Toast.LENGTH_SHORT).show();
            return;
        }
        if(groupName.isEmpty()){
            Toast.makeText(this, "Enter group name", Toast.LENGTH_SHORT).show();
            return;
        }
        if(selectedFriends.size()==0){
            Toast.makeText(this, "Select Friends First", Toast.LENGTH_SHORT).show();
            return;
        }

        GroupWrite newGroup=new GroupWrite(groupName,null,null,Integer.parseInt(myId),null);
        RetrofitClient retrofitClient=new RetrofitClient();
        Call<GroupWrite> call=retrofitClient.jsonPlaceHolderApi.createGroup(newGroup);

        dialog=new LoadingDialog(this);
        dialog.showDialog();
        call.enqueue(new Callback<GroupWrite>() {
            @Override
            public void onResponse(Call<GroupWrite> call, Response<GroupWrite> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(NewGroupActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                GroupWrite group=response.body();
                //GroupRepository repository=new GroupRepository(getApplication());
                //repository.insertOrUpdateGroup(Integer.toString(group.getGroupId()),group.getGroupName(),group.getGroupImage());
                updateGroupDp(Integer.toString(group.getGroupId()),image);

            }

            @Override
            public void onFailure(Call<GroupWrite> call, Throwable t) {
                Toast.makeText(NewGroupActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                dialog.hideDialog();
            }
        });
    }
    public void addMember(String user_id, final String group_id){
        SharedPreferences preferences=getSharedPreferences(getPackageName(),Context.MODE_PRIVATE);
        String myId=preferences.getString("id",null);
        RetrofitClient retrofitClient=new RetrofitClient();
        Call<User> call=retrofitClient.jsonPlaceHolderApi.addMember(user_id, group_id,myId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(NewGroupActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                selectedFriends.remove(0);
                if(selectedFriends.size()==0){
                    dialog.hideDialog();
                    Toast.makeText(NewGroupActivity.this, "Group Created", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    addMember(selectedFriends.get(0).getPersonId(),group_id);
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(NewGroupActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                dialog.hideDialog();
            }
        });
    }
    public void addMe(final String group_id){

        //Integer id=Integer.parseInt(id_string);
        //UserInfoTable me=new UserInfoTable(null,null,null,null,null
          ///      ,id_string,null,null,null,null);

        RetrofitClient retrofitClient=new RetrofitClient();
        Call<User> call=retrofitClient.jsonPlaceHolderApi.addMember(myId, group_id,myId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(NewGroupActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                addMember(selectedFriends.get(0).getPersonId(),group_id);
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(NewGroupActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                dialog.hideDialog();
            }
        });
    }
    Uri image;
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==2){
            if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED){
                Intent intent=new Intent(getApplicationContext(),GalleryActivity.class);
                startActivityForResult(intent,1);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1 && resultCode==RESULT_OK && data!=null){
          image=data.getData();
            ImageView dp=findViewById(R.id.new_group_dp);
            Glide.with(this).load(image.getPath()).into(dp);
        }
    }
    public void chooseGroupDp(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
            }else{
                Intent intent=new Intent(getApplicationContext(),GalleryActivity.class);
                startActivityForResult(intent,1);

            }
        }else {
            Intent intent=new Intent(getApplicationContext(),GalleryActivity.class);
            startActivityForResult(intent,1);
        }
    }
    public void updateGroupDp(final String group_id, Uri image){

        File file = new File(image.toString());
        File compressimagefile=null;
        try {
            compressimagefile =new Compressor(this).compressToFile(file);
        } catch (IOException e) {
            compressimagefile=file;
            e.printStackTrace();
        }

        final RequestBody requestBody = RequestBody.create( compressimagefile, MediaType.parse("multipart/form-data"));
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("group_image",file.getName(),requestBody);

        RetrofitClient retrofitClient=new RetrofitClient();
        Call<GroupRead> call=retrofitClient.jsonPlaceHolderApi.updateGroupDP(group_id,imagePart);
        call.enqueue(new Callback<GroupRead>() {
            @Override
            public void onResponse(Call<GroupRead> call, Response<GroupRead> response) {

                if(!response.isSuccessful()){
                    Toast.makeText(NewGroupActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    finish();
                }
                GroupRead group=response.body();
                //GroupRepository repository=new GroupRepository(getApplication());
                //repository.insertOrUpdateGroup(Integer.toString(group.getGroupId()),group.getGroupName(),group.getGroupImage());
                UserInfoTable chat=new UserInfoTable(group_id,group.getGroupName(),
                        "","",group.getGroupImage(),
                        group_id,"","","","",1);
                MyRepository repository=new MyRepository(getApplication());
                repository.updateOrCreateUserInfo(chat);
                addMe(Integer.toString(group.getGroupId()));

            }

            @Override
            public void onFailure(Call<GroupRead> call, Throwable t) {
                Toast.makeText(NewGroupActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                dialog.hideDialog();
            }
        });
    }
}
