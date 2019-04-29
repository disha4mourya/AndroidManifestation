package com.example.androidmanifestation;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.androidmanifestation.database.AppDatabase;
import com.example.androidmanifestation.database.TaskEntity;

import java.util.List;

public class MainViewModel extends ViewModel {

    private LiveData<List<TaskEntity>> taskList;

    public MainViewModel(Application application){
        AppDatabase appDatabase=AppDatabase.getsInstance(application);
        taskList=appDatabase.taskDao().loadAllTasks();
    }

    public LiveData<List<TaskEntity>> getTaskList()
    {
        return taskList;
    }
}
