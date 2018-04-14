package com.example.android.popular_movie.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popular_movie.Movie;
import com.example.android.popular_movie.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder>
{
    private Movie[] mMovieList;
    private final ListItemClickListener mOnClickListener;
    private static final String TAG = "MovieAdapter.class";
    private static String IMAGE_URL_PREFIX;

    public MovieAdapter(Movie[] movieNames, ListItemClickListener onClickListener)
    {
        this.mMovieList = movieNames;
        this.mOnClickListener = onClickListener;
    }

    public interface ListItemClickListener {
        void onListItemClick(int clickItemIndex);
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.movie_item, parent, false);

        IMAGE_URL_PREFIX = parent.getResources().getString(R.string.mvdb_image_url_prefix);
        MovieViewHolder viewHolder = new MovieViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, int position)
    {
        final String url = IMAGE_URL_PREFIX + mMovieList[position].getImageLink();
        Movie movie = mMovieList[position];
        holder.movie = movie;
        Picasso.get()
                .load(url)
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(holder.movieImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {
                        Picasso.get()
                                .load(url)
                                .into(holder.movieImage);
                    }
                });

    }

    @Override
    public int getItemCount()
    {
        if (mMovieList != null)
            return mMovieList.length;
        else
            return 0;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder
                                        implements View.OnClickListener{

        ImageView movieImage;
        Movie movie;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.iv_movie_image);
            movieImage.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int index = getAdapterPosition();
            mOnClickListener.onListItemClick(index);
        }
    }
}
