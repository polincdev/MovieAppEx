package com.polinc.movieappex.ui.movies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.polinc.movieappex.R;
import com.polinc.movieappex.main.Api;
import com.polinc.movieappex.models.Movie;

import java.util.List;


public class MoviesTopratedAdapter extends  RecyclerView.Adapter<MoviesTopratedAdapter.ViewHolder> {

     private List<Movie> movies;
    private Context context;

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

     public MoviesTopratedAdapter(List<Movie> movies) {
         this.movies = movies;
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
        }


    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public MoviesTopratedAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_mov_top, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(MoviesTopratedAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Movie movie = movies.get(position);
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

     //   Glide.with(this).load("http://goo.gl/gEgYUd").into(imageView);
    }
    private void setBackgroundColor(Palette palette, ViewHolder holder) {
        holder.imageIcon.setBackgroundColor(palette.getVibrantColor(context
                .getResources().getColor(R.color.black_translucent_60)));
    }
    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }



}