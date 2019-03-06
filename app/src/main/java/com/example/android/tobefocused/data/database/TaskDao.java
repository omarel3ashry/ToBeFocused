package com.example.android.tobefocused.data.database;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


@Dao
public interface TaskDao {

    @Insert
    void addTask(TaskEntity taskEntity);

    @Query("SELECT * FROM task_table")
    LiveData<List<TaskEntity>> getAllTasks();

    @Query("SELECT * FROM task_table WHERE id = :id")
    LiveData<TaskEntity> getTaskById(int id);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(TaskEntity taskEntity);

    @Delete
    void deleteTask(TaskEntity taskEntity);

    @Query("DELETE FROM task_table")
    void deleteAllTasks();
}
