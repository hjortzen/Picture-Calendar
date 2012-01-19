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

    def listActiveUsers = {
        def activeUsers = new ArrayList<User>();
        def calendars = Calendar.list();
        for (cal in calendars) {
            activeUsers.add(cal.user)
        }
        render activeUsers as JSON;
    }

    def listCalendarsForUser = {
        def user = User.get(params.userId)
        def calendars = Calendar.findAllByUser(user)
        render calendars as JSON;
    }
}
