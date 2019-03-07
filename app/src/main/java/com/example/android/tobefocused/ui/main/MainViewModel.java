package com.example.android.tobefocused.ui.main;

import android.app.Application;

import com.example.android.tobefocused.AppExecutors;
import com.example.android.tobefocused.data.TaskRepository;
import com.example.android.tobefocused.data.database.AppDatabase;
import com.example.android.tobefocused.data.database.TaskEntity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MainViewModel extends AndroidViewModel {
    private TaskRepository mTaskRepository;
    private LiveData<List<TaskEntity>> mAllTasks;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDatabase = AppDatabase.getInstance(application);
        AppExecutors executors = AppExecutors.getInstance();
        mTaskRepository = TaskRepository.getInstance(appDatabase.taskDao(), executors);
        mAllTasks = mTaskRepository.getAllTasks();
    }


    public LiveData<List<TaskEntity>> getAllTasks() {
        return mAllTasks;
    }

    public void deleteTask(TaskEntity taskEntity) {
        mTaskRepository.deleteTask(taskEntity);
    }

    public void deleteAllTasks() {
        mTaskRepository.deleteAllTasks();
    }
}
