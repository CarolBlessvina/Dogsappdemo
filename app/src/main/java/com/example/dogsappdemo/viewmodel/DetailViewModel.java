package com.example.dogsappdemo.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.dogsappdemo.model.DogBreed;

public class DetailViewModel extends ViewModel {

    public MutableLiveData<DogBreed> dogLiveData = new MutableLiveData<DogBreed>();
    public void fetch()
    {
        DogBreed dog1 = new DogBreed("1", "corgi", "15 years","","","","" );
        dogLiveData.setValue(dog1);

    }

}
