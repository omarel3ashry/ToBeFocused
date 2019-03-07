package com.example.android.tobefocused.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.android.tobefocused.R;
import com.example.android.tobefocused.data.database.TaskEntity;
import com.example.android.tobefocused.databinding.ActivityMainBinding;
import com.example.android.tobefocused.ui.detail.TaskDetailActivity;
import com.example.android.tobefocused.util.Reminder;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ActionBarDrawerToggle drawerToggle;
    private TasksAdapter tasksAdapter1 = new TasksAdapter();
    private TasksAdapter tasksAdapter2 = new TasksAdapter();
    private MainViewModel mMainViewModel;
    private List<TaskEntity> mAllTaskEntities = new ArrayList<>();
    private List<TaskEntity> mTasksList = new ArrayList<>();
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.getAllTasks().observe(this, new Observer<List<TaskEntity>>() {
            @Override
            public void onChanged(List<TaskEntity> taskEntities) {
                tasksAdapter1.submitList(taskEntities);
                mAllTaskEntities = taskEntities;
            }
        });
        setSupportActionBar(binding.mainToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupNavDrawer();
        binding.addTaskFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
                startActivity(intent);
            }
        });
        setupRecyclerView(tasksAdapter1);

        Reminder.scheduleUpdateWidgetReminder(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_all_action) {
            mMainViewModel.deleteAllTasks();
            Toast.makeText(this, R.string.all_tasks_cleared, Toast.LENGTH_LONG).show();
        }

        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView(final TasksAdapter tasksAdapter) {
        binding.taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.taskRecyclerView.setAdapter(tasksAdapter);
        tasksAdapter.setOnTaskClickListener(new TasksAdapter.TaskClickListener() {
            @Override
            public void onClick(TaskEntity taskEntity) {
                Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
                intent.putExtra(TaskDetailActivity.EXTRA_TASK_ID, taskEntity.getId());
                startActivity(intent);
            }
        });
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback
                (0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(
                    @NonNull RecyclerView recyclerView,
                    @NonNull RecyclerView.ViewHolder viewHolder,
                    @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(
                    @NonNull RecyclerView.ViewHolder viewHolder,
                    int direction) {
                int position = viewHolder.getAdapterPosition();
                TaskEntity taskEntity = tasksAdapter.getTaskAtPosition(position);
                if (direction == 4) {
                    Toast.makeText(MainActivity.this,
                            taskEntity.getTitle() + getString(R.string.deleted)
                            , Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this,
                            taskEntity.getTitle() + getString(R.string.finished)
                            , Toast.LENGTH_LONG).show();
                }
                mMainViewModel.deleteTask(taskEntity);
            }
        });
        helper.attachToRecyclerView(binding.taskRecyclerView);
    }


    private void setupNavDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close);
        binding.drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                int id = menuItem.getItemId();
                switch (id) {
                    case R.id.all_tasks:
                        binding.listName.setText(getString(R.string.all_tasks));
                        setupRecyclerView(tasksAdapter1);
                        tasksAdapter1.notifyDataSetChanged();
                        binding.drawerLayout.closeDrawers();
                        break;
                    case R.id.index_list:
                        binding.listName.setText(getString(R.string.index_list));
                        mTasksList.clear();
                        for (int i = 0; i < mAllTaskEntities.size(); i++) {
                            if (mAllTaskEntities.get(i).getTaskList() == 0)
                                mTasksList.add(mAllTaskEntities.get(i));
                        }
                        tasksAdapter2.submitList(mTasksList);
                        setupRecyclerView(tasksAdapter2);
                        tasksAdapter2.notifyDataSetChanged();
                        binding.drawerLayout.closeDrawers();
                        break;
                    case R.id.home_list:
                        binding.listName.setText(getString(R.string.home_list));
                        mTasksList.clear();
                        for (int i = 0; i < mAllTaskEntities.size(); i++) {
                            if (mAllTaskEntities.get(i).getTaskList() == 1)
                                mTasksList.add(mAllTaskEntities.get(i));
                        }
                        tasksAdapter2.submitList(mTasksList);
                        setupRecyclerView(tasksAdapter2);
                        tasksAdapter2.notifyDataSetChanged();
                        binding.drawerLayout.closeDrawers();
                        break;
                    case R.id.work_list:
                        binding.listName.setText(getString(R.string.work_list));
                        mTasksList.clear();
                        for (int i = 0; i < mAllTaskEntities.size(); i++) {
                            if (mAllTaskEntities.get(i).getTaskList() == 2)
                                mTasksList.add(mAllTaskEntities.get(i));
                        }
                        tasksAdapter2.submitList(mTasksList);
                        setupRecyclerView(tasksAdapter2);
                        tasksAdapter2.notifyDataSetChanged();
                        binding.drawerLayout.closeDrawers();
                        break;
                    case R.id.other_list:
                        binding.listName.setText(getString(R.string.other_list));
                        mTasksList.clear();
                        for (int i = 0; i < mAllTaskEntities.size(); i++) {
                            if (mAllTaskEntities.get(i).getTaskList() == 3)
                                mTasksList.add(mAllTaskEntities.get(i));
                        }
                        tasksAdapter2.submitList(mTasksList);
                        setupRecyclerView(tasksAdapter2);
                        tasksAdapter2.notifyDataSetChanged();
                        binding.drawerLayout.closeDrawers();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }
}
