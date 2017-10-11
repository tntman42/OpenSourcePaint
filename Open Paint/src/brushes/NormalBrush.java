package brushes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

public class NormalBrush extends Brush {

	private Point old;
	private Color c;

	private static int size;

	public NormalBrush(String name) {
		super(name);
		size = 5;
	}

	public void setC(Color c) {
		this.c = c;
	}

	public Color getC() {
		return c;
	}
	
	public static void setSize(int nsize) {
		size = nsize;
	}

	@Override
	public void draw(BufferedImage g, Point c) {
		if (old == null) {
			old = new Point();
			old.setLocation(c);
		}
		Graphics2D grap = g.createGraphics();
		grap.setStroke(new BasicStroke(size,BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
		grap.drawLine((int)old.getX(), (int)old.getY(), (int)c.getX(), (int)c.getY());
		if (c != null && old != null) {
			old.setLocation(c);
		}

	}

	@Override
	public void lift() {
		old = null;
	}

	@Override
	public Polygon bounds() {
		int[] xs = {-size/2,-size/2,size/2,size/2};
		int[] ys = {-size/2,size/2,size/2,-size/2};
		return new Polygon(xs,ys,4);
	}
}
