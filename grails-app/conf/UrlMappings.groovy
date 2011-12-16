class UrlMappings {

	static mappings = {
		"/$controller/$action?/$id?"{
			constraints {
				// apply constraints here
			}
		}
        "/REST/calendar/$id?"(controller: "calendar", action: "index")
		"/"(view:"/index")
		"500"(view:'/error')
	}
}
