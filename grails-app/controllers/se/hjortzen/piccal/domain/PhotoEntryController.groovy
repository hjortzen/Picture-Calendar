package se.hjortzen.piccal.domain
import grails.converters.*
import java.text.SimpleDateFormat
import org.springframework.web.multipart.commons.CommonsMultipartFile
import org.grails.plugins.imagetools.*

class PhotoEntryController {
    def createPhotoEntry = {
        println("Params: " + params);
        if (!session.getAttribute("user")) {
            println('Tried to create entry without being logged in');
            render(status: 401, text: 'This action requires login');
            return;
        }
        def cal = se.hjortzen.piccal.domain.Calendar.get(params.cal);
        if (!cal) {
            render(status: 400, text: 'Specified calendar does not exist')
            return
        }
        if (cal.user.id != session.getAttribute("user").id) {
            println('Wrong user tried to create entry, ' + session.getAttribute("user").id + ' instead of ' + cal.user.id);
            render(status: 401, text: 'This requires a logged in user with sufficient authorization')
            return
        }
        def dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH");
        def targetDateStr = params.year + '-' + params.month + '-' + params.day
        def targetAsDate = dateFormat.parse(targetDateStr + 'T00')
        def existingEntries = PhotoEntry.findAllByTargetDateBetween(targetAsDate, targetAsDate + 1)
        //Check if the calendar id is also a match
        boolean alreadyExists = false;
        existingEntries.each() {
            if (it.calendar.id == cal.id) {
                alreadyExists = true
            }
        }
        if (alreadyExists) {
            render(status: 409, text: 'Entry already exists')
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
        newEntry.originalUrl = params.originalUrl;
        newEntry.calendar =  cal;
        if (params.content) {
            newEntry.content = params.content.bytes
            println('Content length: ' + newEntry.content.length)
            //Create thumbnail
            def imageTool = new ImageTool()
            imageTool.load(newEntry.content)
            imageTool.square()
            imageTool.thumbnail(90)
            newEntry.thumbnail = imageTool.getBytes("JPEG")
            println('A thumbnail has been created, size ' + newEntry.thumbnail.length)
        } else {
            newEntry.content = 'n/a'
        }

        if (!newEntry.validate()) {
            if (newEntry.errors.errorCount != 0) {
                response.status = 500
                render newEntry.errors as JSON
                return;
            }
        }
        newEntry.save()
        render(status:  200, text: 'Entry created successfully')
    }

    def getPhotoContent = {
        PhotoEntry entry = PhotoEntry.get(params.id)
        if (entry) {
            if (entry.content) {
                byte[] respContent = entry.content //getFileContent();
                withCacheHeaders {
                    etag {
                        "content_${entry.id}_${entry.version}"
                    }
                    lastModified {
                        entry.createDate
                    }
                    generate {
                        cache shared: true, neverExpires: true
                        response.contentLength = respContent.length
                        response.contentType = "image/jpeg"
                        response.setHeader('Accept-Ranges','bytes')
                        response.outputStream << respContent
                    }
                }
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
            if (entry.thumbnail) {
                byte[] respContent = entry.thumbnail
                cache shared:true, neverExpires:true
                withCacheHeaders {
                    etag {
                        "thmb_${entry.id}_${entry.version}"
                    }
                    lastModified {
                        entry.createDate
                    }
                    generate {
                        cache shared: true, neverExpires: true
                        response.contentLength = respContent.length
                        response.contentType = "image/jpeg"
                        response.setHeader('Accept-Ranges','bytes')
                        response.outputStream << respContent
                    }
                }
            } else {
                render(status: 204, text: 'No content (but entry exists)')
            }
        } else {
            render(status: 204, text: 'No content')
        }
    }
}
