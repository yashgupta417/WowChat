package com.WowChat.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.WowChat.Adapters.ChatBoardAdapter;
import com.WowChat.ModalBottomSheet.ChatBoardBottomSheetDialog;
import com.WowChat.ModalBottomSheet.GroupBottomSheetDialog;
import com.WowChat.R;
import com.WowChat.Retrofit.RetrofitClient;
import com.WowChat.Retrofit.User;
import com.WowChat.Room.Entities.UserInfoTable;
import com.WowChat.ViewModel.MainAcitivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static MainAcitivityViewModel viewModel;
    ChatBoardAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeUIElements();
        setUpRecyclerView();
    }
    public void initializeUIElements(){
        Toolbar toolbar=findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter = new ChatBoardAdapter(this);
        recyclerView.setAdapter(adapter);

    }
    public void setUpRecyclerView(){
        viewModel= ViewModelProviders.of(this).get(MainAcitivityViewModel.class);
        viewModel.getAllChats().observe(this, new Observer<List<UserInfoTable>>() {
            @Override
            public void onChanged(List<UserInfoTable> chats) {
                sortChats(chats);
                adapter.submitList(chats);
                checkIfNoChats(chats);
            }
        });
        adapter.setOnItemClickListener(new ChatBoardAdapter.onItemClickListener() {
            @Override
            public void onItemClick(UserInfoTable userInfoTable) {
                Intent intent=new Intent(getApplicationContext(),ChatActivity.class);
                intent.putExtra("username",userInfoTable.getPersonUsername());
                intent.putExtra("firstName",userInfoTable.getPersonFirstName());
                intent.putExtra("lastName",userInfoTable.getPersonLastName());
                intent.putExtra("email",userInfoTable.getPersonEmail());
                intent.putExtra("image",userInfoTable.getPersonImage());
                intent.putExtra("id",userInfoTable.getPersonId());
                startActivity(intent);
            }

            @Override
            public void onItemLongClick(UserInfoTable userInfoTable) {
                ChatBoardBottomSheetDialog dialog=new ChatBoardBottomSheetDialog(userInfoTable.getPersonId());
                dialog.show(getSupportFragmentManager(),"chatboard_bottom_sheet");
            }
        });
    }
    public void sortChats(List<UserInfoTable> chats){
        Collections.sort(chats, new Comparator<UserInfoTable>() {
            @Override
            public int compare(UserInfoTable o1, UserInfoTable o2) {
                SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a");
                String time1=o1.getLatestMessagedate()+" "+o1.getLatestMessageTime()+" "+o1.getLatestMessageAMorPM();
                String time2=o2.getLatestMessagedate()+" "+o2.getLatestMessageTime()+" "+o2.getLatestMessageAMorPM();
                try {
                    return dateFormat.parse(time2.toLowerCase()).compareTo(dateFormat.parse(time1.toLowerCase()));
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.i("%%%%%%",e.getLocalizedMessage());
                    return 1;
                }
            }
        });
    }
    public void checkIfNoChats(List<UserInfoTable> userInfoTables){
        LinearLayout ll=findViewById(R.id.nochat_ll);
        if(userInfoTables.size()==0){
            ll.setVisibility(View.VISIBLE);
        }else{
            ll.setVisibility(View.GONE);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.main_menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.profile: goToProfile(); break;
            case R.id.search: goToSearch();break;
        }
        return true;
    }
    public void goToSearch(){
        Intent intent=new Intent(getApplicationContext(),SearchActivity.class);
        startActivity(intent);
    }
    public void goToSearch(View view){
        Intent intent=new Intent(getApplicationContext(),SearchActivity.class);
        startActivity(intent);
    }
    public void goToProfile(){
        Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
        startActivity(intent);
    }

    public void goToGroups(View view) {
        Intent intent=new Intent(getApplicationContext(),GroupActivity.class);
        startActivity(intent);
    }

}
