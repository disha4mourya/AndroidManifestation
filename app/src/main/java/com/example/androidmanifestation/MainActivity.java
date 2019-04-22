package com.example.androidmanifestation;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
        retrieveTaskList();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_server_call_options:
            {
                Intent intent=new Intent(MainActivity.this,ServerCallOptions.class);
                startActivity(intent);
            }
        }
        return true;
    }

    private void retrieveTaskList() {

        LiveData<List<TaskEntity>> tasks = mDb.taskDao().loadAllTasks();
        tasks.observe(this, new Observer<List<TaskEntity>>() {
            @Override
            public void onChanged(@Nullable List<TaskEntity> taskEntries) {
                taskAdapter.setTasks(taskEntries);
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
        Intent intent=new Intent(MainActivity.this,AddTaskActivity.class);
        intent.putExtra(AddTaskActivity.SAVED_TASK_ID,taskId);
        startActivity(intent);
    }
}
