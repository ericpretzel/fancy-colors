import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Screen extends JPanel {

    public static int WIDTH = 600;
    public static int HEIGHT = 400;

    final Map<Integer, Point> points;
    QuadTree quadTree;

    private final Color[] colors = new Color[] {
            new Color(255, 0, 0, 100),
            new Color(255, 141, 0, 100),
            new Color(249,255, 0, 100),
            new Color(98,255, 0, 100),
            new Color(0,255, 29, 100),
            new Color(0,255, 231, 100),
            new Color(0, 85, 255, 100),
            new Color(84, 0, 255, 100),
            new Color(192, 0, 255, 100),
            new Color(255, 0,174, 100),
            new Color(255, 103, 197, 100),
            new Color(54,255,174, 100),
            new Color(203, 113, 255, 100),
            new Color(160, 193, 255, 100),
            new Color(255, 174, 157, 100),
            new Color(255, 237, 234, 100),
            new Color(216, 255, 156, 100),
            new Color(255, 105, 246, 100),
    };

    public Screen() {
        super(null);
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setFocusable(true);
        setBackground(Color.white);

        this.points = new ConcurrentHashMap<>();
        quadTree = new QuadTree(new Rect((double) WIDTH / 2, (double) HEIGHT / 2, WIDTH, HEIGHT), 4);
        this.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                WIDTH = getWidth();
                HEIGHT = getHeight();
                quadTree = new QuadTree(new Rect((double) WIDTH / 2, (double) HEIGHT / 2, WIDTH, HEIGHT), 4);
            }
        });

        MouseAdapter adapter = new MouseAdapter() {
            private boolean mousePressed = false;
            private boolean fading = false;

            @Override
            public void mouseDragged(MouseEvent e) {
                if (mousePressed) {
                    int j = points.size() + 1;
                    points.put(j, new Point(j, e.getX(), e.getY(), colors[(int) (Math.random() * colors.length)]));
                }
            }

            public void mousePressed(MouseEvent e) {
                switch (e.getButton()) {

                    case MouseEvent.BUTTON1:
                        mousePressed = true;
                        break;

                    case MouseEvent.BUTTON3:
                        fading = !fading;
                        Point.dAlpha = fading ? 0.5 : 0;
                        break;

                }
            }
            public void mouseReleased(MouseEvent e) {
                mousePressed = false;
            }

        };
        this.addMouseMotionListener(adapter);
        this.addMouseListener(adapter);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        points.values().forEach((point) -> {
            g.setColor(point.color);
            g.fillPolygon(point.polygon);
            g.fillOval((int) point.x, (int) point.y, 5, 5);
        });
    }

    public void animate() {
        while (true) {
            SwingUtilities.invokeLater(this::repaint);
            points.values().forEach(quadTree::insert);
            points.forEach((i, point) -> {
                if (point.alpha >= 0) {
                    points.replace(i, point);
                    point.move();
                    handleCollisions(point, quadTree);
                }
            });
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void handleCollisions(Point p, QuadTree quadTree) {
        Rect rect = new Rect(p.x, p.y, 80, 80);
        p.handleCollision(quadTree.query(rect, new HashSet<>()));
    }
}