import java.lang.Math; 
import java.util.Objects;

public class Point {

	protected int x;
	protected int y;

	@Override
	public String toString() {
		return String.format(x + "@" + y);
	}

	@Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof Point)) {
            return false;
        }
		Point point = (Point) o;
		return x == point.getX() && y == point.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }


	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Point add(Point aPoint) {
		int newX = x + aPoint.getX();
		int newY = y + aPoint.getY();
		return new Point(newX, newY);
	}

	public Point directionTo(Point aPoint) {
		int dx = aPoint.getX() - x;
		int dy = aPoint.getY() - y;
		if (dx != 0) {
			dx = dx / Math.abs(dx);
		}
		if (dy != 0) {
			dy = dy / Math.abs(dy);
		}
		return new Point(dx, dy);
	}

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Point(String s) {
		String[] parts = s.split("@");
		this.x = Integer.parseInt(parts[0]);
		this.y = Integer.parseInt(parts[1]);
	}

}