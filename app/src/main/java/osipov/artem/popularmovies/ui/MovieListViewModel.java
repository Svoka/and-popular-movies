package osipov.artem.popularmovies.ui;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.database.Cursor;
import android.util.Log;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import javax.inject.Inject;
import osipov.artem.popularmovies.App;
import osipov.artem.popularmovies.interactors.MovieInteractor;
import osipov.artem.popularmovies.interactors.PreferredViewInteractor;

/**
 * Created by Artem Osipov on 22/02/2018.
 * ao@enlighted.ru
 */

public class MovieListViewModel extends ViewModel {

    private MutableLiveData<Cursor> mMoviesCursor;


    @Inject MovieInteractor mMovieInteractor;
    @Inject PreferredViewInteractor mPrefferedViewInteractor;

    public MovieListViewModel() {
        App.getInstance().getAppComponent().inject(this);
    }

    public MutableLiveData<Cursor> getMoviesCursor() {
        if (mMoviesCursor == null) {
            this.mMoviesCursor = new MutableLiveData<>();

            mMovieInteractor.getMoviesList()
                    .observeOn(Schedulers.trampoline())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            cursor -> {
                                mMoviesCursor.setValue(cursor);
                            },
                            throwable -> {
                                throwable.printStackTrace();
                            }
                    );
        }
        return this.mMoviesCursor;
    }


    public void setPreferedView(int preferedView) {
        mPrefferedViewInteractor.setPreferredView(preferedView);
    }

}
