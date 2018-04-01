package com.studio.mpak.newsby.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import com.studio.mpak.newsby.domain.Announcement;
import com.studio.mpak.newsby.domain.Response;
import com.studio.mpak.newsby.domain.Vacancy;
import com.studio.mpak.newsby.parser.DocumentParser;
import com.studio.mpak.newsby.util.AppUtil;

import java.util.ArrayList;

/**
 * @author Andrei Kuzniatsou
 */
public class VacancyLoader extends AsyncTaskLoader<ArrayList<Announcement<Vacancy>>> {

    private static final String LOG_TAG = VacancyLoader.class.getSimpleName();
    private static final String URL = "http://www.orshanka.by/?page_id=5342";
    private final DocumentParser<ArrayList<Announcement<Vacancy>>> parser;

    public VacancyLoader(Context context, DocumentParser<ArrayList<Announcement<Vacancy>>> parser) {
        super(context);
        this.parser = parser;
    }

    @Override
    public ArrayList<Announcement<Vacancy>> loadInBackground() {
        ArrayList<Announcement<Vacancy>> result = new ArrayList<>(0);
        Response response = AppUtil.get(URL);
        if (response.getStatus().isError()) {
            Log.e(LOG_TAG, "Error while loading vacancy, " + response.getStatus().getReasonPhrase());
        } else {
            result = parser.parse(response.getData());
        }
        return result;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
