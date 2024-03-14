package ServerStuff;

public interface ClientEventListener {

    void onConnection(ClientHandler handler);

    void receivedMessage(ClientHandler handler, byte[] bytes);

    void receivedMessage(ClientHandler handler, Object object);

    void lostConnection(ClientHandler handler);
}