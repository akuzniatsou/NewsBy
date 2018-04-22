package com.studio.mpak.newsby.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import com.studio.mpak.newsby.domain.Article;
import com.studio.mpak.newsby.parser.DocumentParser;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * @author Andrei Kuzniatsou
 */
public class ContentLoader extends AsyncTaskLoader<ArrayList<Article>> {

    private static final String LOG_TAG = ContentLoader.class.getSimpleName();
    private static final String DEFAULT_URL = "http://www.orshanka.by/?m=";
    private static final int DEFAULT_DURATION = 2;
    private static final int HTTP_STATUS = 404;

    private final DocumentParser<ArrayList<Article>> parser;
    private DateTime lastUpdateDate;

    public ContentLoader(DateTime lastUpdateDate, Context context, DocumentParser<ArrayList<Article>> parser) {
        super(context);
        this.lastUpdateDate = lastUpdateDate;
        this.parser = parser;
    }

    @Override
    public ArrayList<Article> loadInBackground() {
        Document document = null;
        ArrayList<Article> articles = new ArrayList<>();
        try {
            DateTime currentDate = new DateTime();
            int duration = getDurationOrDefault(lastUpdateDate, currentDate);
            for (int i = 0; i < duration; i++) {
                String url = DEFAULT_URL + getPeriod(currentDate);
                try {
                    document = Jsoup.connect(url).userAgent("Mozilla").timeout(10000).get();
                } catch (HttpStatusException e) {
                    if (HTTP_STATUS == e.getStatusCode()) {
                        Log.e(LOG_TAG, "There are no new articles on this month", e);
                        if (duration > 1) {
                            duration++;
                            currentDate = currentDate.minusMonths(1);
                        }
                        continue;
                    }
                }
                articles.addAll(parser.parse(document));
                Integer pageSize = parsePageSize(document);
                for (int page = 2; page < pageSize + 1; page++) {
                    document = Jsoup.connect(url + "&paged=" + page).userAgent("Mozilla").timeout(10000).get();
                    articles.addAll(parser.parse(document));
                }
                currentDate = currentDate.minusMonths(1);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return articles;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    private Integer parsePageSize(Document document) {
        int pagesNumber = 1;
        try {
            Elements select = document.select(".pages");
            if (null != select && select.size() > 0) {
                String text = select.text();
                String pages = text.substring(text.lastIndexOf(" ") + 1, text.length());
                pagesNumber = Integer.parseInt(pages);
            }
        } catch (Exception e) {
            Log.e(LOG_TAG, "Exception occurred during parsing page size", e);
        }
        return pagesNumber;
    }

    private String getPeriod(DateTime dt) {
        return dt.getYear() + new DecimalFormat("00").format(dt.getMonthOfYear());
    }

    private int getDurationOrDefault(DateTime lastUpdateDate, DateTime currentDate) {
        Duration durationDateTime = new Duration(lastUpdateDate, currentDate);
        int duration = (int) Math.ceil(durationDateTime.getStandardDays() / 30.0);
        return duration == 0 || duration > DEFAULT_DURATION ? DEFAULT_DURATION : duration;
    }
}
