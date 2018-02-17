package com.studio.mpak.newsby.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.studio.mpak.newsby.data.article.ArticleContract;
import com.studio.mpak.newsby.data.article.ArticleContract.ArticleEntry;
import com.studio.mpak.newsby.data.category.CategoryContract;
import com.studio.mpak.newsby.data.category.CategoryContract.CategoryEntry;
import com.studio.mpak.newsby.data.relation.ArticleCategoryContract;

/**
 * @author Andrei Kuzniatsou
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = DatabaseHelper.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "newsby.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ArticleContract.SQL_CREATE_ENTRIES);
        db.execSQL(CategoryContract.SQL_CREATE_ENTRIES);
        db.execSQL(CategoryContract.SQL_INIT_ENTRIES);
        db.execSQL(ArticleCategoryContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS" + ArticleEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS" + CategoryEntry.TABLE_NAME);
        onCreate(db);
    }

}
