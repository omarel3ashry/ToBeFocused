package com.example.android.tobefocused.ui.main;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.tobefocused.R;
import com.example.android.tobefocused.data.database.TaskEntity;
import com.example.android.tobefocused.databinding.TaskListItemBinding;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class TasksAdapter extends ListAdapter<TaskEntity, TasksAdapter.TaskViewHolder> {

    private TaskClickListener mTaskClickListener;

    protected TasksAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        TaskListItemBinding binding = DataBindingUtil.inflate
                (LayoutInflater.from(parent.getContext()), R.layout.task_list_item, parent, false);
        return new TaskViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    public TaskEntity getTaskAtPosition(int position) {
        return getItem(position);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        private TaskListItemBinding binding;

        TaskViewHolder(@NonNull View itemView, TaskListItemBinding binding) {
            super(itemView);
            this.binding = binding;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTaskClickListener != null) {
                        mTaskClickListener.onClick(getItem(getAdapterPosition()));
                    }
                }
            });

        }

        void bind(TaskEntity taskEntity) {
            binding.setTaskEntity(taskEntity);
            binding.executePendingBindings();
        }

    }

    private static DiffUtil.ItemCallback<TaskEntity> diffCallback = new DiffUtil.ItemCallback<TaskEntity>() {
        @Override
        public boolean areItemsTheSame(@NonNull TaskEntity oldItem, @NonNull TaskEntity newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TaskEntity oldItem, @NonNull TaskEntity newItem) {
            return oldItem.equals(newItem);
        }
    };

    public void setOnTaskClickListener(TaskClickListener taskClickListener) {
        this.mTaskClickListener = taskClickListener;
    }

    public interface TaskClickListener {
        void onClick(TaskEntity taskEntity);
    }
}
