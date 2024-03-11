package ServerStuff;

import ClientStuff.ServerConnection;

@SuppressWarnings("unused")

public interface ClientEventListener {

    void connectionEvent(ClientHandler handler);

    void messageEvent(ClientHandler handler, byte[] bytes);

    void messageEvent(ClientHandler handler, Object object);

    void lostConnectionEvent(ClientHandler handler) throws InterruptedException;

    void disconnectionEvent(ClientHandler handler);

}