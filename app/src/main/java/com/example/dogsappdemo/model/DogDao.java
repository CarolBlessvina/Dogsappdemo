package com.example.dogsappdemo.model;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DogDao {
    @Insert
    List<Long>  insertAll(DogBreed... dogs);

    @Query("SELECT * FROM dogbreed")
    List<DogBreed> getAlldogs();

    @Query("SELECT * FROM dogbreed WHERE uuid= :dogId")
    DogBreed getDog(int dogId);

    @Query("DELETE FROM dogbreed ")
    void deleteAllDogs();
}
