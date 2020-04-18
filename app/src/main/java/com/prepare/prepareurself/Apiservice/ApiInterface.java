package com.prepare.prepareurself.Apiservice;

import androidx.annotation.IntegerRes;

import com.prepare.prepareurself.Home.content.courses.data.model.GetProjectResponse;
import com.prepare.prepareurself.Home.content.courses.data.model.GetTopicResponseModel;
import com.prepare.prepareurself.Home.content.dashboard.data.model.GetCourseResponseModel;
import com.prepare.prepareurself.Home.content.resources.data.model.GetResourcesResponse;
import com.prepare.prepareurself.Home.content.resources.data.model.ResourceLikesResponse;
import com.prepare.prepareurself.Home.content.resources.data.model.ResourceViewsResponse;
import com.prepare.prepareurself.authentication.data.model.AuthenticationResponseModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {

    @POST("register")
    Call<AuthenticationResponseModel> registerUser(@Query("first_name") String firstName,
                                                   @Query("last_name") String lastName,
                                                   @Query("password") String password,
                                                   @Query("email") String email);

    @POST("login")
    Call<AuthenticationResponseModel> loginUser(@Query("email")String email,
                                                @Query("password")String password);

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
                                              @Query("like")Boolean like )  ;
    @POST("update-user")
    Call<String> updateUser(@Query("token") String token,
                            @Query("first_name") String firstName,
                            @Query("last_name") String lastName,
                            @Query("dob") String dob,
                            @Query("phone_number") String phoneNumber);


    @POST("update-user")
    Call<String> updatePreference(@Query("token") String token,
                                  @Query("preferences") List<Integer> integers);

    @POST("get-projects")
    Call<GetProjectResponse> getProjects(@Query("token") String token,
                                         @Query("course_id") int courseId,
                                         @Query("count") int count,
                                         @Query("page") int pageNumber);


}
