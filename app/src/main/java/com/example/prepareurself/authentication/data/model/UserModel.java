package com.example.prepareurself.authentication.data.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

    @Nullable
    @ColumnInfo(name = "username")
    @SerializedName("username")
    private String username;

    @Nullable
    @ColumnInfo(name = "profile_image")
    @SerializedName("profile_image")
    private String profile_image;

    @Nullable
    @ColumnInfo(name = "dob")
    @SerializedName("dob")
    private String dob;

    @Nullable
    @ColumnInfo(name = "android_token")
    @SerializedName("android_token")
    private String android_token;

    @Nullable
    @ColumnInfo(name = "phone_number")
    @SerializedName("phone_number")
    private String phone_number;

    @Nullable
    @ColumnInfo(name = "user_status")
    @SerializedName("user_status")
    private String user_status;

    @Nullable
    @ColumnInfo(name = "email_verified_at")
    @SerializedName("email_verified_at")
    private String email_verified_at;

    @Nullable
    @ColumnInfo(name = "last_login_at")
    @SerializedName("last_login_at")
    private String last_login_at;

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

    @Nullable
    public String getUsername() {
        return username;
    }

    public void setUsername(@Nullable String username) {
        this.username = username;
    }

    @Nullable
    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(@Nullable String profile_image) {
        this.profile_image = profile_image;
    }

    @Nullable
    public String getDob() {
        return dob;
    }

    public void setDob(@Nullable String dob) {
        this.dob = dob;
    }

    @Nullable
    public String getAndroid_token() {
        return android_token;
    }

    public void setAndroid_token(@Nullable String android_token) {
        this.android_token = android_token;
    }

    @Nullable
    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(@Nullable String phone_number) {
        this.phone_number = phone_number;
    }

    @Nullable
    public String getUser_status() {
        return user_status;
    }

    public void setUser_status(@Nullable String user_status) {
        this.user_status = user_status;
    }

    @Nullable
    public String getEmail_verified_at() {
        return email_verified_at;
    }

    public void setEmail_verified_at(@Nullable String email_verified_at) {
        this.email_verified_at = email_verified_at;
    }

    @Nullable
    public String getLast_login_at() {
        return last_login_at;
    }

    public void setLast_login_at(@Nullable String last_login_at) {
        this.last_login_at = last_login_at;
    }
}
