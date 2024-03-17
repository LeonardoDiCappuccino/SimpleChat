package UI;

import java.awt.*;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ComponentColor extends MouseAdapter implements FocusListener {

    private final Color foreground, background, onClick, onHover, onFocus;

    public ComponentColor(Color foreground, Color background, Color onClick, Color onHover, Color onFocus) {
        this.foreground = foreground;
        this.background = background;
        this.onClick = onClick;
        this.onHover = onHover;
        this.onFocus = onFocus;
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void focusGained(FocusEvent e) {

    }

    @Override
    public void focusLost(FocusEvent e) {

    }

    public Color getForeground() {
        return foreground;
    }

    public Color getBackground() {
        return background;
    }

    public Color getOnClick() {
        return onClick;
    }

    public Color getOnHover() {
        return onHover;
    }

    public Color getOnFocus() {
        return onFocus;
    }
}
