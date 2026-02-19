package main.java.com.justintriescode.mellysgame.ui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class ResourceLoader {

    public static BufferedImage loadImage(String fileName) {
        try {
            return ImageIO.read(ResourceLoader.class.getResourceAsStream("/main/resources/images/" + fileName));
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
