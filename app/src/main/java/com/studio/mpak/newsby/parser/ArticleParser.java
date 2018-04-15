package com.studio.mpak.newsby.parser;

import com.studio.mpak.newsby.domain.Article;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

/**
 * @author Andrei Kuzniatsou
 */
public class ArticleParser implements DocumentParser<Article> {

    // Сжатие картинок под экран
    private final String IMAGE_STYLE = "<style>img{display: inline;height: auto;max-width: 100%;}</style>";
    // Сжатие iframe video под экран
    private final String VIDEO_STYLE = "<style>iframe{max-width: 100%;max-height: 56.25vw;}</style>";
    private final String TEXT_STYLE = "<style>{text-align:justify;}</style>";
    private final String NEW_LINE = "\n";

    @Override
    public Article parse(Document document) {
        if (document == null) {
            return null;
        }
        Article article = new Article();

        document.select("script,.hidden,style").remove();
        document.select(".wp-caption").attr("style", "width: 100%;max-width: 100%;");
        Element content = document.getElementById("content");

        Elements hrefs = content.select(".cat-links > a[href]");
        for (Element href : hrefs) {
            article.getCategories().add(href.text());
        }

        String textShorted = content.select(".entry-content").text().substring(0,100) + "...";
        String title = content.select(".entry-title").text();
        article.setTitle(title);
        String articleUrl = content.select(".posted-on").select("a").attr("href");
        article.setArticleUrl(articleUrl);
        Integer articleId = getArticleId(articleUrl);
        if (null == articleId) {
            return null;
        }
        article.setId(articleId);

        String imageUrl = content.select("img").attr("src");
//        String imageUrl = document.select("meta[property=og:image]").attr("content");

        article.setImageUrl(imageUrl);

        String date = content.select(".posted-on").text();
        article.setDate(date);
        String views = content.select(".post-views").text();
        article.setViews(views);

        content.select(".posted-on > a").prepend("Опубликовано: ").append("<br/>").removeAttr("href");
        content.select(".post-views > a").prepend("Просмотров: ").removeAttr("href");
        content.select(".author, .comments, .cat-links, .uptolike-buttons").remove();


        String htmlMain = "<html><head><title>" + article.getTitle()+ "</title>\n" +
                IMAGE_STYLE + NEW_LINE + VIDEO_STYLE + NEW_LINE + TEXT_STYLE + NEW_LINE +
                "</head>"+ NEW_LINE +
                "<body>"+ NEW_LINE +
                content.html()+ NEW_LINE +
                "</body>"+ NEW_LINE +
                "</html>";

        article.setContent(htmlMain);



        Element navigation = document.getElementsByClass("default-wp-page").first();
        if (null == navigation) {
            return article;
        }
        //TODO {akuzniatsou} Refactor
        for (Node node : navigation.childNodes()) {
            if ("previous".equals(node.attr("class"))) {
                String href = ((Element) node).select("a[href]").attr("href");
                String text = ((Element) node).text();
                Article prevArticle = new Article();
                prevArticle.setArticleUrl(href);
                prevArticle.setTitle(cleanup(text));
                article.setPrev(prevArticle);
                Integer id = getArticleId(href);
                if (null != id) {
                    prevArticle.setId(id);
                }
            }
            if ("next".equals(node.attr("class"))) {
                String href = ((Element) node).select("a[href]").attr("href");
                String text = ((Element) node).text();
                Article nextArticle = new Article();
                nextArticle.setArticleUrl(href);
                nextArticle.setTitle(cleanup(text));
                article.setNext(nextArticle);
                Integer id = getArticleId(href);
                if (null != id) {
                    nextArticle.setId(id);
                }
            }
        }

        Elements relatedPosts = document.getElementsByClass("single-related-posts");
        if (null == relatedPosts) {
            return article;
        }
        for (Element element : relatedPosts) {
            Article relatedArticle = new Article();
            String relatedTitle = element.select(".entry-title").text();
            relatedArticle.setTitle(relatedTitle);
            String relatedArticleUrl = element.select(".posted-on").select("a").attr("href");
            String relatedImageUrl = element.select("img").attr("src");
            relatedArticle.setImageUrl(relatedImageUrl);

            String relatedDate = element.select(".posted-on").text();
            relatedArticle.setDate(relatedDate);
            String relatedViews = element.select(".post-views").text();
            relatedArticle.setViews(relatedViews);

            relatedArticle.setArticleUrl(relatedArticleUrl);


            article.getRelated().add(relatedArticle);
        }
        return article;
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

    private String cleanup(String text) {
        String result = text.replace("→", "");
        result = result.replace("←", "");
        result = result.trim();
        return result;

    }
}
