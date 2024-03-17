package ServerStuff;

import Communication.Container;

public class User extends Client {

    private final int userID;

    public User(Container.Profile profile, ClientHandler boundedHandler) {
        super(boundedHandler);
        userID = profile.userID();

        //Send user his profile
        send(profile);
    }

    @Override
    public void receivedMessage(byte[] bytes) {

    }

    @Override
    public void receivedMessage(Object object) {

    }

    @Override
    public void lostConnection() {

    }

    public int getUserID() {
        return userID;
    }
}
