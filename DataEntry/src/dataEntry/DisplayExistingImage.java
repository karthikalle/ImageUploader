/*package dataEntry;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.BatchPutAttributesRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;
import com.amazonaws.services.simpledb.model.SelectRequest;


public class DisplayExistingImage extends JFrame {

	private JPanel contentPane;
	public JFrame frame;
	public JFrame imageFrame;
	public JTextField imageName;
    private static AmazonSimpleDB sdb;
    private JLabel lblUploadWordsInto;
    private JLabel lblWord;
    private JLabel lblSelectImage;
    private JLabel lblFrequency;
    private JLabel lblCategory;
    private JLabel jlblimage;
    private JLabel lblImageability;
    private JButton lblOrEnterNew;
    public JButton fileChooser;
    private JButton btnUploadImage;
    private ArrayList<String> categories;
    private JComboBox<String> categoryList;
    private JComboBox<String> frequencyList;
    private JComboBox<String> imageabilityList;

    public String word;
    public String imageability;
    public String frequency;
    public String url;
    public String categoryName;

    private int IMG_HEIGHT;
    private int IMG_WIDTH;
    private int choice;

    public boolean showDialogs;
    public JTextField category;
    public static JLabel flagImage;
    public FileChooser fcp;
    

	*//**
	 * Launch the application.
	 *//*
	public void show() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DisplayExistingImage frame = new DisplayExistingImage();
					frame.setVisible(true);
					showImageForReview(item);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	*//**
	 * Create the frame.
	 *//*
	public DisplayExistingImage() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}
	
	public void showImageForReview(Item item) {
		imageFrame = new JFrame();
		imageFrame.setBounds(50, 50, 600, 400);
		imageFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		imageFrame.setAlwaysOnTop(true);
		imageFrame.setLayout(null);
		
		BufferedImage bi;
		try {
			
			JLabel heading = new JLabel("Image with the following properties already exists in the database");
			heading.setFont(new Font("Serif", Font.BOLD, 18));
			heading.setBounds(25, 15, 550, 50);
			imageFrame.add(heading);
			
			JLabel imageNameLabel = new JLabel("Image Name:");
			imageNameLabel.setBounds(63, 86, 161, 36);
			imageNameLabel.setFont(new Font("Arial", Font.BOLD, 13));
			imageFrame.getContentPane().add(imageNameLabel);
			
			JLabel imageName = new JLabel(word);
			imageName.setBounds(165, 86, 161, 36);
			imageFrame.getContentPane().add(imageName);
			
			System.out.println("Category "+getAttributeValue(item, "Category"));
			
			JLabel categoryLabel = new JLabel("Category:");
			categoryLabel.setBounds(63, 106, 161, 36);
			categoryLabel.setFont(new Font("Arial", Font.BOLD, 13));
			imageFrame.getContentPane().add(categoryLabel);
			
			JLabel category = new JLabel(getAttributeValue(item, "Category"));
			category.setBounds(165, 106, 161, 36);
			imageFrame.getContentPane().add(category);
			
			System.out.println("Frequency: "+getAttributeValue(item, "Frequency"));

			JLabel freqLabel = new JLabel("Image Name:");
			freqLabel.setBounds(63, 126, 161, 36);
			freqLabel.setFont(new Font("Arial", Font.BOLD, 13));
			imageFrame.getContentPane().add(freqLabel);
			
			JLabel frequency = new JLabel(getAttributeValue(item, "Frequency"));
			frequency.setBounds(165, 126, 161, 36);
			imageFrame.getContentPane().add(frequency);
			
			JLabel imageabilityLabel = new JLabel("Image Name:");
			imageabilityLabel.setBounds(63, 146, 161, 36);
			imageabilityLabel.setFont(new Font("Arial", Font.BOLD, 13));
			imageFrame.getContentPane().add(imageabilityLabel);
			
			JLabel imageability = new JLabel(getAttributeValue(item, "Imageability"));
			imageability.setBounds(165, 146, 161, 36);
			imageFrame.getContentPane().add(imageability);
			
	System.out.println("URL Value:"+getAttributeValue(item,"URL"));
			
			URL imageURL = new URL(getAttributeValue(item,"URL"));
			URLConnection connection = imageURL.openConnection();
			connection.setRequestProperty("User-Agent", "xxxxxx");
			bi = ImageIO.read(imageURL);
			ImageIcon icon = new ImageIcon(bi.getScaledInstance(200, 200, Image.SCALE_SMOOTH));
			jlblimage.setIcon(icon);
			jlblimage.setBounds(300, 75, 200, 200);
			imageFrame.add(jlblimage);
			
			JButton replaceButton = new JButton("Replace Original");
			replaceButton.setBounds(15, 206, 140, 25);
			imageFrame.getContentPane().add(replaceButton);
			
			JButton useExistingButton = new JButton("Use Existing");
			useExistingButton.setBounds(150, 206, 140, 25);
			imageFrame.getContentPane().add(useExistingButton);
			
			imageFrame.setVisible(true);

			replaceButton.addActionListener(new ActionListener (){
				public void actionPerformed(ActionEvent e) {
					choice = 1;
					imageFrame.dispose();	
				}	
			});
			
			useExistingButton.addActionListener(new ActionListener (){
				public void actionPerformed(ActionEvent e) {
					choice = 0;
					imageFrame.dispose();			
				}	
			});
			
		}
		catch (Exception e) {
			System.out.println("here "+e);
		}
						
	}

	private String getAttributeValue(Item item, String attribute) {
		List<Attribute> attributes = item.getAttributes();
		
		for(int i=0; i<attributes.size(); i++) {
	//		System.out.println("\n"+attributes.get(i).getName()+"\n");
			if(attributes.get(i).getName().equals(attribute))
					return attributes.get(i).getValue();
		}
		return null;
	}

}
*/