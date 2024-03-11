import ServerStuff.*;

public class ServerMain extends Thread {

    public static void main(String[] args) {
        new Server(6969, defaultEvents);
    }

    public static ClientEventListener defaultEvents = new ClientEventListener() {
        @Override
        public void connectionEvent(ClientHandler handler) {
        }

        @Override
        public void messageEvent(ClientHandler handler, byte[] bytes) {
        }

        @Override
        public void messageEvent(ClientHandler handler, Object object) {
        }

        @Override
        public void lostConnectionEvent(ClientHandler handler) throws InterruptedException {
            for(int i = 0; i < 5; i ++) {
                Thread.sleep(1000);
                if(handler.isConnected())
                    return;
            }
        }

        @Override
        public void disconnectionEvent(ClientHandler handler) {
        }
    };
}