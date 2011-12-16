package se.hjortzen.piccal.domain
import grails.converters.*

class CalendarController {
    def scaffold = Calendar
    def jsonList = {
        render Calendar.list() as JSON
    }

    def index = {
        switch(request.method) {
            case "GET":
                if (params.id) {
                    render Calendar.get(params.id) as JSON
                }
                render Calendar.list() as JSON
                break
            case "POST":
                println "Params: " + params
                def calendar = new Calendar(params.Calendar)
                if (calendar.save()) {
                    response.status = 201
                    render calendar as JSON
                }
                //println calendar.errors;
                response.status = 500;
                render "The parameters given were in incorrect format"
                break
            default:
                render "Unknow method";
                break
        }
    }
}
