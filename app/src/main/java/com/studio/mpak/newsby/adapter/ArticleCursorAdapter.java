package com.studio.mpak.newsby.adapter;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
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

/**
 * @author Andrei Kuzniatsou
 */
public class ArticleCursorAdapter extends CursorAdapter {

    private final Context context;

    public ArticleCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
        this.context = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
    }

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
            imageView.setVisibility(View.GONE);
        } else {
            Glide.with(context).load(link).into(imageView);
        }
        int dateColumnIndex = cursor.getColumnIndex(ArticleContract.ArticleEntry.COLUMN_PUB_DATE);

        TextView date = view.findViewById(R.id.date);
        date.setText(cursor.getString(dateColumnIndex));
    }
}
