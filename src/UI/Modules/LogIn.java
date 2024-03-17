package UI.Modules;

import UI.Frame;
import UI.Presets.*;
import UI.Presets.Button;
import UI.Presets.TextField;
import UI.Style;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class LogIn extends JPanel {

    private final TextField serverAddress;
    private final TextField username;
    private final PasswordField password;
    private final CheckBox stayLoggedIn;

    public LogIn() {
        setLayout(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel title = new JLabel("Anmelden");
        title.setFont(Style.getOxygenFont(Font.PLAIN, 80));
        title.setForeground(Style.LIGHT);

        serverAddress = new TextField("Server Adresse");
        serverAddress.setPreferredSize(new  Dimension(1000, 75));
        serverAddress.setFont(Style.getOxygenFont(Font.PLAIN));
        serverAddress.setRound(1f);

        username = new TextField("Benutzername");
        username.setPreferredSize(new  Dimension(1000, 75));
        username.setFont(Style.getOxygenFont(Font.PLAIN));
        username.setRound(1f);

        password = new PasswordField("Passwort");
        password.setPreferredSize(new  Dimension(1000, 75));
        password.setFont(Style.getOxygenFont(Font.PLAIN));
        password.setRound(1f);

        stayLoggedIn = new CheckBox();
        stayLoggedIn.setPreferredSize(new Dimension(300, 40));
        stayLoggedIn.setText("Angemeldet bleiben?");
        stayLoggedIn.setFont(Style.getOxygenFont(Font.PLAIN));

        TextButton logInBtn = new TextButton("Noch kein Konto vorhanden?");
        logInBtn.setPreferredSize(new Dimension(300, 40));
        logInBtn.setFont(Style.getOxygenFont(Font.PLAIN));
        logInBtn.setHorizontalAlignment(SwingConstants.RIGHT);
        logInBtn.addActionListener(this::goToSignInPage);

        Button button = new Button("Anmelden");
        button.setPreferredSize(new  Dimension(230, 75));
        button.setFont(Style.getOxygenFont(Font.PLAIN));
        button.setRound(1f);
        button.addActionListener(this::logIn);

        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.insets.bottom = 100;
        add(title, gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets.bottom = 60;
        add(serverAddress, gbc);

        gbc.insets.bottom = 20;
        add(username, gbc);
        add(password, gbc);


        JPanel subLine = new JPanel(new GridLayout(1, 2));
        subLine.setOpaque(false);
        subLine.add(stayLoggedIn);
        subLine.add(logInBtn);

        add(subLine, gbc);

        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.REMAINDER;
        gbc.insets.top = 40;
        add(button, gbc);
    }

    private void goToSignInPage(ActionEvent actionEvent) {
        Frame.getInstance().show("SignIn");
    }

    private void logIn(ActionEvent actionEvent) {
    }

    private void reset() {
        serverAddress.setText("");
        username.setText("");
        password.setText("");
        stayLoggedIn.setSelected(false);
    }
}
