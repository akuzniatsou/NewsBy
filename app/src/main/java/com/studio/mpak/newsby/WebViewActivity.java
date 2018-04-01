package com.studio.mpak.newsby;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import com.studio.mpak.newsby.adapter.ArticleAdapter;
import com.studio.mpak.newsby.data.article.ArticleContract.ArticleEntry;
import com.studio.mpak.newsby.domain.Article;
import com.studio.mpak.newsby.loader.ArticleLoader;
import com.studio.mpak.newsby.parser.ArticleParser;
import com.studio.mpak.newsby.util.AppUtil;

import java.util.ArrayList;
import java.util.Stack;

/**
 * @author Andrei Kuzniatsou
 */
public class WebViewActivity extends Activity implements LoaderManager.LoaderCallbacks<Article> {

    private static final int ARTICLE_LOADER_ID = 1;
    private WebView webView;
    private String articleUrl;
    private View buttonView;
    private ScrollView scrollView;
    private LinearLayout llRelated;
    private ArticleAdapter mAdapter;
    private LinearLayout llRelatedMain;
    private Stack<String> stack = new Stack<>();
    private TextView tvPrev;
    private TextView tvNext;


    @SuppressLint({"ClickableViewAccessibility", "SetJavaScriptEnabled"})
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_main);
        webView = findViewById(R.id.webView1);
        buttonView = findViewById(R.id.soc_share_panel);
        scrollView = findViewById(R.id.scrollView);
        llRelated = findViewById(R.id.related_list);
        llRelatedMain = findViewById(R.id.related_layout);
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());
        tvPrev = findViewById(R.id.related_nav_prev);
        tvNext = findViewById(R.id.related_nav_next);


        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                buttonView.setVisibility(View.VISIBLE);
                llRelatedMain.setVisibility(View.VISIBLE);
                scrollView.scrollTo(0,0);

            }
        });

        articleUrl = getIntent().getStringExtra(ArticleEntry.COLUMN_URL);
        getLoaderManager().initLoader(ARTICLE_LOADER_ID, null, this);
    }

    @Override
    public Loader<Article> onCreateLoader(int i, Bundle bundle) {
        return new ArticleLoader(articleUrl, WebViewActivity.this, new ArticleParser());
    }

    @Override
    public void onLoadFinished(final Loader<Article> loader, final Article article) {
        if (article != null) {
            mAdapter.clear();
            llRelated.removeAllViewsInLayout();
            mAdapter.addAll(article.getRelated());

            final Article prev = article.getPrev();
            tvPrev.setText(prev.getTitle());
            tvPrev.setOnClickListener(new NavigateButtonClickListener(prev));

            final Article next = article.getNext();
            tvNext.setText(next.getTitle());
            tvNext.setOnClickListener(new NavigateButtonClickListener(next));

            final int adapterCount = mAdapter.getCount();
            for (int i = 0; i < adapterCount; i++) {
                View item = mAdapter.getView(i, null, null);
                final Article relatedItem = mAdapter.getItem(i);
                item.setOnClickListener(new NavigateButtonClickListener(relatedItem));
                llRelated.addView(item);
            }
            webView.loadDataWithBaseURL(article.getArticleUrl(), article.getContent(),"text/html", "UTF-8", null);
        } else {
            webView.loadUrl("about:blank");
        }
    }

    @Override
    public void onLoaderReset(Loader<Article> loader) {
        webView.loadUrl("about:blank");
    }

    @Override
    public void onBackPressed() {
        if (!stack.isEmpty()) {
            webView.loadUrl("about:blank");
            articleUrl = stack.pop();
            getLoaderManager().restartLoader(ARTICLE_LOADER_ID, null, WebViewActivity.this);
        } else {
            super.onBackPressed();
        }
    }

    public void fbShareAction(View view) {
        Uri uri = Uri.parse("https://www.facebook.com/sharer.php").buildUpon()
                .appendQueryParameter("u", articleUrl).build();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(uri);
        startActivity(i);
    }

    public void okShareAction(View view) {
        Uri uri = Uri.parse("https://connect.ok.ru/offer").buildUpon()
                .appendQueryParameter("url", articleUrl).build();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(uri);
        startActivity(i);
    }

    public void vkShareAction(View view) {
        Uri uri = Uri.parse("https://vk.com/share.php").buildUpon()
                .appendQueryParameter("url", articleUrl).build();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(uri);
        startActivity(i);
    }

    public void tweetShareAction(View view) {
        Uri uri = Uri.parse("https://twitter.com/intent/tweet").buildUpon()
                .appendQueryParameter("url", articleUrl).build();
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(uri);
        startActivity(i);
    }

    public void homeAction(View view) {
        stack.clear();
        onBackPressed();
    }

    private class NavigateButtonClickListener implements View.OnClickListener {
        private final Article article;

        NavigateButtonClickListener(Article article) {
            this.article = article;
        }

        @Override
        public void onClick(View view) {
            Toast noConnection = Toast.makeText(getApplicationContext(), "No connection", Toast.LENGTH_SHORT);
            if (AppUtil.isConnected(getApplicationContext())) {
                webView.loadUrl("about:blank");
                articleUrl = article.getArticleUrl();
                stack.push(articleUrl);
                getLoaderManager().restartLoader(ARTICLE_LOADER_ID, null, WebViewActivity.this);
            } else {
                noConnection.cancel();
                noConnection.show();
            }
        }
    }
}
