package se.hjortzen.piccal.domain

class User {
    String loginName;
    String description;
    String email;

    static constraints = {
        email(email: true)
    }
    String toString() {
        return description;
    }
}
