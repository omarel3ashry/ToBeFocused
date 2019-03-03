package com.example.android.tobefocused.data;

import android.app.Application;
import android.os.AsyncTask;

import com.example.android.tobefocused.AppExecutors;
import com.example.android.tobefocused.data.database.Task;
import com.example.android.tobefocused.data.database.TaskDao;
import com.example.android.tobefocused.data.database.AppDatabase;

import java.util.List;

import androidx.lifecycle.LiveData;

public class TaskRepository {

    private static final Object LOCK = new Object();
    private TaskDao mTaskDao;
    private AppExecutors mExecutors;
    private static TaskRepository sInstance;

    private TaskRepository(TaskDao taskDao, AppExecutors executors) {
        mTaskDao = taskDao;
        mExecutors = executors;
    }

    public synchronized static TaskRepository getInstance(TaskDao taskDao, AppExecutors executors) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new TaskRepository(taskDao, executors);
            }
        }
        return sInstance;
    }

    public LiveData<List<Task>> getAllTasks() {
        return mTaskDao.getAllTasks();
    }

    public LiveData<Task> getTask(final int id) {
        return mTaskDao.getTaskById(id);
    }

    public void addTask(final Task task) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mTaskDao.addTask(task);
            }
        });
    }

    public void updateTask(final Task task) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mTaskDao.updateTask(task);
            }
        });
    }

    public void deleteTask(final Task task) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mTaskDao.deleteTask(task);
            }
        });
    }

    public void deleteAllTasks() {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mTaskDao.deleteAllTasks();
            }
        });
    }
}
