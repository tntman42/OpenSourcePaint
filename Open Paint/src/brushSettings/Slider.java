package brushSettings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import Main.Game;

public class Slider {

	private int val;
	private String name;
	private int x;

	private int max = 100;

	private boolean dragging = false;
	
	private int width = 100;
	
	private int textHeight = 12;

	public Slider(String name, int x) {
		val = 0;
		this.name = name;
		this.x = x;
	}

	public Slider(String name, int x, int start) {
		val = start;
		this.name = name;
		this.x = x;
	}

	public void setVal(int val) {
		this.val = val;
	}

	public int getVal() {
		return val;
	}

	public void activeMouse(Point amp) {
		if (dragging) {
			if (amp.getY() < 3 * Game.HEIGHT / 4 + 57) {
				val = max;
			}else if (amp.getY() > (3 * Game.HEIGHT / 4 + 57) + (Game.HEIGHT / 4 - 109)) {
				val = 0;
			}else {
				val =(int)( max - ((amp.getY() - (3 * Game.HEIGHT / 4 + 57)) * max / (Game.HEIGHT / 4 - 109)));
			}
		}
	}

	public void mousePressed(MouseEvent e) {
		if ((new Rectangle(x, 3 * Game.HEIGHT / 4 + 57, 10, Game.HEIGHT / 4 - 109)).contains(e.getPoint())) {
			dragging = true;
		}
		if ((new Rectangle(x + 16, 3 * Game.HEIGHT / 4 + 61 + textHeight * 2, 20, 20)).contains(e.getPoint()) && val < max) {
			val++;
		}
		if ((new Rectangle(x + 16, 3 * Game.HEIGHT / 4 + 83 + textHeight * 2, 20, 20)).contains(e.getPoint()) && val > 0) {
			val--;
		}
	}

	public void mouseReleased() {
		dragging = false;
	}

	public void render(Graphics g) {
		g.setColor(Color.BLACK);
		g.drawRect(x + 7, 3 * Game.HEIGHT / 4 + 57, 3, Game.HEIGHT / 4 - 109);

		g.setColor(Color.LIGHT_GRAY);
		g.fillOval(x + 4, 3 * Game.HEIGHT / 4 + 57 + (max - val) * (Game.HEIGHT / 4 - 109) / max - 2, 10, 5);

		g.setColor(Color.BLACK);
		g.drawOval(x + 4, 3 * Game.HEIGHT / 4 + 57 + (max - val) * (Game.HEIGHT / 4 - 109) / max - 2, 10, 5);

		g.drawString(name + ": ", x + 17, 3 * Game.HEIGHT / 4 + 57 + g.getFontMetrics().getHeight());
		g.drawString(val + "", x + 17, 3 * Game.HEIGHT / 4 + 57 + g.getFontMetrics().getHeight() * 2);
		
		g.setColor(Color.BLACK);
		g.drawRect(x + 16, 3 * Game.HEIGHT / 4 + 61 + g.getFontMetrics().getHeight() * 2, 20, 20);
		g.drawRect(x + 16, 3 * Game.HEIGHT / 4 + 83 + g.getFontMetrics().getHeight() * 2, 20, 20);
		
		g.drawString("+", x + 23, 3 * Game.HEIGHT / 4 + 61 + g.getFontMetrics().getHeight() * 3);
		g.drawString("-", x + 25, 3 * Game.HEIGHT / 4 + 82 + g.getFontMetrics().getHeight() * 3);
		
		textHeight = g.getFontMetrics().getHeight();
		width = 17 + g.getFontMetrics().stringWidth((name + ": "));
	}

	public void setX(int x) {
		this.x = x;
	}
	
	public int getWidth() {
		return width;
	}
}
