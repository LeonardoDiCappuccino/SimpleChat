package UI.Modules;

import UI.Style;

import javax.swing.*;
import java.awt.*;

public class ProfilePictureSelector extends JButton {

    private Image image;

    public ProfilePictureSelector() {
//        setContentAreaFilled(false);
        setBorder(null);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setIcon(new ImageIcon(Style.ADD_PHOTO));
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
