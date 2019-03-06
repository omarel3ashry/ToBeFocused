package com.example.android.tobefocused.data.database;

import android.content.Context;
import android.os.AsyncTask;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {TaskEntity.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();

    private static volatile AppDatabase INSTANCE;
    public static final String DATABASE_NAME = "task_database";

    public static AppDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder
                            (context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                            .addCallback(sRoomDbCallback).build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDbCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INSTANCE).execute();
        }
    };

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final TaskDao mDao;

        PopulateDbAsync(AppDatabase db) {
            mDao = db.taskDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mDao.deleteAllTasks();
            TaskEntity taskEntity = new TaskEntity();
            taskEntity.setTitle("Hey");
            taskEntity.setDate(new Date());
            taskEntity.setTaskList(2);
            mDao.addTask(taskEntity);
            taskEntity = new TaskEntity();
            taskEntity.setTitle("Lol");
            taskEntity.setDate(new Date());
            taskEntity.setTaskList(3);
            mDao.addTask(taskEntity);
            return null;
        }
    }
}
