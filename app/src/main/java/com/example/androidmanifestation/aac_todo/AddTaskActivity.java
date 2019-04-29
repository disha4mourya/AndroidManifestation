package com.example.androidmanifestation.aac_todo;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.androidmanifestation.R;
import com.example.androidmanifestation.database.AppDatabase;
import com.example.androidmanifestation.database.TaskEntity;

import java.util.Date;

public class AddTaskActivity extends AppCompatActivity {

    EditText edtTaskDescription;
    RadioGroup rgTaskPriority;
    Button btnAddTask;

    public AppDatabase mDb;
    public static final int PRIORITY_HIGH = 1;
    public static final int PRIORITY_MEDIUM = 2;
    public static final int PRIORITY_LOW = 3;

    public static final String SAVED_TASK_ID = "savedTaskId";
    private static final int DEFAULT_TASK_ID = -1;

    private int taskId = DEFAULT_TASK_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mDb = AppDatabase.getsInstance(getApplicationContext());
        if (savedInstanceState != null && savedInstanceState.containsKey(SAVED_TASK_ID)) {
            taskId = savedInstanceState.getInt(SAVED_TASK_ID, DEFAULT_TASK_ID);
        }
        initialSetup();
        setClickListenerOnAddTask();
        getIntentData();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(SAVED_TASK_ID, taskId);
        super.onSaveInstanceState(outState);
    }

    private void getIntentData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(SAVED_TASK_ID)) {
            taskId = intent.getIntExtra(SAVED_TASK_ID, DEFAULT_TASK_ID);

            AddTaskViewModelFactory addTaskViewModelFactory = new AddTaskViewModelFactory(mDb, taskId);
            final AddTaskViewModel addTaskViewModel = ViewModelProviders.of(this, addTaskViewModelFactory).get(AddTaskViewModel.class);
            addTaskViewModel.getTaskById().observe(this, new Observer<TaskEntity>() {
                @Override
                public void onChanged(@Nullable TaskEntity taskEntity) {
                    addTaskViewModel.getTaskById().removeObserver(this);
                    populateUI(taskEntity);
                }
            });
        }
    }

    private void populateUI(TaskEntity taskEntity) {
        if (taskEntity == null) {
            return;
        }
        edtTaskDescription.setText(taskEntity.getDescription());
        setPriorityInViews(taskEntity.getPriority());
        btnAddTask.setText(this.getString(R.string.update_task));
    }

    public void setPriorityInViews(int priority) {
        switch (priority) {
            case PRIORITY_HIGH:
                ((RadioGroup) findViewById(R.id.rgPriority)).check(R.id.rbHigh);
                break;
            case PRIORITY_MEDIUM:
                ((RadioGroup) findViewById(R.id.rgPriority)).check(R.id.rbMedium);
                break;
            case PRIORITY_LOW:
                ((RadioGroup) findViewById(R.id.rgPriority)).check(R.id.rbLow);
        }
    }

    private void setClickListenerOnAddTask() {
        btnAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveTask();
            }
        });
    }

    private void saveTask() {
        String description = edtTaskDescription.getText().toString();
        int priority = getPriorityFromViews();
        Date date = new Date();

        final TaskEntity taskEntity = new TaskEntity(description, priority, date);
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (taskId == DEFAULT_TASK_ID) {
                    mDb.taskDao().insertTask(taskEntity);
                } else {
                    taskEntity.setId(taskId);
                    mDb.taskDao().updateTask(taskEntity);
                }
            }
        });

        finish();
    }

    public int getPriorityFromViews() {
        int priority = PRIORITY_HIGH;
        int checkedId = ((RadioGroup) findViewById(R.id.rgPriority)).getCheckedRadioButtonId();
        switch (checkedId) {
            case R.id.rbMedium:
                priority = PRIORITY_MEDIUM;
                break;
            case R.id.rbLow:
                priority = PRIORITY_LOW;
        }
        return priority;
    }

    private void initialSetup() {
        edtTaskDescription = findViewById(R.id.edtTaskDescription);
        rgTaskPriority = findViewById(R.id.rgPriority);
        btnAddTask = findViewById(R.id.btnAddTask);
    }
}
