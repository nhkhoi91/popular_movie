package com.example.android.popular_movie.utils;

import android.util.Log;

import com.example.android.popular_movie.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class JsonUtils
{
    private static final String TAG = "JsonUtils.class";

    public static Movie[] parsePopularMovieList(String movieRawString)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Movie[] movieList = null;
        Log.d(TAG, movieRawString);
        try {
            JSONObject movieJSONObject = new JSONObject(movieRawString);
            JSONArray results = movieJSONObject.getJSONArray("results");
            movieList = new Movie[results.length()];

            for (int i = 0; i < movieList.length; i++)
            {
                movieList[i] = new Movie();
            }

            for (int i = 0; i < results.length(); i++)
            {
                JSONObject movie = results.getJSONObject(i);
                movieList[i].setTitle(movie.optString("title"));
                movieList[i].setOriginalTitle(movie.optString("original_title"));
                movieList[i].setImageLink(movie.optString("poster_path"));
                movieList[i].setUserRating(movie.optInt("vote_average"));
                movieList[i].setPlot(movie.optString("overview"));
                try {
                    date = dateFormat.parse(movie.optString("release_date"));
                } catch (ParseException e) {
                    e.printStackTrace();
                    date = null;
                }
                movieList[i].setReleaseDate(date);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movieList;
    }
}
