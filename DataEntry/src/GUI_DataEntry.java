import java.awt.EventQueue;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.BatchPutAttributesRequest;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.ReplaceableAttribute;
import com.amazonaws.services.simpledb.model.ReplaceableItem;
import com.amazonaws.services.simpledb.model.SelectRequest;

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
    private JFileChooser jfc;
    private JButton btnUploadImage;
    private JButton btnChangeDifficultyFunction;
    private ArrayList<String> categories;
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
	private void initialize() {
		
        try {
			sdb = new AmazonSimpleDBClient(new PropertiesCredentials(
			        Images_SDB.class.getResourceAsStream("AwsCredentials.properties")));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		frame = new JFrame();
		frame.setBounds(100, 100, 510, 330);
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
		
		jfc = new JFileChooser("browse");
		jfc.setBounds(161, 78, 154, 37);
		frame.getContentPane().add(jfc);
		
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
        String selectExpression = "select * from `" + "mossWords" + "`";
        System.out.println("Selecting: " + selectExpression + "\n");
        SelectRequest selectRequest = new SelectRequest(selectExpression);

        categories=new ArrayList<String>();
        for(Item item: sdb.select(selectRequest).getItems()) {
        	for(Attribute attribute: item.getAttributes()) {
        		if(attribute.getName().equals("Category"))
        			if(categories.contains("Category"))
        				categories.add(attribute.getValue());
        	}
        }
        
		categoryList.setBounds(161, 127, 160, 30);
		frame.getContentPane().add(categoryList);
		
		btnChangeDifficultyFunction = new JButton("Change Difficulty Function");
		btnChangeDifficultyFunction.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		
		btnChangeDifficultyFunction.setBounds(301, 243, 188, 21);
		frame.getContentPane().add(btnChangeDifficultyFunction);
		
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
		btnUploadImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = imageName.getText();
				String frequency = Integer.toString(frequencyList.getSelectedIndex()+1);
				String imageability = Integer.toString(imageabilityList.getSelectedIndex()+1);
				int index  = categoryList.getSelectedIndex();
				String category = categories.get(index);
	        	ArrayList<ReplaceableItem> data = new ArrayList<ReplaceableItem>();

				String selectExpression = "select * from `" + "mossWords" + "` where Category = '"+categoryList.getSelectedIndex()+1+"' ";
		        System.out.println("Selecting: " + selectExpression + "\n");
		        SelectRequest selectRequest = new SelectRequest(selectExpression);
		        
		        for (Item item : sdb.select(selectRequest).getItems()) {
		        	if(item.getName().equals(word))
		        	{
		        		JOptionPane.showMessageDialog(frame, "Word Already Exists");
		        		frame.dispose();
		        		new JFrame();
		        	}
		        		data = new ReplaceableItem(word).withAttributes(
		   				 new ReplaceableAttribute("Category", category, true),
			                new ReplaceableAttribute("Length", Integer.toString(word.length()), true),
			                new ReplaceableAttribute("frequency", frequency, true),
			                new ReplaceableAttribute("Imageability", imageability, true),
			                new ReplaceableAttribute("url", "https://s3.amazonaws.com/mosswords/images/nonliving/"+word.toLowerCase()+".jpg", true));
					
					sdb.batchPutAttributes(new BatchPutAttributesRequest("mossWords",data));
		        }
		        
		        	
				//(word,frequencyList.)
			}
		});
		btnUploadImage.setBounds(116, 261, 117, 29);
		frame.getContentPane().add(btnUploadImage);
	}
}
