package com.studio.mpak.newsby.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import com.studio.mpak.newsby.domain.Announcement;
import com.studio.mpak.newsby.domain.Vacancy;
import com.studio.mpak.newsby.parser.DocumentParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

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
        Document document = null;
        try {
            document = Jsoup.connect(URL).timeout(10000).get();
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return parser.parse(document);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
