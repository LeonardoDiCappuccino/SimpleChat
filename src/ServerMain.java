import Communication.Container;
import Communication.Packet;
import Communication.ProtocolHeader;
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
            if (!(object instanceof Packet packet))
                return;

            //Check if it's a LogIn request
            if (packet.isHead(ProtocolHeader.LogIn))
                logInRequest(packet, handler);
            else if (packet.getHead().equals(ProtocolHeader.SignIn))
                signInRequest(packet, handler);
            else
                handler.send(ProtocolHeader.IllegalArgument);
        }

        @Override
        public void lostConnection(ClientHandler handler) {
        }
    };

    public static void logInRequest(Packet packet, ClientHandler handler) {
        //Checks if there are the correct amount of args
        Object[] args = packet.getObjectList().toArray();
        if (args.length != 2) {
            handler.send(ProtocolHeader.IllegalArgument);
            return;
        }

        //Checks if args have correct datatype
        String username, password;
        try {
            username = (String) args[0];
            password = (String) args[1];
        } catch (ClassCastException e) {
            handler.send(ProtocolHeader.IllegalArgument);
            return;
        }

        Container.Profile profile = database.getUser(username, password);

        //If user not found
        if (profile == null) {
            handler.send(ProtocolHeader.InvalidLogInData);
            return;
        }

        //Register a user in the connection cash
        new User(profile, handler);
    }

    public static void signInRequest(Packet packet, ClientHandler handler) {

    }
}