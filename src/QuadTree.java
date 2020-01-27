import java.util.*;

public class QuadTree {

	final Rect boundary;
	final int capacity;
	boolean divided;
	QuadTree northwest, northeast, southwest, southeast;
	final Map<Integer, Point> points;

	QuadTree(Rect boundary, int capacity) {
		this.boundary = boundary;
		this.capacity = capacity;
		this.divided = false;
		this.points = new HashMap<>();
	}

	void insert(Point p) {
		if (this.boundary.contains(p)) {
			if (this.points.size() < this.capacity) {
				points.put(p.id, p);
			} else {
				if (!this.divided) {
					this.divide();
				}
				if (this.northwest.boundary.contains(p)) {
					this.northwest.insert(p);
				} else if (this.northeast.boundary.contains(p)) {
					this.northeast.insert(p);
				} else if (this.southwest.boundary.contains(p)) {
					this.southwest.insert(p);
				} else if (this.southeast.boundary.contains(p)) {
					this.southeast.insert(p);
				}
			}
		}
	}

	void divide() {
		Rect nw = new Rect(this.boundary.x - this.boundary.w / 4, this.boundary.y - this.boundary.h / 4, this.boundary.w / 2, this.boundary.h / 2);
		Rect ne = new Rect(this.boundary.x + this.boundary.w / 4, this.boundary.y - this.boundary.h / 4, this.boundary.w / 2, this.boundary.h / 2);
		Rect sw = new Rect(this.boundary.x - this.boundary.w / 4, this.boundary.y + this.boundary.h / 4, this.boundary.w / 2, this.boundary.h / 2);
		Rect se = new Rect(this.boundary.x + this.boundary.w / 4, this.boundary.y + this.boundary.h / 4, this.boundary.w / 2, this.boundary.h / 2);

		this.northwest = new QuadTree(nw, this.capacity);
		this.northeast = new QuadTree(ne, this.capacity);
		this.southwest = new QuadTree(sw, this.capacity);
		this.southeast = new QuadTree(se, this.capacity);

		this.divided = true;
	}

	Collection<Point> query(Rect r, Collection<Point> relevantPoints) {
		if (this.boundary.intersects(r)) {
			this.points
				.values()
				.stream()
				.filter(r::contains)
				.forEach(relevantPoints::add);
			if (this.divided) {
				this.northwest.query(r, relevantPoints);
				this.northeast.query(r, relevantPoints);
				this.southwest.query(r, relevantPoints);
				this.southeast.query(r, relevantPoints);
			}
		}
		return relevantPoints;
	}

}