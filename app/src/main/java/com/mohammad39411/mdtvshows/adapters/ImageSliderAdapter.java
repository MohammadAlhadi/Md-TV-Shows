package com.mohammad39411.mdtvshows.adapters;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mohammad39411.mdtvshows.R;
import com.mohammad39411.mdtvshows.databinding.ItemContainerSlideImageBinding;

public class ImageSliderAdapter extends RecyclerView.Adapter<ImageSliderAdapter.ImageSliderViewHolder>{

    private String[] sliderImage;
    private LayoutInflater layoutInflater;

    public ImageSliderAdapter(String[] sliderImage) {
        this.sliderImage = sliderImage;
    }

    @NonNull
    @Override
    public ImageSliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater==null){
            layoutInflater = LayoutInflater.from(parent.getContext());
        }
        ItemContainerSlideImageBinding slideImageBinding = DataBindingUtil.inflate(
                layoutInflater , R.layout.item_container_slide_image , parent ,false
        );
        return new ImageSliderViewHolder(slideImageBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageSliderViewHolder holder, int position) {

        holder.bindSliderImage(sliderImage[position]);

    }

    @Override
    public int getItemCount() {
        return sliderImage.length;
    }

    static class ImageSliderViewHolder extends RecyclerView.ViewHolder{

        private ItemContainerSlideImageBinding itemContainerSlideImageBinding;
        public ImageSliderViewHolder(ItemContainerSlideImageBinding itemContainerSlideImageBinding){
            super(itemContainerSlideImageBinding.getRoot());
            this.itemContainerSlideImageBinding = itemContainerSlideImageBinding;

        }

        private void bindSliderImage(String imageURL){
            itemContainerSlideImageBinding.setImageURL(imageURL);
        }

    }
}
