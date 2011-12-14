package se.hjortzen.piccal.domain

class User {
    String userId;
    String description;
    String email;

    static constraints = {
        userId(blank: false, unique: true)
        email(email: true)
    }
}
