import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Section {
	
	static private int MINIMUM_WIDTH_OR_HEIGHT = 4;//in pixels
	
	private Point topLeft;
	private Point bottomRight;
	private BufferedImage image;
	private boolean leaf;
	
	private Section NorthWest;
	private Section NorthEast;
	private Section SouthWest;
	private Section SouthEast;
	//private Section parent;
	
	static int iter = 0;
	
	public Section (Point topLeft, Point bottomRight, BufferedImage image){
		this.topLeft = topLeft;
		this.bottomRight = bottomRight;
		this.image = imageConvert(image);
		//this.image = image;
		//printCoors();
		//colorSection();
		
		
	}
	
	public Section (int x1, int y1, int x2, int y2, BufferedImage image){
		this.topLeft = new Point(x1,y1);
		this.bottomRight = new Point(x2,y2);
		this.image = imageConvert(image);
		//this.image = image;
		//printCoors();
		//colorSection();
	}
	
	private BufferedImage imageConvert(BufferedImage bufImg){
		//System.out.println("Section made @: " + topLeft.x + " , " + topLeft.y + " : " + bottomRight.x + " , " + bottomRight.y);
		//BufferedImage bufImg = ImageIO.read( imageURL );
	    BufferedImage convertedImg = new BufferedImage(bufImg.getWidth(), bufImg.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
	    convertedImg.getGraphics().drawImage(bufImg, 0, 0, null);
	    return convertedImg;
	}
	
	public boolean populateChildren(){
		//check that widths are > minimum
		if ((bottomRight.x - topLeft.x) > MINIMUM_WIDTH_OR_HEIGHT &&
				(topLeft.y - bottomRight.y) > MINIMUM_WIDTH_OR_HEIGHT){
			//run comparison operations!
			
			boolBuff bool = Comparator.compare(image, Comparator.BACK_PROJECTION);
			
			boolean found = bool.returnBool();
			
			
			if (!found){
				//no pattern found, keep searching
				leaf = false;
				//middle
				Point middle = new Point((bottomRight.x - topLeft.x)/2 + topLeft.x,(topLeft.y - bottomRight.y)/2 + bottomRight.y);
				
				
				
				NorthWest = new Section(topLeft,middle,split(topLeft,middle));
				
				
				//System.out.println("Height of NW image: " + NorthWest.getImage().getHeight());
				NorthWest.populateChildren();
				
				NorthEast = new Section(new Point(middle.x,topLeft.y),new Point(bottomRight.x,middle.y)
						, split(new Point(middle.x,topLeft.y),new Point(bottomRight.x,middle.y)));
				
				NorthEast.populateChildren();
				
				SouthWest = new Section(new Point(topLeft.x,middle.y),new Point(middle.x,bottomRight.y)
						, split(new Point(topLeft.x,middle.y),new Point(middle.x,bottomRight.y)));
				
				SouthWest.populateChildren();
				
				SouthEast = new Section(middle,bottomRight,split(middle,bottomRight));
				
				SouthEast.populateChildren();
				//colorSection(Color.BLUE);
				return true;
			}else{
				//found a pattern!
				leaf = true;
				
				//System.out.println("Pattern @: " + topLeft.x + " , " + topLeft.y + " : " + bottomRight.x + " , " + bottomRight.y);
				
				//apply changes from detection
				if (bool.returnImage() != null){
					
					
					image = bool.returnImage();
					
					
					BufferedImage master = QuadTreePartition.getImage();
					
					Graphics2D g2d = master.createGraphics();
					//System.out.println("setting new image");
					g2d.drawImage(image, topLeft.x, master.getHeight() - topLeft.y, null);
					colorSection(Color.BLUE);
				}else{
					colorSection(Color.RED);
				}
				
				
				
				return true;
			}
		}else{
			//System.out.println("Leaf @: " + topLeft.x + " , " + topLeft.y + " : " + bottomRight.x + " , " + bottomRight.y);
			//colorSection(Color.BLACK);
			leaf = true;
			return false;
		}
	}
	
	public boolean isLeaf(){
		return leaf;
	}
	
	private BufferedImage split(Point upperLeft, Point lowerRight){
		//System.out.println("Split @: " + upperLeft.x + " , " + upperLeft.y + " : " + lowerRight.x + " , " + lowerRight.y);
		
		int height = upperLeft.y - lowerRight.y;
		int imageUpperLeftY = QuadTreePartition.getHeight() - upperLeft.y;
		//System.out.println("x: " + upperLeft.x);
		//System.out.println("y: " + imageUpperLeftY);
		//System.out.println("height: " + (upperLeft.y - lowerRight.y));
		//System.out.println("width: " + (lowerRight.x - upperLeft.x));
    	return QuadTreePartition.getImage().getSubimage(upperLeft.x, imageUpperLeftY, 
    			lowerRight.x - upperLeft.x, upperLeft.y - lowerRight.y);
    }
	
	public BufferedImage getImage(){
		return image;
	}
	
	private void colorSection(Color color){
		int imageUpperLeftY = QuadTreePartition.getHeight() - topLeft.y;
		
		Graphics2D g2d = (Graphics2D) QuadTreePartition.getImage().getGraphics();
		g2d.setColor(color);
		g2d.drawRect(topLeft.x, imageUpperLeftY, bottomRight.x - topLeft.x, topLeft.y - bottomRight.y);
		//g2d.drawRect(x, y, width, height);
		//QuadTreePartition.getImage().flush();
		//QuadTreePartition.getImage().notify();
	}
	

}
