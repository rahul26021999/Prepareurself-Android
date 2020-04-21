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
import com.prepare.prepareurself.dashboard.data.db.dao.CourseRoomDao;
import com.prepare.prepareurself.dashboard.data.model.CourseModel;
import com.prepare.prepareurself.resources.data.db.dao.ResourcesRoomDao;
import com.prepare.prepareurself.resources.data.model.ResourceModel;
import com.prepare.prepareurself.authentication.data.db.dao.UserRoomDao;
import com.prepare.prepareurself.authentication.data.model.UserModel;
import com.prepare.prepareurself.utils.persitenceUtils.ThumbnaiTypeConverter;
import com.prepare.prepareurself.utils.persitenceUtils.VideoContentDetailsConverter;
import com.prepare.prepareurself.utils.persitenceUtils.VideoSnippetConverter;
import com.prepare.prepareurself.utils.youtubeplaylistapi.db.PlaylistContentDeatilsDao;
import com.prepare.prepareurself.utils.youtubeplaylistapi.models.VideoItemWrapper;

@Database(entities = {
        UserModel.class,
        CourseModel.class,
        TopicsModel.class,
        ResourceModel.class,
        ProjectsModel.class,
        VideoItemWrapper.class
}, version = 1)
@TypeConverters({VideoContentDetailsConverter.class,
        VideoSnippetConverter.class,
        ThumbnaiTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserRoomDao userRoomDao();
    public abstract CourseRoomDao courseRoomDao();
    public abstract TopicsRoomDao topicsRoomDao();
    public abstract ResourcesRoomDao resourcesRoomDao();
    public abstract ProjectsRoomDao projectsRoomDao();
    public abstract PlaylistContentDeatilsDao playlistVideosDao();

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
