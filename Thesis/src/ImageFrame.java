import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.imgproc.Imgproc;

import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.JLayeredPane;
import javax.swing.BoxLayout;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Component;


public class ImageFrame extends JFrame {

	private JPanel contentPane;
	
	private final JFileChooser chooser;
	private BufferedImage image = null;
	private QuadTreePartition partition = null;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//OpenCV starter stuff
		configureOpenCV();
		
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ImageFrame frame = new ImageFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ImageFrame() {
		
		
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 500, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
		
		this.setTitle("Exterkamp Thesis");	
		//this.setSize(width,height);
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
		
		JMenu imageMenu = new JMenu("Image");	
		JMenuItem qItem = new JMenuItem("QuadTree");
		
		qItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if (image != null){
					partition = new QuadTreePartition(image);
					partition.partition();
					displayBufferedImage(QuadTreePartition.getImage());
					
					
				}
				
				
				
			}		
		});
		
		imageMenu.add(qItem);
		
		JMenuItem biItem = new JMenuItem("Bilinear Blur 75%");
		
		biItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if (image != null){
					image = scaleDown50(image);
					displayBufferedImage(image);
					
				}
				
				
				
			}		
		});
		
		imageMenu.add(biItem);
		
		JMenuItem keyItem = new JMenuItem("Keypoint Looker");
		
		keyItem.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent event){
				if (image != null){
					image = keypointFinder(image);
					displayBufferedImage(image);
				}
				
				
				
			}		
		});
		
		imageMenu.add(keyItem);
		
		fileMenu.add(exitItem);		
		JMenuBar menuBar = new JMenuBar();		
		menuBar.add(fileMenu);	
		menuBar.add(imageMenu);
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
		this.image = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
		this.image.getGraphics().drawImage(image,0,0,null);
		this.setContentPane(new JScrollPane (new JLabel(new ImageIcon(image))));	
		this.validate();	
	}
	
	private static void configureOpenCV(){
		System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	    //Mat mat = Mat.eye( 3, 3, CvType.CV_8UC1 );
	    //System.out.println( "mat = " + mat.dump() );
	}
	
	private BufferedImage scaleDown50(BufferedImage input){
		int width = input.getWidth()/4;
		int height =  input.getHeight()/4;
		BufferedImage newImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = newImage.createGraphics();
	    try {
	        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	        g.setBackground(Color.WHITE);
	        g.clearRect(0, 0, width, height);
	        g.drawImage(input, 0, 0, width, height, null);
	    } finally {
	        g.dispose();
	    }
		
		
		return scaleUp50(newImage);
	}
	
	private BufferedImage scaleUp50(BufferedImage input){
		int width = input.getWidth()*4;
		int height =  input.getHeight()*4;
		BufferedImage newImage = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
	    Graphics2D g = newImage.createGraphics();
	    try {
	        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	        g.setBackground(Color.WHITE);
	        g.clearRect(0, 0, width, height);
	        g.drawImage(input, 0, 0, width, height, null);
	    } finally {
	        g.dispose();
	    }
	    return newImage;
	}
	
	private BufferedImage keypointFinder(BufferedImage input){
		
		int minHessian = 400;
		FeatureDetector dect = FeatureDetector.create(FeatureDetector.SURF);
		byte[] px = ((DataBufferByte) input.getRaster().getDataBuffer()).getData();
		Mat mat = new Mat(input.getHeight(),input.getWidth(),CvType.CV_8UC3);
		mat.put(0, 0, px);
		
		Imgproc.cvtColor(mat,mat,Imgproc.COLOR_RGB2GRAY);
		
		MatOfKeyPoint keypoints_1 = new MatOfKeyPoint();
		
		dect.detect(mat, keypoints_1);
		
		Features2d.drawKeypoints(mat, keypoints_1, mat);
		
		DescriptorExtractor desc =DescriptorExtractor.create(DescriptorExtractor.SURF);//SURF = 2
		
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
        
        return newImg;
		
	}

}
