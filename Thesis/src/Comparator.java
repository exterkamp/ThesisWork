import java.util.List;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.Scalar;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

public class Comparator {
	
	static public final int ORANGE_AND_BLUE = 0;
	static public final int DB_IMAGE_COMPARISON = 1;
	static public final int KEYPOINT_DETECT = 2;
	static public final int BACK_PROJECTION = 3;
	
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
		case BACK_PROJECTION:
				return back_proj(input);
		default:
				System.out.println("choice not found :(");
				return new boolBuff(false);
		}
	}
	
	static public boolBuff matchWithDBImages(BufferedImage input){
		return new boolBuff(false);
	}
	
	static public boolBuff back_proj(BufferedImage input){
		
		Mat ref = Highgui.imread("ref_petal.jpg");
		
		//Imgproc.cvtColor(ref,ref,Imgproc.COLOR_RGB2GRAY);
		Imgproc.cvtColor(ref,ref,Imgproc.COLOR_BGR2HSV);
		
		byte[] px = ((DataBufferByte) input.getRaster().getDataBuffer()).getData();
		Mat mHsv = new Mat(input.getHeight(),input.getWidth(),CvType.CV_8UC3);
		mHsv.put(0, 0, px);
		
		Imgproc.cvtColor(mHsv,mHsv,Imgproc.COLOR_BGR2HSV);
		
		List<Mat> matList = new ArrayList<Mat>();
		matList.add(ref);
		
		Mat hist = new Mat();
		int h_bins = 25;
		int s_bins = 25;
		
		MatOfInt mHistSize = new MatOfInt(h_bins,s_bins);
		
		MatOfFloat mRanges = new MatOfFloat(0,179,0,255);
		MatOfInt mChannels = new MatOfInt(0,1);
		
		
		
		
		
		
		boolean acc = false;
		
		Imgproc.calcHist(matList, mChannels, new Mat(), hist, mHistSize, mRanges);
		
		Core.normalize(hist, hist, 0, 255, Core.NORM_MINMAX, -1, new Mat());
		
		Mat backproj = new Mat();
	    Imgproc.calcBackProject(Arrays.asList(mHsv), mChannels, hist, backproj, mRanges, 1);
	    
	    int type;
		
		if(backproj.channels() == 1)
            type = BufferedImage.TYPE_BYTE_GRAY;
        else
            type = BufferedImage.TYPE_3BYTE_BGR;
		
		byte[] data = new byte[input.getWidth() * input.getHeight() * (int)backproj.elemSize()];
		backproj.get(0, 0, data);

	    BufferedImage newImg = new BufferedImage(input.getWidth(), input.getHeight(), type);
		
        newImg.getRaster().setDataElements(0, 0, input.getWidth(), input.getHeight(), data);
	    
        int avgB = 0;
        int avgG = 0;
        int avgR = 0;
        
        for(int i = 0; i < newImg.getHeight(); i++)
	    {
	        for(int j = 0; j < newImg.getWidth(); j++)
	        {
	        	int color = newImg.getRGB(j, i);
	        	avgB += color & 0xff;
	        	avgG += (color & 0xff00) >> 8;
	        	avgR += (color & 0xff0000) >> 16;
	        	
	            // do something with BGR values...
	        }
	    }
        
        int denom = newImg.getHeight() * newImg.getWidth();
        avgB /= denom;
        avgG /= denom;
        avgR /= denom;
        
        int val = 75;
        
        if (avgB > val || avgG > val || avgR > val){
        	return new boolBuff(true, newImg);
        }
        
	    return new boolBuff(false);
	}
	
	static public boolBuff detectKeypoints(BufferedImage input){
		Random rand = new Random();
		
		int minHessian = 400;
		
		byte[] px = ((DataBufferByte) input.getRaster().getDataBuffer()).getData();
		Mat mat = new Mat(input.getHeight(),input.getWidth(),CvType.CV_8UC3);
		mat.put(0, 0, px);
		
		Imgproc.cvtColor(mat,mat,Imgproc.COLOR_RGB2GRAY);
		
		MatOfKeyPoint keypoints_1 = new MatOfKeyPoint();
		
		dect.detect(mat, keypoints_1);
		
		KeyPoint[] keys = keypoints_1.toArray();
		
		if (keys.length < 2){// && keys.length <= 10){
		
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
