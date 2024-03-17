package UI.Presets;

import UI.Style;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class CheckBox extends JButton {

    private boolean selected = false;
    private boolean focus = false;

    public CheckBox() {
        //Prepare
        setContentAreaFilled(false);
        setFocusPainted(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
        setHorizontalAlignment(SwingConstants.LEFT);

        //Set colors
        setForeground(Style.LIGHT);
        setBackground(Style.DARK_V);

        //Resie behavior
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                float fontSize = getHeight() * 0.7f;
                setFont(getFont().deriveFont(fontSize));
                setBorder(BorderFactory.createEmptyBorder(0, (int) (getHeight() * 1.5f), 0, 0));

                repaint();
            }
        });

        //Hover Behavior
        addMouseListener(new MouseAdapter() {

            //Click behavior
            @Override
            public void mouseClicked(MouseEvent e) {
                selected = !selected;
                repaint();
            }

            //Hover behavior
            @Override
            public void mouseEntered(MouseEvent e) {
                setForeground(Style.LIGHT_V);
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setForeground(Style.LIGHT);

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

        //Space pressed behavior
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                    selected = !selected;
                    repaint();
                }
            }
        });
    }

    protected void paintComponent(Graphics graphics) {
        Graphics2D g2 = (Graphics2D) graphics.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //Draw background
        g2.setColor(getBackground());
        g2.fillRoundRect(0, 0, getHeight(), getHeight(), getHeight() / 2, getHeight() / 2);

        //  Draw Check icon
        if (selected) {
            int ly = (getHeight() - 40) / 2;
            int[] px = {4, 8, 14, 12, 8, 6};
            int[] py = {ly + 8, ly + 14, ly + 5, ly + 3, ly + 10, ly + 6};

            for (int i = 0; i < px.length; i++)
                px[i] = (int) (px[i] * 2.4f);
            for (int i = 0; i < py.length; i++)
                py[i] = (int) (py[i] * 2.5f);

            g2.setColor(getForeground());
            g2.fillPolygon(px, py, px.length);
        }

        //Draw focus ring
        if (focus) {
            int thickness = getHeight() / 30;

            g2.setColor(getForeground());
            g2.setStroke(new BasicStroke(thickness));
            g2.drawRoundRect(thickness, thickness,
                    getHeight() - thickness * 2, getHeight() - thickness * 2,
                    (int) (getHeight() * 0.5f), (int) (getHeight() * 0.5f));
        }
        super.paintComponent(graphics);
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
        repaint();
    }
}