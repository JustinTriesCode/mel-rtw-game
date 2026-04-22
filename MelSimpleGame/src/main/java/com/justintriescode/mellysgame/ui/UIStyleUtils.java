package com.justintriescode.mellysgame.ui;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;

import java.awt.*;

/**
 * Utility class for styling UI components
 */
public class UIStyleUtils {
    // colours
    public static final Color BG_COLOR = new Color(210, 210, 200);
    public static final Color BTN_COLOR = new Color(230, 235, 230);
    public static final Color TEXT_COLOR = new Color(100, 100, 85);
    public static final Color POPUP_BG = new Color(245, 245, 240);
    public static final Color BORDER_GRAY = new Color(180, 185, 180);
    public static final Color OFF_WHITE = new Color(200, 200, 190);
    public static final Color TEXT_DARK = new Color(90, 90, 90);
    public static final Color CYAN = new Color(0, 255, 255);
    public static final Color MAGENTA = new Color(220, 130, 220);
    public static final Color YELLOW = new Color(240, 230, 140);
    public static final Color ORANGE = new Color(255, 165, 0);
    public static final Color BLUE = new Color(100, 149, 237);
    public static final Color DARK_BLUE = new Color(100, 149, 237);

    // sizes
    public static final int MENU_FONT_SIZE = 42;

    public static final Icon BAR_ICON = ResourceLoader.loadIcon("chart_icon.png", 20, 20);

    public static void formatButton(JButton button, int fontSize) {
        button.setBackground(BTN_COLOR);
        button.setForeground(TEXT_COLOR);
        button.setFont(new Font("Serif", Font.BOLD, fontSize));
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(BORDER_GRAY, 2));
    }

    public static void stylePopupPanel(JPanel panel) {
        panel.setBackground(POPUP_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_GRAY, 1),
                BorderFactory.createEmptyBorder(25, 25, 25, 25)));
    }
}
