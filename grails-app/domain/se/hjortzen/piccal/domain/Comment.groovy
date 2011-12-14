package se.hjortzen.piccal.domain

class Comment {
    PhotoEntry photoEntry;
    String content;
    static constraints = {
        content(blank: true)
    }
}
