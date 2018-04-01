package com.studio.mpak.newsby;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref);


//            bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_content_actual)));
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_content_main)));
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_content_society)));
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_content_culture)));
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_content_official)));
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_content_event)));
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_content_economic)));
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_content_heals)));
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_content_sport)));
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_content_citizen)));
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_content_photo)));
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_content_partnership)));
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_content_group)));
//            bindPreferenceSummaryToValue(findPreference(getString(R.string.settings_content_anniversary)));
//
//        }
//
//        @Override
//        public boolean onPreferenceChange(Preference preference, Object value) {
//            boolean checked = Boolean.valueOf(v.toString());
//            return true;
//        }
//
//        private void bindPreferenceSummaryToValue(Preference preference) {
//            preference.setOnPreferenceChangeListener(this);
//            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
//            String preferenceString = preferences.getString(preference.getKey(), "");
//            onPreferenceChange(preference, preferenceString);
//        }
        }
    }

    @Override
    public void onBackPressed() {
        NavUtils.navigateUpFromSameTask(this);
    }
}
