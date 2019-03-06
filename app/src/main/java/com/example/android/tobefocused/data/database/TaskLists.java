package com.example.android.tobefocused.data.database;

import com.google.api.services.tasks.model.TaskList;

import java.util.List;

public class TaskLists {

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public List<TaskList> getItems() {
        return items;
    }

    public void setItems(List<TaskList> items) {
        this.items = items;
    }

    private String etag;
    private String kind;
    private List<TaskList> items;
}
