package com.example.androidmanifestation;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * TaskAdapter creates and binds ViewHolders, that hold the description, created time and priority od a task,
 * to a RecyclerView to efficiently display data.
 */
public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    private static final String DATE_FORMAT="dd/MM/yyyy";
    final private TaskClickListener mTaskClickListener;
    private Context mContext;
    private SimpleDateFormat simpleDateFormat=new SimpleDateFormat(DATE_FORMAT, Locale.getDefault());

    TaskAdapter(Context context,TaskClickListener taskClickListener){
        mContext=context;
        mTaskClickListener=taskClickListener;
    }
    @Override
    public TaskViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(mContext)
                .inflate(R.layout.task_row,viewGroup,false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder taskViewHolder, int i) {

        taskViewHolder.tvDescription.setText("");
        taskViewHolder.tvCreatedTime.setText("");
        taskViewHolder.tvPriority.setText("");
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public interface TaskClickListener{
        void onTaskClickListener(int taskId);
    }
    class TaskViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvDescription;
        TextView tvCreatedTime;
        TextView tvPriority;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDescription=itemView.findViewById(R.id.tvDescription);
            tvCreatedTime=itemView.findViewById(R.id.tvCreatedTime);
            tvPriority=itemView.findViewById(R.id.tvPriority);
        }

        @Override
        public void onClick(View v) {

        }
    }
}
