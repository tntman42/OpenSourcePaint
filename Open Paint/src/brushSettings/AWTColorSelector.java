package brushSettings;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import Main.Game;

public class AWTColorSelector {

	private Color c;
	private float h, s, b;
	private Rectangle sbSquare, hueRect;

	private boolean editingSB = false, editingH = false;

	private int x;

	public AWTColorSelector(int x) {
		c = Color.WHITE;
		h = 0;
		s = 0;
		b = 1;
		sbSquare = new Rectangle(0, 0, 10, 10);
		hueRect = new Rectangle(0, 0, 10, 10);
		this.x = x;
	}

	public AWTColorSelector(Color c) {
		this.c = c;
		h = 0;
		s = 0;
		b = 1;
		sbSquare = new Rectangle(0, 0, 10, 10);
		hueRect = new Rectangle(0, 0, 10, 10);
	}

	public Color getColor() {
		return c;
	}

	public void setColor(Color c) {
		float[] hsb = {h,s,b};
		Color.RGBtoHSB(c.getRed(), c.getBlue(), c.getGreen(), hsb);
		this.c = c;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void activeMouse(Point amp) {
		if (editingSB) {
			if (sbSquare.contains(amp)) {
				s = (float) ((amp.getX() - sbSquare.getX()) / sbSquare.getWidth());
				b = (float) ((sbSquare.getHeight() - (amp.getY() - sbSquare.getY())) / sbSquare.getHeight());
			} else if (amp.getX() >= sbSquare.getX() && amp.getX() <= sbSquare.getX() + sbSquare.getWidth()) {
				s = (float) ((amp.getX() - sbSquare.getX()) / sbSquare.getWidth());
				if (amp.getY() < sbSquare.getY()) {
					b = 1;
				} else {
					b = 0;
				}
			} else if (amp.getY() >= sbSquare.getY() && amp.getY() <= sbSquare.getY() + sbSquare.getHeight()) {
				b = (float) ((sbSquare.getHeight() - (amp.getY() - sbSquare.getY())) / sbSquare.getHeight());
				if (amp.getX() < sbSquare.getX()) {
					s = 0;
				} else {
					s = 1;
				}
			} else {
				if (amp.getY() < sbSquare.getY()) {
					b = 1;
				} else {
					b = 0;
				}
				if (amp.getX() < sbSquare.getX()) {
					s = 0;
				} else {
					s = 1;
				}
			}
			c = Color.getHSBColor(h, s, b);
		}
		if (editingH) {
			if (amp.getY() >= hueRect.getY() && amp.getY() <= hueRect.getY() + hueRect.getHeight()) {
				h = (float) ((amp.getY() - hueRect.getY()) / hueRect.getHeight());
			} else {
				if (amp.getY() < hueRect.getY()) {
					h = 0;
				} else {
					h = 1;
				}
			}
			c = Color.getHSBColor(h, s, b);
		}
	}

	public void mousePressed(MouseEvent e) {
		if (sbSquare.contains(e.getPoint())) {
			editingSB = true;
		}
		if (hueRect.contains(e.getPoint())) {
			editingH = true;

		}
	}

	public void mouseReleased() {
		editingSB = false;
		editingH = false;
	}

	// 5,3 * Game.HEIGHT/4 + 55, Game.WIDTH - 30, Game.HEIGHT/4 - 105
	public void tick() {
		sbSquare.setBounds(7 + x, 3 * Game.HEIGHT / 4 + 57, Game.HEIGHT / 4 + 57, Game.HEIGHT / 4 - 109);
		hueRect.setBounds(7 + Game.HEIGHT / 4 + 57 + 2 + x, 3 * Game.HEIGHT / 4 + 57, 40, Game.HEIGHT / 4 - 109);
	}

	public void render(Graphics g) {
		for (int i = 0; i < sbSquare.getHeight(); i++) {
			for (int j = 0; j < sbSquare.getWidth(); j++) {
				g.setColor(Color.getHSBColor(h, (float) (j / sbSquare.getWidth()),
						(float) ((sbSquare.getHeight() - i) / sbSquare.getHeight())));
				g.fillRect((int) (sbSquare.getX() + j), (int) (sbSquare.getY() + i), 1, 1);
			}
		}
		g.setColor(Color.BLACK);
		Graphics2D g2d = (Graphics2D) g;
		g2d.draw(sbSquare);

		// hue selector
		for (int i = 0; i < hueRect.getHeight(); i++) {
			g.setColor(Color.getHSBColor((float) (i / hueRect.getHeight()), 1, 1));
			g.drawLine((int) (hueRect.getX() + hueRect.getWidth() / 2), (int) (hueRect.getY() + i),
					(int) (hueRect.getX() + hueRect.getWidth()), (int) (hueRect.getY() + i));
			g.setColor(Color.getHSBColor((float) (i / hueRect.getHeight()), s, b));
			g.drawLine((int) (hueRect.getX()), (int) (hueRect.getY() + i),
					(int) (hueRect.getX() + hueRect.getWidth() / 2), (int) (hueRect.getY() + i));
		}

		g.setColor(Color.BLACK);
		g2d.draw(hueRect);

		g.drawString("HSB: [" + h + ", " + s + ", " + b + "]", 7 + Game.HEIGHT / 4 + 57 + 44 + x,
				3 * Game.HEIGHT / 4 + 57 + g.getFontMetrics().getHeight());
		g.drawString("RGB: [" + c.getRed() + ", " + c.getGreen() + ", " + c.getBlue() + "]",
				7 + Game.HEIGHT / 4 + 57 + 44 + x, 3 * Game.HEIGHT / 4 + 57 + 2 * g.getFontMetrics().getHeight());

		g.setColor(c);
		g.fillRect(7 + Game.HEIGHT / 4 + 57 + 46 + x, 3 * Game.HEIGHT / 4 + 57 + 3 * g.getFontMetrics().getHeight(), 50,
				25);

		g.setColor(new Color(255 - c.getRed(), 255 - c.getGreen(), 255 - c.getBlue()));
		g.drawOval((int) (sbSquare.getWidth() * s - 4 + sbSquare.getX()),
				(int) (sbSquare.getHeight() * (1 - b) - 4 + sbSquare.getY()), 8, 8);

		g.drawRect((int) (hueRect.getX() - 1), (int) (h * hueRect.getHeight() + hueRect.getY() - 1), 42, 3);
	}
	
	public int getWidth() {
		return (int)(sbSquare.getWidth() + 2 + hueRect.getWidth() + 252);
	}
}
