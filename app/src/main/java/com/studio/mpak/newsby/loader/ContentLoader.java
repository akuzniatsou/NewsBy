package com.studio.mpak.newsby.loader;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import com.studio.mpak.newsby.domain.Article;
import com.studio.mpak.newsby.domain.HttpStatus;
import com.studio.mpak.newsby.domain.Response;
import com.studio.mpak.newsby.parser.ArticleListParser;
import com.studio.mpak.newsby.parser.DocumentParser;
import com.studio.mpak.newsby.repository.ArticleRepository;
import com.studio.mpak.newsby.util.AppUtil;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.format.DateTimeFormat;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * @author Andrei Kuzniatsou
 */
public class ContentLoader extends AsyncTaskLoader<ArrayList<Article>> {

    private static final String LOG_TAG = ContentLoader.class.getSimpleName();
    private static final String DEFAULT_URL = "http://www.orshanka.by/?m=";
    private static final int DEFAULT_DURATION = 2;

    private ArticleRepository repository;

    public ContentLoader(Context context) {
        super(context);
    }

    @Override
    public ArrayList<Article> loadInBackground() {
        DocumentParser<ArrayList<Article>> parser = new ArticleListParser();
        repository = new ArticleRepository(getContext());
        repository.open();

        DateTime currentDate = new DateTime();

        String last = repository.getLastUpdatedDate();
        int duration;
        if (null != last) {
            DateTime lastUpdateDate = DateTime.parse(last, DateTimeFormat.forPattern("dd.MM.yyyy"));
            duration = getDurationOrDefault(lastUpdateDate, currentDate);
        } else {
            duration = DEFAULT_DURATION;
        }

        ArrayList<Article> articles = new ArrayList<>();

        for (int i = 0; i < duration; i++) {
            String url = DEFAULT_URL + getPeriod(currentDate);

            Response response = AppUtil.get(url);

            if (HttpStatus.NOT_FOUND.equals(response.getStatus())) {
                Log.e(LOG_TAG, "There are no new articles on this month");
                if (duration > 1) {
                    duration++;
                    currentDate = currentDate.minusMonths(1);
                }
                continue;
            } else if (response.getStatus().isError()) {
                Log.e(LOG_TAG, "Could not fetch articles, server error");
                return articles;
            }

            articles.addAll(parser.parse(response.getData()));
            Integer pageSize = parsePageSize(response.getData());
            for (int page = 2; page < pageSize + 1; page++) {
                response = AppUtil.get(url + "&paged=" + page);
                if (response.getStatus().isSuccessful()) {
                    articles.addAll(parser.parse(response.getData()));
                } else {
                    Log.e(LOG_TAG, "Could not fetch articles, server error");
                }
            }
            currentDate = currentDate.minusMonths(1);
        }

        for (Article article : articles) {
            repository.insert(article);
        }
        return articles;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    protected void onAbandon() {
        if (repository != null) {
            repository.close();
        }
        super.onAbandon();
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
