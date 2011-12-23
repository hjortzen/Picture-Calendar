package se.hjortzen.piccal.domain
import grails.converters.*
import java.text.SimpleDateFormat

class PhotoEntryController {
    def createPhotoEntry = {
        def dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH");
        //println('About to create a photo entry for the day ' + params.year + '-' + params.month + '-' + params.day + ' in the calendar with id ' + params.cal)
        def targetDateStr = params.year + '-' + params.month + '-' + params.day
        def targetAsDate = dateFormat.parse(targetDateStr + 'T00')
        def existingEntry = PhotoEntry.findByTargetDateBetween(targetAsDate, targetAsDate + 1)
        if (existingEntry) {
            render (status: 409, text: 'Entry already exists')
            return
        }

        def converter = new JSON(PhotoEntry);
        def jsonData = converter.parse(params.newEntry)
        println(jsonData)
        def newEntry = new PhotoEntry();
        newEntry.description = jsonData.description;
        newEntry.targetDate = dateFormat.parse(targetDateStr + 'T13')
        newEntry.createDate = dateFormat.parse(jsonData.createDate)
        newEntry.originalUrl = jsonData.origingalUrl;
        newEntry.calendar = se.hjortzen.piccal.domain.Calendar.get(params.cal);
        newEntry.content = jsonData.content;
        if (!newEntry.content) {
            newEntry.content = 'n/a'
        }
        newEntry.save();
        println('New object: ' + newEntry);
        render(status:  200, text: 'Entry created successfully')
    }

    def getPhotoContent = {
        PhotoEntry entry = PhotoEntry.get(params.id)
        if (entry) {
            println('Entry (' + entry.description + ') found, checking content')
            if (entry.content) {
                byte[] respContent = getFileContent(); //entry.content
                response.contentLength = respContent.length
                response.contentType = "image/jpeg"
                response.setHeader('Accept-Ranges','bytes')
                response.outputStream << respContent
            } else {
                render "Entry found, no content"
            }
        } else {
            render "No content"
        }
    }

    def getThumbnail = {
        PhotoEntry entry = PhotoEntry.get(params.id)
        if (entry) {
            println('Entry (' + entry.description + ') found, checking thumbnail')
            if (entry.thumbnail) {
                byte[] respContent = getFileContent(); //entry.thumbnail
                response.contentLength = respContent.length
                response.contentType = "image/jpeg"
                response.setHeader('Accept-Ranges','bytes')
                response.outputStream << respContent
            } else {
                render "Entry found, no content"
            }
        } else {
            render "No content"
        }
    }
    
    private byte[] getFileContent() {
        def f = new File("c:/picture.jpg");
        byte[] content = new byte [f.length()];
        InputStream is = new FileInputStream(f);

        int offset = 0;
        int numRead = 0;
        while (offset < content.length
                && (numRead=is.read(content, offset, content.length-offset)) >= 0) {
            offset += numRead;
        }
        is.close();
        return content;
    }
}
