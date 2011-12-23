package se.hjortzen.piccal.domain

class PhotoEntry {
    byte[] content;
    byte[] thumbnail;
    String description;
    Date createDate;
    Date targetDate;
    Calendar calendar;
    String originalUrl;

    static constraints = {
        originalUrl(url: true, nullable: true)
        description(nullable: true)
        thumbnail(nullable: true)
    }
    String toString() {
        return targetDate;
    }
}
