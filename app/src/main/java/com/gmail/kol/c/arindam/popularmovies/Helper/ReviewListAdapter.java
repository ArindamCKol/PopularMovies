package com.gmail.kol.c.arindam.popularmovies.Helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gmail.kol.c.arindam.popularmovies.R;

import java.util.ArrayList;
import java.util.List;

public class ReviewListAdapter extends RecyclerView.Adapter <ReviewListAdapter.ReviewHolder> {

    List<String> movieReviewList = new ArrayList<>();

    public class ReviewHolder extends RecyclerView.ViewHolder {
        public TextView reviewText;
        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            reviewText = itemView.findViewById(R.id.review_text);
        }
    }
    @NonNull
    @Override
    public ReviewListAdapter.ReviewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.review_list_item, viewGroup, false);
        return new ReviewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final ReviewListAdapter.ReviewHolder viewHolder, final int position) {
        viewHolder.reviewText.setText(movieReviewList.get(position));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(viewHolder.reviewText.getLayoutParams().height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                    viewHolder.reviewText.getLayoutParams().height = 200;
                    viewHolder.reviewText.requestLayout();
                } else {
                    viewHolder.reviewText.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    viewHolder.reviewText.requestLayout();
                }
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieReviewList.size();
    }

    public void setMovieReviewList(List<String> list) {
        movieReviewList = list;
        notifyDataSetChanged();
    }
}
