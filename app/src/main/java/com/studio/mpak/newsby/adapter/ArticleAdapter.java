package com.studio.mpak.newsby.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.studio.mpak.newsby.R;
import com.studio.mpak.newsby.domain.Article;
import com.studio.mpak.newsby.util.AppUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

public class ArticleAdapter extends ArrayAdapter<Article> {

    private static final String LOG_TAG = ArticleAdapter.class.getSimpleName();

    public ArticleAdapter(Context context, List<Article> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View view, @NonNull ViewGroup parent) {

        Article article = getItem(position);

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.news_item, parent, false);
        }

        TextView title = view.findViewById(R.id.title);
        title.setText(article.getTitle());

        ImageView imageView = view.findViewById(R.id.img);
        String link = AppUtil.getEncodedUrl(article.getImageUrl());
        if (link == null || TextUtils.isEmpty(link)) {
            // Default image if missing
//            Glide.with(getContext()).load(R.drawable.image_missing).into(imageView);
            imageView.setVisibility(View.GONE);
        } else {
            Glide.with(getContext()).load(link).into(imageView);
        }

        TextView date = view.findViewById(R.id.date);
        date.setText(article.getDate());


        return view;
    }



}
