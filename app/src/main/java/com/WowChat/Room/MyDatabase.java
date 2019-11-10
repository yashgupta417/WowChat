package com.WowChat.Room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.WowChat.Room.DAOs.UserInfoDao;
import com.WowChat.Room.DAOs.MessageDao;
import com.WowChat.Room.Entities.MessageTable;
import com.WowChat.Room.Entities.UserInfoTable;

@Database(entities = {UserInfoTable.class, MessageTable.class},version=22,exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    private static com.WowChat.Room.MyDatabase instance;
    public abstract UserInfoDao chatDao();
    public abstract MessageDao messageDao();

    public static synchronized com.WowChat.Room.MyDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),
                        com.WowChat.Room.MyDatabase.class,"my_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}
