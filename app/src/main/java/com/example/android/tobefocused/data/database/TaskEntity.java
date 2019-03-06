package com.example.android.tobefocused.data.database;

import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "task_table")
public class TaskEntity {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private Date date;
    private int taskList;
    @Ignore
    private String dateFormatted;

    public String getDateFormatted() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        if (date != null && !TextUtils.isEmpty(date.toString()))
            return formatter.format(date);
        else return "";
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getTaskList() {
        return taskList;
    }

    public void setTaskList(int taskList) {
        this.taskList = taskList;
    }
}
