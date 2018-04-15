package com.studio.mpak.newsby.domain;

/**
 * @author Andrei Kuzniatsou
 */
public enum HttpStatus {

    OK(200, "OK"),
    BAD_REQUEST(400, "Bad Request"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    UNKNOWN_ERROR(520, "Unknown Error");


    private final int value;
    private final String reasonPhrase;

    HttpStatus(int value, String reasonPhrase) {
        this.value = value;
        this.reasonPhrase = reasonPhrase;
    }

    public int value() {
        return this.value;
    }

    public String getReasonPhrase() {
        return this.reasonPhrase;
    }

    public boolean isSuccessful() {
        return Series.SUCCESSFUL.equals(series());
    }

    public boolean isRedirection() {
        return Series.REDIRECTION.equals(series());
    }

    public boolean isClientError() {
        return Series.CLIENT_ERROR.equals(series());
    }

    public boolean isServerError() {
        return Series.SERVER_ERROR.equals(series());
    }

    public boolean isError() {
        return isClientError() || isServerError();
    }

    public Series series() {
        return Series.valueOf(this);
    }


    @Override
    public String toString() {
        return Integer.toString(this.value);
    }

    public static HttpStatus valueOf(int statusCode) {
        HttpStatus status = resolve(statusCode);
        if (status == null) {
            return UNKNOWN_ERROR;
//            throw new IllegalArgumentException("No matching constant for [" + statusCode + "]");
        }
        return status;
    }

    public static HttpStatus resolve(int statusCode) {
        for (HttpStatus status : values()) {
            if (status.value == statusCode) {
                return status;
            }
        }
        return null;
    }


    public enum Series {

        INFORMATIONAL(1),
        SUCCESSFUL(2),
        REDIRECTION(3),
        CLIENT_ERROR(4),
        SERVER_ERROR(5);

        private final int value;

        Series(int value) {
            this.value = value;
        }

        public int value() {
            return this.value;
        }

        public static Series valueOf(int status) {
            int seriesCode = status / 100;
            for (Series series : values()) {
                if (series.value == seriesCode) {
                    return series;
                }
            }
            throw new IllegalArgumentException("No matching constant for [" + status + "]");
        }

        public static Series valueOf(HttpStatus status) {
            return valueOf(status.value);
        }
    }

}
