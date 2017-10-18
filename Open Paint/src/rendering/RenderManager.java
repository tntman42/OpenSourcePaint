package rendering;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.LinkedList;

import main.Layer;

public class RenderManager {

	private LinkedList<Layer> layers;

	public RenderManager(LinkedList<Layer> layers) {
		this.layers = layers;
	}

	public BufferedImage render(Dimension size) {
		BufferedImage rendered = new BufferedImage((int) size.getWidth(), (int) size.getHeight(),
				BufferedImage.TYPE_INT_ARGB);

		Graphics g = rendered.getGraphics();
		for (int i = 0; i < layers.size(); i++) {

			double smallDim = size.getWidth();
			double comparison = layers.get(i).getImage().getWidth();
			if (layers.get(i).getImage().getHeight() * smallDim / comparison > size.getHeight()) {
				smallDim = (int) size.getHeight();
				comparison = layers.get(i).getImage().getHeight();
			}
			g.drawImage(layers.get(i).getImage(), 0, 0,
					(int) (layers.get(i).getImage().getWidth() * smallDim / comparison),
					(int) (layers.get(i).getImage().getHeight() * smallDim / comparison), null);
		}

		return rendered;
	}

}
