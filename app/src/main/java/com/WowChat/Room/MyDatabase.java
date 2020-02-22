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

@Database(entities = {UserInfoTable.class, MessageTable.class, GroupTable.class, GroupMessageTable.class},version=28,exportSchema = false)
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
    static  final Migration MIGRATION__24_25=new Migration(24,25) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE GroupMessageTable" +
                    " ADD event TEXT DEFAULT('')");
        }
    };
    static  final Migration MIGRATION__25_28=new Migration(25,28) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE UserInfoTable" +
                    " ADD isGroup INTEGER DEFAULT '0' ");
        }
    };
    static  final Migration MIGRATION__26_28=new Migration(26,28) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE UserInfoTable" +
                    " ADD isGroup INTEGER DEFAULT '0' ");
        }
    };
    static  final Migration MIGRATION__27_28=new Migration(27,28) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE UserInfoTable" +
                    " ADD isGroup INTEGER DEFAULT '0' ");
        }
    };
    public static synchronized MyDatabase getInstance(Context context){
        if(instance==null){
            instance= Room.databaseBuilder(context.getApplicationContext(),
                        MyDatabase.class,"my_database")
                    .addMigrations(MIGRATION_22_24,MIGRATION__24_25,MIGRATION__25_28,MIGRATION__26_28,MIGRATION__27_28)
                    .build();
        }
        return instance;
    }
}
