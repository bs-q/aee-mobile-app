package com.bsq.aee.data.local.sqlite;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.bsq.aee.data.local.sqlite.converter.Converters;
import com.bsq.aee.data.local.sqlite.dao.DbRestaurantDao;
import com.bsq.aee.data.local.sqlite.dao.DbUserDao;
import com.bsq.aee.data.model.db.RestaurantEntity;
import com.bsq.aee.data.model.db.UserEntity;

@Database(entities = {UserEntity.class, RestaurantEntity.class}, version = 2, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract DbUserDao getDbUserDao();
    public abstract DbRestaurantDao getDbRestaurantDao();
}
