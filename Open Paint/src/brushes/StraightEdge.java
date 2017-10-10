package brushes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

public class StraightEdge extends Brush {

	public int size = 10;
	private Color col = Color.WHITE;

	private Graphics2D im;

	private Point start, end;

	public StraightEdge() {
		super("Line");
		start = null;
	}
	
	public void setColor(Color col) {
		this.col = col;
	}
	
	public void setSize(int size) {
		this.size = size;
	}

	@Override
	public void draw(BufferedImage image, Point c) {
		if (start == null) {
			start = new Point();
			start.setLocation(c);
		}
		Graphics2D g = image.createGraphics();
		im = g;
		end = c;
	}

	@Override
	public void lift() {
		if (im != null && start != null) {
			im.setColor(col);
			im.setStroke(new BasicStroke(size));
			im.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY());
		}
		start = null;
	}

	@Override
	public Polygon bounds() {
		int[] xs = { 0, 0 };
		int[] ys = { 0, 0 };

		if (start != null) {
			Point midPoint = new Point();
			midPoint.setLocation((start.getX() + end.getX()) / 2, (start.getY() + end.getY()) / 2);

			xs[0] = (int) (-end.getX() + start.getX());
			ys[0] = (int) (-end.getY() + start.getY());
		}

		return new Polygon(xs, ys, xs.length);
	}

}
