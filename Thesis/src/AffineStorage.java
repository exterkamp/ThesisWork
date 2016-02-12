import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;

public class AffineStorage {
	
	private Point2D.Double point;
	private AffineTransform tx;
	private int imageIndex;
	
	public AffineStorage(Point2D pt, AffineTransform atx, int index){
		point = (Double) pt;
		tx = atx;
		imageIndex = index;
	}
	
	public Point2D.Double getPoint(){
		return point;
	}
	
	public AffineTransform getTransform(){
		return tx;
	}
	
	public int getIndex(){
		return imageIndex;
	}

}
