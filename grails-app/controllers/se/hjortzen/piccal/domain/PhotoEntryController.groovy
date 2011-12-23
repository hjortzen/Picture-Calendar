package se.hjortzen.piccal.domain
import grails.converters.*
import java.text.SimpleDateFormat
import org.springframework.web.multipart.commons.CommonsMultipartFile

class PhotoEntryController {
    def createPhotoEntry = {
        println(params);
        def dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH");
        def targetDateStr = params.year + '-' + params.month + '-' + params.day
        def targetAsDate = dateFormat.parse(targetDateStr + 'T00')
        def existingEntry = PhotoEntry.findByTargetDateBetween(targetAsDate, targetAsDate + 1)
        if (existingEntry) {
            render (status: 409, text: 'Entry already exists')
            return
        }

        def newEntry = new PhotoEntry();
        newEntry.description = params.description;
        newEntry.targetDate = dateFormat.parse(targetDateStr + 'T13')
        if (params.createDate) {
            newEntry.createDate = dateFormat.parse( params.createDate )    
        } else {
            newEntry.createDate = new Date();
        }
        newEntry.originalUrl = params.origingalUrl;
        newEntry.calendar = se.hjortzen.piccal.domain.Calendar.get(params.cal);
        if (params.content) {
            newEntry.content = params.content.bytes
            println('Content length: ' + newEntry.content.length)
        } else {
            newEntry.content = 'n/a'
        }
        newEntry.save();
        render(status:  200, text: 'Entry created successfully')
    }

    def getPhotoContent = {
        PhotoEntry entry = PhotoEntry.get(params.id)
        if (entry) {
            println('Entry (' + entry.description + ') found, checking content')
            if (entry.content) {
                byte[] respContent = entry.content //getFileContent();
                response.contentLength = respContent.length
                response.contentType = "image/jpeg"
                response.setHeader('Accept-Ranges','bytes')
                response.outputStream << respContent
            } else {
                render(status: 204, text: 'No content (but entry exists)')
            }
        } else {
            render(status: 204, text: 'No content')
        }
    }

    def getThumbnail = {
        PhotoEntry entry = PhotoEntry.get(params.id)
        if (entry) {
            println('Entry (' + entry.description + ') found, checking thumbnail')
            if (entry.thumbnail) {
                byte[] respContent = entry.thumbnail
                response.contentLength = respContent.length
                response.contentType = "image/jpeg"
                response.setHeader('Accept-Ranges','bytes')
                response.outputStream << respContent
            } else {
                render(status: 204, text: 'No content (but entry exists)')
            }
        } else {
            render(status: 204, text: 'No content')
        }
    }
}
