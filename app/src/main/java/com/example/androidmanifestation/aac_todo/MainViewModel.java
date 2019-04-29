package com.example.androidmanifestation.aac_todo;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.androidmanifestation.database.AppDatabase;
import com.example.androidmanifestation.database.TaskEntity;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<TaskEntity>> taskList;

    public MainViewModel(Application application){
        super(application);
        AppDatabase appDatabase=AppDatabase.getsInstance(application);
        taskList=appDatabase.taskDao().loadAllTasks();
    }

    LiveData<List<TaskEntity>> getTaskList()
    {
        return taskList;
    }
}
