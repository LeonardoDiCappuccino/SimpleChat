package UI;

import UI.Pages.LogIn;
import UI.Pages.MainPage;
import UI.Pages.SignIn;
import UI.Presets.Button;

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
        view.show(frame.getContentPane(), "LogIn");
    }

    public JPanel test() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();

        Button addFriendBtn = new Button("Freund hinzufügen");
        addFriendBtn.setPreferredSize(new Dimension(300, 50));
        addFriendBtn.setBackground(Style.GREEN);
        addFriendBtn.setRound(1);
        addFriendBtn.setFont(Style.getOxygenFont(Font.PLAIN));

        // Create a JPanel to hold the buttons
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridBagLayout()); // Layout with one column, variable rows

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.weighty = .1f;
        gbc.weightx = 1;
        gbc.gridx = 0;

        // Add buttons to the panel
        for (int i = 1; i <= 10; i++) {
            JButton button = new JButton("Button " + i);
            buttonPanel.add(button, gbc);
            button.setPreferredSize(new Dimension(-1, 50));
        }


        gbc.anchor = GridBagConstraints.PAGE_START;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(20, 20, 20, 20);
        panel.add(addFriendBtn, gbc);

        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.BOTH;
        panel.add(buttonPanel, gbc);


        return panel;
    }

    private static Button b(String text) {
        Button button = new Button(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setRound(1);
        button.setFont(Style.getOxygenFont(Font.PLAIN));

        return button;
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
