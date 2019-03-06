package com.example.android.tobefocused.ui.googleTasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.tobefocused.R;
import com.example.android.tobefocused.databinding.GoogleTaskListItemBinding;
import com.google.api.services.tasks.model.TaskList;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

public class GoogleTasksAdapter extends ListAdapter<TaskList, GoogleTasksAdapter.GoogleTaskViewHolder> {
    private GoogleTaskClickListener mGoogleTaskClickListener;

    protected GoogleTasksAdapter() {
        super(diffCallback);
    }

    @NonNull
    @Override
    public GoogleTaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        GoogleTaskListItemBinding binding = DataBindingUtil.inflate
                (LayoutInflater.from(parent.getContext()), R.layout.google_task_list_item, parent, false);
        return new GoogleTaskViewHolder(binding.getRoot(), binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GoogleTaskViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class GoogleTaskViewHolder extends RecyclerView.ViewHolder {
        private GoogleTaskListItemBinding binding;

        public GoogleTaskViewHolder(@NonNull View itemView, GoogleTaskListItemBinding binding) {
            super(itemView);
            this.binding = binding;
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mGoogleTaskClickListener != null) {
                        mGoogleTaskClickListener.onClick(getItem(getAdapterPosition()));
                    }
                }
            });
        }

        void bind(TaskList taskList) {
            binding.setGoogleTaskList(taskList);
            binding.executePendingBindings();
        }
    }

    private static DiffUtil.ItemCallback<TaskList> diffCallback = new DiffUtil.ItemCallback<TaskList>() {
        @Override
        public boolean areItemsTheSame(@NonNull TaskList oldItem, @NonNull TaskList newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull TaskList oldItem, @NonNull TaskList newItem) {
            return oldItem.equals(newItem);
        }
    };

    public void setOnTaskClickListener(GoogleTaskClickListener taskClickListener) {
        this.mGoogleTaskClickListener = taskClickListener;
    }

    public interface GoogleTaskClickListener {
        void onClick(TaskList taskList);
    }
}
