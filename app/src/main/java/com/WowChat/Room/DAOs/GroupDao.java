package com.WowChat.Room.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.WowChat.Room.Entities.GroupTable;

import java.util.List;

@Dao
public interface GroupDao {

    @Query("SELECT * FROM GroupTable")
    LiveData<List<GroupTable>> getGroups();

    @Query("INSERT OR REPLACE INTO GroupTable(id,name,image) VALUES(:id,:name,:image)")
    void updateOrCreateUserInfo(String id,String name,String image);

    @Delete
    void deleteGroup(GroupTable groupTable);

    @Query("DELETE FROM GroupTable")
    void deleteAllGroups();

}
