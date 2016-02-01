import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.Random;

import javax.imageio.ImageIO;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

public class Comparator {
	
	static public final int ORANGE_AND_BLUE = 0;
	static public final int DB_IMAGE_COMPARISON = 1;
	static public final int KEYPOINT_DETECT = 2;
	static FeatureDetector dect = FeatureDetector.create(FeatureDetector.SURF);
	static DescriptorExtractor desc =DescriptorExtractor.create(DescriptorExtractor.SURF);//SURF = 2
	
	static public boolBuff compare(BufferedImage input, int algorithm){
		switch(algorithm){
			case ORANGE_AND_BLUE:
				return findOrangeAndBlue(input);
		case DB_IMAGE_COMPARISON:
				return matchWithDBImages(input);
		case KEYPOINT_DETECT:
				return detectKeypoints(input);
		default:
				System.out.println("choice not found :(");
				return new boolBuff(false);
		}
	}
	
	static public boolBuff matchWithDBImages(BufferedImage input){
		return new boolBuff(false);
	}
	
	static public boolBuff detectKeypoints(BufferedImage input){
		Random rand = new Random();
		
		if (rand.nextFloat() > 0.75){
			
		int minHessian = 400;
		
		byte[] px = ((DataBufferByte) input.getRaster().getDataBuffer()).getData();
		Mat mat = new Mat(input.getHeight(),input.getWidth(),CvType.CV_8UC3);
		mat.put(0, 0, px);
		
		Imgproc.cvtColor(mat,mat,Imgproc.COLOR_RGB2GRAY);
		
		MatOfKeyPoint keypoints_1 = new MatOfKeyPoint();
		
		dect.detect(mat, keypoints_1);
		
		Features2d.drawKeypoints(mat, keypoints_1, mat);
		
		
		
		Mat descriptors = new Mat();
		
		desc.compute(mat,keypoints_1, descriptors);
		
		int type;
		
		if(mat.channels() == 1)
            type = BufferedImage.TYPE_BYTE_GRAY;
        else
            type = BufferedImage.TYPE_3BYTE_BGR;
		
		byte[] data = new byte[input.getWidth() * input.getHeight() * (int)mat.elemSize()];
		mat.get(0, 0, data);
		
        BufferedImage newImg = new BufferedImage(input.getWidth(), input.getHeight(), type);

        newImg.getRaster().setDataElements(0, 0, input.getWidth(), input.getHeight(), data);
		
        
        //input = newImg;
        System.out.println("return with new image");
        	return new boolBuff(true, newImg);
		}else{
			return new boolBuff(false);
		}
	}
	
	static public boolBuff findOrangeAndBlue(BufferedImage input){
		
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
			
			return new boolBuff(true);
		}
		
		return new boolBuff(false);
		
	}
	
}
