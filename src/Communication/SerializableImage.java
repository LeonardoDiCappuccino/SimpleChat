package Communication;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.*;
import java.io.*;

public class SerializableImage implements Serializable {

    private transient BufferedImage image;

    public SerializableImage(BufferedImage image) {
        this.image = image;
    }

    public Image getImage() {
        return image;
    }

    @Serial
    private void writeObject(ObjectOutputStream out) throws IOException {
        out.defaultWriteObject();

        ImageIO.write(image, "png", out);
    }

    @Serial
    private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
        in.defaultReadObject();

        image = ImageIO.read(in);
    }
}
