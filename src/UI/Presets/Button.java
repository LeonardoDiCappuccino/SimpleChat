package UI.Presets;

import UI.Style;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Button extends JButton {

    private float round = 0f;
    private boolean focus = false;

    public Button() {
        setup();
    }

    public Button(String text) {
        setText(text);
        setup();
    }

    private void setup() {
        //Prepare
        setContentAreaFilled(false);
        setBorder(null);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        //Set colors
        setForeground(Style.LIGHT);
        setBackground(Style.DARK_V);

        //Resie behavior
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                float fontSize = getHeight() / 2.25f;
                int paddingRL = (int) (fontSize * (round / 3 + .5f));
                setFont(getFont().deriveFont(fontSize));

                repaint();
            }
        });

        //Hover Behavior
        addMouseListener(new MouseAdapter() {
            private boolean hover = false;

            //Click behavior
            @Override
            public void mousePressed(MouseEvent e) {
                setBackground(Style.changeBrightness(Style.DARK_V, 15));
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (hover)
                    setBackground(Style.changeBrightness(Style.DARK_V, 7));
                else
                    setBackground(Style.DARK_V);
                repaint();
            }

            //Hover behavior
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                setBackground(Style.changeBrightness(Style.DARK_V, 7));
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                setBackground(Style.DARK_V);
                repaint();
            }
        });

        //Focus behavior
        addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                focus = true;
                repaint();
            }

            @Override
            public void focusLost(FocusEvent e) {
                focus = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Draw background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getWidth(), getHeight(),
                (int) (getHeight() * round), (int) (getHeight() * round));

        //Draw focus ring
        if (focus) {
            int thickness = getHeight() / 40;

            g2.setColor(getForeground());
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(thickness, thickness,
                    getWidth() - thickness * 2, getHeight() - thickness * 2,
                    (int) (getHeight() * round), (int) (getHeight() * round));
        }

        //Draw button
        super.paintComponent(graphics);
    }

    public float getRound() {
        return round;
    }

    public void setRound(float round) {
        this.round = round;
        repaint();
    }
}
