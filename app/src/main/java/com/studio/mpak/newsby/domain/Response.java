package com.studio.mpak.newsby.domain;

import org.jsoup.nodes.Document;

/**
 * @author Andrei Kuzniatsou
 */
public class Response {

    private HttpStatus status;
    private Document data;

    public Response(HttpStatus status, Document data) {
        this.status = status;
        this.data = data;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public Document getData() {
        return data;
    }

}
