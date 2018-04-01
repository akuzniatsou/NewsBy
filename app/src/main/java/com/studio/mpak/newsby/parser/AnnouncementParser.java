package com.studio.mpak.newsby.parser;

import com.studio.mpak.newsby.domain.Announcement;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementParser implements DocumentParser<ArrayList<Announcement<String>>> {

    private static final List<String> VENUES = new ArrayList<String>() {{
        add("Площадь Коллегиума иезуитов");
//        add("Музейный комплекс истории и культуры Оршанщины");
        add("Музей истории и культуры города Орши");
        add("Оршанский этнографический музей «Мельница»");
        add("Оршанская городская художественная галерея В.А.Громыко");
        add("Оршанский музей В.С.Короткевича");
        add("Оршанский музей деревянной скульптуры резчика С.С. Шаврова");
        add("Художественная галерея «Каляровы шлях»");
        add("Оршанская централизованная библиотечная система");
        add("Оршанский городской Центр культуры «Победа»");
        add("Городской Дворец культуры «Орша»");
        add("Дворец культуры г.Барани Оршанского района");
        add("Оршанский районный Дом культуры");
        add("Дом культуры г.п. Болбасово");
        add("Городской детский парк «Сказочная страна»");
        add("Административное здание");
        add("Оршанская детская школа искусств № 1");
        add("Оршанская детская школа искусств № 2");
        add("Оршанская детская школа искусств № 3");
        add("Ореховская детская школа искусств");
        add("Дом культуры железнодорожников");
        add("3D кинотеатр");
    }};
    private static final String SUBTITLE = "Музейный комплекс истории и культуры Оршанщины";
    private static final String NBSP_SYMBOL = "\\u00a0";

    @Override
    public ArrayList<Announcement<String>> parse(Document document) {
        if (document == null) {
            return null;
        }

        document.select("script,.hidden,style, a, img").remove();
//        document.select(":containsOwn(\u00a0)").remove();
        Element content = document.getElementById("content");

        for (Element element : content.select("*")) {
            if (!element.hasText() && element.isBlock()) {
                element.remove();
            }
        }
        content.select("h4").first().remove();
        content.select("h4").tagName("p");
        Elements select = content.select("p");
        ArrayList<Announcement<String>> announcements = new ArrayList<>();
        Announcement<String> announcement = null;
        for (Element element : select) {
            if (element.text().contains(SUBTITLE)) {
                continue;
            }
            List<Node> nodes = element.childNodes();
            for (Node node : nodes) {
                if (node.nodeName().equals("strong")) {
                    String place = ((Element) node).text();
                    if (announcement != null) {
                        if (announcement.getEvents().isEmpty()) {
                            announcement.setPlace(String.format("%s %s", announcement.getPlace(), place));
                            continue;
                        } else {
                            announcements.add(announcement);
                        }
                    }
                    announcement = new Announcement<>();
                    announcement.setPlace(place);
                } else {
                    if (node instanceof TextNode) {
                        String event = ((TextNode) node).text();
                        event = clearText(event);
                        if (!event.equals("")) {
                            announcement.getEvents().add(event);
                        }
                    }
                }
            }
        }
        if (announcement != null) {
            announcements.add(announcement);
        }
        return announcements;
    }

    private String clearText(String event) {
        String text = event.trim();
        text = text.replaceFirst("^-+", "");
        text = text.replaceFirst("^—+", "");
        text = text.replaceFirst(NBSP_SYMBOL, "");
        return text.trim();
    }


}
