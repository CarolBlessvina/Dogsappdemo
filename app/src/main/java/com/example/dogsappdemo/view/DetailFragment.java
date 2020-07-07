package com.example.dogsappdemo.view;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.palette.graphics.Palette;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.dogsappdemo.R;
import com.example.dogsappdemo.databinding.FragmentDetailBinding;
import com.example.dogsappdemo.databinding.FragmentDetailBindingImpl;
import com.example.dogsappdemo.model.DogBreed;
import com.example.dogsappdemo.model.DogPalette;
import com.example.dogsappdemo.viewmodel.DetailViewModel;


public class DetailFragment extends Fragment {
    private int doguuid;
    private DetailViewModel viewModel;
    private FragmentDetailBinding binding;

   /* @BindView(R.id.dogImage)
    ImageView dogImage;

    @BindView(R.id.dogName)
    TextView dogName;
    @BindView(R.id.dogPurpose)
    TextView dogPurpose;
    @BindView(R.id.dogTemperament)
    TextView dogTemperament;

    @BindView(R.id.dogLifespan)
    TextView lifeSpan;*/

    public DetailFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FragmentDetailBinding binding = DataBindingUtil.inflate(inflater,R.layout.fragment_detail,container,false);
        this.binding=binding;
        setHasOptionsMenu(true);
      //  ButterKnife.bind(this,view);
        return binding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            doguuid = DetailFragmentArgs.fromBundle(getArguments()).getDoguuid();
        }
        viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        viewModel.fetch(doguuid);
        observeViewModel();

    }

    private void observeViewModel() {
        viewModel.dogLiveData.observe(this, dogBreed -> {
            if (dogBreed != null && dogBreed instanceof DogBreed && getContext() != null) {
                binding.setDog(dogBreed);
                if (dogBreed.imageUrl != null)
                {
                    setupBackgroundColor(dogBreed.imageUrl);
                }
            }

        });
    }
    private void setupBackgroundColor(String url){
        Glide.with(this)
                .asBitmap()
               .load(url)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        Palette.from(resource)
                        .generate(palette -> {
                           int intColor = palette.getLightMutedSwatch().getRgb();
                            DogPalette myPalette = new DogPalette(intColor);
                            binding.setPalette(myPalette);
                        });
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });


    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.detail_menu,menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_send_sms: {
                Toast.makeText(getContext(),"Action send sms", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.action_share: {
                Toast.makeText(getContext(),"Action share", Toast.LENGTH_SHORT).show();
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }
}
