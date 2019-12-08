package com.WowChat.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.WowChat.Adapters.GroupAdapter;
import com.WowChat.R;
import com.WowChat.Repository.GroupRepository;
import com.WowChat.Room.Entities.GroupTable;

import java.util.List;

public class GroupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        initializeUIElements();
    }
    public void initializeUIElements(){
        Toolbar toolbar=findViewById(R.id.group_toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView=findViewById(R.id.group_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setHasFixedSize(true);
        final GroupAdapter adapter=new GroupAdapter(this);
        recyclerView.setAdapter(adapter);
        GroupRepository groupRepository=new GroupRepository(getApplication());
        groupRepository.getGroups().observe(this, new Observer<List<GroupTable>>() {
            @Override
            public void onChanged(List<GroupTable> groupTables) {
                adapter.submitList(groupTables);
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.group_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.create_group: goToNewGroupActivity();
        }
        return true;
    }
    public void goToNewGroupActivity(){
        Intent intent=new Intent(getApplicationContext(),NewGroupActivity.class);
        startActivity(intent);
    }
}
