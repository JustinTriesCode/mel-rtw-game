package ui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ResourceLoader {

    // Load an image from res/images
    // public static BufferedImage loadImage(String fileName) {
    // try {
    // return ImageIO.read(new File("res/images/" + fileName));
    // } catch (IOException e) {
    // System.out.println("Error: Could not find image " + fileName);
    // return null;
    // }
    // }

    public static BufferedImage loadImage(String fileName) {
        try {
            // This looks inside the 'src' or 'bin' folder context
            // Try adding a "/" at the start if it still fails: "/images/" + fileName
            return ImageIO.read(ResourceLoader.class.getResourceAsStream("/res/images/" + fileName));
        } catch (Exception e) {
            System.out.println("Error: Could not find image " + fileName);
            return null;
        }
    }

    // Load a custom font from res/fonts
    public static Font loadFont(String fileName, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("res/fonts/" + fileName));
            return font.deriveFont(size);
        } catch (Exception e) {
            System.out.println("Error: Could not load font. Using Arial instead.");
            return new Font("Arial", Font.BOLD, (int) size);
        }
    }
}
