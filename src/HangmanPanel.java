import javax.swing.*;
import java.awt.*;

class HangmanPanel extends JPanel {
    private int mistakes;

    public HangmanPanel() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(new Color(230, 240, 250));
    }

    public void setMistakes(int mistakes) {
        this.mistakes = mistakes;
        repaint();
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g.create();
        try {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(3f));

            g2.drawLine(50, 350, 200, 350);
            g2.drawLine(125, 350, 125, 80);
            g2.drawLine(125, 80, 250, 80);
            g2.drawLine(250, 80, 250, 120);
            g2.drawLine(85, 347, 165, 347);
            g2.drawLine(125, 120, 165, 80);

            if (mistakes >= 1) {
                g2.drawOval(225, 120, 50, 50);
            }
            if (mistakes >= 2) {
                g2.drawLine(250, 170, 250, 250);
            }
            if (mistakes >= 3) {
                g2.drawLine(250, 190, 220, 230);
            }
            if (mistakes >= 4) {
                g2.drawLine(250, 190, 280, 230);
            }
            if (mistakes >= 5) {
                g2.drawLine(250, 250, 220, 300);
            }
            if (mistakes >= 6) {
                g2.drawLine(250, 250, 280, 300);
            }
        } finally {
            g2.dispose();
        }
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(400, 380);
    }
}