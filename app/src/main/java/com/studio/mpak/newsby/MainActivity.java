package com.studio.mpak.newsby;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import com.studio.mpak.newsby.fragments.CategoryCursorPagerAdapter;

/**
 * @author Andrei Kuzniatsou
 */
public class MainActivity extends AppCompatActivity {

    private CategoryCursorPagerAdapter fAdapter;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbar();

        ViewPager viewPager = findViewById(R.id.viewpager);
        viewPager.setOffscreenPageLimit(1);
        fAdapter = new CategoryCursorPagerAdapter(getSupportFragmentManager(), MainActivity.this);
        viewPager.setAdapter(fAdapter);

        TabLayout tabLayout = findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        initDrawer();

    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void initDrawer() {
        drawer = findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Intent intent;
                switch (item.getItemId()) {
                    case R.id.nav_content_settings:
                        intent = new Intent(MainActivity.this, SettingsActivity.class);
                        break;
                    case R.id.nav_announcement:
                        intent = new Intent(MainActivity.this, AnnouncementActivity.class);
                        break;
                    case R.id.nav_vacancy:
                        intent = new Intent(MainActivity.this, VacancyActivity.class);
                        break;
                    default:
                        intent = null;
                }
                if (intent != null) {
                    startActivity(intent);
                }
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }
        });

    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_right_menu, menu);
        return true;
    }
}
