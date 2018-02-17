package com.studio.mpak.newsby.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.studio.mpak.newsby.data.DatabaseHelper;
import com.studio.mpak.newsby.data.article.ArticleContract;
import com.studio.mpak.newsby.data.category.CategoryEnum;
import com.studio.mpak.newsby.data.relation.ArticleCategoryContract;
import com.studio.mpak.newsby.domain.Article;

import java.util.List;

/**
 * @author Andrei Kuzniatsou
 */
public class ArticleRepository implements IRepository<Article>{

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public ArticleRepository(Context context) {
        this.dbHelper = new DatabaseHelper(context);

    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void insert(Article article) {
        ContentValues values = new ContentValues();
        values.put(ArticleContract.ArticleEntry._ID, article.getId());
        values.put(ArticleContract.ArticleEntry.COLUMN_TITLE, article.getTitle());
        values.put(ArticleContract.ArticleEntry.COLUMN_URL, article.getArticleUrl());
        values.put(ArticleContract.ArticleEntry.COLUMN_SCR_IMAGE, article.getImageUrl());
        values.put(ArticleContract.ArticleEntry.COLUMN_PUB_DATE, article.getDate());
        values.put(ArticleContract.ArticleEntry.COLUMN_CREATED_DATE, System.currentTimeMillis());
        database.insert(ArticleContract.ArticleEntry.TABLE_NAME, null, values);


        List<String> categories = article.getCategories();
        categories.retainAll(CategoryEnum.categories());

        for (String category : categories) {
            values = new ContentValues();
            values.put(ArticleCategoryContract.ArticleCategoryEntry.COLUMN_ARTICLE_ID, article.getId());
            values.put(ArticleCategoryContract.ArticleCategoryEntry.COLUMN_CATEGORY_ID, CategoryEnum.lookupByName(category).getId());
            database.insert(ArticleCategoryContract.ArticleCategoryEntry.TABLE_NAME, null, values);
        }
    }

    public Article last() {
        Article article = null;
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("select * from articles order by created_date asc limit 1", null);
            if (cursor.moveToFirst()) {
                do {
                    article = new Article();
                    article.setId(cursor.getInt(cursor.getColumnIndex(ArticleContract.ArticleEntry._ID)));
                    article.setDate(cursor.getString(cursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_PUB_DATE)));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return article;
    }
}
