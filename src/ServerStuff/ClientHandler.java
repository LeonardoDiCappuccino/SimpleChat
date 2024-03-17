package ServerStuff;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.concurrent.*;

@SuppressWarnings("unused")
public class ClientHandler {

    private final Socket socket;
    private final DataInputStream reader;
    private final DataOutputStream writer;
    private final Thread messageListenerThread;

    private final ClientEventListener defaultEvent;
    private final List<ClientHandler> clients;

    private Client boundedClient = null;


    public ClientHandler(Socket socket, ClientEventListener defaultEvent, List<ClientHandler> clients) {
        this.socket = socket;
        this.defaultEvent = defaultEvent;
        this.clients = clients;

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

        defaultEvent.onConnection(this);

        messageListenerThread = new Thread(this::receivingMessages);
        messageListenerThread.start();
    }

    private void receivingMessages() {
        while (isConnected()) {
            Object message = getNextMessage();

            if (message instanceof byte[]) {
                if (boundedClient != null)
                    boundedClient.receivedMessage((byte[]) message);
                else
                    defaultEvent.receivedMessage(this, (byte[]) message);
            } else {
                if (boundedClient != null)
                    boundedClient.receivedMessage(message);
                else
                    defaultEvent.receivedMessage(this, message);
            }
        }
    }

    private Object getNextMessage() {
        try {
            int length = reader.readInt();
            if (length > 0) {
                byte[] bytes = new byte[length];
                boolean isObject = reader.readBoolean();
                reader.readFully(bytes);

                if (isObject) {
                    try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                         ObjectInputStream ois = new ObjectInputStream(bis)) {
                        return ois.readObject();
                    } catch (IOException | ClassNotFoundException ignore) {
                        return null;
                    }
                } else return bytes;
            }
        } catch (EOFException e) {
            //On lost connection
            lostConnectionHandling();

        } catch (IOException e) {
            if (!socket.isClosed())
                throw new RuntimeException(e);
        }
        return null;
    }

    public synchronized void send(byte[] bytes) {
        if (!isConnected())
            throw new RuntimeException(new SocketException("Socket is closed"));

        try {
            writer.writeInt(bytes.length);
            //Indicates a send raw byte[] (isObject)
            writer.writeBoolean(false);

            writer.write(bytes);
        } catch (IOException e) {
            //Connection failed
            lostConnectionHandling();
        }
    }

    public synchronized void send(Serializable object) {
        if (!isConnected())
            throw new RuntimeException(new SocketException("Socket is closed"));

        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {

            //Serialize Object to byte[]
            oos.writeObject(object);
            byte[] messageBytes = bos.toByteArray();

            writer.writeInt(messageBytes.length);

            //Indicates a send Object (isObject)
            writer.writeBoolean(true);

            writer.write(messageBytes);

        } catch (NotSerializableException e) {
            throw new RuntimeException(e);

        } catch (IOException e) {
            //Connection failed
            lostConnectionHandling();
        }
    }

    public synchronized Object catchResponse() {
        if (!isConnected())
            throw new RuntimeException(new SocketException("Socket is closed"));

        if (messageListenerThread != null)
            if (!messageListenerThread.equals(Thread.currentThread()))
                throw new RuntimeException(new WrongThreadException(
                        "'catchResponse' can only be called from 'receivedMessage' and 'onConnection'" +
                                " methods. Leads to unexpected behavior"));

        return getNextMessage();
    }

    public synchronized Object catchResponse(int timeout) {
        if (!isConnected())
            throw new RuntimeException(new SocketException("Socket is closed"));

        if (messageListenerThread != null)
            if (!messageListenerThread.equals(Thread.currentThread()))
                throw new RuntimeException(new WrongThreadException(
                        "'catchResponse' can only be called from 'receivedMessage' and 'onConnection'" +
                                " methods. Leads to unexpected behavior"));

        Future<Object> responseCatcher = Executors.newSingleThreadExecutor()
                .submit(this::getNextMessage);

        try {
            return responseCatcher.get(timeout, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            responseCatcher.cancel(true);
            return null;
        }
    }

    public synchronized void closeConnection() {
        if (!isConnected())
            return;

        try {
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Close messageListener
        if (messageListenerThread != null)
            messageListenerThread.interrupt();

        //Remove from server connection list
        clients.remove(this);
    }

    public boolean isConnected() {
        return !socket.isClosed();
    }

    public Socket getSocket() {
        return socket;
    }

    public void boundClient(Client client) {
        boundedClient = client;
    }

    public Client getBoundedClient() {
        return boundedClient;
    }

    private void lostConnectionHandling() {
        closeConnection();

        if (boundedClient != null)
            boundedClient.lostConnection();
        else
            defaultEvent.lostConnection(this);
    }
}