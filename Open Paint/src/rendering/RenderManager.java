package rendering;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import main.Layer;

public class RenderManager {

	private LinkedList<Layer> layers;

	public RenderManager(LinkedList<Layer> layers) {
		this.layers = layers;
	}

	public BufferedImage render() {
		BufferedImage rendered = new BufferedImage(layers.get(0).getImage().getWidth(),
				layers.get(0).getImage().getHeight(), BufferedImage.TYPE_INT_ARGB);
		
		Graphics g = rendered.getGraphics();
		for (int i = 0;i < layers.size();i++) {
			g.drawImage(layers.get(i).getImage(), 0, 0, null);
		}
		
		return rendered;
	}

}
