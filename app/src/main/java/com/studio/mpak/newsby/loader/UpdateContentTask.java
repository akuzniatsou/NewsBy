package com.studio.mpak.newsby.loader;

import android.content.Context;
import android.util.Log;
import com.studio.mpak.newsby.domain.Article;
import com.studio.mpak.newsby.parser.ArticleListParser;
import com.studio.mpak.newsby.parser.DocumentParser;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Andrei Kuzniatsou
 */
public class UpdateContentTask {

    private static final String LOG_TAG = UpdateContentTask.class.getSimpleName();
    private static final String DEFAULT_URL = "http://www.orshanka.by/?cat=";
    private final DocumentParser<ArrayList<Article>> parser;

    public UpdateContentTask() {
        this.parser = new ArticleListParser();
    }

    private Integer parsePageSize(Document document) {
        Elements select = document.select(".pages");
        String text = select.text();
        String pages = text.substring(text.lastIndexOf(" ") + 1, text.length());
        return Integer.parseInt(pages);
    }

    public ArrayList<Article> doInBackground(int category) {
        Document document;
        ArrayList<Article> articles = new ArrayList<>();
        try {
            String url = DEFAULT_URL + category;
            document = Jsoup.connect(url).timeout(10000).get();
            articles.addAll(parser.parse(document));
            Integer pageSize = parsePageSize(document);
            for (int page = 2; page < pageSize + 1; page++) {
                document = Jsoup.connect(url + "&paged=" + page).timeout(10000).get();
                articles.addAll(parser.parse(document));
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return articles;
    }

    public ArrayList<Article> doInBackground(int category, int totalItemsCount) {
        int startPage = 2;
        int skipPages = totalItemsCount < 10 ? 0 :(int) Math.ceil(totalItemsCount / 10.0);
        int pageSize;
        int fetchSize;


        Document document;
        ArrayList<Article> articles = new ArrayList<>();
        try {
            String url = DEFAULT_URL + category;
            document = Jsoup.connect(url).timeout(10000).get();
            pageSize = parsePageSize(document);
            if (skipPages < 1) {
                articles.addAll(parser.parse(document));
                fetchSize = pageSize - startPage > 1 ? startPage + 2 : pageSize + 1;
            } else {
                startPage += skipPages - 1;
                fetchSize = pageSize - startPage > 1 ? startPage + 3 : pageSize + 1;
            }
            for (int page = startPage; page < fetchSize; page++) {
                document = Jsoup.connect(url + "&paged=" + page).timeout(10000).get();
                articles.addAll(parser.parse(document));
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error with creating URL", e);
        }
        return articles;
    }
}
