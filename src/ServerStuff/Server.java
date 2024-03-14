package ServerStuff;

import java.io.IOException;
import java.io.Serializable;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("unused")
public class Server {

    private final ServerSocket serverSocket;
    private final Thread connectionListenerThread;

    private final List<ClientHandler> clients = new LinkedList<>();

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
                clients.add(new ClientHandler(serverSocket.accept(), clientEvents, clients));
            } catch (IOException e) {
                if (isOpen())
                    throw new RuntimeException(e);
            }
        }
    }

    public List<ClientHandler> getClientHandlers() {
        return new ArrayList<>(clients);
    }

    public <T extends Client> List<T> getClients(Class<T> targetType) {
        ArrayList<T> out = new ArrayList<>(clients.size());

        for (ClientHandler clientHandler : clients) {
            Client client = clientHandler.getBoundedClient();
            if (client != null)
                if (targetType.isInstance(client))
                    out.add((T) client);
        }

        out.trimToSize();
        return out;
    }

    public void sendToAll(byte[] object) {
        for(ClientHandler client : clients)
            client.send(object);
    }

    public void sendToAll(Serializable object) {
        for(ClientHandler client : clients)
            client.send(object);
    }

    public void sendToAll(Class<Client> targetType, byte[] object) {
        List<Client> targetClients = getClients(targetType);
        for(Client client : targetClients)
            client.send(object);
    }

    public void sendToAll(Class<Client> targetType, Serializable object) {
        List<Client> targetClients = getClients(targetType);
        for(Client client : targetClients)
            client.send(object);
    }

    public void close() {
        connectionListenerThread.interrupt();

        for (ClientHandler handler : clients)
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