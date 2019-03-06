package com.example.android.tobefocused.data.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static TasksService tasksService;

    public static TasksService getTasksService() {
        if (tasksService != null)
            return tasksService;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/tasks/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        tasksService = retrofit.create(TasksService.class);
        return tasksService;
    }

}
