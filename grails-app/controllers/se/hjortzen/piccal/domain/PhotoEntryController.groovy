package se.hjortzen.piccal.domain

class PhotoEntryController {
    def getPhotoContent = {
        PhotoEntry entry = PhotoEntry.get(params.id)
        if (entry) {
            println('Entry (' + entry.description + ') found, checking content')
            if (entry.content) {
                response.contentType = "image"
                response.outputStream << getFileContent()
                //response.outputStream << entry.content
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
