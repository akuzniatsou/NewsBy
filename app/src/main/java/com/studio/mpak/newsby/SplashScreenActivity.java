package com.studio.mpak.newsby;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import com.studio.mpak.newsby.domain.Article;
import com.studio.mpak.newsby.loader.ContentLoader;
import com.studio.mpak.newsby.parser.ArticleListParser;
import com.studio.mpak.newsby.repository.ArticleRepository;
import com.studio.mpak.newsby.util.AppUtil;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import java.util.ArrayList;

/**
 * @author Andrei Kuzniatsou
 */
public class SplashScreenActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<Article>> {

    private ArticleRepository repository;
    private DateTime lastUpdateDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        repository = new ArticleRepository(this);
        repository.open();

        if (AppUtil.isConnected(this)) {
            lastUpdateDate = new DateTime();
            Article lastArticle = repository.last();
            if (null != lastArticle) {
                String date = lastArticle.getDate();
                lastUpdateDate = DateTime.parse(date, DateTimeFormat.forPattern("dd.MM.yyyy"));
            }
            LoaderManager loaderManager = getSupportLoaderManager();
            loaderManager.initLoader(0, null, this);
        } else {
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
        }


    }

    @Override
    public Loader<ArrayList<Article>> onCreateLoader(int id, Bundle args) {
        return new ContentLoader(lastUpdateDate, this, new ArticleListParser());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Article>> loader, ArrayList<Article> data) {
        storeToDB(data);
        Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
        startActivity(intent);
        repository.close();
    }

    private void storeToDB(ArrayList<Article> articles) {
        for (Article article : articles) {
            repository.insert(article);
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Article>> loader) {
        repository.close();
        loader.abandon();
    }

    @Override
    protected void onResume() {
        repository.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        repository.close();
        super.onPause();
    }
}
