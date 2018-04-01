package com.studio.mpak.newsby.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import com.studio.mpak.newsby.domain.Article;
import com.studio.mpak.newsby.domain.Response;
import com.studio.mpak.newsby.parser.DocumentParser;
import com.studio.mpak.newsby.util.AppUtil;

/**
 * @author Andrei Kuzniatsou
 */
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
        Article article = null;
        Response response = AppUtil.get(url);
        if (response.getStatus().isError()) {
            Log.e(LOG_TAG, "Error while loading article, " + response.getStatus().getReasonPhrase());
        } else {
            article = parser.parse(response.getData());
        }
        return article;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }
}
