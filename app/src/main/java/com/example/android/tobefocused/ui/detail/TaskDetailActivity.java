package com.example.android.tobefocused.ui.detail;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Toast;

import com.example.android.tobefocused.R;
import com.example.android.tobefocused.data.database.TaskEntity;
import com.example.android.tobefocused.databinding.ActivityTaskDetailBinding;
import com.example.android.tobefocused.util.Injector;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class TaskDetailActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String EXTRA_TASK_ID = "extraTaskId";
    private static final int DEFAULT_TASK_ID = -1;
    private ActivityTaskDetailBinding binding;
    private DetailViewModel mDetailViewModel;
    private int taskId = DEFAULT_TASK_ID;
    public static Date mDate;
    private static final String DATE_FORMAT = "dd/MM/yyyy";
    private SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());
    private TaskEntity currentTask;
    private String formatedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_task_detail);
        mDate = null;
        binding.setDateImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        mDetailViewModel = obtainViewModel();
        setSupportActionBar(binding.toolbar);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID)) {
            taskId = intent.getIntExtra(EXTRA_TASK_ID, DEFAULT_TASK_ID);
            binding.addTaskButton.setText(R.string.update);
            mDetailViewModel.getTask(taskId).observe(this, new Observer<TaskEntity>() {
                @Override
                public void onChanged(TaskEntity taskEntity) {
                    mDetailViewModel.getTask(taskId).removeObserver(this);
                    populateUI(taskEntity);
                    currentTask = taskEntity;
                }
            });
        }
        binding.addDescriptionCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    binding.descriptionEditText.setVisibility(View.VISIBLE);
                else {
                    binding.descriptionEditText.setVisibility(View.GONE);
                }
            }
        });
        binding.addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TaskEntity taskEntity = new TaskEntity();
                int taskList = binding.taskListSpinner.getSelectedItemPosition();
                taskEntity.setTaskList(taskList);
                String taskTitle = binding.titleEditText.getText().toString();
                taskEntity.setTitle(taskTitle);
                String taskDetails = binding.descriptionEditText.getText().toString();
                taskEntity.setDescription(taskDetails);
                if (mDate != null) {
                    taskEntity.setDate(mDate);
                }
                if (taskId == DEFAULT_TASK_ID) {
                    {
                        mDetailViewModel.addTask(taskEntity);
                    }
                } else {
                    taskEntity.setId(taskId);
                    mDetailViewModel.updateTask(taskEntity);
                }
                finish();
            }
        });
        if (savedInstanceState != null && savedInstanceState.containsKey(getString(R.string.date_key))) {
            formatedDate = savedInstanceState.getString(getString(R.string.date_key));
            binding.setDateImageButton.setImageResource(R.drawable.ic_date_range_primary_24dp);
            binding.dateTextView.setVisibility(View.VISIBLE);
            binding.dateTextView.setText(formatedDate);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (formatedDate != null && !formatedDate.isEmpty())
            outState.putString(getString(R.string.date_key), formatedDate);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(EXTRA_TASK_ID))
            getMenuInflater().inflate(R.menu.task_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete_task) {
            mDetailViewModel.deleteTask(currentTask);
            Toast.makeText(this, R.string.task_cleared, Toast.LENGTH_LONG).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private DetailViewModel obtainViewModel() {
        DetailViewModelFactory viewModelFactory = Injector.provideDetailViewModelFactory(this);
        return ViewModelProviders.of(this, viewModelFactory).get(DetailViewModel.class);
    }

    private void populateUI(TaskEntity taskEntity) {
        if (taskEntity == null) {
            return;
        }
        binding.titleEditText.setText(taskEntity.getTitle());
        binding.taskListSpinner.setSelection(taskEntity.getTaskList());
        if (taskEntity.getDescription() != null && !TextUtils.isEmpty(taskEntity.getDescription())) {
            binding.descriptionEditText.setText(taskEntity.getDescription());
            binding.addDescriptionCheckBox.setChecked(true);
        }
        if (!TextUtils.isEmpty(taskEntity.getDateFormatted())) {
            binding.setDateImageButton.setImageResource(R.drawable.ic_date_range_primary_24dp);
            binding.dateTextView.setVisibility(View.VISIBLE);
            binding.dateTextView.setText(taskEntity.getDateFormatted());
        }

    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this, this, year, month, day);
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, dayOfMonth);
        mDate = c.getTime();
        formatedDate = formatter.format(mDate);
        binding.setDateImageButton.setImageResource(R.drawable.ic_date_range_primary_24dp);
        binding.dateTextView.setVisibility(View.VISIBLE);
        binding.dateTextView.setText(formatedDate);
    }

}
