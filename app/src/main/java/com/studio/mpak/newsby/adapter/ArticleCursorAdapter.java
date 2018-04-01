package com.studio.mpak.newsby.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.studio.mpak.newsby.R;
import com.studio.mpak.newsby.data.article.ArticleContract;
import com.studio.mpak.newsby.util.AppUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Andrei Kuzniatsou
 */
public class ArticleCursorAdapter extends CursorAdapter {

    private static final String LOG_TAG = ArticleCursorAdapter.class.getSimpleName();
    private final Context context;


    public ArticleCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
        this.context = context;
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list_item.xml
        return LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
    }

    /**
     * This method binds the pet data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        TextView title = view.findViewById(R.id.title);
        int titleColumnIndex = cursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_TITLE);
        String titleText = cursor.getString(titleColumnIndex);
        title.setText(titleText);

        ImageView imageView = view.findViewById(R.id.img);
        int imgColumnIndex = cursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_SCR_IMAGE);

        String link = AppUtil.getEncodedUrl(cursor.getString(imgColumnIndex));
        if (link == null || TextUtils.isEmpty(link)) {
            // Default image if missing
//            Glide.with(getContext()).load(R.drawable.image_missing).into(imageView);
            imageView.setVisibility(View.GONE);
        } else {
            Glide.with(context).load(link).into(imageView);
        }
        int dateColumnIndex = cursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_PUB_DATE);

        TextView date = view.findViewById(R.id.date);
        date.setText(cursor.getString(dateColumnIndex));
    }
}
