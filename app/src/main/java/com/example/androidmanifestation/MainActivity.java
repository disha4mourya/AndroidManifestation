package com.example.androidmanifestation;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.example.androidmanifestation.database.AppDatabase;
import com.example.androidmanifestation.database.TaskEntity;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TaskAdapter.TaskClickListener {

    private RecyclerView rvTasks;
    private FloatingActionButton fabAddTask;
    private TaskAdapter taskAdapter;
    private AppDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDb = AppDatabase.getsInstance(getApplicationContext());
        initialSetup();
        setFabClickListener();
        setAdapterOnRecyclerView();
        setTouchHelperToDeleteTask();
    }

    private void setTouchHelperToDeleteTask() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int i) {
                AppExecutors.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        int position=viewHolder.getAdapterPosition();
                        List<TaskEntity> taskEntities=taskAdapter.getTasks();
                        mDb.taskDao().deleteTask(taskEntities.get(position));
                        retrieveTaskList();
                    }
                });
            }
        }).attachToRecyclerView(rvTasks);
    }

    @Override
    protected void onResume() {
        super.onResume();
        retrieveTaskList();
    }

    private void retrieveTaskList() {
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                final List<TaskEntity> taskEntityList = mDb.taskDao().loadAllTasks();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        taskAdapter.setTasks(taskEntityList);
                    }
                });
            }
        });
    }

    private void setAdapterOnRecyclerView() {
        rvTasks.setLayoutManager(new LinearLayoutManager(this));
        taskAdapter = new TaskAdapter(this, this);
        rvTasks.setAdapter(taskAdapter);
    }

    private void setFabClickListener() {
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTaskIntent = new Intent(MainActivity.this, AddTaskActivity.class);
                startActivity(addTaskIntent);
            }
        });
    }

    private void initialSetup() {
        rvTasks = findViewById(R.id.rvTasks);
        fabAddTask = findViewById(R.id.fabAddTask);
    }

    @Override
    public void onTaskClickListener(int taskId) {

    }
}
