package se.hjortzen.piccal.domain

import grails.converters.*

class UserController {
    def isUserLoggedIn = {
        def loggedIn = session.getAttribute("loggedIn");
        if (!loggedIn) {
            loggedIn = false;
        }
        render "{\"loggedIn\": \"" + loggedIn + "\"}"
    }

    def logout = {
        session.invalidate()
        redirect(uri: "/index.gsp")
    }

    def login = {
        redirect(uri: "/index.gsp")
    }
}
