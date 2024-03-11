package ClientStuff;

@SuppressWarnings("unused")
public interface ServerEventListener {

    void serverNotAvailable(ServerConnection server) throws InterruptedException;

    void messageEvent(ServerConnection server, byte[] bytes) throws InterruptedException;

    void messageEvent(ServerConnection server, Object object) throws InterruptedException;

    void lostConnection(ServerConnection server) throws InterruptedException;

}
