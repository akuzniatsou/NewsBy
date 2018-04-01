package com.studio.mpak.newsby.loader;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import com.studio.mpak.newsby.repository.ArticleRepository;

/**
 * @author Andrei Kuzniatsou
 */
public class ArticleCursorLoader extends SimpleCursorLoader {

    private int categoryId;
    private ArticleRepository repository;

    public ArticleCursorLoader(int categoryId, Context context) {
        super(context);
        this.categoryId = categoryId;
        this.repository = new ArticleRepository(context);
    }

    @Override
    public Cursor loadInBackground() {
        Cursor cursor = null;
        try {
            repository.open();
            cursor = repository.getArticles(categoryId);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (cursor != null) {
            cursor.getCount();
        }

        return cursor;
    }
}
