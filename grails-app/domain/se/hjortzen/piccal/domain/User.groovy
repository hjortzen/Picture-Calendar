package se.hjortzen.piccal.domain

class User {
    String id;
    String description;
    String email;

    static constraints = {
        id(blank: false)
    }
}
