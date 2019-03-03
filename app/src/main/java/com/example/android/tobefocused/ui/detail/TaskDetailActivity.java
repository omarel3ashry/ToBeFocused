package com.example.android.tobefocused.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;

import com.example.android.tobefocused.R;
import com.example.android.tobefocused.data.database.Task;
import com.example.android.tobefocused.databinding.ActivityTaskDetailBinding;
import com.example.android.tobefocused.util.DatePickerFragment;
import com.example.android.tobefocused.util.Injector;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class TaskDetailActivity extends AppCompatActivity {
    public static final String EXTRA_TASK_ID = "extraTaskId";
    private static final int DEFAULT_TASK_ID = -1;
    private ActivityTaskDetailBinding binding;
    private DetailViewModel mDetailViewModel;
    private int taskId = DEFAULT_TASK_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_detail);
        binding.setDateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        mDetailViewModel = obtainViewModel();
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("taskId")) {
            taskId = intent.getIntExtra("taskId", -1);
            binding.addTaskButton.setText(R.string.update);
            mDetailViewModel.getTask(taskId).observe(this, new Observer<Task>() {
                @Override
                public void onChanged(Task task) {
                    mDetailViewModel.getTask(taskId).removeObserver(this);
                    populateUI(task);
                }
            });

        }
        binding.addDetailCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    binding.detailsEditText.setVisibility(View.VISIBLE);
                else {
                    binding.detailsEditText.getText().clear();
                    binding.detailsEditText.setVisibility(View.GONE);
                }
            }
        });
        binding.addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String taskTitle = binding.titleEditText.getText().toString();
                Task task = new Task();
                task.setTitle(taskTitle);
                if (taskId == DEFAULT_TASK_ID) {
                    {
                        mDetailViewModel.addTask(task);
                    }
                } else {
                    task.setId(taskId);
                    mDetailViewModel.updateTask(task);
                }
                finish();
            }
        });
    }

    private void showDatePicker() {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "date picker");
    }

    private DetailViewModel obtainViewModel() {
        DetailViewModelFactory viewModelFactory = Injector.provideDetailViewModelFactory(this);
        return ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel.class);
    }

    private void populateUI(Task task) {
        if (task == null) {
            return;
        }
        binding.titleEditText.setText(task.getTitle());
    }
}
