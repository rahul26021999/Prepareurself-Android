package com.prepare.prepareurself.persistence;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.prepare.prepareurself.courses.data.db.dao.ProjectsRoomDao;
import com.prepare.prepareurself.courses.data.db.dao.TopicsRoomDao;
import com.prepare.prepareurself.courses.data.model.ProjectsModel;
import com.prepare.prepareurself.courses.data.model.TopicsModel;
import com.prepare.prepareurself.banner.BannerImageRoomDao;
import com.prepare.prepareurself.dashboard.data.db.dao.CourseRoomDao;
import com.prepare.prepareurself.dashboard.data.db.dao.SuggestedProjectsDao;
import com.prepare.prepareurself.dashboard.data.db.dao.SuggestedTopicsDao;
import com.prepare.prepareurself.banner.BannerModel;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.dashboard.data.model.SuggestedProjectModel;
import com.prepare.prepareurself.dashboard.data.model.SuggestedTopicsModel;
import com.prepare.prepareurself.favourites.data.db.dao.LikedProjectsDao;
import com.prepare.prepareurself.favourites.data.db.dao.LikedResourcesDao;
import com.prepare.prepareurself.favourites.data.model.LikedProjectsModel;
import com.prepare.prepareurself.favourites.data.model.LikedResourcesModel;
import com.prepare.prepareurself.persistence.persitenceUtils.CourseModelTypeCorveter;
import com.prepare.prepareurself.persistence.persitenceUtils.ListProjectTypeConverter;
import com.prepare.prepareurself.persistence.persitenceUtils.ListResourceTypeConverter;
import com.prepare.prepareurself.persistence.persitenceUtils.ListTopicsTypeConverter;
import com.prepare.prepareurself.profile.data.db.dao.MyPreferenceDao;
import com.prepare.prepareurself.profile.data.db.dao.UserPrefernceDao;
import com.prepare.prepareurself.profile.data.model.MyPreferenceTechStack;
import com.prepare.prepareurself.profile.data.model.PreferredTechStack;
import com.prepare.prepareurself.resources.data.db.dao.ResourcesRoomDao;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.authentication.data.db.dao.UserRoomDao;
import com.prepare.prepareurself.authentication.data.model.UserModel;
import com.prepare.prepareurself.persistence.persitenceUtils.ThumbnaiTypeConverter;
import com.prepare.prepareurself.persistence.persitenceUtils.VideoContentDetailsConverter;
import com.prepare.prepareurself.persistence.persitenceUtils.VideoSnippetConverter;
import com.prepare.prepareurself.search.models.SearchModel;
import com.prepare.prepareurself.search.db.SearchDao;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.db.PlaylistContentDeatilsDao;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.db.SingleVideoItemDao;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.SingleVIdeoItemWrapper;
import com.prepare.prepareurself.youtubeplayer.youtubeplaylistapi.models.VideoItemWrapper;

@Database(entities = {
        UserModel.class,
        CourseModel.class,
        TopicsModel.class,
        ResourceModel.class,
        ProjectsModel.class,
        VideoItemWrapper.class,
        SingleVIdeoItemWrapper.class,
        PreferredTechStack.class,
        MyPreferenceTechStack.class,
        BannerModel.class,
        SuggestedTopicsModel.class,
        SuggestedProjectModel.class,
        SearchModel.class,
        LikedProjectsModel.class,
        LikedResourcesModel.class
}, version = 1)
@TypeConverters({VideoContentDetailsConverter.class,
        VideoSnippetConverter.class,
        ThumbnaiTypeConverter.class,
        CourseModelTypeCorveter.class,
        ListTopicsTypeConverter.class,
        ListResourceTypeConverter.class,
        ListProjectTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserRoomDao userRoomDao();
    public abstract CourseRoomDao courseRoomDao();
    public abstract TopicsRoomDao topicsRoomDao();
    public abstract ResourcesRoomDao resourcesRoomDao();
    public abstract ProjectsRoomDao projectsRoomDao();
    public abstract PlaylistContentDeatilsDao playlistVideosDao();
    public abstract SingleVideoItemDao singleVideoItemDao();
    public abstract UserPrefernceDao userPrefernceDao();
    public abstract MyPreferenceDao myPreferenceDao();
    public abstract BannerImageRoomDao bannerImageRoomDao();
    public abstract SuggestedTopicsDao suggestedTopicsDao();
    public abstract SuggestedProjectsDao suggestedProjectsDao();
    public abstract SearchDao searchDao();
    public abstract LikedProjectsDao likedProjectsDao();
    public abstract LikedResourcesDao likedResourcesDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context){
        if (INSTANCE == null){
            synchronized (AppDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class,"app_database")
                            .build();
                }
            }
        }

        return INSTANCE;
    }

}
