package UI.Modules;

import UI.Frame;
import UI.Presets.*;
import UI.Presets.Button;
import UI.Presets.TextField;
import UI.Style;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SignIn extends JPanel {

    private final TextField serverAddress;
    private final TextField username;
    private final PasswordField password;
    private final ProfilePictureSelector pps;
    private final CheckBox stayLoggedIn;

    public SignIn() {
        setLayout(new GridBagLayout());
        setOpaque(false);

        GridBagConstraints gbc = new GridBagConstraints();

        JLabel title = new JLabel("Konto erstellen");
        title.setFont(Style.getOxygenFont(Font.PLAIN, 80));
        title.setForeground(Style.LIGHT);

        serverAddress = new UI.Presets.TextField("Server Adresse");
        serverAddress.setPreferredSize(new Dimension(1000, 75));
        serverAddress.setFont(Style.getOxygenFont(Font.PLAIN));
        serverAddress.setRound(1f);

        username = new UI.Presets.TextField("Benutzername");
        username.setPreferredSize(new Dimension(800, 75));
        username.setFont(Style.getOxygenFont(Font.PLAIN));
        username.setRound(1f);

        password = new PasswordField("Passwort");
        password.setPreferredSize(new Dimension(800, 75));
        password.setFont(Style.getOxygenFont(Font.PLAIN));
        password.setRound(1f);

        pps = new ProfilePictureSelector();
        pps.setPreferredSize(new Dimension(170, 170));

        stayLoggedIn = new CheckBox();
        stayLoggedIn.setPreferredSize(new Dimension(300, 40));
        stayLoggedIn.setText("Angemeldet bleiben?");
        stayLoggedIn.setFont(Style.getOxygenFont(Font.PLAIN, 30));

        TextButton signInBtn = new TextButton("Konto bereits vorhanden?");
        signInBtn.setPreferredSize(new Dimension(300, 40));
        signInBtn.setFont(Style.getOxygenFont(Font.PLAIN));
        signInBtn.setHorizontalAlignment(SwingConstants.RIGHT);
        signInBtn.addActionListener(this::goToLogInPage);

        Button button = new Button("Registrieren");
        button.setPreferredSize(new Dimension(250, 75));
        button.setFont(Style.getOxygenFont(Font.PLAIN));
        button.setRound(1f);
        signInBtn.addActionListener(this::signIn);


        gbc.anchor = GridBagConstraints.SOUTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.insets.bottom = 100;
        add(title, gbc);

        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridy = 1;
        gbc.insets.bottom = 60;
        add(serverAddress, gbc);

        gbc.fill = GridBagConstraints.REMAINDER;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets.bottom = 20;
        gbc.insets.right = 20;
        add(username, gbc);

        gbc.gridy = 3;
        add(password, gbc);

        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridheight = 2;
        gbc.gridwidth = 1;
        gbc.insets.right = 0;
        add(pps, gbc);

        JPanel subLine = new JPanel(new GridLayout(1, 2));
        subLine.setOpaque(false);
        subLine.add(stayLoggedIn);
        subLine.add(signInBtn);

        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.gridheight = 1;
        gbc.insets.right = 20;
        add(subLine, gbc);

        gbc.anchor = GridBagConstraints.NORTH;
        gbc.fill = GridBagConstraints.REMAINDER;
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        gbc.insets.top = 40;
        gbc.insets.right = 0;
        add(button, gbc);
    }

    private void goToLogInPage(ActionEvent actionEvent) {
        Frame.getInstance().show("LogIn");
    }

    private void signIn(ActionEvent actionEvent) {
    }

    private void reset() {
        serverAddress.setText("");
        username.setText("");
        password.setText("");
        pps.setImage(null);
        stayLoggedIn.setSelected(false);
    }
}
