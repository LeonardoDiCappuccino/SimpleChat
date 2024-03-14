package ClientStuff;

@SuppressWarnings("unused")
public interface ServerEventListener {

    void serverNotAvailable(ServerConnection server) throws InterruptedException;

    void receivedMessage(ServerConnection server, byte[] bytes) throws InterruptedException;

    void receivedMessage(ServerConnection server, Object object) throws InterruptedException;

    void lostConnection(ServerConnection server) throws InterruptedException;

}
