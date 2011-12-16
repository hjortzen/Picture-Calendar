package se.hjortzen.piccal.domain

import grails.test.*

class PhotoEntryTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testUrlValidation() {
        def photoEntry = new PhotoEntry(content: new byte[128], description: "Integration test filed", createDate: new Date(), targetDate: new Date());
        photoEntry.originalUrl = "http://www.photoEntry.com"
        photoEntry.validate();

        def urlError = photoEntry.errors.getFieldError("originalUrl")
        assertNull "No validation errors should have been present at this moment for url", urlError
    }
}
