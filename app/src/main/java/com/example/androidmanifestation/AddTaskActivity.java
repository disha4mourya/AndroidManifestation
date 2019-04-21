package com.example.androidmanifestation;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mDb = AppDatabase.getsInstance(getApplicationContext());
        initialSetup();
        setClickListenerOnAddTask();
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
