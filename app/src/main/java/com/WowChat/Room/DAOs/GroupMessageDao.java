package com.WowChat.Room.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.WowChat.Room.Entities.GroupMessageTable;
import com.WowChat.Room.Entities.MessageTable;

import java.util.List;

@Dao
public interface GroupMessageDao {
    @Insert
    void insert(GroupMessageTable messageTable);

    @Query("UPDATE GroupMessageTable SET status=:status  WHERE id=:messageid")
    void updateMessageStatus(String messageid,String status);

    @Query("UPDATE GroupMessageTable SET image=:image  WHERE id=:messageid")
    void updateImageAddress(String messageid,String image);

    @Query("SELECT * FROM GroupMessageTable WHERE group_id=:g_id")
    LiveData<List<GroupMessageTable>> getMessages(String g_id);

    @Query("DELETE FROM GroupMessageTable")
    void deleteAllGroupMessages();

    @Query(("DELETE FROM GROUPMESSAGETABLE WHERE group_id=:groupId"))
    void deleteMessagesOfGroup(String groupId);
}
