package com.example.android.tobefocused.data.network;

import com.example.android.tobefocused.data.database.TaskLists;
import com.google.api.services.tasks.model.Task;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TasksService {
    String TOKEN = "ya29.GlvEBt_5JXe0vHEeREx0YQl6vTDgWqkq79hOI_OrhHs6vIwyFCn_" +
            "-Q1r6Ywae17-VT4wBjS_Z47G2aJTKSFpKt1-8UNNFj1zoDpjVsNLAFys1CleopPV-AjQawWc";

    @GET("users/@me/lists/?access_token=" + TOKEN)
    Call<TaskLists> getTaskLists();

    @GET("lists/{listId}/tasks?access_token=" + TOKEN)
    Call<List<Task>> getTasks(@Path("listId") String listId);
}
