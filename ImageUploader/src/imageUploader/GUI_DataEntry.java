package imageUploader;
import java.awt.EventQueue;
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
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
public class GUI_DataEntry extends Frame {

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
	public String flagValue;

	private int IMG_HEIGHT;
	private int IMG_WIDTH;

	public boolean showDialogs;
	public JTextField category;
	public static JLabel flagImage;
	public FileChooser fcp;
	public DisplayExisting displayExisting;

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
		initializeContents();
	}

	private void initializeContents() {
		file = null;
		showDialogs = true;
		try {
			sdb = new AmazonSimpleDBClient(new PropertiesCredentials(
					GUI_DataEntry.class.getResourceAsStream("AwsCredentials.properties")));
		} 
		catch (IOException e1) {
			e1.printStackTrace();
		}

		IMG_WIDTH = 800;
		IMG_HEIGHT = 800;
		categoryName = "";
		flagValue = "";

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
		frequencyList.addItem("low");
		frequencyList.addItem("high");
		frame.getContentPane().add(frequencyList);

		imageabilityList = new JComboBox<String>();
		imageabilityList.setBounds(161, 241, 160, 30);
		imageabilityList.addItem("");
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
						flagValue = flag.getText();
						if(flagValue.equals("Replace")) {
							displayExisting.dispose();
							if(showDialogs) {
								uploadImage();
							}
						}
						else if(flagValue.equals("Use Existing"))
							displayExisting.dispose();
						if(showDialogs) {
							if(seekRestart())
								restart();
							else
								System.exit(0);
						}
					}
				}
			}
		});		

		btnUploadImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				word = imageName.getText();
				frequency = (String) frequencyList.getSelectedItem();
				imageability = (String) imageabilityList.getSelectedItem();
				validateUpload();		
				btnUploadImage.setSelected(false);
			}	
		});
	}
	/*End of Initialization*/

	/*Get Parameters from Frame and Upload to SimpleDB*/
	public void validateUpload() {

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
			if(showDialogs)
				JOptionPane.showMessageDialog(null, "Please select Frequency");
			return;
		}
		if(!validateImageability()) {
			if(showDialogs)
				JOptionPane.showMessageDialog(null, "Please select Imageability");
			return;
		}
		System.out.println(validateFile());
		if((validateFile().equals("invalid"))){
			System.out.println("why here");
			restart();
			return;
		}
		checkIfExistsAndSeekReplace();
		return;
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

	public void getFile() {
		fcp = new FileChooser(IMG_WIDTH, IMG_HEIGHT);
	}

	/* Show selected image to user */
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

	/*Upload Image to Simple DB*/
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

	/* "short" if length <= 5 or else "long" */
	public static String computeLength(String word) {

		if(word.length() <= 5)
			return "short";
		else
			return "long";
	}

	/* Levels based on frequency and Length */
	public static String computeLevel(String frequency, String length) {
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

	/* Check if Image Exists in SimpleDB and ask for user permission to replace */
	public boolean checkIfExistsAndSeekReplace() {
		String selectExpression = "select * from `" + "mosswords" + "` where itemName() = '"+word+"'";
		SelectRequest selectRequest = new SelectRequest(selectExpression);

		for(Item item: sdb.select(selectRequest).getItems()) {
			if(item.getName().equals(word)) {
				displayExisting = DisplayExisting.initialize(item);
				return true;
			}
		}
		return true;
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

	/*Seek User Permission to Restart Application*/
	private boolean seekRestart() {
		Object[] options = {"Exit Application","Add another Image"};

		int restartChoice = JOptionPane.showOptionDialog(frame, "Image has been Uploaded","Upload Another Image", 
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[1]);
		System.out.println(restartChoice);
		if(restartChoice == 0)
			return false;
		else
			return true;
	}

	/* Restart Application */
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
}