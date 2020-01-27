public class Rect {
	
	final double x;
    final double y;
    final double w;
    final double h;

	Rect(double x, double y, double w, double h) {
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
	}

	boolean contains(Point p) {
		return p.x >= this.x - this.w / 2 
			&& p.x <= this.x + this.w / 2
			&& p.y >= this.y - this.h / 2
			&& p.y <= this.y + this.h / 2;
	}

	boolean intersects(Rect r) {
		return !(this.x + this.w /2 <= r.x - r.w / 2
			|| this.x - this.w /2 >= r.x + r.w / 2
			|| this.y + this.h / 2 <= r.y - r.h / 2
			|| this.y - this.h / 2 >= r.y + r.h / 2);
	}

}