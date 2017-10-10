package brushes;

import java.awt.Color;
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
		drawRectangleLine((int) c.getX(), (int) c.getY(), (int) old.getX(), (int) old.getY(), g, this.c);
		drawPixRectangle((int) c.getY() - size / 2, (int) c.getX() - size / 2, size, size, g, this.c);
		if (c != null && old != null) {
			old.setLocation(c);
		}

	}

	private static void drawPixLine(int x1, int y1, int x2, int y2, BufferedImage img, Color currCol) {
		double dist = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
		for (int i = 0; i < dist; i++) {
			try {
				img.setRGB((int) (i * (y2 - y1) / dist + y1), (int) (i * (x2 - x1) / dist + x1), currCol.getRGB());
			} catch (Exception e) {
				System.err.println("The coordinate: (" + (i * (y2 - y1) / dist + y1) + ", "
						+ (i * (x2 - x1) / dist + x1) + ") is out of bounds");
			}
		}
	}

	private static void drawPixRectangle(int x, int y, int width, int height, BufferedImage img, Color currCol) {
		for (int i = 0; i < width; i++) {
			drawPixLine(x + i, y, x + i, y + height, img, currCol);
		}
	}

	private static void drawRectangleLine(int x1, int y1, int x2, int y2, BufferedImage img, Color currCol) {
		double dist = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
		for (int i = 0; i < dist; i++) {
			try {
				drawPixRectangle((int) (i * (y2 - y1) / dist + y1) - size / 2,
						(int) (i * (x2 - x1) / dist + x1) - size / 2, size, size, img, currCol);
			} catch (Exception e) {
				System.err.println("The coordinate: (" + (i * (y2 - y1) / dist + y1) + ", "
						+ (i * (x2 - x1) / dist + x1) + ") is out of bounds");
			}
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
