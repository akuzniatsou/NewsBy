package com.studio.mpak.newsby.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;
import com.studio.mpak.newsby.domain.Category;
import com.studio.mpak.newsby.repository.CategoryRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrei Kuzniatsou
 */
public class CategoryCursorPagerAdapter extends FragmentPagerAdapter {

    private Context context;
    private List<Category> categories = new ArrayList<>();

    public CategoryCursorPagerAdapter(FragmentManager fm, Context ctx) {
        super(fm);
        this.context = ctx;
        initTitles();
    }

    private void initTitles() {
        CategoryRepository categoryRepository = new CategoryRepository(context);
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        categoryRepository.open();
        for (Category category : categoryRepository.getCategories()) {
            if (category.getEnable() > 0 && sharedPrefs.getBoolean(category.getName(), true)) {
                categories.add(category);
            }
        }
        categoryRepository.close();
    }

    @Override
    public Fragment getItem(int position) {
        Category category = categories.get(position);
        Fragment fragment = new ArticleCursorFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("category", category.getId());
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return categories.get(position).getName();
    }

    @Override
    public int getCount() {
        return categories.size();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}
