package ServerStuff;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
public class Server {

    private final ServerSocket serverSocket;
    private final Thread connectionListenerThread;

    private final List<ClientHandler> connections = new LinkedList<>();

    private final ClientEventListener clientEvents;

    public Server(int port, ClientEventListener clientEvents) {
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        connectionListenerThread = new Thread(this::connectionFetching);
        connectionListenerThread.start();

        this.clientEvents = clientEvents;
    }

    private void connectionFetching() {
        while (serverSocket != null && isOpen()) {
            try {
                connections.add(new ClientHandler(serverSocket.accept(), clientEvents, connections));
            } catch (IOException e) {
                if (isOpen())
                    throw new RuntimeException(e);
            }
        }
    }

    public List<ClientHandler> getClientHandlers() {
        return new ArrayList<>(connections);
    }

    public List<Client> getClients() {
        return getClients(Client.class);
    }

    public <T extends Client> List<T> getClients(Class<T> targetType) {
        ArrayList<T> out = new ArrayList<>(connections.size());

        for (ClientHandler clientHandler : connections) {
            Client client = clientHandler.getBoundedClient();
            if (client != null)
                if (targetType.isInstance(client))
                    out.add((T) client);
        }

        out.trimToSize();
        return out;
    }

    public void close() {
        connectionListenerThread.interrupt();

        for (ClientHandler handler : connections)
            handler.closeConnection();

        try {
            serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isOpen() {
        return !serverSocket.isClosed();
    }
}