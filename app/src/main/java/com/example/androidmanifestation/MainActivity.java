package com.example.androidmanifestation;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private RecyclerView rvTasks;
    private FloatingActionButton fabAddTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialSetup();
        setFabClickListener();
    }

    private void setFabClickListener() {
        fabAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addTaskIntent=new Intent(MainActivity.this,AddTaskActivity.class);
                startActivity(addTaskIntent);
            }
        });
    }

    private void initialSetup() {
        rvTasks=findViewById(R.id.rvTasks);
        fabAddTask=findViewById(R.id.fabAddTask);
    }
}
