import java.awt.EventQueue;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;

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
    private JLabel lblImageability;
    private JButton fileChooser;
    private JButton btnUploadImage;
    private ArrayList<String> categories;
    private JTextField category;
    private TestFileChooser2 fcp;
    private String url;

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
		
        try {
			sdb = new AmazonSimpleDBClient(new PropertiesCredentials(
			        Images_SDB.class.getResourceAsStream("AwsCredentials.properties")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		frame = new JFrame();
		frame.setBounds(100, 100, 387, 332);
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
		
		fileChooser = new JButton("browse");
		fileChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				    fcp = new TestFileChooser2();
				    String filePath = fcp.getFilePath();
					System.out.println(filePath);
		        }
		}
		);
		        
		fileChooser.setBounds(161, 78, 154, 37);
		frame.getContentPane().add(fileChooser);
		
		
		lblCategory = new JLabel("Category");
		lblCategory.setBounds(63, 133, 61, 16);
		frame.getContentPane().add(lblCategory);
		
		lblFrequency = new JLabel("Frequency");
		lblFrequency.setBounds(63, 174, 86, 16);
		frame.getContentPane().add(lblFrequency);
		
		lblImageability = new JLabel("Imageability");
		lblImageability.setBounds(63, 211, 97, 16);
		frame.getContentPane().add(lblImageability);
		
		categoryList = new JComboBox();
        String selectExpression = "select * from `" + "mossWordsDemo" + "`";
        System.out.println("Selecting: " + selectExpression + "\n");
        SelectRequest selectRequest = new SelectRequest(selectExpression);
/*
        categories=new ArrayList<String>();
        for(Item item: sdb.select(selectRequest).getItems()) {
        	for(Attribute attribute: item.getAttributes()) {
        		if(attribute.getName().equals("Category"))
        			if(categories.contains("Category"))
        				categories.add(attribute.getValue());
        	}
        }
        
		categoryList.setBounds(161, 127, 160, 30);
		frame.getContentPane().add(categoryList);  */
		

		
		category = new JTextField();
		category.setBounds(161, 127, 134, 28);
		frame.getContentPane().add(category);
		category.setColumns(10);
		
		imageName = new JTextField();
		imageName.setBounds(163, 40, 146, 28);
		frame.getContentPane().add(imageName);
		imageName.setColumns(10);
		
		frequencyList = new JComboBox();
		frequencyList.setBounds(161, 168, 160, 30);
		for(int i=1; i<=10; i++)
			frequencyList.addItem(i);
		frame.getContentPane().add(frequencyList);
		
		imageabilityList = new JComboBox();
		imageabilityList.setBounds(161, 205, 160, 30);
		for(int i=1; i<=10; i++)
			imageabilityList.addItem(i);
		frame.getContentPane().add(imageabilityList);
		
		btnUploadImage = new JButton("Upload Image");
        btnUploadImage.setBounds(116, 261, 117, 29);
		frame.getContentPane().add(btnUploadImage);
		
		btnUploadImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = imageName.getText();
				String frequency = Integer.toString(frequencyList.getSelectedIndex()+1);
				String imageability = Integer.toString(imageabilityList.getSelectedIndex()+1);
				int index  = categoryList.getSelectedIndex();
				String categoryVal = category.getText();
	        	ArrayList<ReplaceableItem> data = new ArrayList<ReplaceableItem>();
	        	
	        	System.out.println(word + frequency + imageability + categoryVal);
/*
				String selectExpression = "select * from `" + "mossWordsDemo" + "` where Category = '"+categoryList.getSelectedIndex()+1+"' ";
		        System.out.println("Selecting: " + selectExpression + "\n");
		        SelectRequest selectRequest = new SelectRequest(selectExpression);
		        
		        for (Item item : sdb.select(selectRequest).getItems()) {
		        	if(item.getName().equals(word))
		        	{
		        		JOptionPane.showMessageDialog(frame, "Word Already Exists");
		        		frame.dispose();
		        		new JFrame();
		        	}
		       */ 	
		        	data.add(new ReplaceableItem(word).withAttributes(
		   			 new ReplaceableAttribute("Category", categoryVal, true),
			               new ReplaceableAttribute("Length", Integer.toString(word.length()), true),
			               new ReplaceableAttribute("frequency", frequency, true),
			               new ReplaceableAttribute("Imageability", imageability, true),
			               new ReplaceableAttribute("url", "https://s3.amazonaws.com/mo.jpg", true)));
					
					sdb.batchPutAttributes(new BatchPutAttributesRequest("mossWordsDemo",data));
		        }
		}
		);
		        

	}

	protected String uploadImageToS3() {
		String existingBucketName = "mosswords";
		String keyName = imageName+".JPG";  
		String filePath = fcp.getFilePath();
		
		String amazonFileUploadLocationOriginal=existingBucketName+"/";
		  
		AmazonS3 s3Client;
		try {
			s3Client = new AmazonS3Client(new PropertiesCredentials(
			            Images_SDB.class.getResourceAsStream("AwsCredentials.properties")));

		  FileInputStream stream = new FileInputStream(filePath);
		  ObjectMetadata objectMetadata = new ObjectMetadata();
		  PutObjectRequest putObjectRequest = new PutObjectRequest(amazonFileUploadLocationOriginal, keyName, stream, objectMetadata);
		  PutObjectResult result = s3Client.putObject(putObjectRequest);
		  System.out.println("Etag:" + result.getETag() + "-->" + result);
		  
		}
		catch (IOException e) {
				e.printStackTrace();
			}
		 
		return url;
	}
}
