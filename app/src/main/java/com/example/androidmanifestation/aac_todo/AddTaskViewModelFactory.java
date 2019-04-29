package com.example.androidmanifestation.aac_todo;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.androidmanifestation.database.AppDatabase;

public class AddTaskViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private final AppDatabase appDatabase;
    private final int taskId;

    AddTaskViewModelFactory(AppDatabase appDatabase,int taskId){
        this.appDatabase=appDatabase;
        this.taskId=taskId;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new AddTaskViewModel(appDatabase,taskId);
    }
}
