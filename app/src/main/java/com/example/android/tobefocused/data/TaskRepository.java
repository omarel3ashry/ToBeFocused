package com.example.android.tobefocused.data;

import com.example.android.tobefocused.AppExecutors;
import com.example.android.tobefocused.data.database.TaskEntity;
import com.example.android.tobefocused.data.database.TaskDao;

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

    public LiveData<List<TaskEntity>> getAllTasks() {
        return mTaskDao.getAllTasks();
    }

    public LiveData<TaskEntity> getTask(final int id) {
        return mTaskDao.getTaskById(id);
    }

    public void addTask(final TaskEntity taskEntity) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mTaskDao.addTask(taskEntity);
            }
        });
    }

    public void updateTask(final TaskEntity taskEntity) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mTaskDao.updateTask(taskEntity);
            }
        });
    }

    public void deleteTask(final TaskEntity taskEntity) {
        mExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {
                mTaskDao.deleteTask(taskEntity);
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
