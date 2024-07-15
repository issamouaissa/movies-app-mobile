package com.example.moviesapp;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public class MyMovieAdapter extends RecyclerView.Adapter<MyMovieAdapter.ViewHolder>
        implements Filterable {
    private MyMovieData[] originalMovieData; // Store original movie data
    private List<MyMovieData> filteredMovieData; // Store filtered movie data
    private Context context;
    public MyMovieAdapter(MyMovieData[] myMovieData, Context context) {
        this.originalMovieData = myMovieData;
        this.filteredMovieData = new ArrayList<>(Arrays.asList(myMovieData));
        // Initialize filtered list with original data
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.activity_movie_item_list, parent,
                false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final MyMovieData movieData = filteredMovieData.get(position);
        // Use filtered data
        holder.textViewName.setText(movieData.getMovieName());
        holder.textViewDate.setText(movieData.getMovieDate());
        // Load image using Glide with TMDB URL
        Glide.with(context).load("https://image.tmdb.org/t/p/w500" + movieData.getMovieImage())
                .into(holder.movieImage);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open MovieDetailActivity and pass movie details
                Intent intent = new Intent(context, MovieDetailActivity.class);
                intent.putExtra("movieId", movieData.getMovieId());
                context.startActivity(intent);
            }
        });
    }
    @Override
    public int getItemCount() {
        return filteredMovieData.size(); // Return size of filtered list
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView movieImage;
        TextView textViewName;
        TextView textViewDate;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.imageview);
            textViewName = itemView.findViewById(R.id.textName);
            textViewDate = itemView.findViewById(R.id.textdate);
        }
    }
    @Override
    public Filter getFilter() {
        return movieFilter;
    }
    private Filter movieFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<MyMovieData> filteredList = new ArrayList<>();
            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(Arrays.asList(originalMovieData));
                // If search query is empty, show all movies
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (MyMovieData movie : originalMovieData) {
                    if (movie.getMovieName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(movie);
                        // Add movie to filtered list if its name contains search query
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredMovieData.clear();
            filteredMovieData.addAll((List) results.values);
            notifyDataSetChanged(); // Notify RecyclerView adapter that data set has changed
        }
    };
}
