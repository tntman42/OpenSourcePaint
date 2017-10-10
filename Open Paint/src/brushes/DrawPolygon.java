package brushes;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

public class DrawPolygon extends Brush {

	private Polygon poly = null;
	private int size = 10;
	private Color col;

	public DrawPolygon() {
		super("Polygon");
	}

	public void setColor(Color col) {
		this.col = col;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public void setNumSides(int sides) {
		int[] xs = new int[sides];
		int[] ys = new int[sides];
		for (int i = 0; i < sides; i++) {
			xs[i] = (int) (Math.cos(i * Math.PI * 2 / sides) * size);
			ys[i] = (int) (Math.sin(i * Math.PI * 2 / sides) * size);
		}
		poly = new Polygon(xs, ys, sides);
	}

	@Override
	public void draw(BufferedImage image, Point c) {
		if (poly != null) {
			Polygon clone = new Polygon(poly.xpoints,poly.ypoints,poly.npoints);
			Graphics g = image.createGraphics();
			for (int i = 0; i < clone.npoints; i++) {
				clone.xpoints[i] += c.getX();
				clone.ypoints[i] += c.getY();
			}
			g.setColor(col);
			g.fillPolygon(clone);
		}
	}

	@Override
	public void lift() {
		// doesn't need to do anything
	}

	@Override
	public Polygon bounds() {
		if (poly != null) {
			return poly;
		}
		return null;
	}

}
