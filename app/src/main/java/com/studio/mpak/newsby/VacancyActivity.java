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
import com.studio.mpak.newsby.adapter.ExpandableVacancyAdapter;
import com.studio.mpak.newsby.domain.Announcement;
import com.studio.mpak.newsby.domain.Vacancy;
import com.studio.mpak.newsby.loader.VacancyLoader;
import com.studio.mpak.newsby.parser.VacancyParser;

import java.util.ArrayList;

public class VacancyActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<ArrayList<Announcement<Vacancy>>>,
        SearchView.OnQueryTextListener, SearchView.OnCloseListener{

    private ExpandableVacancyAdapter mAdapter;
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
        mAdapter = new ExpandableVacancyAdapter(this, new ArrayList<Announcement<Vacancy>>());
        exListView.setAdapter(mAdapter);
        getSupportLoaderManager().initLoader(0, null, this);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        search = findViewById(R.id.search);
        search.setVisibility(View.VISIBLE);
        search.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        search.setIconifiedByDefault(false);
        search.setOnQueryTextListener(this);
        search.setOnCloseListener(this);
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
    public Loader<ArrayList<Announcement<Vacancy>>> onCreateLoader(int id, Bundle args) {
        return new VacancyLoader(this, new VacancyParser());
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<Announcement<Vacancy>>> loader,
                               ArrayList<Announcement<Vacancy>> data) {
        bar.setVisibility(View.GONE);
        if (data != null && !data.isEmpty()) {
            mAdapter.clear();
            mAdapter.addAll(data);
        }
        mAdapter.notifyDataSetChanged();

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<Announcement<Vacancy>>> loader) {
        mAdapter.clear();
    }
}


