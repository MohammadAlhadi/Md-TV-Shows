package com.mohammad39411.mdtvshows.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mohammad39411.mdtvshows.R;
import com.mohammad39411.mdtvshows.databinding.ItemContainerTvSowBinding;
import com.mohammad39411.mdtvshows.listeners.WatchlistListener;
import com.mohammad39411.mdtvshows.model.TVShow;

import java.util.List;

public class WatchlistAdapter extends RecyclerView.Adapter<WatchlistAdapter.TVShowsViewHolder>{

    private List<TVShow> tvShows;
    private LayoutInflater layoutInflater;
    private WatchlistListener watchlistListener;

    public WatchlistAdapter(List<TVShow> tvShows , WatchlistListener watchlistListener) {
        this.tvShows = tvShows;
        this.watchlistListener = watchlistListener;
    }

    @NonNull
    @Override
    public TVShowsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null){
            layoutInflater = LayoutInflater.from(parent.getContext());

        }
        ItemContainerTvSowBinding tvSowBinding = DataBindingUtil.inflate(layoutInflater,
                R.layout.item_container_tv_sow , parent ,false);
        return new TVShowsViewHolder(tvSowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull TVShowsViewHolder holder, int position) {

        holder.bindTvShow(tvShows.get(position));

    }

    @Override
    public int getItemCount() {
        return tvShows.size();
    }

     class TVShowsViewHolder extends RecyclerView.ViewHolder{

       private ItemContainerTvSowBinding itemContainerTvSowBinding;
       public TVShowsViewHolder(ItemContainerTvSowBinding itemContainerTvSowBinding){
           super(itemContainerTvSowBinding.getRoot());
           this.itemContainerTvSowBinding = itemContainerTvSowBinding;
       }

       public void bindTvShow(TVShow tvShow){
           itemContainerTvSowBinding.setTvShow(tvShow);
           itemContainerTvSowBinding.executePendingBindings();
           itemContainerTvSowBinding.getRoot().setOnClickListener(view -> watchlistListener.onTvShowClicked(tvShow));
           itemContainerTvSowBinding.imageDelete.setOnClickListener(view -> { watchlistListener.onRemoveShowFromList(tvShow , getAdapterPosition()); });
           itemContainerTvSowBinding.imageDelete.setVisibility(View.VISIBLE);
       }
    }
}
