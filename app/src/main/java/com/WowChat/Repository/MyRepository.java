package com.WowChat.Repository;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Update;

import com.WowChat.Room.DAOs.UserInfoDao;
import com.WowChat.Room.DAOs.MessageDao;
import com.WowChat.Room.Entities.MessageTable;
import com.WowChat.Room.Entities.UserInfoTable;
import com.WowChat.Room.MyDatabase;

import java.util.List;

public class MyRepository {
    private UserInfoDao userInfoDao;
    private MessageDao messageDao;

    public MyRepository(Application application){
        MyDatabase database=MyDatabase.getInstance(application);
        userInfoDao =database.chatDao();
        messageDao=database.messageDao();

    }
    public LiveData<List<MessageTable>> getMessages(String friend){
        return messageDao.getMessages(friend);
    }
    public LiveData<List<MessageTable>> getUnseenMessages(String friend){
        return messageDao.getUnseenMessages(friend,"Sent");
    }
    public LiveData<List<UserInfoTable>> getAllChats()
    {
        return userInfoDao.getAllChats();

    }
//    public int countUnseenMessages(String username){
//        return messageDao.countUnseenMessages(username);
//    }
    public void updateOrCreateUserInfo(UserInfoTable userInfoTable){
        new UpdateOrCreateUserInfoAsyncTask(userInfoDao).execute(userInfoTable);

    }

    public void insertMessage(MessageTable messageTable){
        new InsertMessageAsyncTask(messageDao).execute(messageTable);
    }
    public void updateMessage(MessageTable messageTable){
        new UpdateMessageAsyncTask(messageDao).execute(messageTable);
    }
    public void updateMessageStatus(String messageid,String status){
        new UpdateMessageStatusAsyncTask(messageDao,messageid,status).execute();
    }
    public void deleteMessagesAndUserInfo(){
        new DeleteMessagesAndUserInfoAsyncTask(userInfoDao,messageDao).execute();
    }
    public void setUnseenCount(String friendId){
        new setUnseenCountAsyncTask(userInfoDao,messageDao,friendId).execute();
    }
//    public void incrementUnseenCount(String username){
//        new IncrementUnseenCountAsyncTask(userInfoDao,username).execute();
//    }
//    public void decrementUnseenCount(String username){
//        new DecrementUnseenCountAsyncTask(userInfoDao,username).execute();
//    }
    private static class InsertMessageAsyncTask extends AsyncTask<MessageTable,Void,Void>{
        private MessageDao messageDao;

        public InsertMessageAsyncTask(MessageDao messageDao) {
            this.messageDao = messageDao;
        }

        @Override
        protected Void doInBackground(MessageTable... messageTables) {
            messageDao.insert(messageTables[0]);
            return null;
        }
    }

    private static class UpdateMessageAsyncTask extends AsyncTask<MessageTable,Void,Void>{
        private MessageDao messageDao;

        public UpdateMessageAsyncTask(MessageDao messageDao) {
            this.messageDao = messageDao;
        }

        @Override
        protected Void doInBackground(MessageTable... messageTables) {
            messageDao.update(messageTables[0]);
            return null;
        }
    }
    private static class setUnseenCountAsyncTask extends AsyncTask<Void,Void,Void>{
        private UserInfoDao userInfoDao;
        private String friendId;
        private MessageDao messageDao;

        public setUnseenCountAsyncTask(UserInfoDao userInfoDao,MessageDao messageDao,String friendId) {
            this.userInfoDao = userInfoDao;
            this.messageDao=messageDao;
            this.friendId=friendId;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            Integer count=messageDao.countUnseenMessages(friendId);
            Log.i("****count",Integer.toString(count));
            userInfoDao.setUnseenCount(count,friendId);
            return null;
        }
    }

    private static class UpdateMessageStatusAsyncTask extends AsyncTask<Void,Void,Void>{
        private MessageDao messageDao;
        private String messageid;
        private String status;

        public UpdateMessageStatusAsyncTask(MessageDao messageDao,String messageId,String status) {
            this.messageDao = messageDao;
            this.messageid=messageId;
            this.status=status;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            messageDao.updateMessageStatus(messageid,status);
            return null;
        }

    }
    private static class UpdateOrCreateUserInfoAsyncTask extends AsyncTask<UserInfoTable,Void,Void>{
        private UserInfoDao userInfoDao;

        public UpdateOrCreateUserInfoAsyncTask(UserInfoDao userInfoDao) {
            this.userInfoDao = userInfoDao;
        }

        @Override
        protected Void doInBackground(UserInfoTable... userInfoTables) {
            UserInfoTable userInfoTable=userInfoTables[0];
            String username=userInfoTable.getPersonUsername();
            String firstName=userInfoTable.getPersonFirstName();
            String lastName=userInfoTable.getPersonLastName();
            String email=userInfoTable.getPersonEmail();
            String image=userInfoTable.getPersonImage();
            String id=userInfoTable.getPersonId();
            String latestMessage=userInfoTable.getLatestMesage();
            String latestMessageTime=userInfoTable.getLatestMessageTime();

            userInfoDao.updateOrCreateUserInfo(username,firstName,lastName,email,image,id,latestMessage,latestMessageTime);
            return  null;
        }
    }

    private static class DeleteMessagesAndUserInfoAsyncTask extends AsyncTask<Void,Void,Void>{
        private UserInfoDao userInfoDao;
        private MessageDao messageDao;

        public DeleteMessagesAndUserInfoAsyncTask(UserInfoDao userInfoDao, MessageDao messageDao) {
            this.userInfoDao = userInfoDao;
            this.messageDao = messageDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userInfoDao.deleteAllUserInfo();
            messageDao.deleteAllMessages();
            return null;
        }
    }

}