
package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.JOptionPane;

import Files.TextIO;
import Graphics.Button;
import Image.ImageLoader;
import Main.GRunner;
import Main.Game;
import Main.Menu;
import Main.WindowSettings;
import brushSettings.SettingsPane;
import brushes.Brush;
import rendering.Importer;
import rendering.RenderManager;

public class Runner implements GRunner {

	private LinkedList<Brush> brushes;

	private LinkedList<Layer> layers;
	private Dimension imageSize;
	private Rectangle viewport;

	private int selectedLayer = -1, minListViewInd = 0, selectedBrush = 0, minBrushViewInd = 0;

	private Menu m;

	private boolean drawing = false, drawOutline = false;
	private Polygon brushShape;

	private double smallDim = 0, comparison = 10;

	private int layerCount = 1;

	private SettingsPane settings;

	private LinkedList<LinkedList<Layer>> undoStack;
	private static final char[] commandKeys = { 'z', 's', 'o', 'r' };
	private String savePath;
	private boolean[] commandKeyPress = new boolean[commandKeys.length];
	private boolean ctrl = false;

	@Override
	public void act(int id) {

		if (id == 1) {
			layers.add(new Layer("Layer_" + (layerCount++), imageSize));
			selectedLayer = layers.size() - 1;
		}
		if (id == 2 && minListViewInd > 0) {
			minListViewInd--;
		}
		if (id == 3 && minListViewInd < layers.size() - 1) {
			minListViewInd++;
		}

		if (!layers.isEmpty() && selectedLayer > -1 && selectedLayer < layers.size()) {
			if (id == 4) {
				layers.remove(selectedLayer);
				if (selectedLayer > 0) {
					selectedLayer--;
				}
			}
			if (id == 5) {
				layers.get(selectedLayer).setName(JOptionPane.showInputDialog("Input the new name of the layer: "));
			}
			if (id == 6 && selectedLayer > 0) {
				Layer temp = layers.get(selectedLayer);
				layers.set(selectedLayer, layers.get(selectedLayer - 1));
				layers.set(selectedLayer - 1, temp);
				selectedLayer--;
			}
			if (id == 7 && selectedLayer < layers.size() - 1) {
				Layer temp = layers.get(selectedLayer);
				layers.set(selectedLayer, layers.get(selectedLayer + 1));
				layers.set(selectedLayer + 1, temp);
				selectedLayer++;
			}
			if (id == 8) {
				layers.get(selectedLayer).setVisible(!layers.get(selectedLayer).isVisible());
			}
		}

		// render
		if (id == 9) {
			renderImage();
		}
		// save
		if (id == 10) {
			save();
		}
		// open
		if (id == 11) {
			open();
		}
		// import
		if (id == 12) {
			Importer im = new Importer();
			File f = new File(im.getPath());
			BufferedImage img = (BufferedImage) ImageLoader.createImage(f.getPath());
			Layer l = new Layer(f.getName(), imageSize);
			for (int i = 0; i < l.getImage().getHeight() && i < img.getHeight(); i++) {
				for (int j = 0; j < l.getImage().getWidth() && j < img.getWidth(); j++) {
					l.getImage().setRGB(j, i, img.getRGB(j, i));
				}
			}
			layers.add(l);
		}

		if (id == 13) {
			imageSize.setSize(Integer.valueOf(JOptionPane.showInputDialog("Set the image width: ")),
					Integer.valueOf(JOptionPane.showInputDialog("Set image height:")));
		}
	}

	@Override
	public void activeMousePosition(Point amp) {
		if (drawing && selectedLayer >= 0 && selectedLayer < layers.size()
				&& (new Rectangle((int) viewport.getX(), (int) viewport.getY(),
						(int) (layers.get(selectedLayer).getImage().getWidth() * smallDim / comparison),
						(int) (layers.get(selectedLayer).getImage().getHeight() * smallDim / comparison)))
								.contains(amp)) {
			Point picPnt = new Point((int) ((amp.getX() - viewport.getX()) * comparison / smallDim),
					(int) ((amp.getY() - viewport.getY()) * comparison / smallDim));
			brushes.get(selectedBrush).draw(layers.get(selectedLayer).getImage(), picPnt);
		}
		settings.activeMouse(amp);
		if (viewport.contains(amp)) {
			drawOutline = true;
			brushShape = brushes.get(selectedBrush).bounds();
			for (int i = 0; i < brushShape.npoints; i++) {
				brushShape.xpoints[i] *= smallDim / comparison;
				brushShape.ypoints[i] *= smallDim / comparison;
				brushShape.xpoints[i] += amp.getX();
				brushShape.ypoints[i] += amp.getY();
			}
		}
	}

	public void renderImage() {
		RenderManager rm = new RenderManager(layers);
		Importer im = new Importer();
		im.save(rm.render());
	}

	public void open() {
		Importer im = new Importer();
		savePath = im.getPath();
		ArrayList<String> raw = TextIO.read(savePath);
		Layer tempLayer = null;
		int rowCounter = 0;
		boolean inImage = false;
		for (int i = 0; i < raw.size(); i++) {
			if (inImage) {
				if (raw.get(i).contains("end")) {
					layers.add(tempLayer);
					tempLayer = null;
					rowCounter = 0;
					inImage = false;
				} else {
					String[] row = seperateStringedArray(',', raw.get(i));
					for (int j = 0; j < row.length; j++) {
						tempLayer.getImage().setRGB(j, rowCounter, Integer.valueOf(row[j]));
					}
					rowCounter++;
				}

			}
			if (raw.get(i).contains("Layer")) {
				String[] size = seperateStringedArray(',', raw.get(i).substring(raw.get(i).indexOf('|') + 1));
				tempLayer = new Layer(
						raw.get(i).substring(raw.get(i).indexOf('"') + 1,
								raw.get(i).indexOf('"', raw.get(i).indexOf('"') + 1)),
						new Dimension(Integer.valueOf(size[0]), Integer.valueOf(size[1])));
				inImage = true;
			}
		}
		JOptionPane.showMessageDialog(null, "Successfully loaded project!");
	}

	public void save() {
		ArrayList<String> dat = new ArrayList<String>();
		for (int i = 0; i < layers.size(); i++) {
			dat.add("Layer:\"" + layers.get(i).getName().replaceAll(" ", "_") + "\"|"
					+ layers.get(i).getImage().getWidth() + "," + layers.get(i).getImage().getHeight());
			BufferedImage img = layers.get(i).getImage();
			for (int j = 0; j < img.getHeight(); j++) {
				String row = "";
				for (int k = 0; k < img.getWidth(); k++) {
					row += img.getRGB(k, j) + ",";
				}
				dat.add(row.substring(0, row.length() - 1));
			}
			dat.add("end");
		}
		if (savePath == null) {
			Importer im = new Importer();
			savePath = im.getPath() + ".proj";
			TextIO.write(savePath, dat);
		} else {
			TextIO.write(savePath, dat);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			ctrl = true;
			System.out.println("Control was pressed");
		}
		if (e.getKeyCode() == KeyEvent.VK_Z) {
			commandKeyPress[0] = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_S) {
			commandKeyPress[1] = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_O) {
			commandKeyPress[2] = true;
		}
		if (e.getKeyCode() == KeyEvent.VK_R) {
			commandKeyPress[3] = true;
		}
		if (ctrl && commandKeyPress[0]) { // ctrl + z

		}
		if (ctrl && commandKeyPress[1]) { // ctrl + s
			save();
		}
		if (ctrl && commandKeyPress[2]) { // ctrl + o
			open();
		}
		if (ctrl && commandKeyPress[3]) { // ctrl + r
			renderImage();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
			ctrl = false;
		}
		for (int i = 0; i < commandKeys.length; i++) {
			commandKeyPress[i] = false;
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if (viewport.contains(e.getPoint())) {
			drawing = true;
		} else {
			for (int i = minListViewInd; i < (int) ((3 * Game.HEIGHT / 4 - 20) / 30 + minListViewInd)
					&& i < layers.size(); i++) {
				if ((new Rectangle(7, 72 + (i - minListViewInd) * 30, Game.WIDTH / 5 - 39, 28))
						.contains(e.getPoint())) {
					selectedLayer = i;
				}
			}

			for (int i = minBrushViewInd; i < (int) ((3 * Game.HEIGHT / 4 - 20) / 12 + minListViewInd)
					&& i < brushes.size(); i++) {
				if ((new Rectangle(4 * Game.WIDTH / 5 + 7, 76 + (i) * 19, Game.WIDTH / 5 - 39, 14))
						.contains(e.getPoint())) {
					selectedBrush = i;
				}
			}
		}
		settings.mousePressed(e);
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		drawing = false;
		for (int i = 0; i < brushes.size(); i++) {
			brushes.get(i).lift();
		}
		settings.mouseReleased();
		// undo copies
		LinkedList<Layer> clone = new LinkedList<Layer>();
		layers.forEach(layer -> clone.add(layer));
		undoStack.add(clone);
	}

	@Override
	public void render(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		// image rendering of combined layers

		if (layers != null && !layers.isEmpty()) {

			for (int i = 0; i < layers.size(); i++) {
				smallDim = viewport.getWidth();
				comparison = layers.get(i).getImage().getWidth();
				if (layers.get(i).getImage().getHeight() * smallDim / comparison > viewport.getHeight()) {
					smallDim = (int) viewport.getHeight();
					comparison = layers.get(i).getImage().getHeight();
				}
				BufferedImage img = layers.get(i).getImage();
				g.drawImage((Image) img, (int) viewport.getX(), (int) viewport.getY(),
						(int) (img.getWidth() * smallDim / comparison), (int) (img.getHeight() * smallDim / comparison),
						null);
				g.setColor(Color.LIGHT_GRAY);
				g.drawRect((int) viewport.getX(), (int) viewport.getY(), (int) (img.getWidth() * smallDim / comparison),
						(int) (img.getHeight() * smallDim / comparison));
			}
		}
		g.setColor(Color.BLACK);
		g2d.draw(viewport);
		// layer list
		g.setColor(Color.BLACK);
		g.drawRect(5, 50, Game.WIDTH / 5 - 10, 3 * Game.HEIGHT / 4);
		g.drawLine(Game.WIDTH / 5 - 30, 50, Game.WIDTH / 5 - 30, 3 * Game.HEIGHT / 4 + 50);

		if (layers != null && !layers.isEmpty()) {
			for (int i = minListViewInd; i < (int) ((3 * Game.HEIGHT / 4 - 20) / 30 + minListViewInd)
					&& i < layers.size(); i++) {
				g.setColor(Color.BLACK);
				if (i == selectedLayer) {
					g.setColor(Color.LIGHT_GRAY);
				}
				g.drawRect(7, 72 + (i - minListViewInd) * 30, Game.WIDTH / 5 - 39, 28);
				g.drawRect(10, 74 + (i - minListViewInd) * 30, (Game.WIDTH / 5 - 39) / 4, 24);
				g.drawImage(layers.get(i).getImage(), 10, 74 + (i - minListViewInd) * 30, (Game.WIDTH / 5 - 39) / 4, 24,
						null);
				if (layers.get(i).isVisible()) {
					g.drawString(layers.get(i).getName(), 15 + (Game.WIDTH / 5 - 39) / 4,
							80 + (i - minListViewInd) * 30 + g.getFontMetrics().getHeight() / 2);
				} else {
					g.drawString(layers.get(i).getName() + " (Not shown)", 15 + (Game.WIDTH / 5 - 39) / 4,
							80 + (i - minListViewInd) * 30 + g.getFontMetrics().getHeight() / 2);
				}
			}
		}

		// brush palate
		g.setColor(Color.BLACK);
		g.drawRect(4 * Game.WIDTH / 5 + 5, 50, Game.WIDTH / 5 - 30, 3 * Game.HEIGHT / 4);
		for (int i = 0; i < brushes.size()
				&& i < (int) ((3 * Game.HEIGHT / 4 - 20) / (g.getFontMetrics().getHeight() + 2)
						+ minBrushViewInd); i++) {
			g.setColor(Color.BLACK);
			if (i == selectedBrush) {
				g.setColor(Color.LIGHT_GRAY);
			}
			g.drawString(brushes.get(i).getName(), 4 * Game.WIDTH / 5 + 7,
					72 + g.getFontMetrics().getHeight() + i * (g.getFontMetrics().getHeight() + 2));
		}
		g.setColor(Color.BLACK);
		g.drawRect(4 * Game.WIDTH / 5 + 5, 50, Game.WIDTH / 5 - 30, 20);
		g.drawString("Brush", 4 * Game.WIDTH / 5 + 7, 60 + g.getFontMetrics().getHeight() / 2);
		settings.render(g);

		if (drawOutline) {
			g.setColor(Color.BLACK);
			g2d.draw(brushShape);
		}
	}

	@Override
	public void setUp(Menu m) {
		layers = new LinkedList<Layer>();
		imageSize = new Dimension(1000, 1000);
		viewport = new Rectangle(100, 0, 700, 600);

		brushes = new LinkedList<Brush>();
		Brush.getBrushSet(brushes);

		settings = new SettingsPane(brushes);

		undoStack = new LinkedList<LinkedList<Layer>>();

		this.m = m;

		m.addButton(new Button(0, 0, 100, 50, 1, "+ add layer", 0, Color.BLACK, true));

		m.addButton(new Button(0, 0, 100, 100, 2, "/\\", 0, Color.BLACK, true));
		m.addButton(new Button(0, 0, 100, 100, 3, "\\/", 0, Color.BLACK, true));

		// layer functions
		m.addButton(new Button(5, 10, 100, 30, 4, "Remove Layer", 0, Color.BLACK, true));
		m.addButton(new Button(110, 10, 100, 30, 5, "Change Name", 0, Color.BLACK, true));
		m.addButton(new Button(215, 10, 100, 30, 6, "Move Up", 0, Color.BLACK, true));
		m.addButton(new Button(320, 10, 100, 30, 7, "Move Down", 0, Color.BLACK, true));
		m.addButton(new Button(425, 10, 100, 30, 8, "Toggle Visible", 0, Color.BLACK, true));

		m.addButton(new Button(0, 0, 100, 30, 9, "Render Image", 0, Color.BLACK, true));
		m.addButton(new Button(0, 0, 100, 30, 10, "Save Project", 0, Color.BLACK, true));
		m.addButton(new Button(0, 0, 100, 30, 11, "Open Project", 0, Color.BLACK, true));
		m.addButton(new Button(0, 0, 100, 30, 12, "Import image", 0, Color.BLACK, true));

		m.addButton(new Button(0, 0, 100, 30, 13, "Resize image", 0, Color.BLACK, true));
	}

	@Override
	public void tick() {
		viewport.setBounds(Game.WIDTH / 5, 50, 3 * Game.WIDTH / 5, 3 * Game.HEIGHT / 4);
		m.getButton(1, 0).setBounds(5, 50, Game.WIDTH / 5 - 35, 20);
		m.getButton(2, 0).setBounds(Game.WIDTH / 5 - 30, 50, 25, 20);
		m.getButton(3, 0).setBounds(Game.WIDTH / 5 - 30, 30 + 3 * Game.HEIGHT / 4, 25, 20);
		settings.tick();
		settings.setSelectedBrush(selectedBrush);

		m.getButton(9, 0).setBounds(Game.WIDTH - 125, 10, 100, 30);
		m.getButton(10, 0).setBounds(Game.WIDTH - 230, 10, 100, 30);
		m.getButton(11, 0).setBounds(Game.WIDTH - 335, 10, 100, 30);
		m.getButton(12, 0).setBounds(Game.WIDTH - 440, 10, 100, 30);
		m.getButton(13, 0).setBounds(Game.WIDTH - 545, 10, 100, 30);
	}

	public static String[] seperateStringedArray(char seperator, String str) {
		str = str + seperator;
		int arrayLength = 0;
		for (int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == seperator) {
				arrayLength++;
			}
		}
		String[] array = new String[arrayLength];
		for (int i = 0; i < array.length; i++) {
			array[i] = str.substring(0, str.indexOf(seperator));
			if (str.indexOf(seperator) >= 0) {
				str = str.substring(str.indexOf(seperator) + 1);
			}
		}
		return array;
	}

	public static void main(String[] args) {
		WindowSettings ws = new WindowSettings();
		ws.decoration(true);
		ws.maximized(false);
		ws.resizeable(false);
		ws.setSize(new Dimension(1080, 890));
		ws.setCloseOperation(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				e.getWindow().dispose();
				System.exit(0);
			}
		});
		Game.main("Paint", Color.GRAY, new Runner(), ws);
	}

}
