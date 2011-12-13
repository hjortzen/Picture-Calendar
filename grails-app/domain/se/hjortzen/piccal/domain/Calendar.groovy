package se.hjortzen.piccal.domain

class Calendar {
    long id;
    String userId;

    static constraints = {
        id (unique: true)
        userId(blank: false)
    }
}
