package osipov.artem.popularmovies;

import android.app.Application;
import com.facebook.stetho.Stetho;
import osipov.artem.popularmovies.di.AppComponent;
import osipov.artem.popularmovies.di.AppModule;
import osipov.artem.popularmovies.di.DaggerAppComponent;

/**
 * Created by Artem Osipov on 04/03/2018.
 * ao@enlighted.ru
 */

public class App extends Application {

    private static App instance;
    private AppComponent mAppComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Stetho.initializeWithDefaults(this);

        mAppComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();

    }

    public static App getInstance() {
        return instance;
    }

    public AppComponent getAppComponent() {
        return mAppComponent;
    }
}
