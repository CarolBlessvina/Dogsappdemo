package com.example.dogsappdemo.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.telecom.Call;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dogsappdemo.R;
import com.example.dogsappdemo.model.DogBreed;
import com.example.dogsappdemo.viewmodel.DetailViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DetailFragment extends Fragment {
    private int doguuid;
    private DetailViewModel viewModel;

@BindView(R.id.dogImage)
    ImageView dogImage;

@BindView(R.id.dogName)
    TextView dogName;
    @BindView(R.id.dogPurpose)
    TextView dogPurpose;
    @BindView(R.id.dogTemperament)
    TextView dogTemperament;

    @BindView(R.id.dogLifespan)
    TextView lifeSpan;

    public DetailFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view =  inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this,view);
        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            doguuid = DetailFragmentArgs.fromBundle(getArguments()).getDoguuid();
        }
        viewModel = ViewModelProviders.of(this).get(DetailViewModel.class);
        viewModel.fetch();
        observeViewModel();

    }

    private void observeViewModel(){
        viewModel.dogLiveData.observe(this, dogBreed -> {
            if(dogBreed != null && dogBreed instanceof DogBreed ){
                dogName.setText(dogBreed.dogBreed);
                dogPurpose.setText(dogBreed.bredFor);
                dogTemperament.setText(dogBreed.temperament);
                lifeSpan.setText(dogBreed.lifeSpan);
            }
        });
    }
}
