package com.example.android.popular_movie.asyncTask;

public interface AsyncTaskCompleteListener<T> {

    public void onPreExecute();
    public void onTaskComplete(T result);
}
