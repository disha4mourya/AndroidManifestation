package com.example.androidmanifestation;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

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

    public static final String SAVED_TASK_ID="savedTaskId";
    private int taskId=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mDb = AppDatabase.getsInstance(getApplicationContext());
        initialSetup();
        setClickListenerOnAddTask();
        getIntentData();
    }

    private void getIntentData() {
        Intent intent=getIntent();
        if (intent!=null&&intent.hasExtra(SAVED_TASK_ID)){
            taskId=intent.getIntExtra(SAVED_TASK_ID,-1);
            AppExecutors.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    final TaskEntity taskEntity=mDb.taskDao().loadTaskById(taskId);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            populateUI(taskEntity);
                        }
                    });
                }
            });
        }
    }

    private void populateUI(TaskEntity taskEntity) {
        if (taskEntity==null){ return;}
        edtTaskDescription.setText(taskEntity.getDescription());
         setPriorityInViews(taskEntity.getPriority());
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
                mDb.taskDao().insertTask(taskEntity);
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
