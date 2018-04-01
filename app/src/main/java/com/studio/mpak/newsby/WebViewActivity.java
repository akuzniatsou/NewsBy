package com.studio.mpak.newsby;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import com.studio.mpak.newsby.adapter.ArticleAdapter;
import com.studio.mpak.newsby.data.article.ArticleContract.ArticleEntry;
import com.studio.mpak.newsby.domain.Article;
import com.studio.mpak.newsby.loader.ArticleLoader;
import com.studio.mpak.newsby.parser.ArticleParser;

import java.util.ArrayList;
import java.util.Stack;

public class WebViewActivity extends Activity implements LoaderManager.LoaderCallbacks<Article> {

    private WebView webView;
    private String articleUrl;
    private View buttonView;
    private ScrollView scrollView;
    private LinearLayout llRelated;
    private ArticleAdapter mAdapter;
    private ListView listView;
    LinearLayout llRelatedMain;


    private Stack<String> stack = new Stack<>();


    TextView tvPrev;
    TextView tvNext;

    public static final int ARTICLE_LOADER_ID = 1;

    @SuppressLint("ClickableViewAccessibility")
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_main);
        webView = findViewById(R.id.webView1);
        buttonView = findViewById(R.id.soc_share_panel);
        scrollView = findViewById(R.id.scrollView);
        llRelated = findViewById(R.id.related_list);
        llRelatedMain = findViewById(R.id.related_layout);
        mAdapter = new ArticleAdapter(this, new ArrayList<Article>());
//        listView = findViewById(R.id.list);
//        listView.setVisibility(View.INVISIBLE);
//        listView.setAdapter(mAdapter);

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

//        fixInnerScroll();



        articleUrl = getIntent().getStringExtra(ArticleEntry.COLUMN_URL);
        getLoaderManager().initLoader(ARTICLE_LOADER_ID, null, this);
    }

    private void fixInnerScroll() {
        scrollView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                findViewById(R.id.list).getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });
        listView.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event)
            {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
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
            tvPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    webView.loadUrl("about:blank");
                    articleUrl = prev.getArticleUrl();
                    stack.push(articleUrl);
                    getLoaderManager().restartLoader(ARTICLE_LOADER_ID, null, WebViewActivity.this);
                }
            });
            final Article next = article.getNext();
            tvNext.setText(next.getTitle());
            tvNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    webView.loadUrl("about:blank");
                    articleUrl = next.getArticleUrl();
                    stack.push(articleUrl);

                    getLoaderManager().restartLoader(ARTICLE_LOADER_ID, null, WebViewActivity.this);
                }
            });
            final int adapterCount = mAdapter.getCount();
            for (int i = 0; i < adapterCount; i++) {
                View item = mAdapter.getView(i, null, null);
                final Article relatedItem = mAdapter.getItem(i);
                item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        webView.loadUrl("about:blank");
                        articleUrl = relatedItem.getArticleUrl();
                        stack.push(articleUrl);
                        getLoaderManager().restartLoader(ARTICLE_LOADER_ID, null, WebViewActivity.this);
                    }
                });
                llRelated.addView(item);

            }
        }
        webView.loadDataWithBaseURL(article.getArticleUrl(), article.getContent(),"text/html", "UTF-8", null);
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

}
