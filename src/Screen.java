import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class Screen extends JPanel {

    public static int WIDTH = 450;
    public static int HEIGHT = 450;

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

        /*this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                    points.clear();
                }
            }
        });*/

        this.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                switch (e.getButton()) {

                    case MouseEvent.BUTTON1:
                        for (int i = 0; i < 5; i++) {
                            int j = points.keySet().size() + 1;
                            points.put(j, new Point(j, e.getX(), e.getY(), colors[(int)(Math.random() * colors.length)]));
                        }
                        break;

                    case MouseEvent.BUTTON3:
                        points.values().forEach(p -> p.color = colors[(int)(Math.random() * colors.length)]);
                        break;

                }
            }
        });
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
                points.replace(i, point);
                point.move();
                handleCollisions(point, quadTree);
            });
            try {
                Thread.sleep(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void handleCollisions(Point p, QuadTree quadTree) {
        Rect rect = new Rect(p.x, p.y, 100, 100);
        p.handleCollision(quadTree.query(rect, new HashSet<>()));
    }
}