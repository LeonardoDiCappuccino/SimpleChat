package ServerStuff;

import java.util.List;

public class User extends Client {

    private static int count = 0;

    private final int id;

    public User(ClientHandler handler) {
        super(handler);
        id = count++;
        System.out.println("Client" + id + " connected");
    }

    public int getId() {
        return id;
    }

    @Override
    public void messageEvent(byte[] message) {
        print("Client" + id + " says: " + new String(message));
    }

    @Override
    public void messageEvent(Object object) {
        List<Integer> list = (List<Integer>) object;
        for (int i : list)
            System.out.println(i);
    }

    @Override
    public void disconnectionEvent() {
        System.out.println("Client" + id + " disconnected");
    }

    public static synchronized void print(String s) {
        System.out.println(s);
    }
}
