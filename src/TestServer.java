import Communication.ProtocolHeader;
import Communication.SerializableImage;
import ServerStuff.ClientEventListener;
import ServerStuff.ClientHandler;
import ServerStuff.Server;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class TestServer {

    public static void main(String[] args) {
        Server server = new Server(6969, new ClientEventListener() {
            @Override
            public void onConnection(ClientHandler handler) {
                handler.send("Hi client".getBytes());

                byte[] response = (byte[]) handler.catchResponse();
                if (Arrays.equals(response, "Hi server".getBytes())) {
                    handler.send("OK".getBytes());
                    System.out.println(handler + " connected");

                    try {
                        handler.send(ProtocolHeader.Image);
                        SerializableImage img = new SerializableImage(
                                ImageIO.read(new File("/home/lasse/Downloads/Implementationsdiagram.png")));
                        handler.send(img);

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void receivedMessage(ClientHandler handler, byte[] bytes) {
                System.out.println(handler + " says: " + new String(bytes));
            }

            @Override
            public void receivedMessage(ClientHandler handler, Object object) {

            }

            @Override
            public void lostConnection(ClientHandler handler) {
                System.out.println(handler + " disconnected");
            }
        });
    }
}
