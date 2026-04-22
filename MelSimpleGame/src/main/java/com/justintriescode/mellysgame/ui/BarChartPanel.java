package com.justintriescode.mellysgame.ui;

import com.justintriescode.mellysgame.game.PlayerProfile;
import com.justintriescode.mellysgame.model.Stats;

import javax.swing.*;
import java.awt.*;

/**
 * A component that draws a side-by-side bar chart comparing
 * average scores across different symptom severities and difficulties.
 */
public class BarChartPanel extends JPanel {
    private final String[] severities = { "None", "Mild", "Moderate", "Severe" };

    private final int[] easyScores = new int[4];
    private final int[] hardScores = new int[4];
    private final String easyName;
    private final String hardName;
    private int maxScore = 1;

    private final Color colorEasy = UIStyleUtils.DARK_BLUE;
    private final Color colorHard = UIStyleUtils.ORANGE;

    /**
     * Creates a new BarChartPanel for the given player profile and game type.
     * 
     * @param profile  the player's profile containing session history and
     *                 statistics
     * @param gameName the name of the game for which to display statistics
     */
    public BarChartPanel(PlayerProfile profile, String gameName) {
        this.easyName = gameName.equals("Equationista") ? "Numberista" : "Letter Soup";
        this.hardName = gameName.equals("Equationista") ? "Equationista" : "Alphabet Soup";

        // Pre-calculate all data once to avoid doing it in paintComponent
        for (int i = 0; i < severities.length; i++) {
            String sev = severities[i];
            easyScores[i] = Stats.getAverageScoreBySeverityAndDifficulty(profile, sev, gameName, false);
            hardScores[i] = Stats.getAverageScoreBySeverityAndDifficulty(profile, sev, gameName, true);

            maxScore = Math.max(maxScore, easyScores[i]);
            maxScore = Math.max(maxScore, hardScores[i]);
        }
        maxScore = (int) (maxScore * 1.15); // adds 15% padding

        setPreferredSize(new Dimension(550, 350));
        setOpaque(false);
    }

    /**
     * Paints the bar chart by overriding the paintComponent method.
     * 
     * @param g the Graphics object used for drawing the component
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int width = getWidth();
        int height = getHeight();
        int padding = 40;
        int bottomPadding = 40;

        g2.setColor(UIStyleUtils.TEXT_DARK);
        g2.setStroke(new BasicStroke(2));
        g2.drawLine(padding, height - bottomPadding, padding, padding); // Y axis
        g2.drawLine(padding, height - bottomPadding, width - padding, height - bottomPadding); // X axis

        int chartWidth = width - (padding * 2);
        int groupWidth = chartWidth / severities.length;
        int barWidth = groupWidth / 3;

        for (int i = 0; i < severities.length; i++) {
            String sev = severities[i];
            int easyScore = easyScores[i];
            int hardScore = hardScores[i];

            int xStart = padding + (i * groupWidth) + (groupWidth / 6);
            int chartHeight = height - padding - bottomPadding;

            int easyBarHeight = (int) (((double) easyScore / maxScore) * chartHeight);
            g2.setColor(colorEasy);
            g2.fillRect(xStart, height - bottomPadding - easyBarHeight, barWidth, easyBarHeight);

            int hardBarHeight = (int) (((double) hardScore / maxScore) * chartHeight);
            g2.setColor(colorHard);
            g2.fillRect(xStart + barWidth, height - bottomPadding - hardBarHeight, barWidth, hardBarHeight);

            // Draw score labels above bars
            g2.setColor(UIStyleUtils.TEXT_DARK);
            g2.setFont(new Font("Serif", Font.BOLD, 14));
            FontMetrics numMetrics = g2.getFontMetrics();

            // Easy mode score label
            String easyText = String.valueOf(easyScore);
            int easyTextX = xStart + (barWidth / 2) - (numMetrics.stringWidth(easyText) / 2);
            g2.drawString(easyText, easyTextX, height - bottomPadding - easyBarHeight - 5);

            // Hard mode score label
            String hardText = String.valueOf(hardScore);
            int hardTextX = xStart + barWidth + (barWidth / 2) - (numMetrics.stringWidth(hardText) / 2);
            g2.drawString(hardText, hardTextX, height - bottomPadding - hardBarHeight - 5);

            g2.setColor(UIStyleUtils.TEXT_DARK);
            g2.setFont(new Font("Serif", Font.PLAIN, 16));
            FontMetrics metrics = g2.getFontMetrics();
            int labelWidth = metrics.stringWidth(sev);
            g2.drawString(sev, xStart + barWidth - (labelWidth / 2), height - 15);
        }

        g2.setFont(new Font("Serif", Font.BOLD, 14));
        g2.setColor(colorEasy);
        g2.fillRect(width - 150, 20, 15, 15);
        g2.setColor(colorHard);
        g2.fillRect(width - 150, 45, 15, 15);
        g2.setColor(UIStyleUtils.TEXT_DARK);

        g2.drawString(easyName, width - 130, 32);
        g2.drawString(hardName, width - 130, 57);
    }
}
