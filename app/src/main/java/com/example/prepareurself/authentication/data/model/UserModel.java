package com.example.prepareurself.authentication.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "user_info")
public class UserModel {

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
    @ColumnInfo(name = "created_at")
    @SerializedName("created_at")
    private String created_at;

    @NonNull
    @ColumnInfo(name = "updated_at")
    @SerializedName("updated_at")
    private String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(@NonNull String first_name) {
        this.first_name = first_name;
    }

    @NonNull
    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(@NonNull String last_name) {
        this.last_name = last_name;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public void setEmail(@NonNull String email) {
        this.email = email;
    }

    @NonNull
    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(@NonNull String created_at) {
        this.created_at = created_at;
    }

    @NonNull
    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(@NonNull String updated_at) {
        this.updated_at = updated_at;
    }
}
