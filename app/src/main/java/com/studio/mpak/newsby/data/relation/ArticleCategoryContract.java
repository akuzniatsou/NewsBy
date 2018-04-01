package com.studio.mpak.newsby.data.relation;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author Andrei Kuzniatsou
 */
public class ArticleCategoryContract {

    static final String CONTENT_AUTHORITY = "com.studio.mpak.newsby.data.article";
    private static final String SCHEME = "content://";
    private static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + CONTENT_AUTHORITY);

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ArticleCategoryContract.ArticleCategoryEntry.TABLE_NAME + " ("
                    + ArticleCategoryContract.ArticleCategoryEntry.COLUMN_ARTICLE_ID + " INTEGER NOT NULL, "
                    + ArticleCategoryContract.ArticleCategoryEntry.COLUMN_CATEGORY_ID + " INTEGER NOT NULL, "
                    + "PRIMARY KEY (" + ArticleCategoryContract.ArticleCategoryEntry.COLUMN_ARTICLE_ID + ", "
                    + ArticleCategoryContract.ArticleCategoryEntry.COLUMN_CATEGORY_ID + ") ON CONFLICT IGNORE"
                    + ");";

    public static abstract class ArticleCategoryEntry implements BaseColumns {

        public final static String TABLE_NAME = "article_category";
        public final static String COLUMN_ARTICLE_ID ="article_id";
        public final static String COLUMN_CATEGORY_ID = "category_id";

    }
}
