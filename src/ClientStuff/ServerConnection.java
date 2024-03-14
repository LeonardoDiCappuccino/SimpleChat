package ClientStuff;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;

@SuppressWarnings("unused")
public class ServerConnection {

    private Socket socket = null;
    private final String serverIP;
    private final int serverPort;

    private DataInputStream reader;
    private DataOutputStream writer;
    private Thread messageListenerThread;
    private final ServerEventListener serverEvents;

    public ServerConnection(String serverIP, int serverPort, ServerEventListener serverEvents) {
        this.serverIP = serverIP;
        this.serverPort = serverPort;
        this.serverEvents = serverEvents;

        tryToConnect(serverIP, serverPort, true);
    }

    private void tryToConnect(String serverIP, int serverPort, boolean init) {
        try {
            try {
                socket = new Socket(serverIP, serverPort);
            } catch (IOException ignore) {
                if (init) {
                    try {
                        serverEvents.serverNotAvailable(this);
                    } catch (InterruptedException ignore2) {
                    }
                }
                return;
            }

            reader = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
            writer = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        if (messageListenerThread != null)
            messageListenerThread.interrupt();


        messageListenerThread = new Thread(this::receivingMessages);
        messageListenerThread.start();
    }

    public void connect() {
        if (socket == null)
            tryToConnect(serverIP, serverPort, false);
    }

    public void reconnect() {
        if (!isConnected())
            tryToConnect(serverIP, serverPort, false);
    }

    private void receivingMessages() {
        while (isConnected()) {
            Object message = getNextMessage();

            try {
                if (message instanceof byte[])
                    serverEvents.receivedMessage(this, (byte[]) message);
                else
                    serverEvents.receivedMessage(this, message);
            } catch (InterruptedException ignored) {
            }
        }
    }

    private Object getNextMessage() {
        try {
            int length = reader.readInt();
            if (length < 1)
                return null;

            byte[] bytes = new byte[length];
            boolean isObject = reader.readBoolean();

            reader.readFully(bytes);

            //Call messageEvent
            if (isObject) {
                try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                     ObjectInputStream ois = new ObjectInputStream(bis)) {
                    return ois.readObject();
                } catch (IOException | ClassNotFoundException e) {
                    System.err.println(e.getMessage());
                    return null;
                }
            } else return bytes;

        } catch (EOFException e) {
            //On lost connection
            lostConnectionHandling();

        } catch (IOException e) {
            if (!socket.isClosed())
                throw new RuntimeException(e);
        }

        return null;
    }

    public void send(byte[] bytes) {
        try {
            writer.writeInt(bytes.length);
            //Indicates a send raw byte[] (isObject)
            writer.writeBoolean(false);

            writer.write(bytes);
        } catch (IOException e) {
            //Connection failed
            closeConnection();
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

        } catch (NotSerializableException e) {
            throw new RuntimeException(e);

        } catch (IOException e) {
            //Connection failed
            closeConnection();
        }
    }

    public Object catchResponse() {
        return getNextMessage();
    }

    public Object catchResponse(int timeout) {
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

        closeSocketAndStreams();

        //Close messageListener
        messageListenerThread.interrupt();
    }

    public boolean isConnected() {
        if (socket == null)
            return false;
        return !socket.isClosed();
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

    private void lostConnectionHandling() {
        closeSocketAndStreams();

        try {
            serverEvents.lostConnection(this);
        } catch (InterruptedException ignore) {
        }

        //If no reconnected happens close current Thread
        if (!isConnected())
            messageListenerThread.interrupt();
    }
}