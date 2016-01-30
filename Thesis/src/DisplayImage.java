/*import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
import javax.swing.*;

public class DisplayImage {
	private static final int WIDTH = 400;
	private static final int HEIGHT = 400;
	public static void main( String[] args){
		JFrame frame = new ImageFrame(WIDTH,HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}

class ImageFrame extends JFrame{
	private final JFileChooser chooser;
	private BufferedImage image = null;
	
	public ImageFrame(int width, int height){
		this.setTitle("Exterkamp Thesis");	
		this.setSize(width,height);
		addMenu();		
		chooser = new JFileChooser();		
		chooser.setCurrentDirectory(new File("."));		
	}
	
	private void addMenu(){
		
		JMenu fileMenu = new JMenu("File");		
		JMenuItem openItem = new JMenuItem("Open");	
		
		openItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				open();			
			}		
		});
		
		fileMenu.add(openItem);
		
		JMenuItem exitItem = new JMenuItem("Exit");
		
		exitItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){			
				System.exit(0);			
			}		
		});
		
		fileMenu.add(exitItem);		
		JMenuBar menuBar = new JMenuBar();		
		menuBar.add(fileMenu);		
		this.setJMenuBar(menuBar);		
	}
	
	private void open(){
	
		File file = getFile();
		if (file != null){		
			displayFile( file);		
		}	
	}
	
	private File getFile(){
	
		File file = null;		
		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
			file = chooser.getSelectedFile();
		}
		return file;	
	}
	
	private void displayFile(File file){
	
		try{
			displayBufferedImage(ImageIO.read(file));		
		}
		
		catch (IOException exception){		
			JOptionPane.showMessageDialog(this, exception);		
		}	
	}
	
	public void displayBufferedImage(BufferedImage image){
	
		this.setContentPane(new JScrollPane (new JLabel(new ImageIcon(image))));	
		this.validate();	
	}

}*/