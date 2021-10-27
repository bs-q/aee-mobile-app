package com.bsq.aee.data.local.sqlite;

import androidx.lifecycle.LiveData;

import com.bsq.aee.data.model.db.RestaurantEntity;
import com.bsq.aee.data.model.db.UserEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface DbService {

    Observable<List<UserEntity>> getAllDbUser();

    LiveData<List<UserEntity>> loadAllToLiveData();

    Observable<Long> insertUser(UserEntity user);

    Observable<Boolean> deleteUser(UserEntity user);

    Observable<UserEntity> findById(Long id);

    Observable<List<RestaurantEntity>> getAllDbRestaurant();

    LiveData<List<RestaurantEntity>> loadAllRestaurantToLiveData();

    Observable<Long> insertRestaurant(RestaurantEntity restaurant);

    Observable<Boolean> deleteRestaurant(RestaurantEntity restaurant);

    Observable<RestaurantEntity> findRestaurantById(String id);

}
