package com.example.android.tobefocused.ui.detail;

import com.example.android.tobefocused.data.TaskRepository;
import com.example.android.tobefocused.data.database.Task;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class DetailViewModel extends ViewModel {
    private TaskRepository mTaskRepository;

    public DetailViewModel(TaskRepository repository) {
        mTaskRepository = repository;
    }

    public LiveData<Task> getTask(int id) {
        return mTaskRepository.getTask(id);
    }

    public void deleteTask(Task task) {
        mTaskRepository.deleteTask(task);
    }

    public void addTask(Task task) {
        mTaskRepository.addTask(task);
    }

    public void updateTask(Task task) {
        mTaskRepository.updateTask(task);
    }
}
