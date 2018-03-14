package osipov.artem.popularmovies.interactors;

import static android.content.Context.MODE_PRIVATE;

import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import io.reactivex.subjects.BehaviorSubject;
import javax.inject.Inject;

/**
 * Created by Artem Osipov on 06/03/2018.
 * ao@enlighted.ru
 */

public class PreferredViewInteractor {

    private SharedPreferences mSharedPreferences;

    public static final String ARG_PREFS = "APP_PREFS";
    public static final String ARG_PREFERED_VIEW = "PREFERED_VIEW";
    public static final Integer ARG_VIEW_HIGH_RATED = 0;
    public static final Integer ARG_VIEW_POPULAR = 1;
    public static final Integer ARG_VIEW_FAVOURITE = 2;

    public BehaviorSubject<Integer> preferredView;
    private int currentPref = -1;

    @Inject
    public PreferredViewInteractor(Application application){
        mSharedPreferences = application.getSharedPreferences(ARG_PREFS, MODE_PRIVATE);
        preferredView = BehaviorSubject.create();
        preferredView.onNext(getPreferredView());
    }


    public int getPreferredView() {
        return mSharedPreferences.getInt(ARG_PREFERED_VIEW, ARG_VIEW_HIGH_RATED);
    }

    public void setPreferredView(int view) {
        if (view == currentPref) {
            return;
        } else {
            currentPref = view;
        }
        Editor editor = mSharedPreferences.edit();
        editor.putInt(ARG_PREFERED_VIEW, view);
        editor.apply();
        preferredView.onNext(view);
    }

}
