import com.sun.org.apache.bcel.internal.generic.GETFIELD

class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}

        "/REST/calendar/$id?" (controller: "calendar", parseRequest: true) {
            action = [
                GET: "getCalendar",
                PUT: "createCalendar"
            ]
        }

        "/REST/calendar/$cal/$year/$month?/$day?" (controller: "calendar", parseRequest: true) {
            action = [
                    GET: "getPhotoContent",
            ]
        }

		"/"(view:"/index")
		"500"(view:'/error')
	}
}
