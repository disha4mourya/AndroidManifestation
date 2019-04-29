package com.example.androidmanifestation.aac_todo;


import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.androidmanifestation.database.AppDatabase;
import com.example.androidmanifestation.database.TaskEntity;

public class AddTaskViewModel extends ViewModel {

    private LiveData<TaskEntity> taskList;

    AddTaskViewModel(AppDatabase appDatabase,int taskId){
        taskList=appDatabase.taskDao().loadTaskById(taskId);
    }

    public LiveData<TaskEntity> getTaskById(){return taskList;}
}
