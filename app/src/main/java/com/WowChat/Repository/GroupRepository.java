package com.WowChat.Repository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.WowChat.Room.DAOs.GroupDao;
import com.WowChat.Room.DAOs.GroupMessageDao;
import com.WowChat.Room.DAOs.MessageDao;
import com.WowChat.Room.DAOs.UserInfoDao;
import com.WowChat.Room.Entities.GroupMessageTable;
import com.WowChat.Room.Entities.GroupTable;
import com.WowChat.Room.Entities.MessageTable;
import com.WowChat.Room.MyDatabase;

import java.util.ArrayList;
import java.util.List;

public class GroupRepository {
    private GroupDao groupDao;
    private GroupMessageDao groupMessageDao;


    public GroupRepository(Application application) {
        MyDatabase database=MyDatabase.getInstance(application);
        this.groupDao = database.groupDao();
        this.groupMessageDao = database.groupMessageDao();
    }

    public LiveData<List<GroupTable>> getGroups(){
        return groupDao.getGroups();
    }
    public LiveData<List<GroupMessageTable>> getGroupMessages(String groupId){
        return groupMessageDao.getMessages(groupId);
    }


    @SuppressLint("StaticFieldLeak")
    public void insertMessage(final GroupMessageTable groupMessageTable){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                groupMessageDao.insert(groupMessageTable);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void updateMessageStatus(final String messageId, final String status){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                groupMessageDao.updateMessageStatus(messageId,status);
                return null;
            }
        }.execute();
    }

    @SuppressLint("StaticFieldLeak")
    public void updateImageAddress(final String messageId, final String image){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                groupMessageDao.updateImageAddress(messageId,image);
                return null;
            }
        }.execute();
    }


    @SuppressLint("StaticFieldLeak")
    public void deleteGroup(final String id){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                groupMessageDao.deleteMessagesOfGroup(id);
                return null;
            }
        }.execute();
    }
    @SuppressLint("StaticFieldLeak")
    public void deleteAllGroupsAndMessages(){
        new AsyncTask<Void,Void,Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                groupMessageDao.deleteAllGroupMessages();
                return null;
            }
        }.execute();
    }

    public LiveData<List<GroupMessageTable>> getUnseenMessages(String groupId){
        return groupMessageDao.getUnseenMessages(groupId);
    }






}
