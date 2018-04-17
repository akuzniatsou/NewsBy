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


    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_ERROR = 2;

    private static final String LOG_TAG = BackgroundService.class.getSimpleName();
    private ArticleRepository repository;
    private int period;

    public BackgroundService() {
        super(BackgroundService.class.getSimpleName());
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        final ResultReceiver receiver = intent.getParcelableExtra("receiver");
        period = intent.getIntExtra("period", 1);
        Bundle bundle = new Bundle();
        repository = new ArticleRepository(this);
        repository.open();
        long countToUpdate = repository.countArticleWithoutContent(period);
        long initialCount = countToUpdate;
        while (countToUpdate > 0) {
            Log.i(LOG_TAG, format("%d: articles to update", countToUpdate));
            updateData();
            bundle.putLong(Intent.EXTRA_TEXT, (long) (100 - Math.ceil(countToUpdate * 100 / initialCount)));
            receiver.send(STATUS_RUNNING, bundle);
            countToUpdate = repository.countArticleWithoutContent(period);
            sleep(3000);
        }
    }

    @Override
    public void onDestroy() {
        repository.close();
    }

    private void updateData() {
        Article articleWithoutContent = repository.findArticleWithoutContent(period);
        if (null == articleWithoutContent) {
            return;
        }
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
