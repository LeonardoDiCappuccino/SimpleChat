import ClientStuff.ServerConnection;
import ClientStuff.ServerEventListener;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientMain {

    public static void main(String[] args) {

        ServerConnection client = new ServerConnection("localhost", 6969, events);

        if (!client.isConnected())
            return;

        ArrayList<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        list.add(5);

        client.send(list);

        client.closeConnection();
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
        }

        @Override
        public void messageEvent(ServerConnection server, byte[] message) {

        }

        @Override
        public void messageEvent(ServerConnection server, Object object) {

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
