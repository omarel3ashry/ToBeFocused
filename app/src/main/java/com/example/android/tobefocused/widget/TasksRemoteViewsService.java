package com.example.android.tobefocused.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.tobefocused.R;
import com.example.android.tobefocused.data.database.TaskEntity;
import com.example.android.tobefocused.util.Prefs;

import java.util.List;

public class TasksRemoteViewsService extends RemoteViewsService {
    public static void updateWidget(Context context, List<TaskEntity> tasks) {
        Prefs.saveTasks(context, tasks);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context, TaskProviderWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list_view);
        TaskProviderWidget.updateAppWidgets(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        return new TasksRemoteViewsFactory(getApplicationContext());
    }

    class TasksRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
        private Context mContext;
        private List<TaskEntity> mTaskList;

        TasksRemoteViewsFactory(Context context) {
            mContext = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            mTaskList = Prefs.loadTasks(mContext);
        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (mTaskList == null || mTaskList.isEmpty())
                return 0;
            else
                return mTaskList.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
            TaskEntity taskEntity = mTaskList.get(position);
            remoteViews.setTextViewText(R.id.list_view_text_item, taskEntity.getTitle());
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
