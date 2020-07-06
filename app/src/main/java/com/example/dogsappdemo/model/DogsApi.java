package com.example.dogsappdemo.model;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface DogsApi {
    // observable to return a single value and finishes
    @GET("DevTides/DogsApi/master/dogs.json")
    Single<List<DogBreed>>  getDogs();


}
