package brushes;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;

public class Ereaser extends Brush{

	private static int size = 5;
	private Point old;
	
	public Ereaser() {
		super("Ereaser");
	}

	public void setSize(int ns) {
		size = ns;
	}
	
	@Override
	public void draw(BufferedImage image, Point c) {
		if (old == null) {
			old = new Point();
			old.setLocation(c);
		}
		int col = (new BufferedImage(1,1,BufferedImage.TYPE_INT_ARGB)).getRGB(0, 0);
		drawRectangleLine((int) c.getX(), (int) c.getY(), (int) old.getX(), (int) old.getY(), image, col);
		drawPixRectangle((int) c.getY() - size / 2, (int) c.getX() - size / 2, size, size, image, col);
		if (c != null && old != null) {
			old.setLocation(c);
		}
	}

	private static void drawPixLine(int x1, int y1, int x2, int y2, BufferedImage img, int currCol) {
		double dist = Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
		for (int i = 0; i < dist; i++) {
			try {
				img.setRGB((int) (i * (y2 - y1) / dist + y1), (int) (i * (x2 - x1) / dist + x1), currCol);
			} catch (Exception e) {
				System.err.println("The coordinate: (" + (i * (y2 - y1) / dist + y1) + ", "
						+ (i * (x2 - x1) / dist + x1) + ") is out of bounds");
			}
		}
	}

	private static void drawPixRectangle(int x, int y, int width, int height, BufferedImage img, int currCol) {
		for (int i = 0; i < width; i++) {
			drawPixLine(x + i, y, x + i, y + height, img, currCol);
		}
	}

	private static void drawRectangleLine(int x1, int y1, int x2, int y2, BufferedImage img, int currCol) {
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
