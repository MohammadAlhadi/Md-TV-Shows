package com.mohammad39411.mdtvshows.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.text.HtmlCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.mohammad39411.mdtvshows.R;
import com.mohammad39411.mdtvshows.adapters.EpisodesAdapter;
import com.mohammad39411.mdtvshows.adapters.ImageSliderAdapter;
import com.mohammad39411.mdtvshows.databinding.ActivityTVShowDetailsBinding;
import com.mohammad39411.mdtvshows.databinding.LayoutEpisodesBottomSheetBinding;
import com.mohammad39411.mdtvshows.model.TVShow;
import com.mohammad39411.mdtvshows.viewmodels.TVShowDetailsViewModel;

import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TVShowDetailsActivity extends AppCompatActivity {

    // TODO: 12/15/2020 Location : SUDAN - MEDaNI

    private ActivityTVShowDetailsBinding binding;
    private TVShowDetailsViewModel viewModel;
    private BottomSheetDialog episodesSheetDialog;
    private LayoutEpisodesBottomSheetBinding layout_episodes_bottom_sheet_binding;
    private TVShow tvShow;

    private Boolean isTvShowAvailableInWatchlist = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_t_v_show_details);

        doInitialization();

    }

    private void doInitialization() {
        viewModel = new ViewModelProvider(this).get(TVShowDetailsViewModel.class);
        binding.imageBack.setOnClickListener(view -> onBackPressed());

        tvShow = (TVShow) getIntent().getSerializableExtra("tvShow");
        checkTvShowInWatchList();
        getShowDetails();
    }

    private void checkTvShowInWatchList(){
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(viewModel.getTvShowFromWatchlist(String.valueOf(tvShow.getId()))
        .subscribeOn(Schedulers.computation())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(tvShow1 -> {
            isTvShowAvailableInWatchlist = true;
            binding.imgWatchList.setImageResource(R.drawable.ic_check);
            compositeDisposable.dispose();
        }));
    }

    private void getShowDetails() {
        binding.setIsLoading(true);
        String tvShowId = String.valueOf(tvShow.getId());
        viewModel.getTVShowDetails(tvShowId).observe(
                this, tvShowDetailsResponse -> {
                    binding.setIsLoading(false);
                    if (tvShowDetailsResponse.getTvShowsDetails() != null) {
                        if (tvShowDetailsResponse.getTvShowsDetails().getPictures() != null) {
                            loadImageSlider(tvShowDetailsResponse.getTvShowsDetails().getPictures());

                        }
                        binding.setTvShowImageURL(tvShowDetailsResponse.getTvShowsDetails().getImagePath());
                        binding.imageTvShow.setVisibility(View.VISIBLE);
                        binding.setDescription(
                                String.valueOf(
                                        HtmlCompat.fromHtml(
                                                tvShowDetailsResponse.getTvShowsDetails().getDescription(),
                                                HtmlCompat.FROM_HTML_MODE_LEGACY
                                        )
                                )
                        );
                        binding.textDescription.setVisibility(View.VISIBLE);
                        binding.textReadMore.setVisibility(View.VISIBLE);
                        binding.textReadMore.setOnClickListener(view -> {
                            if (binding.textReadMore.getText().toString().equals("Read More")) {
                                binding.textDescription.setMaxLines(Integer.MAX_VALUE);
                                binding.textDescription.setEllipsize(null);
                                binding.textReadMore.setText("Read less");
                            } else {
                                binding.textDescription.setMaxLines(4);
                                binding.textDescription.setEllipsize(TextUtils.TruncateAt.END);
                                binding.textReadMore.setText("Read More");
                            }
                        });
                        binding.setRating(String.format(
                                Locale.getDefault(),
                                "%.2f",
                                Double.parseDouble(tvShowDetailsResponse.getTvShowsDetails().getRating())
                        ));

                        if (tvShowDetailsResponse.getTvShowsDetails().getGenres() != null) {
                            binding.setGenre(tvShowDetailsResponse.getTvShowsDetails().getGenres()[0]);
                        } else {
                            binding.setGenre("N/A");
                        }
                        binding.setRuntime(tvShowDetailsResponse.getTvShowsDetails().getRuntime() + " Min");
                        binding.viewDivider1.setVisibility(View.VISIBLE);
                        binding.layoutMisc.setVisibility(View.VISIBLE);
                        binding.viewDivider2.setVisibility(View.VISIBLE);
                        binding.btnWebsite.setVisibility(View.VISIBLE);
                        binding.btnEpisodes.setVisibility(View.VISIBLE);
                        binding.btnWebsite.setOnClickListener(view -> {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(tvShowDetailsResponse.getTvShowsDetails().getUrl()));
                            startActivity(intent);
                        });
                        binding.btnEpisodes.setOnClickListener(view -> {
                            if (episodesSheetDialog == null) {
                                episodesSheetDialog = new BottomSheetDialog(TVShowDetailsActivity.this);
                                layout_episodes_bottom_sheet_binding = DataBindingUtil.inflate(
                                        LayoutInflater.from(TVShowDetailsActivity.this),
                                        R.layout.layout_episodes_bottom_sheet,
                                        findViewById(R.id.episodesContainer),
                                        false
                                );

                                episodesSheetDialog.setContentView(layout_episodes_bottom_sheet_binding.getRoot());
                                layout_episodes_bottom_sheet_binding.episodesRecyclerView.setAdapter(
                                        new EpisodesAdapter(tvShowDetailsResponse.getTvShowsDetails().getEpisodes())
                                );

                                layout_episodes_bottom_sheet_binding.textTitle.setText(
                                        String.format("Episodes | %s", tvShow.getName())
                                );


                                layout_episodes_bottom_sheet_binding.imageClose.setOnClickListener(view1 ->
                                        episodesSheetDialog.dismiss());
                            }

                            //MOHAMED)*
                            FrameLayout frameLayout = episodesSheetDialog.findViewById(
                                    com.google.android.material.R.id.design_bottom_sheet
                            );
                            if (frameLayout != null) {
                                BottomSheetBehavior<View> bottomSheetBehavior = BottomSheetBehavior.from(frameLayout);
                                bottomSheetBehavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels);
                                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                            }
                            episodesSheetDialog.show();
                        });
                        binding.imgWatchList.setOnClickListener(view -> {
                            CompositeDisposable compositeDisposable = new CompositeDisposable();
                            if (isTvShowAvailableInWatchlist){
                                compositeDisposable.add(viewModel.removeFromWatchList(tvShow)
                                .subscribeOn(Schedulers.computation())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(() -> {
                                    isTvShowAvailableInWatchlist = false;
                                    binding.imgWatchList.setImageResource(R.drawable.ic_seen);
                                    showMessage("Removed Form List");
                                    compositeDisposable.dispose();
                                }));
                            }
                            else
                            {
                                compositeDisposable.add(viewModel.addToWatchList(tvShow)
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(() -> {
                                            binding.imgWatchList.setImageResource(R.drawable.ic_check);
                                            showMessage("add On Watch List");
                                            compositeDisposable.dispose();
                                        })
                                );
                            }
                        });
                        binding.imgWatchList.setVisibility(View.VISIBLE);
                        loadBasicTvShowDetails();
                    }

                });
    }

    private void loadImageSlider(String[] sliderImages) {

        binding.sliderPager.setOffscreenPageLimit(1);
        binding.sliderPager.setAdapter(new ImageSliderAdapter(sliderImages));
        binding.sliderPager.setVisibility(View.VISIBLE);
        binding.viewFadingEdg.setVisibility(View.VISIBLE);
        setupSliderIndicators(sliderImages.length);
        binding.sliderPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setCurrentSliderIndicators(position);
            }
        });
    }

    private void setupSliderIndicators(int count) {
        ImageView[] indicator = new ImageView[count];

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );

        layoutParams.setMargins(8, 0, 8, 0);
        for (int i = 0; i < indicator.length; i++) {
            indicator[i] = new ImageView(getApplicationContext());
            indicator[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.slider_inactive_indicator
            ));
            indicator[i].setLayoutParams(layoutParams);
            binding.layoutSliderIndicator.addView(indicator[i]);
        }
        binding.layoutSliderIndicator.setVisibility(View.VISIBLE);
        setCurrentSliderIndicators(0);
    }


    private void setCurrentSliderIndicators(int position) {

        int childCount = binding.layoutSliderIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) binding.layoutSliderIndicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.slider_active_indicator)
                );
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),
                                R.drawable.slider_inactive_indicator)
                );

            }
        }

    }

    private void loadBasicTvShowDetails() {
        binding.setTvShowName(tvShow.getName());
        binding.setNetworkCountry(tvShow.getNetwork() + " ("
                + tvShow.getCountry() + " )");
        binding.setStatus(tvShow.getStatus());
        binding.setStartedDate(tvShow.getStartDate());

        binding.textName.setVisibility(View.VISIBLE);
        binding.textNetworkCountry.setVisibility(View.VISIBLE);
        binding.textStartedDate.setVisibility(View.VISIBLE);
        binding.textStatus.setVisibility(View.VISIBLE);
    }

    private  void showMessage(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }


}
