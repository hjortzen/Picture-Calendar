package picture.calendar

import se.hjortzen.piccal.domain.User
import edu.yale.its.tp.cas.client.filter.CASFilter

class CASFilters {

    def filters = {
        all(controller:'*', action:'*') {

            before = {
                if(session?.getAttribute("loggedIn") == null) {
                    def username = session?.getAttribute(CASFilter.CAS_FILTER_USER)
                    if (!username) {
                        //println('User not logged in')
                        return;
                    }
                    if(!User.findByLoginName(username)) {
                        def us = new User(description: username, loginName: username, email: "default@nowhere.org" )
                        us.save();
                    }
                    User user = User.findByLoginName(username)
                    session.setAttribute("loggedIn", true)
                    session.setAttribute("loggedInUser", username)
                    session.setAttribute("user", user)
                } else {
                    //println("User " + session?.getAttribute("user")+ " was already logged in")
                }
            }
            after = {
                
            }
            afterView = {
                
            }
        }
    }
}
