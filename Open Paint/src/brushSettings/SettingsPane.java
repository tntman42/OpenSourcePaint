package brushSettings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import Main.Game;
import brushes.Brush;
import brushes.Ereaser;
import brushes.Fill;
import brushes.NormalBrush;

public class SettingsPane {

	private LinkedList<Brush> brushes;
	private LinkedList<Slider> sliders;
	private int selectedBrush = 1;

	private AWTColorSelector colors;
	
	private static final int NORMAL_SIZE = 0,EREASER_SIZE = 1;

	public SettingsPane(LinkedList<Brush> brushes) {
		this.brushes = brushes;
		colors = new AWTColorSelector(0);
		
		sliders = new LinkedList<Slider>();
		sliders.add(new Slider("Size of brush", 200));
		sliders.add(new Slider("Size of ereaser",0));
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
	}

	public void activeMouse(Point amp) {
		
		Brush b = brushes.get(selectedBrush);
		if (b instanceof NormalBrush) {
			sliders.get(NORMAL_SIZE).activeMouse(amp);
			colors.activeMouse(amp);
			
		}
		if (b instanceof Fill) {
			colors.activeMouse(amp);
		}
		if (b instanceof Ereaser) {
			sliders.get(EREASER_SIZE).activeMouse(amp);
		}
	}

	public void mouseReleased() {
		
		Brush b = brushes.get(selectedBrush);
		if (b instanceof NormalBrush) {
			sliders.get(NORMAL_SIZE).mouseReleased();
			colors.mouseReleased();
		}
		if (b instanceof Fill) {
			colors.mouseReleased();
		}
		if (b instanceof Ereaser) {
			sliders.get(EREASER_SIZE).mouseReleased();
		}
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
	}

}
