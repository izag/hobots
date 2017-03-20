package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class PaintRectangle extends JPanel
{
	private static final long serialVersionUID = 1L;
	public PaintRectangle() {
        setPreferredSize(new Dimension(500, 500));

        MouseAdapter listener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                updateMouseRectangle(e);
            }

            private void updateMouseRectangle(MouseEvent e) {
                e.getPoint();
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                updateMouseRectangle(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                repaint();
            }
        };
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    private Rectangle getRectangle()
    {
        return new Rectangle(100, 100, 30, 30);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Rectangle rectangle = getRectangle();
        if(rectangle != null) {
            Graphics2D gg = (Graphics2D) g;
            gg.setColor(Color.BLUE);
            gg.fill(rectangle);
            gg.setColor(Color.BLACK);
            gg.draw(rectangle);
        }
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame frame = new JFrame("Test");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(new PaintRectangle());
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
