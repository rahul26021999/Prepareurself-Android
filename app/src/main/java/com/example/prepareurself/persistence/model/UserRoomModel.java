package com.example.prepareurself.persistence.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "user_info")
public class UserRoomModel {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "id")
    @SerializedName("id")
    private int id;

    @NonNull
    @ColumnInfo(name = "first_name")
    @SerializedName("first_name")
    private String first_name;

    @NonNull
    @ColumnInfo(name = "last_name")
    @SerializedName("last_name")
    private String last_name;

    @NonNull
    @ColumnInfo(name = "email")
    @SerializedName("email")
    private String email;

    @NonNull
    @ColumnInfo(name = "password")
    @SerializedName("password")
    private String password;

    @NonNull
    @ColumnInfo(name = "username")
    @SerializedName("username")
    private String username;

    @NonNull
    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    private String created_at;

    @NonNull
    @ColumnInfo(name = "updated_at")
    @SerializedName("updated_at")
    private String updated_at;

}
