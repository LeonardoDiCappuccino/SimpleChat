package UI.Pages;

import UI.Presets.Button;
import UI.Style;

import javax.swing.*;
import java.awt.*;

public class MainPage extends JPanel {

    private final JPanel chatsBar;

    private static final MainPage instance = new MainPage();

    private MainPage() {
        //Margin is making the division lines
        setLayout(new BorderLayout(1,1));
        setBackground(Style.LIGHT_V);


        chatsBar = createChatBar();
        GridBagConstraints gbc = new GridBagConstraints();

        Button addFriendBtn = new Button("Freund hinzufügen");
        addFriendBtn.setPreferredSize(new  Dimension(300, 50));
        addFriendBtn.setBackground(Style.GREEN);
        addFriendBtn.setRound(1);
        addFriendBtn.setFont(Style.getOxygenFont(Font.PLAIN));

        add(createTopBar(), BorderLayout.PAGE_START);
        add(chatsBar, BorderLayout.LINE_START);
        add(createChatPanel(), BorderLayout.CENTER);
    }

    public static MainPage getInstance() {
        return instance;
    }

    private JPanel createTopBar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Style.DARK);
        panel.setPreferredSize(new Dimension(-1, 100));

        return panel;
    }

    private JPanel createChatBar() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Style.DARK);
        panel.setPreferredSize(new Dimension(550, -1));


        return panel;
    }

    private JPanel createChatPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(Style.DARK);

        return panel;
    }
}
