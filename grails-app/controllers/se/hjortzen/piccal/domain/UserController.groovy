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
        def redirectUrl = params.redirect
        if (!redirectUrl) {
            redirectUrl = "/index.gsp"
        }
        redirect(uri: redirectUrl)
    }

    def login = {
        def redirectUrl = params.redirect
        if (!redirectUrl) {
            redirectUrl = "/index.gsp"
        }
        redirect(uri: redirectUrl)
    }

    def listActiveUsers = {
        def activeUsers = new ArrayList<User>();
        def calendars = Calendar.list();
        for (cal in calendars) {
            if (!activeUsers.contains(cal.user)) {
                activeUsers.add(cal.user)
            }
        }
        render activeUsers as JSON;
    }

    def listCalendarsForUser = {
        def user = User.get(params.userId)
        def calendars = Calendar.findAllByUser(user)
        render calendars as JSON;
    }
}
