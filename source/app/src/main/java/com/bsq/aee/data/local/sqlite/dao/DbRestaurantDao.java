package com.hq.remview.data.local.sqlite.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.hq.remview.data.model.db.RestaurantEntity;

import java.util.List;

@Dao
public interface DbRestaurantDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(RestaurantEntity restaurantEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<RestaurantEntity> restaurantEntity);

    @Query("SELECT * FROM db_restaurant")
    List<RestaurantEntity> loadAll();

    @Query("SELECT * FROM db_restaurant")
    LiveData<List<RestaurantEntity>> loadAllToLiveData();

    @Query("SELECT * FROM db_restaurant WHERE id=:id")
    RestaurantEntity findById(String id);

    @Delete
    void delete(RestaurantEntity restaurantEntity);
}
