package com.studio.mpak.newsby.util;

import android.app.ActivityManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.studio.mpak.newsby.domain.HttpStatus;
import com.studio.mpak.newsby.domain.Response;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author Andrei Kuzniatsou
 */
public final class AppUtil {

    private static final String LOG_TAG = AppUtil.class.getSimpleName();
    private static final String USER_AGENT = "Mozilla";
    private static final int TIMEOUT = 10000;

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

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static Response get(String url) {
        Document document = null;
        HttpStatus httpStatus;
        try {
            document = Jsoup.connect(url).userAgent(USER_AGENT).timeout(TIMEOUT).get();
            httpStatus = HttpStatus.OK;
        } catch (HttpStatusException e) {
            httpStatus = HttpStatus.valueOf(e.getStatusCode());
        } catch (Exception e) {
            httpStatus = HttpStatus.UNKNOWN_ERROR;
        }
        return new Response(httpStatus, document);
    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
