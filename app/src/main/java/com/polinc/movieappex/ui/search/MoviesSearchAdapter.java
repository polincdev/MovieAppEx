package com.polinc.movieappex.ui.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.view.ViewCompat;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.polinc.movieappex.R;
import com.polinc.movieappex.main.Api;
import com.polinc.movieappex.models.Movie;

import java.util.Comparator;
import java.util.List;


public class MoviesSearchAdapter extends  RecyclerView.Adapter<MoviesSearchAdapter.ViewHolder> {

     private List<Movie> movies;
    private Context context;


    private static final Comparator<Movie> ALPHABETICAL_COMPARATOR = new Comparator<Movie>() {
        @Override
        public int compare(Movie a, Movie b) {
            return a.getTitle().compareTo(b.getTitle());
        }
    };

    private static final Comparator<Movie> ORIGINAL_ORDER_COMPARATOR = new Comparator<Movie>() {
        @Override
        public int compare(Movie a, Movie b) {
            return a.order>b.order? 1: (a.order==b.order? 0:-1);
        }
    };
    private final SortedList.Callback<Movie> mCallback = new SortedList.Callback<Movie>() {

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted(position, count);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved(position, count);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved(fromPosition, toPosition);
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged(position, count);
        }

        @Override
        public int compare(Movie a, Movie b) {
            return ORIGINAL_ORDER_COMPARATOR.compare(a, b);
        }

        @Override
        public boolean areContentsTheSame(Movie oldItem, Movie newItem) {
            return oldItem.equals(newItem);
        }

        @Override
        public boolean areItemsTheSame(Movie item1, Movie item2) {
            return item1.getId() == item2.getId();
        }
    };

    final SortedList<Movie> sortedList = new SortedList<>(Movie.class, mCallback);

    /***** Creating OnItemClickListener *****/

    // Define listener member variable
    private OnItemClickListener listener;
    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    // Define listener member variable
    private OnButtonClickListener listener2;
    // Define the listener interface
    public interface OnButtonClickListener {
        void onButtonClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnButtonClickListener(OnButtonClickListener listener) {
        this.listener2 = listener;
    }

    // Define listener member variable
    private OnImageClickListener listener3;
    // Define the listener interface
    public interface OnImageClickListener {
        void onImageClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnImageClickListener(OnImageClickListener listener) {
        this.listener3 = listener;
    }

     public MoviesSearchAdapter(List<Movie> movies) {

        this.movies = movies;
        //search version
        add(movies);

    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public Button detailButton;
        public ImageView imageIcon;
        public Movie movie;


        public ViewHolder( View itemView) {

            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.movie_name);
            detailButton = (Button) itemView.findViewById(R.id.movie_detail_button);
            imageIcon = (ImageView) itemView.findViewById(R.id.imgMovieIcon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.out.println("itemView onClick1");
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        System.out.println("itemView onClick2="+listener);
                        int position = getAdapterPosition();
                        System.out.println("itemView onClick3="+listener+" "+position);
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });

            detailButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener2 != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener2.onButtonClick(detailButton, position);
                        }
                    }
                }
            });

            imageIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener3 != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener3.onImageClick(imageIcon, position);
                        }
                    }
                }
            });
        }


    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_mov_pop, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the data model based on position
        //Movie movie = movies.get(position);
        //Serach version
        final Movie movie   = sortedList.get(position);
        holder.movie = movie;

        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        textView.setText((position+1)+". "+movie.getTitle());
        Button button = holder.detailButton;
        button.setText(movie.getOverview().length()>0 ? "Overviews" : "No data");
        button.setEnabled(movie.getOverview().length()>0);

        ImageView imageView = holder.imageIcon;

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .priority(Priority.HIGH);

        Glide.with(context)
                .asBitmap()
                .load(Api.getPosterPath(holder.movie.getPosterPath()))
                .apply(options)
                .into(imageView);

        //Set unigue transition name for
        ViewCompat.setTransitionName( imageView, "sharedBanner"+position);


    }
    private void setBackgroundColor(Palette palette, ViewHolder holder) {
        holder.imageIcon.setBackgroundColor(palette.getVibrantColor(context
                .getResources().getColor(R.color.black_translucent_60)));
    }
    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        //
        //return movies.size();
        //Search verion
        return sortedList.size();
    }

//Search methods
public void add(Movie model) {
    sortedList.add(model);
}

    public void remove(Movie model) {
        sortedList.remove(model);
    }

    public void add(List<Movie> models) {
        sortedList.addAll(models);
        for(Movie mov: models)
            System.out.println("Added movie="+mov.getTitle());
    }

    public void remove(List<Movie> models) {
        sortedList.beginBatchedUpdates();
        for (Movie model : models) {
            sortedList.remove(model);
        }
        sortedList.endBatchedUpdates();
    }
    public void replaceAll(List<Movie> models) {
        sortedList.beginBatchedUpdates();
        for (int i = sortedList.size() - 1; i >= 0; i--) {
            final Movie model = sortedList.get(i);
            if (!models.contains(model)) {
                sortedList.remove(model);
            }
        }
        sortedList.addAll(models);
        sortedList.endBatchedUpdates();
    }

    public  List<Movie> getCurrentMovies(){return movies;};

}

