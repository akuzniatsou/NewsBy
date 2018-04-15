package com.studio.mpak.newsby.repository;

import static com.studio.mpak.newsby.util.AppUtil.join;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.studio.mpak.newsby.data.DatabaseHelper;
import com.studio.mpak.newsby.data.article.ArticleContract.ArticleEntry;
import com.studio.mpak.newsby.data.category.CategoryEnum;
import com.studio.mpak.newsby.data.relation.ArticleCategoryContract.ArticleCategoryEntry;
import com.studio.mpak.newsby.domain.Article;
import org.jsoup.helper.StringUtil;

import java.util.ArrayList;
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

    public void update(Article article) {
        if (database.isOpen()) {
            ContentValues values = new ContentValues();
            values.put(ArticleEntry.COLUMN_CONTENT, article.getContent());
            values.put(ArticleEntry.COLUMN_PREV_ID, article.getPrev().getId());
            values.put(ArticleEntry.COLUMN_NEXT_ID, article.getNext().getId());
            database.update(ArticleEntry.TABLE_NAME, values, ArticleEntry._ID + "= ?",
                    new String[]{String.valueOf(article.getId())});
        }
    }

    public Article last() {
        Article article = null;
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(String.format(
                    "select * from %s order by %s asc limit 1",
                    ArticleEntry.TABLE_NAME, ArticleEntry.COLUMN_CREATED_DATE),
                    null);
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

    public Article findArticle(Integer articeId) {
        Article article = null;
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(String.format(
                    "select * from %s where %s = ?",
                    ArticleEntry.TABLE_NAME, ArticleEntry._ID),
                    new String[]{String.valueOf(articeId)});
            if (cursor.moveToFirst()) {
                do {
                    article = new Article();
                    article.setId(cursor.getInt(cursor.getColumnIndex(ArticleEntry._ID)));
                    article.setDate(cursor.getString(cursor.getColumnIndex(ArticleEntry.COLUMN_PUB_DATE)));
                    article.setArticleUrl(cursor.getString(cursor.getColumnIndex(ArticleEntry.COLUMN_URL)));
                    article.setTitle(cursor.getString(cursor.getColumnIndex(ArticleEntry.COLUMN_TITLE)));
                    article.setContent(cursor.getString(cursor.getColumnIndex(ArticleEntry.COLUMN_CONTENT)));
                    Integer prev_id = cursor.getInt(cursor.getColumnIndex(ArticleEntry.COLUMN_PREV_ID));
                    Integer next_id = cursor.getInt(cursor.getColumnIndex(ArticleEntry.COLUMN_NEXT_ID));
                    if (null != prev_id) article.setPrev(new Article(prev_id));
                    if (null != next_id) article.setNext(new Article(next_id));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return article;
    }

    public List<Article> findArticles(List<Integer> articeIds) {
        List<Article> articles = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(String.format(
                    "select * from %s where %s in (%s)",
                    ArticleEntry.TABLE_NAME, ArticleEntry._ID, StringUtil.join(articeIds,",")),
                    null);
            if (cursor.moveToFirst()) {
                do {
                    Article article = new Article();
                    article.setId(cursor.getInt(cursor.getColumnIndex(ArticleEntry._ID)));
                    article.setDate(cursor.getString(cursor.getColumnIndex(ArticleEntry.COLUMN_PUB_DATE)));
                    article.setArticleUrl(cursor.getString(cursor.getColumnIndex(ArticleEntry.COLUMN_URL)));
                    article.setContent(cursor.getString(cursor.getColumnIndex(ArticleEntry.COLUMN_CONTENT)));
                    int prev_id = cursor.getInt(cursor.getColumnIndex(ArticleEntry.COLUMN_PREV_ID));
                    int next_id = cursor.getInt(cursor.getColumnIndex(ArticleEntry.COLUMN_NEXT_ID));
                    article.setPrev(new Article(prev_id));
                    article.setPrev(new Article(next_id));
                    articles.add(article);

                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return articles;
    }

    public Article findArticleWithoutContent() {
        Article article = null;
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(String.format(
                    "select * from %s where %s is null",
                    ArticleEntry.TABLE_NAME, ArticleEntry.COLUMN_CONTENT),
                    null);
            if (cursor.moveToFirst()) {
                do {
                    article = new Article();
                    article.setId(cursor.getInt(cursor.getColumnIndex(ArticleEntry._ID)));
                    article.setArticleUrl(cursor.getString(cursor.getColumnIndex(ArticleEntry.COLUMN_URL)));
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return article;
    }

    public long countArticleWithoutContent() {
        long numEntries = 0;
        if (database.isOpen()) {
            numEntries = DatabaseUtils.queryNumEntries(database, ArticleEntry.TABLE_NAME, ArticleEntry.COLUMN_CONTENT + " is null");
        }
        return numEntries;
    }

    public Cursor getArticles(Integer categoryId) {
        if (null == categoryId) {
            return null;
        }
        return database.rawQuery(String.format(
                "select * from %s inner join %s on %s = %s where %s = ?",
                ArticleEntry.TABLE_NAME, ArticleCategoryEntry.TABLE_NAME, ARTICLE_ID, ARTICLE_ID_JOIN, CATEGORY),
                new String[]{String.valueOf(categoryId)});
    }
}
