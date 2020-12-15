package com.mohammad39411.mdtvshows.adapters;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mohammad39411.mdtvshows.R;
import com.mohammad39411.mdtvshows.databinding.ItemContainerSpisodesBinding;
import com.mohammad39411.mdtvshows.model.Episode;

import java.util.List;

public class EpisodesAdapter extends RecyclerView.Adapter<EpisodesAdapter.EpisodeViewHolder> {

    private List<Episode> episodes;
    private LayoutInflater inflater;

    public EpisodesAdapter(List<Episode> episodes) {
        this.episodes = episodes;
    }

    @NonNull
    @Override
    public EpisodeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (inflater == null){
            inflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerSpisodesBinding itemContainerSpisodesBinding = DataBindingUtil.inflate(
                inflater , R.layout.item_container_spisodes , parent , false
        );
        return new EpisodeViewHolder(itemContainerSpisodesBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull EpisodeViewHolder holder, int position) {

        holder.bindEpisode(episodes.get(position));

    }

    @Override
    public int getItemCount() {
        return episodes.size();
    }

    public class EpisodeViewHolder extends RecyclerView.ViewHolder{
        private ItemContainerSpisodesBinding itemContainerSpisodesBinding;

        public EpisodeViewHolder(ItemContainerSpisodesBinding itemContainerSpisodesBinding){
            super(itemContainerSpisodesBinding.getRoot());
            this.itemContainerSpisodesBinding = itemContainerSpisodesBinding;
        }

        public void bindEpisode(Episode episode){

            String title = "S";
            String season = episode.getSeason();
            if (season.length() == 1){
                season = "0".concat(season);
            }
            String episodeNumber = episode.getEpisode();
            if (episodeNumber.length() == 1){
                episodeNumber = "0".concat(episodeNumber);

            }
            episodeNumber = "E".concat(episodeNumber);
            title = title.concat(season).concat(episodeNumber);
            itemContainerSpisodesBinding.setTitle(title);
            itemContainerSpisodesBinding.setName(episode.getName());
            itemContainerSpisodesBinding.setAirDate(episode.getAirDate());
        }

    }
}
