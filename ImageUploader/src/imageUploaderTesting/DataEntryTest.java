package imageUploaderTesting;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import imageUploader.FileChooser;
import imageUploader.GUI_DataEntry;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;

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

		@SuppressWarnings("serial")
		GUI_DataEntry gde = new GUI_DataEntry(){
			public boolean validateAndGetCategory() {
				return true;
			}
			public boolean validateImageName(){
				return true;
			}
			public boolean checkIfExistsAndSeekReplace() {
				return true;
			}
			public String uploadImageToS3(){
				return "testURL";
			}
		};
		String imageName = "SaucePan";

		gde.showDialogs=false;
		gde.word = imageName;	
		gde.frequency = "high";
		gde.imageability = "low";
		gde.categoryName= "testCategory";
		gde.validateUpload();

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
		assertTrue(gde.checkIfExistsAndSeekReplace());
		gde.displayExisting.dispose();
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
		@SuppressWarnings("serial")
		GUI_DataEntry gde = new GUI_DataEntry(){
			public String validateFile() {
				return "/Users/karthikalle/Desktop/testimages/Cap.png";
			}
		};
		gde.word = "Cap";
		gde.categoryName = "NonLiving";
		gde.showDialogs = false;
		assertEquals("https://s3.amazonaws.com/mosswords/Images/NonLiving/Cap.jpg",gde.uploadImageToS3());
		gde.frame.dispose();
	}

	@Test
	public void testFileIsNull() {
		@SuppressWarnings("serial")
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
			public boolean checkIfExistsAndSeekReplace() {
				return true;
			}
		};

		String imageName = "Test";

		gde.word = imageName;
		gde.categoryName = "testCategory";
		gde.showDialogs = false;
		gde.validateUpload();

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

		@SuppressWarnings("serial")
		GUI_DataEntry gde = new GUI_DataEntry(){
			public void getFile() {
				FileChooser fcp = new FileChooser(800,800);
				fcp.flag = 0;
				fcp.frame2.setVisible(false);
			}
		};
		gde.fileChooser.setSelected(true);
		gde.showDialogs=false;		
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

	public void testComputeLengthShort() {
		GUI_DataEntry gde = new GUI_DataEntry();
		gde.word = "Cup";
		String length = GUI_DataEntry.computeLength(gde.word);
		assertEquals("short", length);
	}

	@Test
	public void testComputeLengthLong() {
		GUI_DataEntry gde = new GUI_DataEntry();
		gde.word = "Crocodile";
		String length = GUI_DataEntry.computeLength(gde.word);
		assertEquals("long", length);
	}

	@Test
	public void testComputeLevel1() {
		GUI_DataEntry gde = new GUI_DataEntry();
		gde.word = "Cup";
		gde.frequency = "high";
		String length = GUI_DataEntry.computeLength(gde.word);
		assertEquals("1", GUI_DataEntry.computeLevel(gde.frequency, length));
	}

	@Test
	public void testComputeLevel2() {
		GUI_DataEntry gde = new GUI_DataEntry();
		gde.word = "Cap";
		gde.frequency = "low";
		String length = GUI_DataEntry.computeLength(gde.word);
		assertEquals("2", GUI_DataEntry.computeLevel(gde.frequency, length));
	}

	@Test
	public void testComputeLevel3() {
		GUI_DataEntry gde = new GUI_DataEntry();
		gde.word = "Crocodile";
		gde.frequency = "high";
		String length = GUI_DataEntry.computeLength(gde.word);
		assertEquals("3", GUI_DataEntry.computeLevel(gde.frequency, length));
	}

	@Test
	public void testComputeLevel4() {
		GUI_DataEntry gde = new GUI_DataEntry();
		gde.word = "Basketball";
		gde.frequency = "low";
		String length = GUI_DataEntry.computeLength(gde.word);
		assertEquals("4", GUI_DataEntry.computeLevel(gde.frequency, length));
	}

}