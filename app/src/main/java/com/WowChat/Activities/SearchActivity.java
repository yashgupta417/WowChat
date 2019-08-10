package com.WowChat.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.WowChat.Retrofit.RetrofitClient;
import com.WowChat.Retrofit.User;
import com.bumptech.glide.Glide;
import com.WowChat.R;

import pl.droidsonroids.gif.GifImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
    ImageView image;
    TextView name;
    TextView username;
    com.WowChat.Retrofit.User user;
    CardView cardView;
    SearchView searchView;
    GifImageView gifImageView;
    TextView findTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        searchView=findViewById(R.id.searchView);
        image=findViewById(R.id.search_image);
        name=findViewById(R.id.search_name);
        username=findViewById(R.id.search_username);
        cardView=findViewById(R.id.search_cardView);
        findTextView=findViewById(R.id.findText);
        gifImageView=findViewById(R.id.gifimage);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchByUsername(query);
                InputMethodManager methodManager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                methodManager.hideSoftInputFromWindow(searchView.getWindowToken(),0);
                gifImageView.setVisibility(View.VISIBLE);
                cardView.setVisibility(View.INVISIBLE);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }
    public void goToChatScreen(View view){
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
    public void searchByUsername(final String query){

        com.WowChat.Retrofit.RetrofitClient retrofitClient=new RetrofitClient();
        Call<com.WowChat.Retrofit.User> call=retrofitClient.jsonPlaceHolderApi.searchByUsername(query);
        call.enqueue(new Callback<com.WowChat.Retrofit.User>() {
            @Override
            public void onResponse(Call<com.WowChat.Retrofit.User> call, Response<com.WowChat.Retrofit.User> response) {
                if(!response.isSuccessful()){
                    Toast.makeText(SearchActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    return;
                }
                gifImageView.setVisibility(View.INVISIBLE);
                user=response.body();
                name.setText(user.getFirstName()+" "+user.getLastName());
                username.setText("@"+user.getUsername());
                cardView.setVisibility(View.VISIBLE);

//                searchView.animate().translationYBy(-150).setDuration(1500);
                if(user.getImage()!=null){
                    Glide.with(SearchActivity.this).load(user.getImage()).placeholder(R.drawable.user_img).into(image);
                }else{
                    Glide.with(SearchActivity.this).load(R.drawable.user_img).into(image);
                }


            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(SearchActivity.this, "Check your connection", Toast.LENGTH_SHORT).show();
            }
        });

    }

}
