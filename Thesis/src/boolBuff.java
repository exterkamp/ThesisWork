import java.awt.image.BufferedImage;


public class boolBuff {
	
	private boolean returnBoolean;
	private BufferedImage returnImage;
	
	boolBuff(boolean b, BufferedImage img){
		returnBoolean = b;
		returnImage = img;
	}
	
	boolBuff(boolean b){
		returnBoolean = b;
		returnImage = null;
	}
	
	public boolean returnBool(){
		return returnBoolean;
	}
	
	public BufferedImage returnImage(){
		return returnImage;
	}

}
