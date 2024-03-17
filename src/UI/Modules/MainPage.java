package UI.Modules;

import UI.Style;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainPage extends JPanel {

    private final JPanel chatsBar;

    private static final MainPage instance = new MainPage();

    private MainPage() {
        //Margin is making the division lines
        setLayout(new BorderLayout(1,1));
        setBackground(Style.LIGHT_V);

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = e.getComponent().getWidth() / 4;
                chatsBar.setPreferredSize(new Dimension(width, -1));
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();

        JPanel topBar = new JPanel();
        topBar.setBackground(Style.DARK);
        topBar.setPreferredSize(new Dimension(-1, 100));

        chatsBar = new JPanel();
        chatsBar.setBackground(Style.DARK);

        JPanel chatPanel = new JPanel();
        chatPanel.setBackground(Style.DARK);

        add(topBar, BorderLayout.PAGE_START);
        add(chatsBar, BorderLayout.LINE_START);
        add(chatPanel, BorderLayout.CENTER);
    }

    public static MainPage getInstance() {
        return instance;
    }
}
