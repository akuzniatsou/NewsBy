package com.studio.mpak.newsby.repository;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.studio.mpak.newsby.data.DatabaseHelper;
import com.studio.mpak.newsby.data.category.CategoryContract.CategoryEntry;
import com.studio.mpak.newsby.domain.Category;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrei Kuzniatsou
 */
public class CategoryRepository {

    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public CategoryRepository(Context context) {
        this.dbHelper = DatabaseHelper.getInstance(context);

    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public List<Category> getCategories() {
        List<Category> categories = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = database.rawQuery(String.format(
                    "select * from %s order by %s asc",
                    CategoryEntry.TABLE_NAME, CategoryEntry.COLUMN_ORDER), null);
            if (cursor.moveToFirst()) {
                do {
                    Category category = new Category();
                    category.setId(cursor.getInt(cursor.getColumnIndex(CategoryEntry._ID)));
                    category.setName(cursor.getString(cursor.getColumnIndex(CategoryEntry.COLUMN_NAME)));
                    category.setEnable(cursor.getInt(cursor.getColumnIndex(CategoryEntry.COLUMN_ENABLE)));
                    categories.add(category);
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return categories;
    }
}
