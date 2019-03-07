package com.example.android.tobefocused.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

import com.example.android.tobefocused.data.database.AppDatabase;
import com.example.android.tobefocused.data.database.TaskEntity;
import com.example.android.tobefocused.widget.TasksRemoteViewsService;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import java.util.List;

public class ScheduledJobService extends JobService {
    private AsyncTask mBackgroundTask;
    private static List<TaskEntity> taskEntities;

    @SuppressWarnings("unchecked")
    @SuppressLint("StaticFieldLeak")
    @Override
    public boolean onStartJob(final JobParameters job) {

        mBackgroundTask = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] objects) {
                Context context = ScheduledJobService.this;
                AppDatabase db = AppDatabase.getInstance(context);
                taskEntities = db.taskDao().getAllTasksAsync();
                TasksRemoteViewsService.updateWidget(context, taskEntities);
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                jobFinished(job, false);
            }
        };
        mBackgroundTask.execute();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        if (mBackgroundTask != null)
            mBackgroundTask.cancel(true);
        return true;
    }
}
