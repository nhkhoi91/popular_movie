package com.example.android.popular_movie.utils;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;


public class MvdbUtils {

    private static final String API_KEY_STRING = "api_key";
    private static final String BASE_URL = "https://api.themoviedb.org/3";
    private static final String MOVIE = "movie";
    private static final String LANGUAGE = "language";
    private static final String PAGE = "page";
    private static final String REGION = "region";
    private static final String TAG = "MvdbUtils.class";
    private static final String API_KEY_VALUE;

    private Context mCtx;

    public MvdbUtils(Context context)
    {
        this.mCtx = context;
        API_KEY_VALUE = Resources.getSystem().getString(android.R.keys.API_KEY_VALUE);
    }

    public URL getPopularMovieListUrl()
    {
        return getPopularMovieListUrl(1);
    }

    public URL getPopularMovieListUrl(int page)
    {
        return getPopularMovieListUrl(null, page, null);
    }

    public URL getPopularMovieListUrl(
            String language,
            int page,
            String region)
    {
        List<String> paths = new ArrayList<>();
        paths.add(MOVIE);
        paths.add("popular");

        HashMap<String, String> queries = new LinkedHashMap<>();
        if (language != null && language.length() > 0)
            queries.put(LANGUAGE, language);

        queries.put(PAGE, String.valueOf(page));

        if (region != null && region.length() > 0)
            queries.put(REGION, region);

        URL url = urlBuilder(paths, queries, null);
        Log.d(TAG, "url: " + url);
        return url;
    }

    public URL getHighestRatedMovieListUrl() {
        return getHighestRatedMovieListUrl(1);
    }

    public URL getHighestRatedMovieListUrl(int page) {
        return getHighestRatedMovieListUrl(null, page, null);
    }

    public URL getHighestRatedMovieListUrl(
            String language,
            int page,
            String region)
    {
        List<String> paths = new ArrayList<>();
        paths.add(MOVIE);
        paths.add("top_rated");

        HashMap<String, String> queries = new LinkedHashMap<>();
        if (language != null && language.length() > 0)
            queries.put(LANGUAGE, language);

        queries.put(PAGE, String.valueOf(page));

        if (region != null && region.length() > 0)
            queries.put(REGION, region);

        URL url = urlBuilder(paths, queries, null);
        Log.d(TAG, "url: " + url);
        return url;
    }


    private static URL urlBuilder(
            List<String> paths,
            HashMap<String, String> queries,
            String fragment)
    {
        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();

        for (String path : paths)
        {
            builder.appendPath(path);
        }

        builder.appendQueryParameter(API_KEY_STRING, API_KEY_VALUE);

        for (Map.Entry<String, String> query : queries.entrySet())
        {
            builder.appendQueryParameter(query.getKey(), query.getValue());
        }

        if (fragment != null)
        {
            builder.fragment(fragment);
        }

        URL url = null;
        try {
            url = new URL(builder.build().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
