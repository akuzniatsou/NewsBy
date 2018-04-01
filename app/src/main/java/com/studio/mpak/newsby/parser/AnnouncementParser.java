package com.studio.mpak.newsby.parser;

import com.studio.mpak.newsby.domain.Announcement;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrei Kuzniatsou
 */
public class AnnouncementParser implements DocumentParser<ArrayList<Announcement<String>>> {

    private static final String SUBTITLE = "Музейный комплекс истории и культуры Оршанщины";
    private static final String NBSP_SYMBOL = "\\u00a0";

    @Override
    public ArrayList<Announcement<String>> parse(Document document) {
        if (document == null) {
            return null;
        }

        document.select("script,.hidden,style, a, img").remove();
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
