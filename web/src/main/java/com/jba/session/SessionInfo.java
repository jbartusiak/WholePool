package com.jba.session;

import com.jba.dao2.user.enitity.User;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class SessionInfo {
    private User userInSession;

    public User getUserInSession() {
        return userInSession;
    }

    public void setUserInSession(User userInSession) {
        this.userInSession = userInSession;
    }
}
