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
        }
        if (cal && cal.user.id != session.getAttribute("user").id) {
            render "Not authorized"
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

        println("Requesting date: " + requestDate)
        println("To: " + toDate.time)
        def photoEntries = PhotoEntry.findAllByTargetDateBetween(requestDate, toDate.time)
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
        //TODO: Get this working
        println "Params: " + request.JSON
        def calendar = new Calendar(params.Calendar)
        if (calendar.save()) {
            response.status = 201
            render calendar as JSON
        }
        //println calendar.errors;
        response.status = 500;
        render "The parameters given were in incorrect format"
    }
}
