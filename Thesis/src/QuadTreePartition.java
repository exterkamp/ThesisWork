import java.awt.image.BufferedImage;

public class QuadTreePartition {
	
	static private BufferedImage image;
	private Section root;
	static private int IMAGE_HEIGHT;
	
	public QuadTreePartition(BufferedImage image){
		this.image = image;
		root = new Section(0,image.getHeight(),image.getWidth(),0,image);
		IMAGE_HEIGHT = image.getHeight();
	}
	
	public void partition(){
		root.populateChildren();
		
	}
	
	static public int getHeight(){
		return IMAGE_HEIGHT;
	}
	
	static public BufferedImage getImage(){
		return image;
	}
	
	
}
