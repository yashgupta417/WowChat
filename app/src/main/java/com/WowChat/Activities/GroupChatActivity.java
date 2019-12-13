package com.WowChat.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
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

import com.WowChat.Adapters.GroupMessageAdapter;
import com.WowChat.R;
import com.WowChat.Repository.GroupRepository;
import com.WowChat.Retrofit.GroupMessage;
import com.WowChat.Retrofit.Message;
import com.WowChat.Retrofit.RetrofitClient;
import com.WowChat.Room.Entities.GroupMessageTable;
import com.WowChat.Room.Entities.GroupTable;
import com.WowChat.Room.Entities.MessageTable;
import com.WowChat.Room.Entities.UserInfoTable;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupChatActivity extends AppCompatActivity {
    String groupId,myId,myName,myImage;
    EditText editText;
    ImageView imageSend,textSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        //*******SETTING UP BASIC UI********
        Toolbar toolbar=findViewById(R.id.group_chat_toolbar);
        setSupportActionBar(toolbar);

        editText=findViewById(R.id.group_chat_editText);
        imageSend=findViewById(R.id.group_image_message_button);
        textSend=findViewById(R.id.group_text_message_button);
        setUpEditTextListener();

        SharedPreferences sharedPreferences=getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);
        myId=sharedPreferences.getString("id",null);
        myName=sharedPreferences.getString("first_name",null);
        myImage=sharedPreferences.getString("image",null);
        groupId=getIntent().getStringExtra("group_id");

        GroupRepository repository=new GroupRepository(getApplication());
        repository.getGroupDetail(groupId).observe(this, new Observer<GroupTable>() {
            @Override
            public void onChanged(GroupTable groupTable) {
                showBasicUI(groupTable.getName(),groupTable.getImage());
            }
        });


        //*************************************

        //*******RecycleView*********
        final RecyclerView recyclerView=findViewById(R.id.groupchatRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final GroupMessageAdapter adapter=new GroupMessageAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);


        repository.getGroupMessages(groupId).observe(this, new Observer<List<GroupMessageTable>>() {
            @Override
            public void onChanged(List<GroupMessageTable> groupMessageTables) {
                Log.i("***********","size: "+groupMessageTables.size());
                recyclerView.scrollToPosition(groupMessageTables.size()-1);
                adapter.submitList(groupMessageTables);
            }
        });
        adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                recyclerView.scrollToPosition(adapter.getItemCount()-1);
            }
        });
        adapter.setOnItemClickListener(new GroupMessageAdapter.onItemClickListener() {
            @Override
            public void onItemClick(GroupMessageTable groupMessageTable) {
                if(groupMessageTable.getImage()!=null){
                    Intent intent=new Intent(getApplicationContext(),ImageViewerActivity.class);
                    intent.putExtra("uri",groupMessageTable.getImage());
                    startActivity(intent);
                }
            }
        });
        //**************************

    }
    public void showBasicUI(String groupName,String groupImage){
        TextView groupNameTextView=findViewById(R.id.group_name);
        groupNameTextView.setText(groupName);
        CircleImageView dp=findViewById(R.id.group_dp);
        Glide.with(this).load(groupImage).diskCacheStrategy(DiskCacheStrategy.DATA).placeholder(R.drawable.loadingc).into(dp);
    }
    public void setUpEditTextListener(){
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String text=s.toString();
                if(text.trim().isEmpty()){
                    textSend.setEnabled(false);
                    imageSend.setEnabled(true);
                    imageSend.setVisibility(View.VISIBLE);
                    textSend.setVisibility(View.INVISIBLE);

                }else{
                    textSend.setEnabled(true);
                    imageSend.setEnabled(false);
                    imageSend.setVisibility(View.INVISIBLE);
                    textSend.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void goToWall(View view){
        Intent intent=new Intent(getApplicationContext(), WallActivity.class);
        intent.putExtra("group_id",groupId);
        startActivity(intent);
    }
    public void sendGMsg(View view){
        if(editText.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "No message", Toast.LENGTH_SHORT).show();
            return;
        }
        String text=editText.getText().toString();
        editText.setText("");
        Calendar c = Calendar.getInstance();
        SimpleDateFormat timePreciseformat = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat AMOrPMFormat=new SimpleDateFormat("a");
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        //String time = timeformat.format(c.getTime());
        String timePrecise=timePreciseformat.format(c.getTime());
        String date=dateformat.format(c.getTime());
        String amorpm=AMOrPMFormat.format(c.getTime());
        String id= UUID.randomUUID()+date+timePrecise;

        GroupMessage groupMessage=new GroupMessage(id,null,text,Integer.parseInt(myId),Integer.parseInt(groupId),
                date,timePrecise,amorpm,null);

        GroupMessageTable groupMessageTable=new GroupMessageTable(id,text,null,null,myId,myName,myImage,groupId,date,timePrecise
                        ,amorpm,"sending");

        GroupRepository repository=new GroupRepository(getApplication());
        repository.insertMessage(groupMessageTable);

        RetrofitClient retrofitClient=new RetrofitClient();
        Call<GroupMessage> call=retrofitClient.jsonPlaceHolderApi.sendGroupMessage(groupMessage);
        call.enqueue(new Callback<GroupMessage>() {
            @Override
            public void onResponse(Call<GroupMessage> call, Response<GroupMessage> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(GroupChatActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(GroupChatActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                postSendingMessageWork(response.body());
            }

            @Override
            public void onFailure(Call<GroupMessage> call, Throwable t) {
                Toast.makeText(GroupChatActivity.this, t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    Uri image;
    public void sendGroupImageMessage(View view){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},2);
            }else{
                Intent intent=new Intent(getApplicationContext(),GalleryActivity.class);
                startActivityForResult(intent,1);

            }
        }
    }
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
            Log.i("********uri",image.toString());
            if(image!=null){
                sendImageMessage(image.toString());
            }
        }
    }
    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void sendImageMessage(String image) {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat timePreciseformat = new SimpleDateFormat("hh:mm:ss");
        SimpleDateFormat AMOrPMFormat=new SimpleDateFormat("a");
        SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        //String time = timeformat.format(c.getTime());
        String timePrecise=timePreciseformat.format(c.getTime());
        String date=dateformat.format(c.getTime());
        String amorpm=AMOrPMFormat.format(c.getTime());
        String id= UUID.randomUUID()+date+timePrecise;

        GroupMessage groupMessage=new GroupMessage(id,null,null,Integer.parseInt(myId),Integer.parseInt(groupId),
                date,timePrecise,amorpm,image);

        GroupMessageTable groupMessageTable=new GroupMessageTable(id,null,null,image,myId,myName,myImage,groupId,date,timePrecise
                ,amorpm,"sending");

        GroupRepository repository=new GroupRepository(getApplication());
        repository.insertMessage(groupMessageTable);

        File file = new File(image);
        File compressimagefile = null;
        try {
            compressimagefile = new Compressor(this).compressToFile(file);
        } catch (IOException e) {
            compressimagefile = file;
            e.printStackTrace();
        }

        final RequestBody requestBody = RequestBody.create(compressimagefile, MediaType.parse("multipart/form-data"));
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", file.getName(), requestBody);
        RequestBody idPart = RequestBody.create(groupMessage.getId(), MediaType.parse("multipart/form-data"));
        RequestBody textPart = RequestBody.create("", MediaType.parse("multipart/form-data"));
        RequestBody senderPart = RequestBody.create(Integer.toString(groupMessage.getSender()), MediaType.parse("multipart/form-data"));
        RequestBody groupPart = RequestBody.create(Integer.toString(groupMessage.getGroup()), MediaType.parse("multipart/form-data"));
        RequestBody datePart = RequestBody.create(groupMessage.getDate(), MediaType.parse("multipart/form-data"));
        RequestBody timePart = RequestBody.create(groupMessage.getTime(), MediaType.parse("multipart/form-data"));
        RequestBody amorpmPart = RequestBody.create(groupMessage.getAmorpm(), MediaType.parse("multipart/form-data"));
        RetrofitClient retrofitClient = new RetrofitClient();

        Call<GroupMessage> call = retrofitClient.jsonPlaceHolderApi.sendGroupImageMessage(idPart, textPart, senderPart,groupPart, datePart, timePart, amorpmPart, imagePart);
        call.enqueue(new Callback<GroupMessage>() {
            @Override
            public void onResponse(Call<GroupMessage> call, Response<GroupMessage> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(GroupChatActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(GroupChatActivity.this, "Image Message sent", Toast.LENGTH_SHORT).show();
                postSendingMessageWork(response.body());
            }

            @Override
            public void onFailure(Call<GroupMessage> call, Throwable t) {
                Toast.makeText(GroupChatActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void postSendingMessageWork(GroupMessage rMessage){

        GroupRepository repository=new GroupRepository(getApplication());
        repository.updateMessageStatus(rMessage.getId(),"Sent");

        if(rMessage.getImage()!=null){
            repository.updateImageAddress(rMessage.getId(),rMessage.getImage());
        }

    }

}
