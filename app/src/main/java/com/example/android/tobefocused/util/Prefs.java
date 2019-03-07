package com.example.android.tobefocused.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.android.tobefocused.R;
import com.example.android.tobefocused.data.database.TaskEntity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class Prefs {
    public static void saveTasks(Context context, List<TaskEntity> tasks) {
        Gson gson = new Gson();
        String taskJsonString = gson.toJson(tasks);
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.task_string), taskJsonString);
        editor.apply();
    }

    public static List<TaskEntity> loadTasks(Context context) {
        Gson gson = new Gson();
        Type type = new TypeToken<List<TaskEntity>>() {
        }.getType();
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.pref), Context.MODE_PRIVATE);
        String taskJsonString = sharedPreferences.getString(context.getString(R.string.task_string), "");
        return gson.fromJson(taskJsonString, type);
    }
}
