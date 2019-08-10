package com.WowChat.ViewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.WowChat.Repository.MyRepository;
import com.WowChat.Room.Entities.MessageTable;
import com.WowChat.Room.Entities.UserInfoTable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ChatActivityViewModel extends AndroidViewModel {
    private com.WowChat.Repository.MyRepository myRepository;

    public ChatActivityViewModel(@NonNull Application application) {
        super(application);
        myRepository = new MyRepository(application);
    }

    public LiveData<List<MessageTable>> getMessages(String friend){
        return myRepository.getMessages(friend);

    }
    public LiveData<List<MessageTable>> getUnseenMessages(String friend){
        return myRepository.getUnseenMessages(friend);

    }
    public void insertMessage(MessageTable messageTable){


        messageTable.setStatus("sending");
        myRepository.insertMessage(messageTable);
    }
    public void updateMessage(MessageTable messageTable){

        myRepository.updateMessage(messageTable);
    }


    public void updateOrCreateUserInfo(UserInfoTable userInfoTable){
        myRepository.updateOrCreateUserInfo(userInfoTable);
    }
}
