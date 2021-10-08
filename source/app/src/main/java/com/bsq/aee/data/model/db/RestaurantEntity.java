package com.hq.remview.data.model.db;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
@Entity(tableName = "db_restaurant")
public class RestaurantEntity extends BaseEntity {
    @PrimaryKey()
    @ColumnInfo(name = "id")
    @NonNull
    public String id;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "last_access_date")
    public Date lastAccessDate;

    @ColumnInfo(name = "token")
    public String token;

    public RestaurantEntity(){}
}
