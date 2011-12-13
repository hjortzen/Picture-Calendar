package se.hjortzen.piccal.domain

class PhotoEntry {
    long id;
    String description;
    Date createDate;
    Date targetDate;
    String userId;
    String originalUrl;

    static constraints = {
    }
}
