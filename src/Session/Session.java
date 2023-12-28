package Session;

import Model.User;

public class Session {
    private static Session instance;
    private User loggedInUser;

    private Session() {
        // Private constructor to enforce singleton pattern
    }

    public static Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public void clearSession() {
        loggedInUser = null;
    }
}

