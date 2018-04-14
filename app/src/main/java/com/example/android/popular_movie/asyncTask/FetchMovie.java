package com.example.android.popular_movie.asyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.example.android.popular_movie.utils.MvdbUtils;

import java.io.IOException;
import java.net.URL;

public class FetchMovie extends AsyncTask<URL, Void, String> {

    AsyncTaskCompleteListener mListener;
    Context mContext;

    public FetchMovie(Context ctx, AsyncTaskCompleteListener<String> listener)
    {
        this.mContext = ctx;
        this.mListener = listener;
    }
    @Override
    protected String doInBackground(URL... urls) {
        URL searchUrl = urls[0];
        String result = null;
        try
        {
            result = MvdbUtils.getResponseFromHttpUrl(searchUrl);
        } catch (IOException e) {
             = e;
            e.printStackTrace();
        }
        return result;
    }
}
