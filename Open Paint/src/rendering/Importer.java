package rendering;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

public class Importer {
	private JFileChooser jfc;
	
	public Importer(){
		jfc = new JFileChooser();
		
	}
	
	public String getPath(){
		JFrame frame = new JFrame("NOTHING");
		frame.setVisible(false);
		
		int ret = jfc.showOpenDialog(frame);
		
		if (ret == JFileChooser.APPROVE_OPTION){
			return jfc.getSelectedFile().getPath();
		}
		else{
			return null;
		}
	}
	
	public void save(Image i){
		String path = "";
		
		JFrame frame = new JFrame("NOTHING");
		frame.setVisible(false);
		
		int ret = jfc.showSaveDialog(frame);
		if (ret == JFileChooser.APPROVE_OPTION){
			path = jfc.getSelectedFile().getPath();
		}
		if (path.indexOf(".png") < 0){
			path = path + ".png";
		}
		System.out.println("Writing to path: " + path);
		try{
			BufferedImage bi;
	    	if (i instanceof BufferedImage)
	    	{
	    		bi = (BufferedImage) i;
	    	}
	    	bi = new BufferedImage(i.getWidth(null), i.getHeight(null), BufferedImage.TYPE_INT_ARGB);
	    	Graphics2D bGr = bi.createGraphics();
	    	bGr.drawImage(i, 0, 0, null);
	    	bGr.dispose();
			File fil = new File(path);
			ImageIO.write(bi, "png", fil);
		}catch(IOException e){
			System.err.println("Could not save file: " + path);
		}
	}
}
