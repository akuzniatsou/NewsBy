package com.studio.mpak.newsby.fragments;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import com.studio.mpak.newsby.R;
import com.studio.mpak.newsby.WebViewActivity;
import com.studio.mpak.newsby.adapter.ArticleCursorAdapter;
import com.studio.mpak.newsby.adapter.EndlessScrollListener;
import com.studio.mpak.newsby.data.article.ArticleContract.ArticleEntry;
import com.studio.mpak.newsby.domain.Article;
import com.studio.mpak.newsby.loader.ArticleCursorLoader;
import com.studio.mpak.newsby.loader.UpdateContentTask;
import com.studio.mpak.newsby.repository.ArticleRepository;

import java.util.List;

/**
 * @author Andrei Kuzniatsou
 */
public class ArticleCursorFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final String ARTICLE_URL = "http://www.orshanka.by/?cat=%d";
    private static final String ARTICLE_URL_PAGED = ARTICLE_URL + "&paged=%d";
    public static final int LOADER_ID = 0;
    private ArticleCursorAdapter mCursorAdapter;
    private AsyncTask<Integer, Void, List<Article>> asyncTask;
    private int categoryId;
    private String url;
    ProgressBar bar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.list_activity, container, false);
        mCursorAdapter = new ArticleCursorAdapter(getActivity(), null);
        final ListView listView = rootView.findViewById(R.id.list);
        listView.setAdapter(mCursorAdapter);
        listView.setOnScrollListener(new EndlessScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                loadNextDataFromApi(page, totalItemsCount);
                return true;
            }
        });

        bar = rootView.findViewById(R.id.loading);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                String url = cursor.getString(cursor.getColumnIndex(ArticleEntry.COLUMN_URL));
                Uri webPage = Uri.parse(url);
                Intent intent = new Intent(getActivity().getApplicationContext(), WebViewActivity.class);
                intent.putExtra(ArticleEntry.COLUMN_URL, webPage.toString());
                startActivity(intent);
                mCursorAdapter.notifyDataSetChanged();
            }
        });

        categoryId = getArguments().getInt("category");
        url = String.format(ARTICLE_URL, categoryId);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        bar.setVisibility(View.VISIBLE);
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    public void loadNextDataFromApi(int offset, final int totalItemsCount) {
        bar.setVisibility(View.VISIBLE);

        asyncTask = new AsyncTask<Integer, Void, List<Article>>() {
            @Override
            protected List<Article> doInBackground(Integer... categories) {
                return new UpdateContentTask().doInBackground(categories[0], totalItemsCount);
            }

            @Override
            protected void onPostExecute(List<Article> articles) {
                if (articles == null) {
                    return;
                }
                ArticleRepository articleRepository = new ArticleRepository(getActivity().getApplicationContext());
                articleRepository.open();
                for (Article article : articles) {
                    articleRepository.insert(article);
                }
                articleRepository.close();
                getLoaderManager().restartLoader(0, null, ArticleCursorFragment.this);
                mCursorAdapter.notifyDataSetChanged();

            }

        };
        asyncTask.execute(categoryId);


    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new ArticleCursorLoader(categoryId, getActivity().getApplicationContext());

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        bar.setVisibility(View.GONE);
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
    }



    @Override
    public void onPause() {
        super.onPause();
    }
}
