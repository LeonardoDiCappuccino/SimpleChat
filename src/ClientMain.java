import ClientStuff.ServerConnection;
import ClientStuff.ServerEventListener;
import UI.Frame;

public class ClientMain {

    public static void main(String[] args) {
//        ServerConnection client = new ServerConnection("localhost", 6969, events);
        Frame frame = Frame.getInstance();

    }

    private static final ServerEventListener events = new ServerEventListener() {
        @Override
        public void serverNotAvailable(ServerConnection server) throws InterruptedException {
            for (int i = 0; i < 5; i++) {
                System.out.println("Build up connection...");
                Thread.sleep(1000);

                server.connect();
                if (server.isConnected())
                    return;
            }
            System.out.println("Server not found");
            System.exit(0);
        }

        @Override
        public void receivedMessage(ServerConnection server, byte[] bytes) {

        }

        @Override
        public void receivedMessage(ServerConnection server, Object object) {

        }

        @Override
        public void lostConnection(ServerConnection server) throws InterruptedException {
            for (int i = 0; i < 5; i++) {
                System.out.println("Rebuild connection...");
                Thread.sleep(1000);

                server.reconnect();
                if (server.isConnected()) {
                    server.send("I'm back mf".getBytes());
                    return;
                }
            }
            System.out.println("Lost server");
        }
    };
}
