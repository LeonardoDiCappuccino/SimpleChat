import Communication.Container;
import Communication.ProtocolInformation;
import SQLiteStuff.ChatDB;
import ServerStuff.*;

public class ServerMain extends Thread {

    private static final ChatDB database = ChatDB.getInstance();
    private static Server server;

    public static void main(String[] args) {
        server = new Server(6969, defaultEvents);
    }

    public static ClientEventListener defaultEvents = new ClientEventListener() {
        @Override
        public void onConnection(ClientHandler handler) {
        }

        @Override
        public void receivedMessage(ClientHandler handler, byte[] bytes) {
        }

        @Override
        public void receivedMessage(ClientHandler handler, Object object) {
            //Ignore everything but packets
            switch (object) {
                case Container.LogInData data -> {
                    Container.Profile profile = database.getUserProfile(data.username(), data.password());

                    //If profile found, register user as connected, if not send error to client
                    if(profile != null)
                        new User(profile, handler);
                    else
                        handler.send(ProtocolInformation.InvalidLogInData);
                }

                case Container.SignInData data -> {
                    Container.Profile profile = database.createUser
                            (data.username(), data.password(), data.profilePicture());

                    //If profile successfully created, register user as connected, if not send error to client
                    if(profile != null)
                        new User(profile, handler);
                    else
                        handler.send(ProtocolInformation.UsernameTaken);
                }

                default -> handler.send(ProtocolInformation.IllegalArgument);
            };
        }

        @Override
        public void lostConnection(ClientHandler handler) {
        }
    };
}