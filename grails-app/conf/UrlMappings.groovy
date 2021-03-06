import com.sun.org.apache.bcel.internal.generic.GETFIELD

class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

        //PhotoEntry
        "/REST/entry/$id/content"(controller: "photoEntry", parseRequest: true) {
            action = [
                    GET: "getPhotoContent"
            ]
        }
        "/REST/entry/$id/thumbnail"(controller: "photoEntry", parseRequest: true) {
            action = [
                    GET: "getThumbnail"
            ]
        }

        "/REST/entry/$cal/$year/$month/$day"(controller: "photoEntry", parseRequest: true) {
            action = [
                    POST:  "createPhotoEntry",
                    PUT: "createPhotoEntry"
            ]
        }
        //END PhotoEntry

        "/REST/calendar/$id?"(controller: "calendar", parseRequest: true) {
            action = [
                GET: "getCalendar",
                PUT: "createCalendar",
                POST: "createCalendar"
            ]
        }

        "/REST/calendar/$cal/$year/$month?/$day?" (controller: "calendar", parseRequest: true) {
            action = [
                    GET: "getPhotoContent"
            ]
        }

        //USER
        "/REST/isLoggedIn"(controller: "user") {
            action = [
                    GET: "isUserLoggedIn"
            ]
        }

        "/REST/logout"(controller: "user") {
            action = [
                    GET: "logout"
            ]
        }

        "/REST/login"(controller: "user") {
            action = [
                    GET: "login"
            ]
        }
        "/REST/users"(controller: "user") {
            action = [
                    GET: "listActiveUsers"
            ]
        }
        "/REST/users/$userId/calendars"(controller: "user") {
            action = [
                    GET: "listCalendarsForUser"
            ]
        }
        //END User
		"/"(view:"/index")
		"500"(view:'/error')
	}
}
