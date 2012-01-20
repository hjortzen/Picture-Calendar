package se.hjortzen.piccal.domain
import grails.converters.*
import java.text.SimpleDateFormat

class CalendarController {
    def scaffold = Calendar
    def jsonList = {
        render Calendar.list() as JSON
    }

    def getPhotoContent = {
        //We might get year, month and day as params. Year is required
        def cal = Calendar.get(params.cal)
        if (!cal) {
            render "Calendar not found"
            return;
        }
        def pattern = "yyyy"
        def reqDateStr = params.year;
        if (params.month) {
            pattern += "MM"
            reqDateStr += params.month
            if (params.day) {
                pattern += "dd"
                reqDateStr += params.day
            }
        }
        def formatter = new SimpleDateFormat(pattern)
        def requestDate = formatter.parse(reqDateStr)
        def toDate = java.util.Calendar.getInstance()
        toDate.setTime(requestDate)
        if (params.day) {
            toDate.set(java.util.Calendar.HOUR_OF_DAY, toDate.getActualMaximum(java.util.Calendar.HOUR_OF_DAY))
        } else if (params.month) {
            toDate.set(java.util.Calendar.DAY_OF_MONTH, toDate.getActualMaximum(java.util.Calendar.DAY_OF_MONTH))
        } else {
            toDate.set(java.util.Calendar.DAY_OF_YEAR, toDate.getActualMaximum(java.util.Calendar.DAY_OF_YEAR))
        }
        def photoEntries = PhotoEntry.findAllByCalendarAndTargetDateBetween(cal, requestDate, (toDate.time+1))
        if (photoEntries) {
            render photoEntries as JSON
        } else {
            render "No result"
        }
    }

    def getCalendar = {
        if (params.id) {
            def cal = Calendar.get(params.id)
            if (cal) {
                //TODO: Should we really check for ownership here? GET should always be available to public?
                if (cal.user.id != session.getAttribute("user").id) {
                    render "Not yours!"
                    return;
                } else {
                    render cal as JSON
                }
            }
        } else {
            def calendars = Calendar.findAllByUser(session.getAttribute("user"))
            if (calendars) {
                render calendars as JSON
            }
        }
        render "No result"
    }

    def createCalendar = {
        def user = session.getAttribute("user")
        if (!user) {
            println('Tried to create calendar without being logged in');
            render(status: 401, text: 'This action requires login');
            return;
        }
        def calendar = new Calendar()
        calendar.user = user
        calendar.description = params.name;
        if (calendar.save()) {
            redirect(uri: "/display_users.html")
            return;
        }
        response.status = 500;
        render "The parameters given were in incorrect format"
    }
}
