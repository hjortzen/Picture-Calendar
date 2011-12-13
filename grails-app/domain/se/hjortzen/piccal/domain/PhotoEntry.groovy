package se.hjortzen.piccal.domain

class PhotoEntry {
    long id;
    String description;
    Date createDate;
    Date targetDate;
    String calendarId;
    String originalUrl;

    static constraints = {
        originalUrl(url: true)
    }
}
