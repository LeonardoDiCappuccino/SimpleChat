package ServerStuff;

import java.io.Serializable;
import java.net.Socket;

@SuppressWarnings("unused")
public abstract class Client {

    private final ClientHandler boundedHandler;

    public Client(ClientHandler boundedHandler) {
        this.boundedHandler = boundedHandler;
        boundedHandler.boundClient(this);
    }

    public void send(byte[] bytes) {
        boundedHandler.send(bytes);
    }

    public void send(Serializable object) {
        boundedHandler.send(object);
    }

    protected Socket getSocket() {
        return boundedHandler.getSocket();
    }

    public void close() {
        boundedHandler.closeConnection();
    }

    public boolean isConnected() {
        return boundedHandler.isConnected();
    }

    public abstract void receivedMessage(byte[] bytes);

    public abstract void receivedMessage(Object object);

    public abstract void lostConnection();
}
