package com.example.android.tobefocused.util;

import android.content.Context;

import com.example.android.tobefocused.AppExecutors;
import com.example.android.tobefocused.data.TaskRepository;
import com.example.android.tobefocused.data.database.AppDatabase;
import com.example.android.tobefocused.ui.detail.DetailViewModelFactory;

public class Injector {
    public static TaskRepository provideTaskRepository(Context context) {
        AppDatabase database = AppDatabase.getInstance(context.getApplicationContext());
        AppExecutors executors = AppExecutors.getInstance();
        return TaskRepository.getInstance(database.taskDao(), executors);
    }

    public static DetailViewModelFactory provideDetailViewModelFactory(Context context) {
        TaskRepository repository = provideTaskRepository(context);
        return new DetailViewModelFactory(repository);
    }
}
