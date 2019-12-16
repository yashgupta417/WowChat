package com.WowChat.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.WowChat.Adapters.GroupQueryAdapter;
import com.WowChat.Adapters.UserAdapter;
import com.WowChat.R;
import com.WowChat.Retrofit.GroupRead;
import com.WowChat.Retrofit.RetrofitClient;
import com.WowChat.Retrofit.User;

import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupQueryActivity extends AppCompatActivity {
    androidx.appcompat.widget.SearchView searchView;
    GifImageView load;
    RecyclerView recyclerView;
    GroupQueryAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_query);
        initializeUIElements();
    }
    public void initializeUIElements(){
        Toolbar toolbar=findViewById(R.id.gq_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        load=findViewById(R.id.gq_load);
        searchView=findViewById(R.id.gq_searchView);

        recyclerView=findViewById(R.id.gq_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter=new GroupQueryAdapter(this);
        recyclerView.setAdapter(adapter);
        initializeSearchView();
    }
    public void initializeSearchView(){
        //When we have to use diffUtil then use listadapter, as it implements some functions itself,else use recyclerView.
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                load.setVisibility(View.VISIBLE);
                hideNoSearchFallBack();
                searchQuery(query);
                InputMethodManager methodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                methodManager.hideSoftInputFromWindow(searchView.getWindowToken(),0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.trim().isEmpty()){
                    return false;
                }
                hideNoSearchFallBack();
                load.setVisibility(View.VISIBLE);
                searchQuery(newText);
                return true;
            }
        });
    }
    public void hideNoSearchFallBack(){

    }
    public void goBackFromGroupSearch(View view){
        finish();
    }

    public void searchQuery(final String query){

        RetrofitClient retrofitClient=new RetrofitClient();
        Call<List<GroupRead>> call=retrofitClient.jsonPlaceHolderApi.queryGroups(query);
        call.enqueue(new Callback<List<GroupRead>>() {
            @Override
            public void onResponse(Call<List<GroupRead>> call, Response<List<GroupRead>> response) {
                if(response.isSuccessful()){
                    ArrayList<GroupRead> groups=new ArrayList<GroupRead>(response.body());
                    load.setVisibility(View.INVISIBLE);
                    setRecyclerView(groups);
                    checkIfNoResults(groups.size());
                    return;
                }
                load.setVisibility(View.INVISIBLE);
                Toast.makeText(GroupQueryActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<GroupRead>> call, Throwable t) {
                Toast.makeText(GroupQueryActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void checkIfNoResults(int size){
        LinearLayout linearLayout = findViewById(R.id.gq_no_result_ll);
        if(size==0) {
            linearLayout.setVisibility(View.VISIBLE);
        }else {
            linearLayout.setVisibility(View.INVISIBLE);
        }
    }
    public void setRecyclerView(ArrayList<GroupRead> groups){
        adapter.submitList(groups);
        adapter.setOnItemClickListener(new GroupQueryAdapter.onItemClickListener() {
            @Override
            public void onGroupClick(GroupRead groupRead) {
                Intent intent=new Intent(getApplicationContext(),OtherGroupActivity.class);
                intent.putExtra("group_id",Integer.toString(groupRead.getGroupId()));
                startActivity(intent);
            }
        });
    }
}
