package com.example.android.tobefocused.ui.detail;

import com.example.android.tobefocused.data.TaskRepository;
import com.example.android.tobefocused.data.database.TaskEntity;

import java.util.Date;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DetailViewModel extends ViewModel {
    private TaskRepository mTaskRepository;

    public DetailViewModel(TaskRepository repository) {
        mTaskRepository = repository;
    }

    public LiveData<TaskEntity> getTask(int id) {
        return mTaskRepository.getTask(id);
    }

    public void deleteTask(TaskEntity taskEntity) {
        mTaskRepository.deleteTask(taskEntity);
    }

    public void addTask(TaskEntity taskEntity) {
        mTaskRepository.addTask(taskEntity);
    }

    public void updateTask(TaskEntity taskEntity) {
        mTaskRepository.updateTask(taskEntity);
    }
}
