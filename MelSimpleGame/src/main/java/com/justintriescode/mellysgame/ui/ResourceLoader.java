package com.justintriescode.mellysgame.ui;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ResourceLoader {

    public static BufferedImage loadImage(String fileName) {
        try {
            return ImageIO.read(ResourceLoader.class.getResourceAsStream("/images/" + fileName));
        } catch (Exception e) {
            System.out.println("Error: Could not find image " + fileName);
            return null;
        }
    }

    // Load a custom font from res/fonts
    public static Font loadFont(String fileName, float size) {
    try {
        return Font.createFont(Font.TRUETYPE_FONT, 
            ResourceLoader.class.getResourceAsStream("/fonts/" + fileName))
            .deriveFont(size);
    } catch (Exception e) {
        System.out.println("Error: Could not load font " + fileName + ". Using Serif.");
        return new Font("Serif", Font.BOLD, (int) size);
    }
}
}
