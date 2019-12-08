package com.WowChat.Room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.WowChat.Room.DAOs.GroupDao;
import com.WowChat.Room.DAOs.GroupMessageDao;
import com.WowChat.Room.DAOs.UserInfoDao;
import com.WowChat.Room.DAOs.MessageDao;
import com.WowChat.Room.Entities.GroupMessageTable;
import com.WowChat.Room.Entities.GroupTable;
import com.WowChat.Room.Entities.MessageTable;
import com.WowChat.Room.Entities.UserInfoTable;

@Database(entities = {UserInfoTable.class, MessageTable.class, GroupTable.class, GroupMessageTable.class},version=24,exportSchema = false)
public abstract class MyDatabase extends RoomDatabase {
    private static MyDatabase instance;
    public abstract UserInfoDao chatDao();
    public abstract MessageDao messageDao();
    public abstract GroupDao groupDao();
    public abstract GroupMessageDao groupMessageDao();
    static final Migration MIGRATION_22_24 = new Migration(22,24) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE GroupTable (id TEXT NOT NULL, name TEXT, image TEXT, unseenMessages INTEGER, PRIMARY KEY(id))");
            database.execSQL("CREATE TABLE GroupMessageTable (id TEXT NOT NULL, text TEXT, image TEXT, senderId TEXT, senderName TEXT, senderImage TEXT" +
                    ", group_id TEXT , dateofmessaging TEXT ,timeofmessaging TEXT , AMorPM TEXT ,status TEXT, PRIMARY KEY(id))");
        }
    };
    public static synchronized MyDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),
                        MyDatabase.class,"my_database")
                    .addMigrations(MIGRATION_22_24)
                    .build();
        }
        return instance;
    }
}
