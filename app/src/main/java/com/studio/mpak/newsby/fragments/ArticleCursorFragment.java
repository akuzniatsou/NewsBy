package com.studio.mpak.newsby.fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.studio.mpak.newsby.R;
import com.studio.mpak.newsby.WebViewActivity;
import com.studio.mpak.newsby.adapter.ArticleCursorAdapter;
import com.studio.mpak.newsby.adapter.EndlessScrollListener;
import com.studio.mpak.newsby.data.article.ArticleContract.ArticleEntry;
import com.studio.mpak.newsby.domain.Article;
import com.studio.mpak.newsby.loader.ArticleCursorLoader;
import com.studio.mpak.newsby.loader.UpdateContentTask;
import com.studio.mpak.newsby.repository.ArticleRepository;
import com.studio.mpak.newsby.util.AppUtil;

import java.util.List;

/**
 * @author Andrei Kuzniatsou
 */
public class ArticleCursorFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    private static final int ARTICLE_CURSOR_LOADER_ID = 0;

    private ArticleCursorAdapter mCursorAdapter;
    private AsyncTask<Integer, Void, List<Article>> asyncTask;
    private int categoryId;
    private ProgressBar bar;
    private Toast mToast;
    private Context context;

    @Override
    public void onAttach(Context context) {
        this.context = context;
        super.onAttach(context);
    }

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
                if (AppUtil.isConnected(context)) {
                    Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                    String url = cursor.getString(cursor.getColumnIndex(ArticleEntry.COLUMN_URL));
                    Uri webPage = Uri.parse(url);
                    Intent intent = new Intent(context, WebViewActivity.class);
                    intent.putExtra(ArticleEntry.COLUMN_URL, webPage.toString());
                    startActivity(intent);
                    mCursorAdapter.notifyDataSetChanged();
                } else {
                    showAToast("No connection");
                }
            }
        });

        categoryId = getArguments() != null ? getArguments().getInt("category") : -1;
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        bar.setVisibility(View.VISIBLE);
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(ARTICLE_CURSOR_LOADER_ID, null, this);
    }

    void loadNextDataFromApi(int offset, final int totalItemsCount) {
        bar.setVisibility(View.VISIBLE);
        asyncTask = new UpdateContentAsyncTask(totalItemsCount, mCursorAdapter, getLoaderManager(),
                ArticleCursorFragment.this, getActivity());
        asyncTask.execute(categoryId);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new ArticleCursorLoader(categoryId, context);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        bar.setVisibility(View.GONE);
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        bar.setVisibility(View.GONE);
        mCursorAdapter.swapCursor(null);
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
    }



    @Override
    public void onPause() {
        bar.setVisibility(View.GONE);
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
        mCursorAdapter.swapCursor(null);
        super.onPause();
    }

    private static class UpdateContentAsyncTask extends AsyncTask<Integer, Void, List<Article>> {
        private final int totalItemsCount;
        private ArticleCursorAdapter mCursorAdapter;
        private LoaderManager loaderManager;
        private ArticleCursorFragment fragment;
        private final ThreadLocal<FragmentActivity> activity = new ThreadLocal<>();
        private ArticleRepository repository;

        UpdateContentAsyncTask(int totalItemsCount, ArticleCursorAdapter mCursorAdapter,
                               LoaderManager loaderManager, ArticleCursorFragment fragment,
                               FragmentActivity activity) {
            this.totalItemsCount = totalItemsCount;
            this.mCursorAdapter = mCursorAdapter;
            this.loaderManager = loaderManager;
            this.fragment = fragment;
            this.activity.set(activity);
        }

        @Override
        protected List<Article> doInBackground(Integer... categories) {
            return new UpdateContentTask().doInBackground(categories[0], totalItemsCount);
        }

        @Override
        protected void onPostExecute(List<Article> articles) {
            if (articles == null) {
                return;
            }
            repository = new ArticleRepository(activity.get().getApplicationContext());
            repository.open();
            for (Article article : articles) {
                repository.insert(article);
            }
            repository.close();
            loaderManager.restartLoader(ARTICLE_CURSOR_LOADER_ID, null, fragment);
            mCursorAdapter.notifyDataSetChanged();

        }

        @Override
        protected void onCancelled() {
            if (null != repository) {
                repository.close();
            }
            super.onCancelled();
        }
    }

    public void showAToast(String message){
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT);
        mToast.show();
    }
}
