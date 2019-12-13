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
    void updateOrCreateGroup(String id,String name,String image);

    @Query("DELETE FROM GROUPTABLE WHERE id=:id")
    void deleteGroup(String id);

    @Query("DELETE FROM GroupTable")
    void deleteAllGroups();

    @Query("SELECT * FROM GROUPTABLE WHERE id=:id")
    LiveData<GroupTable> getGroupDetails(String id);




}
