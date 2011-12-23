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
        content(maxSize: 1048567, nullable: true)
        thumbnail(maxSize: 1048567, nullable: true)
    }

    String toString() {
        return targetDate;
    }
}
