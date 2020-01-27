import java.awt.*;
import java.util.*;
import java.util.List;

public class Point {

	public final int id;
	public double x, y;
	private double dX, dY;
	public Polygon polygon;
	public Color color;

	Point(int id, int x, int y, Color color) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.polygon = new Polygon();
		this.color = color;

		dX = (0.025 + (Math.random() * 0.25)) * (Math.random() > 0.5 ? 1 : -1);
		dY = (0.025 + (Math.random() * 0.25)) * (Math.random() > 0.5 ? 1 : -1);
	}

	void move() {
		if (this.x + 5 >= Screen.WIDTH || this.x <= 0) dX *= -1;
		if (this.y + 5 >= Screen.HEIGHT || this.y <= 0) dY *= -1;

		this.x += this.dX;
		this.y += this.dY;
	}

	void handleCollision(Collection<Point> toCheck) {
		List<Point> points = new ArrayList<>(toCheck);
		this.polygon = generatePolygon(points);
	}

	Polygon generatePolygon(Collection<Point> points) {
		Polygon polygon = new Polygon();
		polygon.addPoint((int)this.x, (int)this.y);

		for (Point p : points) {
			polygon.addPoint((int)(p.x + 2.5), (int)(p.y + 2.5));
		}
		return polygon;
	}
}