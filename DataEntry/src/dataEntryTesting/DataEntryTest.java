package dataEntryTesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;

import dataEntry.FileChooser;
import dataEntry.GUI_DataEntry;
import junit.framework.Assert;
import junit.framework.TestCase;

public class DataEntryTest {
	
	public AmazonSimpleDB sdb;
	
	@Before
	public void setUp () {
		try{
	
		sdb = new AmazonSimpleDBClient(new PropertiesCredentials(
		        GUI_DataEntry.class.getResourceAsStream("AwsCredentials.properties")));
		}
		catch (Exception e){
			System.out.println(e);
		}
	}
	
	@Test
	public void uploadImageData() {
		
			GUI_DataEntry gde = new GUI_DataEntry(){
				public boolean validateAndGetCategory() {
					return true;
				}
				public boolean validateImageName(){
					return true;
				}
				public boolean checkIfAlreadyExistsAndReplace() {
					return true;
				}
				public String uploadImageToS3(){
					return "testURL";
				}
			};
			String imageName = "SaucePan";
			
			gde.showDialogs=false;
			gde.word = imageName;	
			gde.frequency = "4";
			gde.imageability = "6";
			gde.categoryName= "testCategory";
			gde.uploadToSimpleDB();
			
			String selectExpression = "select * from `" + "mossWords" + "` where itemName() = '"+imageName+"'";
		    SelectRequest selectRequest = new SelectRequest(selectExpression);
	
		    String itemName=""; 
		    for(Item item: sdb.select(selectRequest).getItems()) {
		    		itemName = item.getName().toString();
		    }	
		    assertEquals(imageName, itemName);
			gde.frame.dispose();

	}
	
	@Test
	public void testImageNameEmpty() {
		GUI_DataEntry gde = new GUI_DataEntry();
		gde.word = "";
		gde.showDialogs=false;		
		assertFalse(gde.validateImageName());
		gde.frame.dispose();
	}
	
	@Test
	public void testImageNameValid() {
		GUI_DataEntry gde = new GUI_DataEntry();
		gde.word = "Cup";
		gde.showDialogs=false;		
		assertTrue(gde.validateImageName());
		gde.frame.dispose();
	}
	
	@Test
	public void testImageAlreadyExists() {
		
		GUI_DataEntry gde = new GUI_DataEntry();
		gde.word = "Cup";
		gde.showDialogs=false;		
		assertTrue(gde.checkIfAlreadyExistsAndReplace());
		gde.frame.dispose();
	}
	
	@Test
	public void testCategoryEmpty () {
		GUI_DataEntry gde = new GUI_DataEntry();
		gde.categoryName = "";
		gde.showDialogs = false;
		assertFalse(gde.validateAndGetCategory());
		gde.frame.dispose();
	}
	
	@Test
	public void testUploadImageToS3() {
		GUI_DataEntry gde = new GUI_DataEntry(){
			public String validateFile() {
				return "/Users/karthikalle/Desktop/testimages/cup_of_coffee_on_dish.png";
			}
		};
		gde.word = "Cup";
		gde.categoryName = "testCategory";
		gde.showDialogs = false;
		assertEquals("https://s3.amazonaws.com/mosswords/testCategory/Cup.jpg",gde.uploadImageToS3());
		gde.frame.dispose();
	}
	
	@Test
	public void testFileIsNull() {
		GUI_DataEntry gde = new GUI_DataEntry(){
			public String validateFile() {
				return null;
			}
			public boolean validateImageName() {
				return true;
			}
		    public boolean validateAndGetCategory(){
		    	return true;
		    }
			public boolean checkIfAlreadyExistsAndReplace() {
				return true;
			}
		};
		
		String imageName = "Test";
		
		gde.word = imageName;
		gde.categoryName = "testCategory";
		gde.showDialogs = false;
		gde.uploadToSimpleDB();
	
		String selectExpression = "select * from `" + "mossWords" + 
					"` where itemName() = '"+imageName+"'";
	    SelectRequest selectRequest = new SelectRequest(selectExpression);
	
	    String itemName=""; 
	    for(Item item: sdb.select(selectRequest).getItems()) {
	    		itemName = item.getName().toString();
	    }	
	    assertEquals("", itemName);
		gde.frame.dispose();
	}
	
	@Test
	public void testNoFileSelected() {
		
		GUI_DataEntry gde = new GUI_DataEntry(){
			public void getFile() {
				FileChooser fcp = new FileChooser(800,800);
				fcp.flag = 0;
				fcp.frame2.setVisible(false);
			}
		};
		gde.fileChooser.setSelected(true);
		
		assertEquals("",GUI_DataEntry.flagImage.getText());
		gde.frame.dispose();
	}
	
	@Test
	public void testRescaleHighResolutionImage() {
		FileChooser fc = new FileChooser(800, 800);
		String filePath = "/Users/karthikalle/Desktop/testimages/cup_of_coffee_on_dish.png";
		File file = new File(filePath);
		fc.showDialogs = false;
		fc.reScaleImage(file);
		try {
			BufferedImage image = ImageIO.read(file);
			assertEquals(800, image.getWidth());
			assertEquals(800, image.getHeight());
		} 
		catch (IOException e) {
			System.out.println(e);
		}		
		fc.frame2.dispose();
	}
	
	@Test
	public void testLowResolutionImage() {
		FileChooser fc = new FileChooser(800, 800);
		fc.showDialogs = false;
		String filePath = "/Users/karthikalle/Desktop/testimages/pika.png";
		File file = new File(filePath);
		assertFalse(fc.reScaleImage(file));
		fc.frame2.dispose();
	}
	
	@Test
	public void testShowImageForReview() {
		GUI_DataEntry gde = new GUI_DataEntry();
		gde.word = "Cup";
		gde.categoryName = "NonLiving";
		String selectExpression = "select * from `" + "mosswords" + "` where itemName() = '"+gde.word+"'";
	    SelectRequest selectRequest = new SelectRequest(selectExpression);
	
	    for(Item item: sdb.select(selectRequest).getItems()) {
	    	if(item.getName().equals(gde.word)) {
	    		gde.showImageForReview(item);
	    	}
	    while(true){}
	    }
	}
	
}