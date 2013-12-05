package dataEntry;
import java.awt.EventQueue;
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
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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


@SuppressWarnings("serial")
public class GUI_DataEntry extends Frame{

	public JFrame frame;
	public JPanel imageFrame;
	public JTextField imageName;
    private static AmazonSimpleDB sdb;
    private JLabel lblUploadWordsInto;
    private JLabel lblWord;
    private JLabel lblSelectImage;
    private JLabel lblFrequency;
    private JLabel lblCategory;
    private JLabel jlblimage;
    private JLabel lblImageability;
    public static JLabel flag;
    
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
    
    public static File file;

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

	public GUI_DataEntry() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		file = null;
		showDialogs = true;
		choice = -1;
        try {
			sdb = new AmazonSimpleDBClient(new PropertiesCredentials(
			        GUI_DataEntry.class.getResourceAsStream("AwsCredentials.properties")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
        
        IMG_WIDTH = 800;
        IMG_HEIGHT = 800;
        
        categoryName = "";
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
		lblSelectImage.setBounds(63, 166, 86, 16);
		frame.getContentPane().add(lblSelectImage);
		
		fileChooser = new JButton("Browse..");
		flagImage = new JLabel("");
		flagImage.setVisible(false);
		
		flagImage.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
            	System.out.println(evt.getPropertyName());
                if (evt.getPropertyName().equals("text")) {
                	if(flagImage.getText()!="")
                		showImage(file);
                }
            }
        });
		
		fileChooser.setBounds(161, 161, 86, 28);
		frame.getContentPane().add(fileChooser);
		
		fileChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					getFile();
			}
		});
		
		lblCategory = new JLabel("Category");
		lblCategory.setBounds(63, 89, 61, 16);
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
		
		categoryList = new JComboBox<String>();
        String selectExpression = "select Category from `" + "mosswords" + "`";
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
		categoryList.setBounds(161, 83, 160, 30);
		frame.getContentPane().add(categoryList);  
				
		category = new JTextField();
		category.setBounds(161, 125, 154, 28);
		frame.getContentPane().add(category);
		category.setVisible(false);
		category.setColumns(10);
		category.setText("");

		
		imageName = new JTextField();
		imageName.setBounds(163, 40, 146, 28);
		frame.getContentPane().add(imageName);
		imageName.setColumns(10);
		
		frequencyList = new JComboBox<String>();
		frequencyList.setBounds(161, 200, 160, 30);
		frequencyList.addItem("");
	/*	for(int i=1; i<=10; i++)
			frequencyList.addItem(Integer.toString(i));	*/
		frequencyList.addItem("low");
		frequencyList.addItem("high");
		frame.getContentPane().add(frequencyList);
		
		imageabilityList = new JComboBox<String>();
		imageabilityList.setBounds(161, 241, 160, 30);
		imageabilityList.addItem("");
	/*	for(int i=1; i<=10; i++)
			imageabilityList.addItem(Integer.toString(i)); */
		imageabilityList.addItem("low");
		imageabilityList.addItem("high");
		frame.getContentPane().add(imageabilityList);
		
		btnUploadImage = new JButton("Upload Image");
        btnUploadImage.setBounds(116, 283, 117, 29);
		frame.getContentPane().add(btnUploadImage);
		
		lblOrEnterNew = new JButton("Enter New Category");
		lblOrEnterNew.setBounds(21, 129, 129, 22);
		frame.getContentPane().add(lblOrEnterNew);
		
		JLabel lblOr = new JLabel("Or");
		lblOr.setBounds(77, 108, 15, 16);
		frame.getContentPane().add(lblOr);
		
		lblOrEnterNew.addActionListener(new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				category.setVisible(true);
			}
		});
		
		flag = new JLabel();
		flag.setText("");
		flag.setVisible(false);
		flag.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
            	System.out.println(evt.getPropertyName());
                if (evt.getPropertyName().equals("text")) {
                	if(flag.getText()!=""){
                		if(flag.getText().equals("Replace")){
		                	uploadImage();
		                	DisplayExisting.dialog.dispose();
	                		if(showDialogs)
		        	        	seekRestart();
                		}
                		else{
                			DisplayExisting.dialog.dispose();
                			if(showDialogs)
    	        	        	seekRestart();
                		}
                	}
                }
            }
        });		
		btnUploadImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				word = imageName.getText();
			//	frequency = Integer.toString(frequencyList.getSelectedIndex()+1);
			//	imageability = Integer.toString(imageabilityList.getSelectedIndex()+1);
				frequency = (String) frequencyList.getSelectedItem();
				imageability = (String) imageabilityList.getSelectedItem();
				uploadToSimpleDB();		
				btnUploadImage.setSelected(false);
			}	
		});
	}

	
	public void uploadToSimpleDB() {
		
		if(!validateImageName()){
			System.out.println("here21");
			return;
		}
		
		
	    if(!validateAndGetCategory()){
	    	System.out.println("here23");
	    	return;
	    }
    	
    	System.out.println(word + frequency + imageability + categoryName);
    	
    	if(!validateFrequency()) {
    		JOptionPane.showMessageDialog(null, "Please select Frequency");
    		return;
    	}
    	
    	if(!validateImageability()) {
    		JOptionPane.showMessageDialog(null, "Please select Imageability");
    		return;
    	}
    	
    	System.out.println(validateFile());
    	if((validateFile().equals("invalid"))){
    		System.out.println("why here");
    		restart();
			return;
    	}
		if(checkIfAlreadyExistsAndReplace()){
			/*uploadImage();

	        if(showDialogs) {
	    		seekRestart();
			}*/
		}
    	return;
	}

	private void seekRestart() {
		Object[] options = {"Exit Application","Add another Image"};
		
		int restartChoice = JOptionPane.showOptionDialog(frame, "Image has been Uploaded","Upload Another Image", 
				JOptionPane.YES_NO_CANCEL_OPTION,
			    JOptionPane.QUESTION_MESSAGE,
			    null,
			    options,
			    options[1]);
		System.out.println(restartChoice);
		if(restartChoice == 0)
				System.exit(0);
		else
			restart();
	}

	private void uploadImage() {
		System.out.println("here22");
		ArrayList<ReplaceableItem> data = new ArrayList<ReplaceableItem>();	
		System.out.println(word+categoryName+word.length()+frequency+imageability+uploadImageToS3()+computeLevel(frequency,Integer.toString(word.length())));
			data.add(new ReplaceableItem(word).withAttributes(
			new ReplaceableAttribute("Category", categoryName, true),
		    new ReplaceableAttribute("Length", computeLength(word), true),
		    new ReplaceableAttribute("Frequency", frequency, true),
			new ReplaceableAttribute("Imageability", imageability, true),
		    new ReplaceableAttribute("URL", uploadImageToS3(), true),
			new ReplaceableAttribute("Level", computeLevel(frequency, computeLength(word)),true)
		));
		sdb.batchPutAttributes(new BatchPutAttributesRequest("mosswords",data));
	}
	
	private static String computeLength(String word) {

    	if(word.length() <= 5)
    		return "short";
    	else
    		return "long";
	}

	private static String computeLevel(String frequency, String length) {
		if(length.equals("short") && frequency.equals("high"))
			return "1";
		if(length.equals("short") && frequency.equals("low"))
			return "2";
		if(length.equals("long") && frequency.equals("high"))
			return "3";
		if(length.equals("long") && frequency.equals("low"))
			return "4";
		return null;
	}

	private boolean validateImageability() {
		if(frequencyList.getSelectedIndex()==0)
			return false;
		return true;
	}

	private boolean validateFrequency() {
		if(imageabilityList.getSelectedIndex()==0)
			return false;
		return true;
	}

	public boolean validateAndGetCategory() {
		
		int index  = categoryList.getSelectedIndex();
		if(index != 0){
			categoryName = (String) categoryList.getItemAt(index);
		}
		
		if (!(category.getText().equals(""))) {
				categoryName = category.getText();
		}
		
		System.out.println(categoryName);
		
		if(categoryName.length()==0) {
			if(showDialogs)
				JOptionPane.showMessageDialog(null, "Please Enter a category");
			return false;
		}
		return true;
	}
	
	public boolean validateImageName() {
		if(word.length()==0){
			if(showDialogs)
				JOptionPane.showMessageDialog(frame, "Please Enter Image Name");
			return false;
		}
		return true;
	}
	
	public void getFile() {
		fcp = new FileChooser(IMG_WIDTH, IMG_HEIGHT);
	}
	
	public boolean checkIfAlreadyExistsAndReplace() {
		String selectExpression = "select * from `" + "mosswords" + "` where itemName() = '"+word+"'";
	    SelectRequest selectRequest = new SelectRequest(selectExpression);
	
	    for(Item item: sdb.select(selectRequest).getItems()) {
	    	if(item.getName().equals(word)) {
	    		
	    		url = "https://s3.amazonaws.com/"+"mosswords"+"/Images/"+categoryName+"/"+word+".jpg";
	    		
	    	// DisplayExistingImage display = new DisplayExistingImage(item);
	    		//showImageForReview(item);
	    		/* 		Object[] options = {"No, use the older one","Yes, replace"};
	    		
	    		
	    	    JOptionPane optionPane = new JOptionPane(showImageForReview(item), 
	    	    		JOptionPane.QUESTION_MESSAGE, 
	    	    		JOptionPane.YES_NO_CANCEL_OPTION,
	    	    		null,
	    	    		options,
	    	    		options[1]
	    	    		);  
	    	    JDialog dialog = optionPane.createDialog(frame, "Enter Data");  
	    	    dialog.setVisible(true);
	    		dialog.show();
	    		JPanel imgPanel = new JPanel();
	    		imgPanel = showImageForReview(item);
	    		imgPanel; */
	    	    /*     		choice = JOptionPane.showOptionDialog(frame,
                        showImageForReview(item),
                       " ",
                      JOptionPane.YES_NO_CANCEL_OPTION,
                      JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
	   		
	    		System.out.println("exists");
	    		Object[] options = {"No, use the older one","Yes, replace"};
	    		if(showDialogs) {
					int choice = JOptionPane.showOptionDialog(frame, "Image Name already exists","Replace Image?", 
							JOptionPane.YES_NO_CANCEL_OPTION,
						    JOptionPane.QUESTION_MESSAGE,
						    null,
						    options,
						    options[1]);
					System.out.println(choice); */
	    		System.out.println("hi");
				DisplayExisting.initialize(item);
	    		if(DisplayExisting.option == 0)
	    			return false;
	    		else if(DisplayExisting.option == 1)
	    			return true;
	    	}
	    }
	    return true;
	}
	
	public JPanel showImageForReview(Item item) {
		imageFrame = new JPanel();
		imageFrame.setBounds(50, 50, 600, 400);
		imageFrame.setLayout(null);
		imageFrame.setVisible(true);
	
		BufferedImage bi;
		try {
			
			JLabel heading = new JLabel("Image with the following properties already exists in the database");
			heading.setFont(new Font("Serif", Font.BOLD, 18));
			heading.setBounds(25, 15, 550, 50);
			imageFrame.add(heading);
			
			JLabel imageNameLabel = new JLabel("Image Name:");
			imageNameLabel.setBounds(63, 86, 161, 36);
			imageNameLabel.setFont(new Font("Arial", Font.BOLD, 13));
			imageFrame.add(imageNameLabel);
			
			JLabel imageName = new JLabel(word);
			imageName.setBounds(165, 86, 161, 36);
			imageFrame.add(imageName);
			
			System.out.println("Category "+getAttributeValue(item, "Category"));
			
			JLabel categoryLabel = new JLabel("Category:");
			categoryLabel.setBounds(63, 106, 161, 36);
			categoryLabel.setFont(new Font("Arial", Font.BOLD, 13));
			imageFrame.add(categoryLabel);
			
			JLabel category = new JLabel(getAttributeValue(item, "Category"));
			category.setBounds(165, 106, 161, 36);
			imageFrame.add(category);
			
			System.out.println("Frequency: "+getAttributeValue(item, "Frequency"));

			JLabel freqLabel = new JLabel("Image Name:");
			freqLabel.setBounds(63, 126, 161, 36);
			freqLabel.setFont(new Font("Arial", Font.BOLD, 13));
			imageFrame.add(freqLabel);
			
			JLabel frequency = new JLabel(getAttributeValue(item, "Frequency"));
			frequency.setBounds(165, 126, 161, 36);
			imageFrame.add(frequency);
			
			JLabel imageabilityLabel = new JLabel("Image Name:");
			imageabilityLabel.setBounds(63, 146, 161, 36);
			imageabilityLabel.setFont(new Font("Arial", Font.BOLD, 13));
			imageFrame.add(imageabilityLabel);
			
			JLabel imageability = new JLabel(getAttributeValue(item, "Imageability"));
			imageability.setBounds(165, 146, 161, 36);
			imageFrame.add(imageability);
			
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
			imageFrame.add(replaceButton);
			
			JButton useExistingButton = new JButton("Use Existing");
			useExistingButton.setBounds(150, 206, 140, 25);
			imageFrame.add(useExistingButton);
			
			imageFrame.setVisible(true);

			replaceButton.addActionListener(new ActionListener (){
				public void actionPerformed(ActionEvent e) {
					choice = 1;
				//	imageFrame.dispose();	
				}	
			});
			
			useExistingButton.addActionListener(new ActionListener (){
				public void actionPerformed(ActionEvent e) {
					choice = 0;
				//	imageFrame.dispose();			
				}	
			});
			
		}
		catch (Exception e) {
			System.out.println("here "+e);
		}
		return imageFrame;
						
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

	public void restart() {
		imageName.setText("");
		category.setText("");
		categoryList.setSelectedIndex(0);
		frequencyList.setSelectedIndex(0);
		imageabilityList.setSelectedIndex(0);
		frame.setBounds(10, 0, 462, 350);
		flagImage.setText("");
		jlblimage.setIcon(null);
		frame.getContentPane().remove(flagImage); 
		return;
	}
	
	public void showImage(File file) {
		BufferedImage bi;
		try {
			System.out.println(file);
			bi = ImageIO.read(file);
			ImageIcon icon = new ImageIcon(bi.getScaledInstance(200, 200, Image.SCALE_SMOOTH));
			jlblimage.setIcon(icon);
			frame.setBounds(10, 0, 662, frame.getHeight());
			jlblimage.setBounds(340, 60, 200, 200);
			frame.getContentPane().add(jlblimage);
		}
		catch (Exception e) {
			System.out.println("here "+e);
		}		
	}

	public String uploadImageToS3() {
		String existingBucketName = "mosswords";
		String keyName = word + ".jpg";

		String filePath = validateFile();
		if(filePath == null){
			restart();
		}
		String amazonFileUploadLocationOriginal = existingBucketName+"/Images/"+categoryName;

		System.out.println(existingBucketName);
		System.out.println(categoryName);
		
		AmazonS3 s3Client;
		try {
			s3Client = new AmazonS3Client(new PropertiesCredentials(
			            GUI_DataEntry.class.getResourceAsStream("AwsCredentials.properties")));
	
			  FileInputStream stream = new FileInputStream(filePath);
			  ObjectMetadata objectMetadata = new ObjectMetadata();
			  
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
	
	public String validateFile() {
		
		if(file == null) {
			if(showDialogs)
				JOptionPane.showMessageDialog(frame, "Please choose an image");
			return "invalid";	
		}
		
		String filePath = file.getPath();
		if(fcp==null||filePath==null||filePath=="") {
			if(showDialogs)
				JOptionPane.showMessageDialog(frame, "Please choose an image");
			return "invalid";
		}
		return filePath;
	}
}