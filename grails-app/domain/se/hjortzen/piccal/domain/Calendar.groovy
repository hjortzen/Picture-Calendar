package se.hjortzen.piccal.domain

class Calendar {
    User user;
    String description;

    static constraints = {
        description(blank: false)
    }
    String toString() {
        return description;
    }
}
