package ServerStuff;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

@SuppressWarnings("unused")

public class ClientHandler {

    private final Socket socket;
    private final DataInputStream reader;
    private final DataOutputStream writer;
    private final Thread messageListenerThread;

    private final ClientEventListener defaultEvent;
    private final List<ClientHandler> connections;

    private static int count = 0;
    private static Queue<Integer> freeIDs = new LinkedList<>();

    private final int id;

    private Client boundedClient = null;

    public void boundClient(Client client) {
        boundedClient = client;
    }

    public ClientHandler(Socket socket, ClientEventListener defaultEvent, List<ClientHandler> connections) {
        this.socket = socket;
        this.defaultEvent = defaultEvent;
        this.connections = connections;

        try {
            reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            writer = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            try {
                socket.close();
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
            throw new RuntimeException(e);
        }

        defaultEvent.connectionEvent(this);

        messageListenerThread = new Thread(this::receivingMessages);
        messageListenerThread.start();

        if (!freeIDs.isEmpty())
            id = freeIDs.remove();
        else
            id = count++;
    }

    private void receivingMessages() {
        while (isConnected()) {
            try {
                int length = reader.readInt();
                if (length > 0) {
                    byte[] bytes = new byte[length];
                    boolean isObject = reader.readBoolean();
                    reader.readFully(bytes);

                    if (isObject) {
                        Object object;
                        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                             ObjectInputStream ois = new ObjectInputStream(bis)) {
                            object = ois.readObject();
                        } catch (IOException | ClassNotFoundException ignore) {
                            return;
                        }

                        if (boundedClient != null)
                            boundedClient.messageEvent(object);
                        else
                            defaultEvent.messageEvent(this, object);

                    } else {
                        if (boundedClient != null)
                            boundedClient.messageEvent(bytes);
                        else
                            defaultEvent.messageEvent(this, bytes);
                    }
                }

            } catch (EOFException e) {
                //On lost connection
                closeSocketAndStreams();

                try {
                    if (boundedClient != null)
                        boundedClient.lostConnection();
                    else
                        defaultEvent.lostConnection(this);
                } catch (InterruptedException ignore) {
                }

                //If no reconnected happens close current Thread
                if (!isConnected())
                    messageListenerThread.interrupt();

            } catch (IOException e) {
                if (!socket.isClosed())
                    throw new RuntimeException(e);
            }
        }
    }

    public void send(byte[] bytes) {
        try {
            writer.writeInt(bytes.length);
            //Indicates a send raw byte[] (isObject)
            writer.writeBoolean(false);

            writer.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void send(Serializable object) {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {

            //Serialize Object to byte[]
            oos.writeObject(object);
            byte[] messageBytes = bos.toByteArray();

            writer.writeInt(messageBytes.length);

            //Indicates a send Object (isObject)
            writer.writeBoolean(true);

            writer.write(messageBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeConnection() {
        try {
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Close messageListener
        messageListenerThread.interrupt();

        //On disconnect
        if (boundedClient != null) boundedClient.disconnectionEvent();
        else defaultEvent.disconnectionEvent(this);

        //Remove from server connection list
        connections.remove(this);

        //Release id
        freeIDs.add(id);
    }

    public boolean isConnected() {
        return !socket.isClosed();
    }

    public Client getBoundedClient() {
        return boundedClient;
    }

    public Socket getSocket() {
        return socket;
    }

    private void closeSocketAndStreams() {
        try {
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}