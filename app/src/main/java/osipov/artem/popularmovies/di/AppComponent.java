package osipov.artem.popularmovies.di;

import dagger.Component;
import javax.inject.Singleton;

import osipov.artem.popularmovies.ui.MovieDetailViewModel;
import osipov.artem.popularmovies.ui.MovieListViewModel;

/**
 * Created by Artem Osipov on 06/03/2018.
 * ao@enlighted.ru
 */

@Singleton
@Component(modules={AppModule.class})
public interface AppComponent {
   void inject(MovieListViewModel movieListViewModel);
   void inject(MovieDetailViewModel movieDetailViewModel);
}
