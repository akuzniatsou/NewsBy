package com.studio.mpak.newsby.validator;

import android.text.TextUtils;
import com.studio.mpak.newsby.domain.Article;

/**
 * @author Andrei Kuzniatsou
 */
public class ShortArticleValidator implements Validator<Article> {

    @Override
    public boolean isValid(Article domain) {
        String date = domain.getDate();
        String title = domain.getTitle();

        return !TextUtils.isEmpty(date) && !TextUtils.isEmpty(title);
    }
}
