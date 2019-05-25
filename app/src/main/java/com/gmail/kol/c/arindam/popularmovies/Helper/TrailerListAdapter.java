package com.gmail.kol.c.arindam.popularmovies.Helper;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gmail.kol.c.arindam.popularmovies.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TrailerListAdapter extends RecyclerView.Adapter <TrailerListAdapter.TrailerHolder> {
    private List<String> youtubeKeyList = new ArrayList<>();
    private TrailerClickListener listener;

    public TrailerListAdapter(TrailerClickListener listener) {
        this.listener = listener;
    }

    public class TrailerHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public ImageView traierThumbnail;
        public TrailerHolder(@NonNull View itemView) {
            super(itemView);
            traierThumbnail = itemView.findViewById(R.id.youtube_thumbnail);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String currentTrailer = youtubeKeyList.get(getAdapterPosition());
            listener.onClick(currentTrailer);
        }
    }

    @NonNull
    @Override
    public TrailerListAdapter.TrailerHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.trailer_list_item, viewGroup, false);
        return new TrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerHolder trailerHolder, int position) {
        String thumbnailURL = "https://img.youtube.com/vi/" +youtubeKeyList.get(position)+ "/hqdefault.jpg";
        Picasso.get().load(thumbnailURL).into(trailerHolder.traierThumbnail);
    }

    public interface TrailerClickListener {
        void onClick (String youtubeKey);
    }

    @Override
    public int getItemCount() {
        return youtubeKeyList.size();
    }

    public void setYoutubeKeyList (List<String> list) {
        youtubeKeyList = list;
        notifyDataSetChanged();
    }
}
