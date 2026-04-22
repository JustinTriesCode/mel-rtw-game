package com.justintriescode.mellysgame.ui;

import com.justintriescode.mellysgame.events.Event;
import com.justintriescode.mellysgame.events.EventLog;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * A utility class for loading resources such as images and fonts.
 */
public class ResourceLoader {

    /**
     * Load an image from res/images
     * 
     * @param fileName
     * @return the loaded BufferedImage, or null if loading fails
     */
    public static BufferedImage loadImage(String fileName) {
        try {
            return ImageIO.read(ResourceLoader.class.getResourceAsStream("/images/" + fileName));
        } catch (Exception e) {
            EventLog.getInstance().addEvent(new Event("Failed to load image: " + fileName, e));
            return null;
        }
    }

    /**
     * Load an icon from res/icons
     * 
     * @param fileName
     * @return the loaded ImageIcon, or an empty icon if loading fails
     */
    public static ImageIcon loadIcon(String fileName) {
        try {
            java.net.URL resource = ResourceLoader.class.getResource("/icons/" + fileName);
            if (resource == null) {
                EventLog.getInstance().addEvent(new Event("CRITICAL: Icon not found at /icons/" + fileName));
                return new ImageIcon();
            }
            BufferedImage img = ImageIO.read(resource);
            if (img == null) {
                EventLog.getInstance().addEvent(
                        new Event("CRITICAL: Java cannot read this image format (is it an SVG?): " + fileName));
                return new ImageIcon();
            }
            return new ImageIcon(img);
        } catch (Exception e) {
            EventLog.getInstance().addEvent(new Event("Failed to load icon: " + fileName, e));
            return new ImageIcon(); // Return an empty icon to prevent UI crashes
        }
    }

    /**
     * Load an icon from res/icons and scale it to a specific width and height.
     * 
     * @param fileName
     * @param width
     * @param height
     * @return the scaled ImageIcon
     */
    public static ImageIcon loadIcon(String fileName, int width, int height) {
        ImageIcon originalIcon = loadIcon(fileName);
        if (originalIcon.getIconWidth() == -1)
            return originalIcon; // Return empty if loading failed
        Image scaled = originalIcon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    /**
     * Load a custom font from res/fonts
     * 
     * @param fileName
     * @param size
     * @return the loaded Font, or a default font if loading fails
     */
    public static Font loadFont(String fileName, float size) {
        try {
            return Font.createFont(Font.TRUETYPE_FONT,
                    ResourceLoader.class.getResourceAsStream("/fonts/" + fileName))
                    .deriveFont(size);
        } catch (Exception e) {
            EventLog.getInstance().addEvent(new Event("Failed to load font: " + fileName, e));
            return new Font("Serif", Font.BOLD, (int) size);
        }
    }
}
