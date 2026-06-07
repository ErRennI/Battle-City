import javax.swing.*;
import java.awt.*;

public class StartScreen extends JPanel {

    public StartScreen() {
        setBackground(Color.BLACK);
        setFocusable(true);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.RED);
        g.setFont(new Font("Monospaced", Font.BOLD, 48));

        String title = "BATTLE CITY";
        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(title)) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g.drawString(title, x, y);
    }
}