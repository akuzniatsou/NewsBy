package com.studio.mpak.newsby.repository;

import static com.studio.mpak.newsby.util.AppUtil.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.studio.mpak.newsby.data.DatabaseHelper;
import com.studio.mpak.newsby.data.article.ArticleContract.ArticleEntry;
import com.studio.mpak.newsby.data.category.CategoryEnum;
import com.studio.mpak.newsby.data.relation.ArticleCategoryContract;
import com.studio.mpak.newsby.data.relation.ArticleCategoryContract.ArticleCategoryEntry;
import com.studio.mpak.newsby.domain.Article;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Andrei Kuzniatsou
 */
public class ArticleRepository implements IRepository<Article>{

    private static final String CATEGORY = join(ArticleCategoryEntry.TABLE_NAME, ArticleCategoryEntry.COLUMN_CATEGORY_ID);
    private static final String ARTICLE_ID = join(ArticleEntry.TABLE_NAME, ArticleEntry._ID);
    private static final String ARTICLE_ID_JOIN = join(ArticleCategoryEntry.TABLE_NAME, ArticleCategoryEntry.COLUMN_ARTICLE_ID);
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
        values.put(ArticleEntry._ID, article.getId());
        values.put(ArticleEntry.COLUMN_TITLE, article.getTitle());
        values.put(ArticleEntry.COLUMN_URL, article.getArticleUrl());
        values.put(ArticleEntry.COLUMN_SCR_IMAGE, article.getImageUrl());
        values.put(ArticleEntry.COLUMN_PUB_DATE, article.getDate());
        values.put(ArticleEntry.COLUMN_CREATED_DATE, System.currentTimeMillis());
        database.insert(ArticleEntry.TABLE_NAME, null, values);


        Set<String> categories = new HashSet<>(article.getCategories());
        categories.retainAll(CategoryEnum.categories());

        for (String category : categories) {
            values = new ContentValues();
            values.put(ArticleCategoryEntry.COLUMN_ARTICLE_ID, article.getId());
            values.put(ArticleCategoryEntry.COLUMN_CATEGORY_ID, CategoryEnum.lookupByName(category).getId());
            database.insert(ArticleCategoryEntry.TABLE_NAME, null, values);
        }
    }

    public Article last() {
        Article article = null;
        Cursor cursor = null;
        try {
            cursor = database.rawQuery("select * from " + ArticleEntry.TABLE_NAME
                    + " order by " + ArticleEntry.COLUMN_CREATED_DATE + " asc limit 1", null);
            if (cursor.moveToFirst()) {
                do {
                    article = new Article();
                    article.setId(cursor.getInt(cursor.getColumnIndex(ArticleEntry._ID)));
                    article.setDate(cursor.getString(cursor.getColumnIndex(ArticleEntry.COLUMN_PUB_DATE)));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return article;
    }

    public Cursor getArticles(int categoryId) {
        return database.rawQuery("select * from " + ArticleEntry.TABLE_NAME
                        + " inner join " + ArticleCategoryEntry.TABLE_NAME + " on "
                        + ARTICLE_ID + " = " + ARTICLE_ID_JOIN + " where " + CATEGORY + " = ?",
                new String[]{String.valueOf(categoryId)});
    }
}
