package com.example.androidmanifestation;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.AndroidViewModel;
import android.util.Log;

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
