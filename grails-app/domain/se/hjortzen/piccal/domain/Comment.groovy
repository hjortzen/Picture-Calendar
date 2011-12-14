package se.hjortzen.piccal.domain

class Comment {
    String content;
    static constraints = {
        content(blank: true)
    }
}
