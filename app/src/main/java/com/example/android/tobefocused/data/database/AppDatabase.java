package com.example.android.tobefocused.data.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Task.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract TaskDao taskDao();

    private static volatile AppDatabase INCTANCE;
    public static final String DATABASE_NAME = "task_database";

    public static AppDatabase getInstance(final Context context) {
        if (INCTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INCTANCE == null) {
                    INCTANCE = Room.databaseBuilder
                            (context.getApplicationContext(), AppDatabase.class, DATABASE_NAME)
                            .addCallback(sRoomDbCallback).build();
                }
            }
        }
        return INCTANCE;
    }

    private static RoomDatabase.Callback sRoomDbCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsync(INCTANCE).execute();
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
            Task task = new Task();
            task.setTitle("Hey");
            mDao.addTask(task);
            task = new Task();
            task.setTitle("Lol");
            mDao.addTask(task);
            return null;
        }
    }
}
