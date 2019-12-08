package com.WowChat.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.WowChat.R;
import com.WowChat.Repository.GroupRepository;
import com.WowChat.Retrofit.GroupRead;
import com.WowChat.Retrofit.GroupWrite;
import com.WowChat.Retrofit.RetrofitClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewGroupActivity extends AppCompatActivity {
    EditText groupNameEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_group);
        groupNameEditText=findViewById(R.id.groupNameEditText);
    }
    public void createGroup(View view){
        createGroup();
    }
    public void createGroup(){
        String groupName=groupNameEditText.getText().toString();
        if(groupName.isEmpty()){
            Toast.makeText(this, "Enter group name", Toast.LENGTH_SHORT).show();
            return;
        }
        SharedPreferences sharedPreferences=getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        String id_string=sharedPreferences.getString("id",null);
        Integer id=Integer.parseInt(id_string);
        ArrayList<Integer> members=new ArrayList<Integer>();
        members.add(id);
        GroupWrite newGroup=new GroupWrite(groupName,null,members,id,null);
        RetrofitClient retrofitClient=new RetrofitClient();
        Call<GroupWrite> call=retrofitClient.jsonPlaceHolderApi.createGroup(newGroup);
        call.enqueue(new Callback<GroupWrite>() {
            @Override
            public void onResponse(Call<GroupWrite> call, Response<GroupWrite> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(NewGroupActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                GroupWrite group=response.body();
                GroupRepository repository=new GroupRepository(getApplication());
                repository.insertOrUpdateGroup(Integer.toString(group.getGroupId()),group.getGroupName(),group.getGroupImage());
                Toast.makeText(NewGroupActivity.this, "Group created", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GroupWrite> call, Throwable t) {
                Toast.makeText(NewGroupActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
