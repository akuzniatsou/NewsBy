package com.studio.mpak.newsby;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import com.studio.mpak.newsby.adapter.ExpandableAnnouncementAdapter;
import com.studio.mpak.newsby.domain.Announcement;
import com.studio.mpak.newsby.loader.AnnouncementLoader;
import com.studio.mpak.newsby.parser.AnnouncementParser;

import java.util.ArrayList;

/**
 * @author Andrei Kuzniatsou
 */
public class AnnouncementActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Announcement<String>>>,
        SearchView.OnQueryTextListener, SearchView.OnCloseListener{

    private ExpandableAnnouncementAdapter mAdapter;
    private ProgressBar bar;
    private SearchView search;
    private ExpandableListView exListView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandable_list_activity);


        exListView = findViewById(R.id.list_expandable);
        bar = findViewById(R.id.loading);
        bar.setVisibility(View.VISIBLE);
        mAdapter = new ExpandableAnnouncementAdapter(this, new ArrayList<Announcement<String>>());
        exListView.setAdapter(mAdapter);
        getSupportLoaderManager().initLoader(0, null, this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search = findViewById(R.id.search);
        search.setVisibility(View.VISIBLE);
        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(this);
        collapseAll();

    }

    @Override
    public boolean onClose() {
        mAdapter.filter("");
        collapseAll();
        return false;
    }

    @Override
    public boolean onQueryTextChange(String query) {
        mAdapter.filter(query);
        if (query.isEmpty()) {
            collapseAll();
        } else {
            expandAll();
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        mAdapter.filter(query);
        expandAll();
        return false;
    }


    private void expandAll() {
        int count = mAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            exListView.expandGroup(i);
        }
    }

    private void collapseAll() {
        int count = mAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            exListView.collapseGroup(i);
        }
    }

    @Override
    public Loader<ArrayList<Announcement<String>>> onCreateLoader(int id, Bundle args) {
        return new AnnouncementLoader(this, new AnnouncementParser());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Announcement<String>>> loader,
                               ArrayList<Announcement<String>> data) {
        bar.setVisibility(View.GONE);
        if (data != null && !data.isEmpty()) {
            mAdapter.clear();
            mAdapter.addAll(data);
        }
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Announcement<String>>> loader) {
        mAdapter.clear();
    }
}


