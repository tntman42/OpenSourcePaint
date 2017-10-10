package brushes;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

public class Fill extends Brush {

	private Color c;
	
	public Fill() {
		super("Fill Bucket");
		c = Color.WHITE;
	}
	public void setC(Color c) {
		this.c = c;
	}

	public Color getC() {
		return c;
	}
	
	@Override
	public void draw(BufferedImage image, Point c) {
		int passable = image.getRGB((int) c.getX(), (int) c.getY());
		int nc = this.c.getRGB();
		
		int[][] imageArray = new int[image.getHeight()][image.getWidth()];
		for (int i = 0;i < imageArray.length;i++) {
			for (int j = 0;j < imageArray[i].length;j++) {
				imageArray[i][j] = image.getRGB(j, i);
			}
		}
		fill(imageArray,(int)c.getX(),(int)c.getY(),passable,nc);
		for (int i = 0;i < imageArray.length;i++) {
			for (int j = 0;j < imageArray[i].length;j++) {
				image.setRGB(j, i, imageArray[i][j]);
			}
		}
	}

	@Override
	public void lift() {
		// this doesn't need to do anything
	}

	public boolean fill(int[][] path, int x,int y,int passable,int nc) {
		if (passable == nc) {
			return false;
		}
		LinkedList<Point> nodeList = new LinkedList<Point>();
		while (true) {
			path[y][x] = 1;
			boolean moved = false;
			if (y < path.length - 1 && path[y+1][x] == passable) {
				nodeList.add(new Point(x,y));
				moved = true;
				y++;
			}else if (x < path[0].length - 1 && path[y][x+1] == passable) {
				nodeList.add(new Point(x,y));
				moved = true;
				x++;
			}else if (x > 0 && path[y][x-1] == passable) {
				nodeList.add(new Point(x,y));
				moved = true;
				x--;
			}else if (y > 0 && path[y-1][x] == passable) {
				nodeList.add(new Point(x,y));
				moved = true;
				y--;
			}
			
			if (nodeList.isEmpty()) {
				return true;
			}
			if (!moved) {
				path[y][x] = nc;
				Point last = nodeList.remove(nodeList.size() - 1);
				x = (int) last.getX();
				y = (int) last.getY();
			}
		}
	}
	
	@Override
	public Polygon bounds() {
		int[] xs = {0,0,0};
		int[] ys = {0,0,0};
		
		return new Polygon(xs,ys,3);
	}
}
