package com.WowChat.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.WowChat.Repository.MyRepository;
import com.WowChat.Room.Entities.UserInfoTable;

import java.util.List;

public class MainAcitivityViewModel extends AndroidViewModel {
    private MyRepository myRepository;

    public MainAcitivityViewModel(@NonNull Application application) {
        super(application);
        myRepository = new MyRepository(application);
    }

    public LiveData<List<UserInfoTable>> getAllChats(){
        return myRepository.getAllChats();
    }

    public void deleteMessagesAndUserInfo(){
        myRepository.deleteMessagesAndUserInfo();
    }
}
