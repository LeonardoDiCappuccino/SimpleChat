import ServerStuff.Client;
import ServerStuff.ClientEventListener;
import ServerStuff.ClientHandler;
import ServerStuff.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.ArrayList;

public class Test {

    public static void main(String[] args) {
    }

    public static void printBytes(Serializable object) {
        StringBuilder result = new StringBuilder();
        byte[] bytes = getBytes(object);
        for (byte aByte : bytes) {
            result.append(String.format("%02x", aByte).toUpperCase()).append(' ');
            // upper case
            // result.append(String.format("%02X", aByte));
        }
        System.out.print("Length:" + bytes.length + " -> ");
        System.out.println(result);
    }

    public static void printBytes(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(String.format("%02x", aByte).toUpperCase()).append(' ');
            // upper case
            // result.append(String.format("%02X", aByte));
        }
        System.out.print("Length:" + bytes.length + " -> ");
        System.out.println(result);
    }

    public static byte[] getBytes(Serializable object) {
        byte[] out;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
             ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(object);
            return bos.toByteArray();
        } catch (IOException e) {
            return new byte[0];
        }
    }
}
