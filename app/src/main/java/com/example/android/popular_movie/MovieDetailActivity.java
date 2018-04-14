package com.example.android.popular_movie;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import butterknife.BindBitmap;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String MOVIE = "MovieActivity.MOVIE";
    private Movie mMovie;

    @BindView(R.id.iv_movie_detail_image)
    ImageView mImage;
    @BindView(R.id.tv_original_title)
    TextView mOriginalTitle;
    @BindView(R.id.tv_plot)
    TextView mPlot;
    @BindView(R.id.tv_release_date)
    TextView mReleaseDate;
    @BindView(R.id.tv_user_rating)
    TextView mUserRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ButterKnife.bind(this);
        Intent intentThatStartedThisActivity = getIntent();
        if (intentThatStartedThisActivity.hasExtra(MOVIE))
        {
            this.mMovie = intentThatStartedThisActivity.getParcelableExtra(MOVIE);
        }

        final String url = getResources().getString(R.string.mvdb_image_url_prefix)
                + mMovie.getImageLink();

        mOriginalTitle.setText(mMovie.getOriginalTitle());
        mPlot.setText(mMovie.getPlot());
        mUserRating.setText(String.valueOf(mMovie.getUserRating()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        mReleaseDate.setText(dateFormat.format(mMovie.getReleaseDate()));

        Picasso.get()
                .load(url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(mImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(url)
                                .into(mImage);
                    }
                });
    }

    public static Intent newInstance(Context packageContext, Movie movie) {
        Intent intent = new Intent(packageContext, MovieDetailActivity.class);
        intent.putExtra(MOVIE, movie);
        return intent;
    }


}
