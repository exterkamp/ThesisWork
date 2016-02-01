import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.imgproc.Imgproc;

public class Comparator {
	
	static public final int ORANGE_AND_BLUE = 0;
	static public final int DB_IMAGE_COMPARISON = 1;
	
	
	static public boolean compare(BufferedImage input, int algorithm){
		switch(algorithm){
			case ORANGE_AND_BLUE:
				return findOrangeAndBlue(input);
		case DB_IMAGE_COMPARISON:
				return matchWithDBImages(input);
		default:
				System.out.println("choice not found :(");
				return false;
		}
	}
	
	static public boolean matchWithDBImages(BufferedImage input){
		return false;
	}
	
	static public boolean detectKeypoints(BufferedImage input){
		
		
		
		return false;
	}
	
	static public boolean findOrangeAndBlue(BufferedImage input){
		
		int averageR = 0;
		int averageG = 0;
		int averageB = 0;
		int totalPixels = 0;
		for (int x = 0; x < input.getWidth(); x++){
			for (int y = 0; y < input.getHeight();y++){
				int argb = input.getRGB(x, y);
				averageR += 0xFF & ( argb >> 16);
				averageG += 0xFF & (argb >> 8 );
				averageB += 0xFF & (argb >> 0 );
				
				totalPixels++;
			}
		}
		averageR /= totalPixels;
		averageG /= totalPixels;
		averageB /= totalPixels;
		//averageR == 229 && averageG == 103 && averageB == 37
		//averageR == 38 && averageG == 53 && averageB == 115
		if ((averageR > 150 && averageR < 250 &&
				averageG > 50 && averageG < 150 &&
				averageB > 0 && averageB < 100) 
				||
			((averageR > 0 && averageR < 100 &&
					averageG > 25 && averageG < 100 &&
					averageB > 50 && averageB < 175) )){
			
			//System.out.println("R: " + averageR + " G: " + averageG + " B: " + averageB);
			
			return true;
		}
		
		return false;
		
	}
	

}
