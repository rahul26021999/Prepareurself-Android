package com.prepare.prepareurself.Apiservice;

import com.prepare.prepareurself.authentication.data.model.ForgotPasswordResponseModel;
import com.prepare.prepareurself.authentication.data.model.RegisterResponseModel;
import com.prepare.prepareurself.courses.data.model.GetProjectResponse;
import com.prepare.prepareurself.courses.data.model.GetTopicResponseModel;
import com.prepare.prepareurself.dashboard.data.model.GetCourseResponseModel;
import com.prepare.prepareurself.profile.data.model.UpdatePasswordResponseModel;
import com.prepare.prepareurself.profile.data.model.UpdatePreferenceResponseModel;
import com.prepare.prepareurself.profile.data.model.AllPreferencesResponseModel;
import com.prepare.prepareurself.resources.data.model.GetResourcesResponse;
import com.prepare.prepareurself.resources.data.model.ResourceLikesResponse;
import com.prepare.prepareurself.resources.data.model.ResourceViewsResponse;
import com.prepare.prepareurself.authentication.data.model.AuthenticationResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("register")
    Call<RegisterResponseModel> registerUser(@Query("first_name") String firstName,
                                             @Query("last_name") String lastName,
                                             @Query("password") String password,
                                             @Query("email") String email,
                                             @Query("android_token") String androidToken);

    @POST("login")
    Call<AuthenticationResponseModel> loginUser(@Query("email")String email,
                                                @Query("password")String password,
                                                @Query("android_token") String androidToken);

    @POST("get-courses")
    Call<GetCourseResponseModel> getCourses(@Query("token") String token);

    @POST("get-topics")
    Call<GetTopicResponseModel> getTopics(@Query("token") String token,
                                          @Query("course_id") int courseId,
                                          @Query("count") int count,
                                          @Query("page_number") int pageNumber);

    @POST("get-resources")
    Call<GetResourcesResponse> getResources(@Query("token") String token,
                                            @Query("topic_id") int topicId,
                                            @Query("page_number") int pageNumber,
                                            @Query("count") int count,
                                            @Query("type") String type);
    @POST("view-resource")
    Call<ResourceViewsResponse> resourceViewed(@Query("token") String token,
                                               @Query("resource_id") int resourceId);

    //likes-api
    @POST("hit-like")
    Call<ResourceLikesResponse> resourceLiked(@Query("token") String token,
                                              @Query("resource_id") int resource_id,
                                              @Query("like")int like )  ;
    @POST("update-user")
    Call<UpdatePreferenceResponseModel> updateUser(@Query("token") String token,
                            @Query("first_name") String firstName,
                            @Query("last_name") String lastName,
                            @Query("dob") String dob,
                            @Query("phone_number") String phoneNumber);


    @POST("update-user")
    Call<UpdatePreferenceResponseModel> updatePreference(@Query("token") String token,
                                                         @Query("preferences[]") List<Integer> integers);

    @POST("get-projects")
    Call<GetProjectResponse> getProjects(@Query("token") String token,
                                         @Query("course_id") int courseId,
                                         @Query("count") int count,
                                         @Query("page") int pageNumber);

    @POST("get-all-preferences")
    Call<AllPreferencesResponseModel> getAllPreferences(@Query("token") String token);

    @POST("update-password")
    Call<UpdatePasswordResponseModel> updatePassword(@Query("token") String token,
                                                     @Query("oldPassword") String oldPassword,
                                                     @Query("newPassword") String newPassword);

    @POST("forget-password")
    Call<ForgotPasswordResponseModel> forgotPassword(@Query("email") String email);


}
