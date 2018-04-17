package com.studio.mpak.newsby.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;
import com.studio.mpak.newsby.domain.Article;
import com.studio.mpak.newsby.domain.Response;
import com.studio.mpak.newsby.parser.DocumentParser;
import com.studio.mpak.newsby.repository.ArticleRepository;
import com.studio.mpak.newsby.util.AppUtil;

/**
 * @author Andrei Kuzniatsou
 */
public class ArticleLoader extends AsyncTaskLoader<Article> {

    private static final String LOG_TAG = ArticleLoader.class.getSimpleName();
    private final DocumentParser<Article> parser;
    private final ArticleRepository repository;
    private String url;

    public ArticleLoader(String url, Context context, DocumentParser<Article> parser) {
        super(context);
        this.url = url;
        this.parser = parser;
        repository = new ArticleRepository(context);
        repository.open();
    }

    @Override
    public Article loadInBackground() {

        Integer articleId = getArticleId(url);
        Article article = repository.findArticle(articleId);

        if (article.getContent() == null) {
            Response response = AppUtil.get(url);
            if (response.getStatus().isError()) {
                Log.e(LOG_TAG, "Error while loading article, " + response.getStatus().getReasonPhrase());
            } else {
                article = parser.parse(response.getData());
                repository.updateContent(article);
            }
        } else {
            Article prev = repository.findArticle(article.getPrev().getId());
            Article next = repository.findArticle(article.getNext().getId());
            article.setPrev(prev);
            article.setNext(next);
        }
        return article;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    private Integer getArticleId(String articleUrl) {
        Integer id = null;
        try {
            String articleId = articleUrl.substring(articleUrl.lastIndexOf("=") + 1);
            id = Integer.valueOf(articleId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            System.out.println("Failed to get article id of " + articleUrl);
        }
        return id;
    }

}
