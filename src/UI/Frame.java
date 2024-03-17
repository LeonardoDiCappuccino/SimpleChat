package UI;

import UI.Modules.LogIn;
import UI.Modules.MainPage;
import UI.Modules.SignIn;

import javax.swing.*;
import java.awt.*;

public class Frame {

    private final JFrame frame;
    private final CardLayout view;

    private static float scaleFactor;

    private static final Frame instance = new Frame();

    private Frame() {
        Style.loadResources();

        frame = new JFrame("Simple Chat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Window sizing
        Dimension windowSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setSize(windowSize.width / 2, windowSize.height / 2);

        int width = windowSize.width / 2;
        frame.setMinimumSize(new Dimension(width, (int) (width * (9.f / 16.f))));


        frame.setLocationRelativeTo(null);

        frame.getContentPane().setBackground(Style.DARK);

        view = new CardLayout();
        frame.getContentPane().setLayout(view);

        frame.add(test(), "Test");
        frame.add(new LogIn(), "LogIn");
        frame.add(new SignIn(), "SignIn");
        frame.add(MainPage.getInstance(), "Main");

        frame.setVisible(true);
        view.show(frame.getContentPane(), "Main");
    }

    public JPanel test() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();



        return panel;
    }

    public static Frame getInstance() {
        return instance;
    }

    public static int scale(int val) {
        return (int) (val * scaleFactor);
    }

    public void show(String page) {
        view.show(frame.getContentPane(), page);
    }
}
