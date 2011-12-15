package se.hjortzen.piccal.domain

import grails.test.*

class UserTests extends GrailsUnitTestCase {
    protected void setUp() {
        super.setUp()
    }

    protected void tearDown() {
        super.tearDown()
    }

    void testEmailValidation() {
        def user = new User(description: "A Unit Test User", loginName: "UnitUser")
        user.email = "failed"
        assertFalse "Incorrect email should have generated an error ", user.validate()
        def emailError = user.errors.getFieldError("email")
        assertNotNull "An error with error code for email should have been generated", emailError
        user.email = "user@domain.com"
        user.validate();
        emailError = user.errors.getFieldError("email")
        assertNull "No validation errors should have been present at this moment for email", emailError
    }
}
