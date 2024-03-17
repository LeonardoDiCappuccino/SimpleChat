package UI.Presets;

import UI.Style;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.util.Map;

public class TextButton extends JButton {

    public TextButton(String text) {
        setText(text);
        //Prepare
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorder(null);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        //Set colors
        setForeground(Style.LIGHT);
        setBackground(null);

        //Resie behavior
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                float fontSize = getHeight() * 0.7f;
                setFont(getFont().deriveFont(fontSize));
            }
        });

        //Hover Behavior
        addMouseListener(new MouseAdapter() {
            private boolean hover = false;

            //Click behavior
            @Override
            public void mousePressed(MouseEvent e) {
                setForeground(Style.changeBrightness(Style.LIGHT_V, -30));
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (hover)
                    setForeground(Style.LIGHT_V);
                else
                    setForeground(Style.LIGHT);
            }

            //Hover behavior
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                setForeground(Style.LIGHT_V);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                setForeground(Style.LIGHT);
            }
        });
    }
}
