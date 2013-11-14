import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
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

import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class GUI_DataEntry {

	private JFrame frame;
	private JTextField imageName;
    private static AmazonSimpleDB sdb;
    private JComboBox frequencyList;
    private JLabel lblUploadWordsInto;
    private JComboBox imageabilityList;
    private JComboBox categoryList;
    private JLabel lblWord;
    private JLabel lblSelectImage;
    private JLabel lblFrequency;
    private JLabel lblCategory;
    private JLabel jlblimage;
    private JLabel lblImageability;
    private JButton fileChooser;
    private JButton btnUploadImage;
    private ArrayList<String> categories;
    private JTextField category;
    public static JLabel flagImage;
    private FileChooser fcp;
    public String url;
    private String categoryVal;
    private int flag;
    private int IMG_HEIGHT;
    private int IMG_WIDTH;
    
    public static File file;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI_DataEntry window = new GUI_DataEntry();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI_DataEntry() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	@SuppressWarnings({ "rawtypes", "unchecked", "rawtypes", "unchecked" })
	private void initialize() {
		flag = 0;
        try {
			sdb = new AmazonSimpleDBClient(new PropertiesCredentials(
			        GUI_DataEntry.class.getResourceAsStream("AwsCredentials.properties")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        
        IMG_WIDTH = 600;
        IMG_HEIGHT = 600;
        
		frame = new JFrame();
		frame.setBounds(10, 0, 462, 350);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		lblUploadWordsInto = new JLabel("Upload Words Into MossTalk");
		lblUploadWordsInto.setBounds(135, 5, 180, 16);
		lblUploadWordsInto.setHorizontalAlignment(SwingConstants.CENTER);
		frame.getContentPane().add(lblUploadWordsInto);
		
		lblWord = new JLabel("Word");
		lblWord.setBounds(63, 46, 61, 16);
		frame.getContentPane().add(lblWord);
		
		lblSelectImage = new JLabel("Select Image");
		lblSelectImage.setBounds(63, 89, 86, 16);
		frame.getContentPane().add(lblSelectImage);
		
		fileChooser = new JButton("Browse..");
		flagImage = new JLabel("");
		flagImage.setVisible(false);
		
		flagImage.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
            	System.out.println(evt.getPropertyName());
                if (evt.getPropertyName().equals("text")) {
                    showImage(fcp.getFile());
                }
            }
        });
		
		fileChooser.setBounds(161, 84, 86, 28);
		frame.getContentPane().add(fileChooser);
		
		fileChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					flag = 1;
					FileAndPathOnly fpo = new FileAndPathOnly();
					fcp = new FileChooser(fpo, IMG_WIDTH, IMG_HEIGHT);
				//	flagImage.setText("Changed");
		        }
		}
		);
		
		lblCategory = new JLabel("Category");
		lblCategory.setBounds(63, 130, 61, 16);
		frame.getContentPane().add(lblCategory);
		
		lblFrequency = new JLabel("Frequency");
		lblFrequency.setBounds(63, 206, 86, 16);
		frame.getContentPane().add(lblFrequency);
		
		lblImageability = new JLabel("Imageability");
		lblImageability.setBounds(63, 247, 97, 16);
		frame.getContentPane().add(lblImageability);
		
		jlblimage = new JLabel();
		jlblimage.setBounds(320, 24, 225, 178);
		frame.getContentPane().add(jlblimage);
		
		categoryList = new JComboBox();
        String selectExpression = "select Category from `" + "mossWords" + "`";
        System.out.println("Selecting: " + selectExpression + "\n");
        SelectRequest selectRequest = new SelectRequest(selectExpression);

        categories=new ArrayList<String>();
        categoryList.addItem("");
        for(Item item: sdb.select(selectRequest).getItems()) {
        	for(Attribute attribute: item.getAttributes()) {
        		if(attribute.getName().equals("Category"))
        			if(!categories.contains(attribute.getValue())) {
        				categories.add(attribute.getValue());
        				categoryList.addItem(attribute.getValue());
        			}
        	}
        }
        
        categoryList.setSelectedIndex(0);
		categoryList.setBounds(161, 124, 160, 30);
		frame.getContentPane().add(categoryList);  
		

		
		category = new JTextField();
		category.setBounds(161, 160, 154, 28);
		category.setText("");
		frame.getContentPane().add(category);
		category.setColumns(10);
		
		imageName = new JTextField();
		imageName.setBounds(163, 40, 146, 28);
		frame.getContentPane().add(imageName);
		imageName.setColumns(10);
		
		frequencyList = new JComboBox();
		frequencyList.setBounds(161, 200, 160, 30);
		for(int i=1; i<=10; i++)
			frequencyList.addItem(i);
		frame.getContentPane().add(frequencyList);
		
		imageabilityList = new JComboBox();
		imageabilityList.setBounds(161, 241, 160, 30);
		for(int i=1; i<=10; i++)
			imageabilityList.addItem(i);
		frame.getContentPane().add(imageabilityList);
		
		btnUploadImage = new JButton("Upload Image");
        btnUploadImage.setBounds(116, 283, 117, 29);
		frame.getContentPane().add(btnUploadImage);
		
		JLabel lblOrEnterNew = new JLabel("Or Enter New Category");
		lblOrEnterNew.setBounds(6, 146, 209, 56);
		frame.getContentPane().add(lblOrEnterNew);
		
		
		if(flag==1)
			//showImage(fcp.getFile());
			showImage(file);
		
		btnUploadImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = imageName.getText();
				String frequency = Integer.toString(frequencyList.getSelectedIndex()+1);
				String imageability = Integer.toString(imageabilityList.getSelectedIndex()+1);
				
				if(word=="")
					JOptionPane.showMessageDialog(frame, "Please Enter Image Name");

				String selectExpression = "select * from `" + "mossWords" + "` where itemName() = '"+word+"'";
		        SelectRequest selectRequest = new SelectRequest(selectExpression);

		        for(Item item: sdb.select(selectRequest).getItems()) {
		        	if(item.getName().equals(word))
		    			JOptionPane.showMessageDialog(frame, "Image Name already exists");		
		        }
			
				int index  = categoryList.getSelectedIndex();
				categoryVal = category.getText();
				if(index!=0) {
					categoryVal = (String) categoryList.getItemAt(index);
				}
				else if (categoryVal.equals(""))
					JOptionPane.showMessageDialog(null, "Please Enter a category");
				
				
	        	ArrayList<ReplaceableItem> data = new ArrayList<ReplaceableItem>();
	        	
	        	System.out.println(word + frequency + imageability + categoryVal);
	        	
		        	data.add(new ReplaceableItem(word).withAttributes(
		   			 new ReplaceableAttribute("Category", categoryVal, true),
			               new ReplaceableAttribute("Length", Integer.toString(word.length()), true),
			               new ReplaceableAttribute("frequency", frequency, true),
			               new ReplaceableAttribute("Imageability", imageability, true),
			               new ReplaceableAttribute("url", uploadImageToS3(), true)));
					
		        	if(categoryVal==""||word=="")
						JOptionPane.showMessageDialog(null, "Please Enter a category");
					sdb.batchPutAttributes(new BatchPutAttributesRequest("mossWords",data));
					
				//	showImage(fcp.getFile());
					restart();
					
			}
				
		}
		);
		        

	}

	public void showImage(File file) {
		
		BufferedImage bi;
		try {
			JFrame uploadCompletedFrame = new JFrame();
	//		uploadCompletedFrame.setAlwaysOnTop(true);
	//		uploadCompletedFrame.setVisible(true);
			System.out.println(file);
			bi = ImageIO.read(file);
			ImageIcon icon = new ImageIcon(bi.getScaledInstance(200, 200, Image.SCALE_SMOOTH));
			jlblimage.setIcon(icon);
		//	uploadCompletedFrame.setBounds(220,220,220,220);
			frame.setBounds(10, 0, frame.getWidth() + 200, frame.getHeight());
			jlblimage.setBounds(340, 60, 200, 200);
			frame.getContentPane().add(jlblimage);
		}
		catch (Exception e) {
			System.out.println("here "+e);
		}		
	}

	protected String uploadImageToS3() {
		String existingBucketName = "mosswords";
		String keyName = imageName.getText()+".jpg";
		if(fcp==null)
			JOptionPane.showMessageDialog(frame, "Please choose an image");
		
		String filePath = fcp.getFilePath();			

		String amazonFileUploadLocationOriginal = existingBucketName+"/"+categoryVal;

		System.out.println(existingBucketName);
		System.out.println(categoryVal);
		
		AmazonS3 s3Client;
		try {
			s3Client = new AmazonS3Client(new PropertiesCredentials(
			            GUI_DataEntry.class.getResourceAsStream("AwsCredentials.properties")));

		  FileInputStream stream = new FileInputStream(filePath);
		  ObjectMetadata objectMetadata = new ObjectMetadata();
		  
		  if(filePath==null||filePath=="")
				JOptionPane.showMessageDialog(frame, "Please choose an image");
		  
		  objectMetadata.setContentType("image/jpeg");
		  System.out.println("ObjectMetadata content type: "+objectMetadata.getContentType());
		  PutObjectRequest putObjectRequest = new PutObjectRequest(amazonFileUploadLocationOriginal, keyName, stream, objectMetadata);
		  putObjectRequest.setInputStream(stream);
		  PutObjectResult result = s3Client.putObject(putObjectRequest);
		  System.out.println("Etag:" + result.getETag() + "-->" + result);
		  
		}
		catch (IOException e) {
				restart();
		}
		 url="https://s3.amazonaws.com/"+amazonFileUploadLocationOriginal+"/"+keyName;
		 System.out.println("KeyName: "+keyName);
		 System.out.println("filePath: "+filePath);
		 System.out.println("amazonFileUploadLocationOriginal: "+amazonFileUploadLocationOriginal);
		 System.out.println("URL: "+url);
			
		return url;
	}
	
	public void restart() {
		imageName.setText("");
		category.setText("");
		categoryList.setSelectedIndex(0);
		frequencyList.setSelectedIndex(0);
		imageabilityList.setSelectedIndex(0);
		return;
	}
}
