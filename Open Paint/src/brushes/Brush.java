package brushes;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public abstract class Brush {

	private String name;

	public Brush(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static void getBrushSet(LinkedList<Brush> b) {
		b.add(new NormalBrush("Square Brush"));
		b.add(new Fill());
		b.add(new Ereaser());
		b.add(new DrawPolygon());
		b.add(new StraightEdge());
	}

	public abstract void draw(BufferedImage image, Point c);

	public abstract void lift();
	
	public abstract Polygon bounds();
}
