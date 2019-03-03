package com.example.android.tobefocused.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Toast;

import com.example.android.tobefocused.R;
import com.example.android.tobefocused.data.database.Task;
import com.example.android.tobefocused.databinding.ActivityMainBinding;
import com.example.android.tobefocused.ui.detail.TaskDetailActivity;
import com.google.android.material.navigation.NavigationView;

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
    private TasksAdapter tasksAdapter = new TasksAdapter();
    private MainViewModel mMainViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mMainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        mMainViewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                tasksAdapter.submitList(tasks);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setupNavDrawer();
        binding.addTaskFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
                startActivity(intent);
            }
        });
        setupRecyclerView();
        addNavMenuItem();
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
            Toast.makeText(this, "All tasks cleared.", Toast.LENGTH_LONG).show();
        }

        if (drawerToggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    private void setupRecyclerView() {
        binding.taskRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.taskRecyclerView.setAdapter(tasksAdapter);
        tasksAdapter.setOnTaskClickListener(new TasksAdapter.TaskClickListener() {
            @Override
            public void onClick(Task task) {
                Intent intent = new Intent(MainActivity.this, TaskDetailActivity.class);
                intent.putExtra("taskId", task.getId());
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
                Task task = tasksAdapter.getTaskAtPosition(position);
                if (direction == 4) {
                    Toast.makeText(MainActivity.this,
                            task.getTitle() + " Deleted"
                            , Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this,
                            task.getTitle() + " Finished"
                            , Toast.LENGTH_LONG).show();
                }
                mMainViewModel.deleteTask(task);
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
                    case R.id.today_action:
                        binding.listName.setText("Today");
                        binding.drawerLayout.closeDrawers();
                        break;
                    case R.id.settings_action:
                        binding.listName.setText("Settings");
                        binding.drawerLayout.closeDrawers();
                        break;
                    default:
                        break;
                }
                return true;
            }
        });
    }

    private void addNavMenuItem() {
        Menu menu = binding.navigationView.getMenu();
        SubMenu topChannelMenu = menu.addSubMenu("Top Channels");
        topChannelMenu.add("One");
        topChannelMenu.add("Two");
        topChannelMenu.add("Three");
        MenuItem menuItem = menu.getItem(menu.size() - 1);
        menuItem.setTitle(menuItem.getTitle());
    }

}
