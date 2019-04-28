package com.example.androidmanifestation.server_calls;

public interface LoadCallback<T> {
    void onSuccess(T response);
    void onFailure(Throwable throwable);
}
