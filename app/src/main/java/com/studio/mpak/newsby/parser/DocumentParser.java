package com.studio.mpak.newsby.parser;

import org.jsoup.nodes.Document;

/**
 * @author Andrei Kuzniatsou
 */
public interface DocumentParser<T> {

    T parse(Document document);
}
