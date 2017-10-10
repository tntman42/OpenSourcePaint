package main;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

public class Layer {

	private BufferedImage image;
	private String name;

	private boolean visible = true;

	public Layer(String name, Dimension imageSize) {
		image = new BufferedImage((int) imageSize.getWidth(), (int) imageSize.getHeight(), BufferedImage.TYPE_INT_ARGB);
		this.name = name;
	}

	public BufferedImage getImage() {
		if (visible) {
			return image;
		} else {
			return new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
		}
	}

	public boolean isVisible() {
		return visible;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void setImage(BufferedImage image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
