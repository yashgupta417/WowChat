package com.WowChat.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
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
import com.WowChat.R;
import com.WowChat.Retrofit.RetrofitClient;
import com.WowChat.Retrofit.User;
import com.WowChat.Room.Entities.UserInfoTable;
import com.WowChat.ViewModel.MainAcitivityViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    public static MainAcitivityViewModel viewModel;

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("****MainAC"," i am pasued");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("****MainAC"," i am resumed");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("****MainAC"," i am restarted");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("****MainAC"," i am destroyed");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("****MainAC"," i am created");
        setContentView(R.layout.activity_main);
        Toolbar toolbar=findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        final ChatBoardAdapter adapter = new ChatBoardAdapter(this);
        recyclerView.setAdapter(adapter);
        viewModel= ViewModelProviders.of(this).get(MainAcitivityViewModel.class);
        viewModel.getAllChats().observe(this, new Observer<List<UserInfoTable>>() {
            @Override
            public void onChanged(List<UserInfoTable> userInfoTables) {
//                for(UserInfoTable userInfoTable:userInfoTables){
//                    Log.i("****main",userInfoTable.getPersonImage());
//                }

                adapter.submitList(userInfoTables);
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
                Log.i("****",userInfoTable.getPersonImage());
                intent.putExtra("id",userInfoTable.getPersonId());
                startActivity(intent);



            }
        });

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
        }
        return true;
    }
    public void goToSearch(View view){
        Intent intent=new Intent(getApplicationContext(),SearchActivity.class);
        startActivity(intent);
    }
    public void goToProfile(){
        Intent intent=new Intent(getApplicationContext(),ProfileActivity.class);
        startActivity(intent);
    }


}
