package osipov.artem.popularmovies.di;

import android.app.Application;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;
import osipov.artem.popularmovies.interactors.MovieInteractor;
import osipov.artem.popularmovies.interactors.PreferredViewInteractor;

/**
 * Created by Artem Osipov on 06/03/2018.
 * ao@enlighted.ru
 */

@Module
public class AppModule {

    private Application mApplication;
    public AppModule(Application application) {
        mApplication = application;
    }

    @Provides
    @Singleton
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @Singleton
    PreferredViewInteractor providesPreferredViewInteractor(Application application) {
        return new PreferredViewInteractor(application);
    }

    @Provides
    @Singleton
    MovieInteractor provideMovieInteractor(Application application, PreferredViewInteractor preferredViewInteractor) {
        return new MovieInteractor(application, preferredViewInteractor);
    }
}
