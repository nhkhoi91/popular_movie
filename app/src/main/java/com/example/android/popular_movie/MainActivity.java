package com.example.android.popular_movie;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.popular_movie.adapters.MovieAdapter;
import com.example.android.popular_movie.asyncTask.AsyncTaskCompleteListener;
import com.example.android.popular_movie.utils.JsonUtils;
import com.example.android.popular_movie.utils.MvdbUtils;

import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MovieAdapter.ListItemClickListener, AsyncTaskCompleteListener<String> {

    private MovieAdapter mMovieAdapter;
    private RecyclerView mRecyclerView;
    private String mRawResultResponse;
    private static final String TAG = "MainActivity.class";
    private Movie[] mMovieList;
    private Exception asyncException;


    @BindView(R.id.pb_mainscreen)
    ProgressBar mProgressBar;
    @BindView(R.id.tv_retry)
    TextView mRetryTextView;
    @BindView(R.id.bt_retry)
    Button mRetryButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mRecyclerView =  findViewById(R.id.rc_movie_list);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 3);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(true);

        MvdbUtils mvdbUtils = new MvdbUtils(this);
        final URL popularMovieList = mvdbUtils.getPopularMovieListUrl();
        new MvdbQueryTask(this).execute(popularMovieList);

        mRetryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MvdbQueryTask(MainActivity.this).execute(popularMovieList);
            }
        });

    }

    @Override
    public void onListItemClick(int clickItemIndex) {
        Log.d(TAG, "access onclick item!");
        Intent intent = MovieDetailActivity.newInstance(this, mMovieList[clickItemIndex]);
            startActivity(intent);
    }

    @Override
    public void onPreExecute() {
        mProgressBar.setVisibility(View.VISIBLE);
        mRetryTextView.setVisibility(View.INVISIBLE);
        mRetryButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onTaskComplete(String s) {
        mProgressBar.setVisibility(View.INVISIBLE);

        if (asyncException != null)
        {
            if (asyncException instanceof ConnectException) {
                mRetryTextView.setVisibility(View.VISIBLE);
                mRetryButton.setVisibility(View.VISIBLE);
                return;
            }
        }

        if (s != null && !s.equals("")) {
            mRawResultResponse = s;
            mMovieList = JsonUtils.parsePopularMovieList(mRawResultResponse);

            mMovieAdapter = new MovieAdapter(mMovieList, this);
            mRecyclerView.setAdapter(mMovieAdapter);
        } else {
            mRawResultResponse = null;
            mRetryTextView.setVisibility(View.VISIBLE);
            mRetryButton.setVisibility(View.VISIBLE);
        }
    }

    public class MvdbQueryTask extends AsyncTask<URL, Void, String>
    {
        Exception error = null;
        MovieAdapter.ListItemClickListener mListener;

        public MvdbQueryTask(MovieAdapter.ListItemClickListener listener)
        {
            this.mListener = listener;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(URL... urls) {

        }

        @Override
        protected void onPostExecute(String s) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.mn_main_screen_highest_rated:
            {
                MvdbUtils mvdbUtils = new MvdbUtils(this);
                final URL highestRatedMovieListUrl = mvdbUtils.getHighestRatedMovieListUrl();
                new MvdbQueryTask(this).execute(highestRatedMovieListUrl);

                mRetryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new MvdbQueryTask(MainActivity.this).execute(highestRatedMovieListUrl);
                    }
                });
                return true;
            }
            case R.id.mn_main_screen_most_popular:
            {
                MvdbUtils mvdbUtils = new MvdbUtils(this);
                final URL popularMovieList = mvdbUtils.getPopularMovieListUrl();
                new MvdbQueryTask(this).execute(popularMovieList);

                mRetryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new MvdbQueryTask(MainActivity.this).execute(popularMovieList);
                    }
                });
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
