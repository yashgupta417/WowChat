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
import android.widget.Toast;

import com.WowChat.Adapters.ShotAdapter;
import com.WowChat.Adapters.UserAdapter;
import com.WowChat.Retrofit.RetrofitClient;
import com.WowChat.Retrofit.User;
import com.bumptech.glide.Glide;
import com.WowChat.R;

import java.util.ArrayList;
import java.util.List;


import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {

    androidx.appcompat.widget.SearchView searchView;
    GifImageView load;
    RecyclerView recyclerView;
    UserAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        //Toolbar toolbar=findViewById(R.id.s_toolbar);
        //setSupportActionBar(toolbar);
        load=findViewById(R.id.s_load);
        searchView=findViewById(R.id.searchView);

        recyclerView=findViewById(R.id.user_query_recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        adapter=new UserAdapter(this);
        recyclerView.setAdapter(adapter);
        //When we have to use diffUtil then use listadapter, as it implements some functions itself,else use recyclerView.



        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                load.setVisibility(View.VISIBLE);
                searchQuery(query);
                InputMethodManager methodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                methodManager.hideSoftInputFromWindow(searchView.getWindowToken(),0);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                load.setVisibility(View.VISIBLE);
                searchQuery(newText);
                return false;
            }
        });
    }
    public void goBack(View view){
        finish();
    }

    public void searchQuery(final String query){

        RetrofitClient retrofitClient=new RetrofitClient();
        Call<List<User>> call=retrofitClient.jsonPlaceHolderApi.queryUsers(query);
        call.enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                if(response.isSuccessful()){
                    ArrayList<User> users=new ArrayList<User>(response.body());
                    load.setVisibility(View.INVISIBLE);
                    setRecyclerView(users);
                    return;
                }
                load.setVisibility(View.INVISIBLE);
                Toast.makeText(SearchActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                load.setVisibility(View.INVISIBLE);
                Toast.makeText(SearchActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void setRecyclerView(ArrayList<User> users){

        adapter.submitList(users);
        adapter.setOnItemClickListener(new UserAdapter.onItemClickListener() {
            @Override
            public void onItemClick(User user) {
                Intent intent=new Intent(getApplicationContext(), OtherProfileActivity.class);
                if(user.getImage()!=null){
                    intent.putExtra("image",user.getImage().toString());
                }else {
                    intent.putExtra("image","");
                }

                intent.putExtra("username",user.getUsername());
                intent.putExtra("first_name",user.getFirstName());
                intent.putExtra("last_name",user.getLastName());
                intent.putExtra("email",user.getEmail());
                intent.putExtra("id",Integer.toString(user.getId()));
                startActivity(intent);
            }

            @Override
            public void onMessageClick(User user) {
                Intent intent=new Intent(getApplicationContext(), ChatActivity.class);
                if(user.getImage()!=null){
                    intent.putExtra("image",user.getImage().toString());
                }else {
                    intent.putExtra("image","");
                }

                intent.putExtra("username",user.getUsername());
                intent.putExtra("firstName",user.getFirstName());
                intent.putExtra("lastName",user.getLastName());
                intent.putExtra("email",user.getEmail());
                intent.putExtra("id",Integer.toString(user.getId()));
                startActivity(intent);
            }
        });
    }

}
