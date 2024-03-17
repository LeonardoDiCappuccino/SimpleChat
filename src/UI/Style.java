package UI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

@SuppressWarnings("unused")
public class Style {

    //Colors
    public static final Color DARK = Color.decode("#2c3e50"), DARK_V = Color.decode("#34495e");
    public static final Color GRAY = Color.decode("#7f8c8d"), GRAY_V = Color.decode("#95a5a6");
    public static final Color LIGHT = Color.decode("#ecf0f1"), LIGHT_V = Color.decode("#bdc3c7");
    public static final Color RED = Color.decode("#c0392b"), RED_V = Color.decode("#e74c3c");
    public static final Color GREEN = Color.decode("#27ae60"), GREEN_V = Color.decode("#2ecc71");
    public static final Color DARK_GREEN = Color.decode("#16a085"), DARK_GREEN_V = Color.decode("#1abc9c");

    //Fonts
    public static Color changeBrightness(Color color, int degree) {
        int red = color.getRed() + degree;
        int green = color.getGreen() + degree;
        int blue = color.getBlue() + degree;

        return new Color(red, green, blue);
    }

    private static Font[] fonts = null;

    public static Font getOxygenFont(int pStyle, int pSize) {
        if (fonts == null)
            loadFonts();
        return fonts[pStyle].deriveFont((float) pSize);
    }

    public static Font getOxygenFont(int pStyle) {
        if (fonts == null)
            loadFonts();
        return fonts[pStyle];
    }

    private static void loadFonts() {
        String[] fontStyles = {"Regular", "Bold", "Light"};
        fonts = new Font[fontStyles.length];
        try {
            for (int i = 0; i < fonts.length; i++) {

                fonts[i] = Font.createFont(Font.TRUETYPE_FONT, new File("rsc/Oxygen-"
                        + fontStyles[i] + ".ttf"));
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();

                ge.registerFont(fonts[i]);
            }
        } catch (IOException | FontFormatException e) {
            throw new RuntimeException(e);
        }
    }

    //Image resources

    public static Image ADD_PHOTO;

    private static void loadImages() {
        try {
            ADD_PHOTO = ImageIO.read(new File("/home/lasse/Downloads/AddPhoto.png"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void loadResources() {
        loadFonts();
        loadImages();
    }
}
