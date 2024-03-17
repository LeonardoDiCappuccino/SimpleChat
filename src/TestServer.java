import Communication.ProtocolInformation;
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
                System.out.println(handler + " connected");
            }

            @Override
            public void receivedMessage(ClientHandler handler, byte[] bytes) {

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
