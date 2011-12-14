package se.hjortzen.piccal.domain

class Calendar {
    String userId;

    static constraints = {
        userId(blank: false)
    }
}
