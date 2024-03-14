import ClientStuff.ServerConnection;
import ClientStuff.ServerEventListener;
import Communication.Packet;
import Communication.ProtocolHeader;
import Communication.SerializableImage;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.Arrays;

public class ClientMain {

    public static void main(String[] args) {

        ServerConnection client = new ServerConnection("localhost", 6969, events);
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
            if (Arrays.equals(bytes, "Hi client".getBytes())) {
                server.send("Hi server".getBytes());

                byte[] response = (byte[]) server.catchResponse();
                if (Arrays.equals(response, "OK".getBytes()))
                    System.out.println("Accepted");
            }
        }

        @Override
        public void receivedMessage(ServerConnection server, Object object) {
            if (object.equals(ProtocolHeader.Image)) {
                Object response = server.catchResponse();
                if (response instanceof SerializableImage) {
                    Image img = ((SerializableImage) response).getImage();
                    Test.showImage(img);
                }
            }
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
