package se.hjortzen.piccal.domain

class PhotoEntry {
    String description;
    Date createDate;
    Date targetDate;
    Calendar calendar;
    String originalUrl;

    static constraints = {
        originalUrl(url: true)
    }
    String toString() {
        return targetDate;
    }
}
