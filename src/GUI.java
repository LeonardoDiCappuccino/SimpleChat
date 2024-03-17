import UI.Style;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GUI {

    private JPanel mainPanel;
    private JPanel logIn;
    private JTextField serverAddress;
    private JRadioButton radioButton1;
    private CardLayout view;

    public GUI() {
        view = new CardLayout();
        mainPanel.setLayout(view);

        mainPanel.add(logIn, "logIn");

        view.show(mainPanel, "logIn");

        serverAddress.setPreferredSize(new Dimension(200, 50));

        serverAddress.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                serverAddress.setPreferredSize(new Dimension(1000, 50));
                serverAddress.repaint();
            }
        });
    }

    public void foo() {
        serverAddress.setPreferredSize(new Dimension(1000, 100));
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Simple Chat");
        frame.setContentPane(new GUI().mainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);


    }
}
