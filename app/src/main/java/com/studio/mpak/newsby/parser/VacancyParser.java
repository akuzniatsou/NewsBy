package com.studio.mpak.newsby.parser;

import com.studio.mpak.newsby.domain.Announcement;
import com.studio.mpak.newsby.domain.Vacancy;
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
public class VacancyParser implements DocumentParser<ArrayList<Announcement<Vacancy>>> {

    @Override
    public ArrayList<Announcement<Vacancy>> parse(Document document) {
        if (document == null) {
            return null;
        }

        document.select("script,.hidden,style, a, img, posts").remove();
        Element content = document.getElementById("post-5342");

        Elements elements = content.select("p");
        ArrayList<Announcement<Vacancy>> announcements = new ArrayList<>();
        Announcement<Vacancy> announcement = null;

        for (Element element : elements) {
            // exclude elements without vacancy
            if (element.hasAttr("style")) {
                continue;
            }
            // Prepare map with length of elements
            List<Node> nodes = element.childNodes();
            for (Node node : nodes) {
                // choose title
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
                        event = event.trim();
                        if (!event.equals("")) {
                            int index = event.lastIndexOf(" ");
                            String position = event.substring(0, index);
                            String salary = event.substring(index + 1, event.length());

                            announcement.getEvents().add(new Vacancy(position, salary));
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
}
