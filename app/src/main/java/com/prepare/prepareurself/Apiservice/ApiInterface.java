package com.prepare.prepareurself.Apiservice;

import com.prepare.prepareurself.authentication.data.model.ForgotPasswordResponseModel;
import com.prepare.prepareurself.authentication.data.model.RegisterResponseModel;
import com.prepare.prepareurself.authentication.data.model.UserModel;
import com.prepare.prepareurself.courses.data.model.AddToUserPrefResponseModel;
import com.prepare.prepareurself.courses.data.model.CourseDetailReponseModel;
import com.prepare.prepareurself.courses.data.model.GetProjectResponse;
import com.prepare.prepareurself.courses.data.model.GetTopicResponseModel;
import com.prepare.prepareurself.courses.data.model.ProjectResponseModel;
import com.prepare.prepareurself.banner.BannerImageResponseModel;
import com.prepare.prepareurself.courses.data.model.RateCourseResponseModel;
import com.prepare.prepareurself.dashboard.data.model.GetCourseResponseModel;
import com.prepare.prepareurself.dashboard.data.model.GetSuggestedProjectsModel;
import com.prepare.prepareurself.dashboard.data.model.GetSuggestedTopicsModel;
import com.prepare.prepareurself.dashboard.data.model.HomepageResponseModel;
import com.prepare.prepareurself.favourites.data.model.FavouritesResponseModel;
import com.prepare.prepareurself.feedback.data.model.FeedbacksubmitModel;
import com.prepare.prepareurself.forum.data.DoReplyResponseModel;
import com.prepare.prepareurself.forum.data.ForumPostQueryResponseModel;
import com.prepare.prepareurself.forum.data.GetQueriesResponseModel;
import com.prepare.prepareurself.forum.data.UploadImageResponseModel;
import com.prepare.prepareurself.preferences.data.PrefernceResponseModel;
import com.prepare.prepareurself.preferences.data.UpdatePrefResponseModel;
import com.prepare.prepareurself.profile.data.model.UpdatePasswordResponseModel;
import com.prepare.prepareurself.profile.data.model.UpdatePreferenceResponseModel;
import com.prepare.prepareurself.profile.data.model.AllPreferencesResponseModel;
import com.prepare.prepareurself.profile.data.model.UploadImageResponse;
import com.prepare.prepareurself.quizv2.data.QuizAnswerResponse;
import com.prepare.prepareurself.quizv2.data.QuizResponseModel;
import com.prepare.prepareurself.quizv2.data.ResponsesModel;
import com.prepare.prepareurself.resources.data.model.GetResourcesResponse;
import com.prepare.prepareurself.resources.data.model.ResourceLikesResponse;
import com.prepare.prepareurself.resources.data.model.ResourceViewsResponse;
import com.prepare.prepareurself.authentication.data.model.AuthenticationResponseModel;
import com.prepare.prepareurself.resources.data.model.VideoShareResponseModel;
import com.prepare.prepareurself.search.models.SearchResponseModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
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
                                          @Query("page") int pageNumber);

    @POST("get-resources")
    Call<GetResourcesResponse> getResources(@Query("token") String token,
                                            @Query("topic_id") int topicId,
                                            @Query("page") int pageNumber,
                                            @Query("count") int count,
                                            @Query("type") String type);
    @POST("view-resource-project")
    Call<ResourceViewsResponse> resourceViewed(@Query("token") String token,
                                               @Query("resource_id") int resourceId);

    @POST("view-resource-project")
    Call<ResourceViewsResponse> projectViewed(@Query("token") String token,
                                               @Query("project_id") int projectId);

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
                                                         @Query("preferences[]") List<String> integers);

    @POST("get-projects")
    Call<GetProjectResponse> getProjects(@Query("token") String token,
                                         @Query("course_id") int courseId,
                                         @Query("count") int count,
                                         @Query("page") int pageNumber);

    @POST("get-all-preferences")
    Call<AllPreferencesResponseModel> getAllPreferences(@Query("token") String token);

    @POST("get-all-preferences")
    Call<PrefernceResponseModel> getPreferences(@Query("token") String token);

    @POST("update-password")
    Call<UpdatePasswordResponseModel> updatePassword(@Query("token") String token,
                                                     @Query("oldPassword") String oldPassword,
                                                     @Query("newPassword") String newPassword);

    @POST("forget-password")
    Call<ForgotPasswordResponseModel> forgotPassword(@Query("email") String email);

    @POST("hit-like")
    Call<ResourceLikesResponse> likeProject(@Query("token") String token,
                                            @Query("project_id") int id,
                                            @Query("like") int like);

    @POST("resource")
    Call<VideoShareResponseModel> getResouceById(@Query("token") String token,
                                                 @Query("resource_id") int resourceId);

    @Multipart
    @POST("update-user")
    Call<UpdatePreferenceResponseModel> uploadImage(@Query("token") String token,
                                                    @Part MultipartBody.Part image);

    @Multipart
    @POST("upload-query-image")
    Call<UploadImageResponseModel> uploadQueryImage(@Query("token") String token,
                                                    @Query("type") int type,
                                                    @Part MultipartBody.Part image);

    @POST("project")
    Call<ProjectResponseModel> getProjectById(@Query("token") String token,
                                              @Query("project_id") int projectId);

    @POST("get-banner")
    Call<BannerImageResponseModel> getBanners(@Query("token") String token);

    @POST("get-suggested-projects")
    Call<GetSuggestedProjectsModel> getSuggestedProjects(@Query("token") String token);

    @POST("get-suggested-topics")
    Call<GetSuggestedTopicsModel> getSuggestedTopics(@Query("token") String token);


    @POST("store-feedback")
    Call<FeedbacksubmitModel> saveFeedback(@Query("token") String token,
                                           @Query("answers[]") List<String> answers);

    @POST("search-without-pagination")
    Call<SearchResponseModel> search(@Query("token") String token,
                                     @Query("query") String query);

    @POST("search")
    Call<SearchResponseModel> searchWithPagination(@Query("token") String token,
                                                   @Query("query") String query,
                                                   @Query("count") int count,
                                                   @Query("page") int page);

    @POST("get-home-page")
    Call<HomepageResponseModel> fetchHomePage(@Query("token") String token);

    @POST("get-my-liked-things")
    Call<FavouritesResponseModel> fetchFavourites(@Query("token") String token,
                                                  @Query("type") String type,
                                                  @Query("count") int count,
                                                  @Query("page") int page);

    @POST("update-user")
    Call<UpdatePreferenceResponseModel> updateAndroidToken(@Query("token") String token,
                                                         @Query("android_token") String androidToken);

    @POST("user")
    Call<AuthenticationResponseModel> getUser(@Query("token") String token);

    @POST("social-register")
    Call<AuthenticationResponseModel> socialRegister(@Query("first_name") String firstName,
                                                     @Query("last_name") String lastName,
                                                     @Query("android_token") String androidToken,
                                                     @Query("google_id") String googleId,
                                                     @Query("email") String email,
                                                     @Query("profile_image") String profileImage);

    @POST("course")
    Call<CourseDetailReponseModel> fetchCourseDetails(@Query("token") String token,
                                                      @Query("course_id") int courseId);

    @POST("rate-course")
    Call<RateCourseResponseModel> fetchRateCourseDetails(@Query("token") String token,
                                                         @Query("course_id") int courseId,
                                                         @Query("rating") int rating);

    @POST("get-user-preferences")
    Call<PrefernceResponseModel> getUserPrefs(@Query("token") String token);

    @POST("update-user-preferences")
    Call<UpdatePrefResponseModel> updateUserPref(@Query("token") String token,
                                                 @Query("preferences[]") List<Integer> list);

    @POST("get-course-quiz")
    Call<QuizResponseModel> fetchQuiz(@Query("token") String token,
                                      @Query("course_id") Integer courseId,
                                      @Query("level") String level);

    @POST("add-to-user-preferences")
    Call<AddToUserPrefResponseModel> addToUserPref(@Query("token") String token,
                                                   @Query("course_id") int course_id,
                                                   @Query("type") int type);

    @POST("save-course-quiz-response")
    Call<QuizAnswerResponse> submitQuiz(@Query("token") String token,
                            @Query("quiz_id")Integer quizId,
                            @Body List<ResponsesModel> responses);

    @POST("save-individual-quiz-response")
    Call<QuizAnswerResponse> submitIndividualQuestion(@Query("token") String token,
                                                      @Query("quiz_id")Integer quizId,
                                                      @Query("question_id")Integer questionId,
                                                      @Query("answer_id")Integer optionId);

    @POST("ask-query")
    Call<ForumPostQueryResponseModel> askQuery(@Query("token") String token,
                                               @Query("course_id") int courseId,
                                               @Query("query") String query,
                                               @Query("images")ArrayList<String> images);

    @POST("get-queries")
    Call<GetQueriesResponseModel> getQueries(@Query("token") String token,
                                             @Query("course_id") int courseId,
                                             @Query("count") int count,
                                             @Query("page") int page);

    @POST("get-query-replies")
    Call<GetQueriesResponseModel> getReplies(@Query("token") String token,
                                             @Query("query_id") int queryId,
                                             @Query("count") int count,
                                             @Query("page") int page);

    @POST("do-reply")
    Call<DoReplyResponseModel> doReply(@Query("token") String token,
                                       @Query("query_id")int queryId,
                                       @Query("reply")String reply);

}
