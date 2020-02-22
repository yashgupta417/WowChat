package com.WowChat.Room.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.WowChat.Room.Entities.UserInfoTable;

import java.util.List;

@Dao
public interface UserInfoDao {

    @Insert
    void insert(UserInfoTable userInfoTable);

    @Update
    void update(UserInfoTable userInfoTable);


    @Query("SELECT * FROM UserInfoTable")//ORDER BY latestMessagedate DESC,latestMessageAMorPM DESC,latestMessageTime DESC
    LiveData<List<UserInfoTable>> getAllChats();

    @Query("SELECT * FROM UserInfoTable WHERE isGroup=0")/** USE IN NEW GROUP ACTIVITY**/
    List<UserInfoTable> getAllFriends();



    @Query("INSERT OR REPLACE INTO UserInfoTable(personUsername,personFirstName,personLastName,personEmail,personImage,personId,latestMesage,latestMessageTime,latestMessagedate,latestMessageAMorPM,isGroup) VALUES(:personUsername,:personFirstName,:personLastName,:personEmail,:personImage,:personId,:latestMesage,:latestMessageTime,:latestMessageDate,:latestMessageAmorPm,:isGroup)")
    void updateOrCreateUserInfo(String personUsername, String personFirstName, String personLastName, String personEmail, String personImage, String personId, String latestMesage,String latestMessageTime,String latestMessageDate,String latestMessageAmorPm,Integer isGroup);

    @Query("DELETE FROM UserInfoTable")
    void deleteAllUserInfo();

    @Query("UPDATE userinfotable SET unseenMessageCount=:count WHERE personId=:id")
    void setUnseenCount(Integer count,String id);

    @Query("UPDATE userinfotable SET personImage=:image WHERE personId=:id")
    void updateImage(String image,String id);

    @Query("UPDATE userinfotable SET latestMesage=:text ," +
            "latestMessageTime=:time, latestMessagedate=:date , latestMessageAMorPM=:amorpm WHERE personId=:id ")
    void updateLatestMessage(String text,String time,String date,String amorpm,String id);

    @Query("DELETE FROM USERINFOTABLE WHERE personId=:id")
    void deleteChat(String id);


}
