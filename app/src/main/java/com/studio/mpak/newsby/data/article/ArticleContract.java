package com.studio.mpak.newsby.data.article;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * @author Andrei Kuzniatsou
 */
public class ArticleContract {

    static final String CONTENT_AUTHORITY = "com.studio.mpak.newsby.data.article";
    private static final String SCHEME = "content://";
    private static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + CONTENT_AUTHORITY);

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ArticleEntry.TABLE_NAME + " ("
                    + ArticleEntry._ID + " INTEGER PRIMARY KEY ON CONFLICT IGNORE, "
                    + ArticleEntry.COLUMN_TITLE + " TEXT NOT NULL, "
                    + ArticleEntry.COLUMN_URL + " TEXT NOT NULL, "
                    + ArticleEntry.COLUMN_SCR_IMAGE + " TEXT NOT NULL, "
                    + ArticleEntry.COLUMN_CONTENT + " TEXT, "
                    + ArticleEntry.COLUMN_COMMENT_COUNT + " TEXT, "
                    + ArticleEntry.COLUMN_PREV_ID + " TEXT, "
                    + ArticleEntry.COLUMN_NEXT_ID + " TEXT, "
                    + ArticleEntry.COLUMN_PUB_DATE + " TEXT NOT NULL, "
                    + ArticleEntry.COLUMN_CREATED_DATE + " DATETIME);";

    public static abstract class ArticleEntry implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, ArticleEntry.TABLE_NAME);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + ArticleEntry.TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + ArticleEntry.TABLE_NAME;

        public final static String TABLE_NAME = "articles";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_TITLE ="title";
        public final static String COLUMN_URL = "url";
        public final static String COLUMN_SCR_IMAGE = "img";
        public final static String COLUMN_PUB_DATE = "date";
        public final static String COLUMN_CONTENT = "content";
        public final static String COLUMN_DESCRIPTION = "description";
        public final static String COLUMN_CATEGORY = "category_id";
        public final static String COLUMN_PREV_ID = "prev_article_id";
        public final static String COLUMN_NEXT_ID = "next_article_id";
        public final static String COLUMN_COMMENT_COUNT = "comment_count";
        public final static String COLUMN_CREATED_DATE = "created_date";


    }
}
