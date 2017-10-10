package brushSettings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import Main.Game;
import brushes.Brush;
import brushes.DrawPolygon;
import brushes.Ereaser;
import brushes.Fill;
import brushes.NormalBrush;
import brushes.StraightEdge;

public class SettingsPane {

	private LinkedList<Brush> brushes;
	private LinkedList<Slider> sliders;
	private int selectedBrush = 1;

	private AWTColorSelector colors;

	private static final int NORMAL_SIZE = 0, EREASER_SIZE = 1, POLYGON_SIDES = 2, POLYGON_SIZE = 3, LINE_THICKNESS = 4;

	public SettingsPane(LinkedList<Brush> brushes) {
		this.brushes = brushes;
		colors = new AWTColorSelector(0);

		sliders = new LinkedList<Slider>();
		sliders.add(new Slider("Size of brush", 200));
		sliders.add(new Slider("Size of ereaser", 200));
		sliders.add(new Slider("Number of sides", 360));
		sliders.add(new Slider("Size of shape", 200));
		sliders.add(new Slider("Thickness of line", 200));
	}

	public void setSelectedBrush(int b) {
		selectedBrush = b;
	}

	public void mousePressed(MouseEvent e) {

		Brush b = brushes.get(selectedBrush);
		if (b instanceof NormalBrush) {
			sliders.get(NORMAL_SIZE).mousePressed(e);
			colors.mousePressed(e);
			NormalBrush nb = (NormalBrush) b;
			colors.setColor(nb.getC());
		}
		if (b instanceof Fill) {
			colors.mousePressed(e);
			Fill f = (Fill) b;
			colors.setColor(f.getC());
		}

		if (b instanceof Ereaser) {
			sliders.get(EREASER_SIZE).mousePressed(e);
		}

		if (b instanceof DrawPolygon) {
			colors.mousePressed(e);
			sliders.get(POLYGON_SIDES).mousePressed(e);
			sliders.get(POLYGON_SIZE).mousePressed(e);
		}
		if (b instanceof StraightEdge) {
			colors.mousePressed(e);
			sliders.get(LINE_THICKNESS).mousePressed(e);
		}
	}

	public void activeMouse(Point amp) {
		sliders.forEach(slide->slide.activeMouse(amp));
		colors.activeMouse(amp);
	}

	public void mouseReleased() {
		sliders.forEach(slide->slide.mouseReleased());
		colors.mouseReleased();
	}

	public void tick() {
		Brush b = brushes.get(selectedBrush);
		if (b instanceof NormalBrush) {
			NormalBrush nb = (NormalBrush) b;
			nb.setC(colors.getColor());
			colors.tick();
			colors.setX(0);
			NormalBrush.setSize(sliders.get(NORMAL_SIZE).getVal());
			sliders.get(NORMAL_SIZE).setX(colors.getWidth() + 2);
		}

		if (b instanceof Fill) {
			Fill f = (Fill) b;
			f.setC(colors.getColor());
			colors.tick();
			colors.setX(0);
		}

		if (b instanceof Ereaser) {
			Ereaser e = (Ereaser) b;
			e.setSize(sliders.get(EREASER_SIZE).getVal());
		}
		if (b instanceof DrawPolygon) {
			DrawPolygon p = (DrawPolygon) b;
			p.setColor(colors.getColor());
			sliders.get(POLYGON_SIDES).setX(colors.getWidth() + 2);
			p.setNumSides(sliders.get(POLYGON_SIDES).getVal());
			sliders.get(POLYGON_SIZE).setX(colors.getWidth() + sliders.get(POLYGON_SIDES).getWidth() + 4);
			p.setSize(sliders.get(POLYGON_SIZE).getVal());
		}
		if (b instanceof StraightEdge) {
			StraightEdge se = (StraightEdge) b;
			se.setColor(colors.getColor());
			sliders.get(LINE_THICKNESS).setX(colors.getWidth() + 2);
			se.setSize(sliders.get(LINE_THICKNESS).getVal());
		}
	}

	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawRect(5, 3 * Game.HEIGHT / 4 + 55, Game.WIDTH - 30, Game.HEIGHT / 4 - 105);
		Brush b = brushes.get(selectedBrush);
		if (b instanceof NormalBrush) {
			colors.render(g);
			sliders.get(NORMAL_SIZE).render(g);
		}
		if (b instanceof Fill) {
			colors.render(g);
		}
		if (b instanceof Ereaser) {
			sliders.get(EREASER_SIZE).render(g);
		}
		if (b instanceof DrawPolygon) {
			colors.render(g);
			sliders.get(POLYGON_SIDES).render(g);
			sliders.get(POLYGON_SIZE).render(g);
		}
		if (b instanceof StraightEdge) {
			colors.render(g);
			sliders.get(LINE_THICKNESS).render(g);
		}
	}

}
