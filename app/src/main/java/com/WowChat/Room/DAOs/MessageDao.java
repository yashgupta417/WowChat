package com.WowChat.Room.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.WowChat.Room.Entities.MessageTable;

import java.util.List;

@Dao
public interface MessageDao {
    @Insert
    void insert(MessageTable messageTable);

    @Update
    void update(MessageTable messageTable);

    @Query("UPDATE MessageTable SET status=:status WHERE messageid=:messageid")
    void updateMessageStatus(String messageid,String status);

    @Query("SELECT * FROM MessageTable WHERE sender=:friend OR recipient=:friend")
    LiveData<List<MessageTable>> getMessages(String friend);

    @Query("SELECT * FROM MessageTable WHERE sender=:friend AND status=:status ")
    LiveData<List<MessageTable>> getUnseenMessages(String friend,String status);//status =Sent

    @Query("DELETE FROM MessageTable")
    void deleteAllMessages();

    @Query("SELECT COUNT(*) From MessageTable WHERE sender=:friendId AND status='Sent'")
    int countUnseenMessages(String friendId);


}
