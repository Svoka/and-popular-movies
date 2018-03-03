package osipov.artem.popularmovies.repository.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import osipov.artem.popularmovies.repository.model.Movie;

/**
 * Created by Artem Osipov on 23/02/2018.
 * ao@enlighted.ru
 */
@Database(entities = {Movie.class}, version = 1)
public abstract class PopularDatabase extends RoomDatabase {

    @SuppressWarnings("WeakerAccess")
    public abstract PopularDao movie();

    /** The only instance */
    private static PopularDatabase sInstance;


    /*
    * https://developer.android.com/training/data-storage/room/index.html
    * Note: You should follow the singleton design pattern when instantiating
    * an AppDatabase object, as each RoomDatabase instance is fairly expensive,
    * and you rarely need access to multiple instances.
    * */
    public static synchronized PopularDatabase getInstance(Context context) {
        if (sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), PopularDatabase.class, "popular")
                    .build();
        }
        return sInstance;
    }


}
