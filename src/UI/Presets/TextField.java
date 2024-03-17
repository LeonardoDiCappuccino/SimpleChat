package UI.Presets;

import UI.Style;

import java.awt.*;
import java.awt.event.*;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class TextField extends JTextField {

    private float round = 0f;
    private String placeholder;

    private boolean focus = false;

    public TextField() {
        setup();
    }

    public TextField(String placeholder) {
        this.placeholder = placeholder;
        setup();
    }

    private void setup() {
        //Prepare
        setOpaque(false);

        //Set colors
        setForeground(Style.LIGHT);
        setBackground(Style.DARK_V);

        setSelectionColor(Style.LIGHT_V);
        setSelectedTextColor(Style.DARK);
        setDisabledTextColor(Style.GRAY);
        setCaretColor(Style.LIGHT);

        //Resie behavior
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                float fontSize = getHeight() / 2.25f;
                int paddingRL = (int) (fontSize * (round / 3 + .5f));
                setFont(getFont().deriveFont(fontSize));
                setBorder(new EmptyBorder(0, paddingRL, 0, paddingRL));

                repaint();
            }
        });

        //Hover behavior
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setBackground(Style.changeBrightness(Style.DARK_V, 7));
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
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

        //Draw placeholder
        if (placeholder != null)
            if (!placeholder.isEmpty() && getText().isEmpty()) {
                g2.setFont(getFont());
                g2.setColor(getDisabledTextColor());

                //Prevent placeholder from overflow
                int paddingRL = (int) (getHeight() / 2.25f * (round / 3 + .5f));
                g2.setClip(new Rectangle(paddingRL, 0,
                        getWidth() - paddingRL * 2, getHeight()));

                //Magic number corrects vertical alignment to written text
                g2.drawString(placeholder, getInsets().left,
                        getFontMetrics(getFont()).getMaxAscent() * 1.479f);
            }

        //Draw text field
        super.paintComponent(graphics);
    }

    public float getRound() {
        return round;
    }

    public void setRound(float round) {
        this.round = round;
        repaint();
    }

    public String getPlaceholder() {
        return placeholder;
    }

    public void setPlaceholder(String placeholder) {
        this.placeholder = placeholder;
        repaint();
    }
}