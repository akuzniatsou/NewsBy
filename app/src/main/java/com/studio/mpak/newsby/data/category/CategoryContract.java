package com.studio.mpak.newsby.data.category;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Locale;

/**
 * @author Andrei Kuzniatsou
 */
public class CategoryContract {

    static final String CONTENT_AUTHORITY = "com.studio.mpak.newsby.data.category";
    private static final String SCHEME = "content://";
//    private static final Uri BASE_CONTENT_URI = Uri.parse(SCHEME + CONTENT_AUTHORITY);

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + CategoryEntry.TABLE_NAME + " ("
                    + CategoryEntry._ID + " INTEGER PRIMARY KEY, "
                    + CategoryEntry.COLUMN_NAME + " TEXT NOT NULL" +
                    ", "
                    + CategoryEntry.COLUMN_ENABLE + " INTEGER NOT NULL" +
                    ", "
                    + CategoryEntry.COLUMN_ORDER + " INTEGER NOT NULL" +
                    ");";

    public static final String SQL_INIT_ENTRIES = "INSERT INTO " + CategoryEntry.TABLE_NAME + " ("
            + CategoryEntry._ID
            + ','
            + CategoryEntry.COLUMN_NAME
            + ','
            + CategoryEntry.COLUMN_ENABLE
            + ','
            + CategoryEntry.COLUMN_ORDER
            + ") VALUES "
            + prepareValues();

    private static String prepareValues() {
        StringBuilder values = new StringBuilder();
        for (CategoryEnum category : CategoryEnum.values()) {
            values.append('(')
                    .append(category.getId())
                    .append(',')
                    .append(String.format("\"%s\"", category.getName()))
                    .append(',')
                    .append(category.getIsVisibleDefault())
                    .append(',')
                    .append(category.ordinal())
                    .append(')')
                    .append(',')
            ;
        }
        return values.toString().substring(0, values.length() - 1) + ';';
    }

    public static abstract class CategoryEntry implements BaseColumns {

//        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, CategoryEntry.TABLE_NAME);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + CategoryEntry.TABLE_NAME;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + CategoryEntry.TABLE_NAME;

        public final static String TABLE_NAME = "categories";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME ="name";
        public final static String COLUMN_ENABLE ="enable";
        public final static String COLUMN_ORDER ="position";

    }

}
