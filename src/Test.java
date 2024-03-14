import Communication.Container;
import Communication.SerializableImage;
import SQLiteStuff.ChatDB;
import org.sqlite.SQLiteErrorCode;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.*;
import java.lang.reflect.Array;

public class Test {

    public static void main(String[] args) throws IOException {
        ChatDB db = ChatDB.getInstance();
        Container.Profile profile = db.createUser("Peter", "Passwort1234");

        if (profile != null) {
            System.out.println(profile.userID() + ": " + profile.username());
            showImage(profile.profilePicture().getImage());
        } else System.out.println("Not found");
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

    public static void showImage(Image img) {
        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        JPanel panel = new JPanel() {
            @Override
            public void paintComponent(Graphics g) {
                super.paintComponent(g);

                int frameW = frame.getWidth();
                int frameH = frame.getContentPane().getHeight();

                g.drawImage(img, 0, 0, frameW, frameH, null);
            }
        };
        frame.add(panel);

        Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(windowSize.width / 2, windowSize.height / 2);
        frame.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                super.componentResized(e);
                panel.repaint();
            }
        });

        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
