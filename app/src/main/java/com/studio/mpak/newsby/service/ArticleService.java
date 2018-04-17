package com.studio.mpak.newsby.service;

import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.content.Context;
import com.studio.mpak.newsby.domain.Article;
import com.studio.mpak.newsby.repository.ArticleRepository;
import org.joda.time.LocalDate;

/**
 * @author Andrei Kuzniatsou
 */
public class ArticleService {

    private ArticleRepository articleRepository;

    public ArticleService(Context context) {
        articleRepository = new ArticleRepository(context);
        articleRepository.open();
    }

    public long countArticleWithoutContentForPeriod(int period) {
        if (articleRepository.isOpen()) {
            String periodWithYear = getDateWithShift(period);
            return articleRepository.countArticleWithoutContent(periodWithYear);
        } else {
            return 0;
        }
    }

    public Article findArticleWithoutContent(int period) {
        Article article = null;
        if (articleRepository.isOpen()) {
            String periodWithYear = getDateWithShift(period);
            article = articleRepository.findArticleWithoutContent(periodWithYear);
        }
        return article;
    }

    public void close() {
        if (articleRepository.isOpen()) {
            articleRepository.close();
        }
    }

    public void update(Article article) {
        if (articleRepository.isOpen()) {
            articleRepository.updateContent(article);
        }
    }

    @SuppressLint("DefaultLocale")
    private String getDateWithShift(int period) {
        LocalDate date = LocalDate.now().minusMonths(period);
        int month = date.getMonthOfYear();
        int year = date.getYear();
        // Format like MM.yyyy with leading zero
        return format("%02d.%d", month, year);
    }
}
