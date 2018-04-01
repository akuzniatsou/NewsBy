package com.studio.mpak.newsby.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import com.studio.mpak.newsby.domain.Article;
import com.studio.mpak.newsby.parser.DocumentParser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class ArticleLoader extends AsyncTaskLoader<Article> {

    private static final String LOG_TAG = ArticleLoader.class.getSimpleName();
    private final DocumentParser<Article> parser;
    private String url;

    public ArticleLoader(String url, Context context, DocumentParser<Article> parser) {
        super(context);
        this.url = url;
        this.parser = parser;
    }

    @Override
    public Article loadInBackground() {
        Document document = null;
        try {
            document = Jsoup.connect(url).timeout(10000).get();
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
