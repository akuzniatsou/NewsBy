package com.studio.mpak.newsby.service;

import static java.lang.String.format;

import android.app.IntentService;
import android.content.*;
import android.os.*;
import android.support.annotation.Nullable;
import android.util.Log;
import com.studio.mpak.newsby.domain.Article;
import com.studio.mpak.newsby.domain.Response;
import com.studio.mpak.newsby.parser.ArticleParser;
import com.studio.mpak.newsby.repository.ArticleRepository;
import com.studio.mpak.newsby.util.AppUtil;

public class BackgroundService extends IntentService {

    private static final String LOG_TAG = BackgroundService.class.getSimpleName();
    private ArticleRepository repository;

    public BackgroundService() {
        super(BackgroundService.class.getSimpleName());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        repository = new ArticleRepository(this);
        repository.open();
        long countArticleWithoutContent;
        while ((countArticleWithoutContent = repository.countArticleWithoutContent()) > 0) {
            Log.i(LOG_TAG, format("%d: articles to update", countArticleWithoutContent));
            updateData();
            sleep(3000);
        }
    }

    @Override
    public void onDestroy() {
        repository.close();
    }

    private void updateData() {
        Article articleWithoutContent = repository.findArticleWithoutContent();
        Response response = AppUtil.get(articleWithoutContent.getArticleUrl());
        if (response.getStatus().isError()) {
            Log.e(LOG_TAG, "Error while loading article, " + articleWithoutContent.getArticleUrl() +
                    " due to " + response.getStatus().getReasonPhrase());
        } else {
            Article article = new ArticleParser().parse(response.getData());
            Log.e(LOG_TAG, "Successfully updated:  " + article.getArticleUrl());
            repository.update(article);
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
