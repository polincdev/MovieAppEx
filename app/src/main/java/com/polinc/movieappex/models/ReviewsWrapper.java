package com.polinc.movieappex.models;
 

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class ReviewsWrapper {

	 @SerializedName(   "results")
    private List<Review> reviews;

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}
