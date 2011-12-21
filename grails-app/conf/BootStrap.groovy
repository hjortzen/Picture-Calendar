import grails.converters.JSON

class BootStrap {

    def init = {servletContext ->
        JSON.registerObjectMarshaller(se.hjortzen.piccal.domain.PhotoEntry) {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['description'] = it.description
            returnArray['createDate'] = it.createDate
            returnArray['targetDate'] = it.targetDate
            returnArray['originalUrl'] = it.originalUrl

            return returnArray
        }
        JSON.registerObjectMarshaller(se.hjortzen.piccal.domain.Calendar) {
            def returnArray = [:]
            returnArray['id'] = it.id
            returnArray['description'] = it.description

            return returnArray
        }
    }

    def destroy = {
    }
}
