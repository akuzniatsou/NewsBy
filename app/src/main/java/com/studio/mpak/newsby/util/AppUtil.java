package com.studio.mpak.newsby.util;

import android.util.Log;
import com.studio.mpak.newsby.adapter.ArticleAdapter;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Andrei Kuzniatsou
 */
public final class AppUtil {

    private static final String LOG_TAG = AppUtil.class.getSimpleName();

    public static String getEncodedUrl(String url) {
        String link = null;
        try {
            String path = url.substring(0, url.lastIndexOf("/") + 1);
            String lastPath = url.substring(url.lastIndexOf("/") + 1);
            link = path + URLEncoder.encode(lastPath, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            Log.e(LOG_TAG, "Error with encoding URL", e);
            e.printStackTrace();
        }
        return link;
    }

    public static String join(String table, String column) {
        return table + '.' + column;
    }
}
