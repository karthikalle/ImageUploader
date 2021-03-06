package imageUploaderTesting;

import static org.junit.Assert.assertEquals;
import imageUploader.DisplayExisting;
import imageUploader.GUI_DataEntry;

import org.junit.Before;
import org.junit.Test;

import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDB;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;

public class DisplayExistingTest {

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
	public void testShowImageForReview() {
		GUI_DataEntry gde = new GUI_DataEntry();
		gde.word = "Cup";
		gde.categoryName = "NonLiving";
		gde.showDialogs = false;
		String selectExpression = "select * from `" + "mosswords" + "` where itemName() = '"+gde.word+"'";
		SelectRequest selectRequest = new SelectRequest(selectExpression);

		for(Item item: sdb.select(selectRequest).getItems()) {
			if(item.getName().equals(gde.word)) {
				gde.displayExisting = DisplayExisting.initialize(item);
				try {
					Thread.sleep(2000);
				} catch(InterruptedException ex) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	@Test
	public void testReplaceImage() {
		GUI_DataEntry gde = new GUI_DataEntry();
		gde.word = "Cup";
		gde.categoryName = "NonLiving";
		gde.frequency = "high";
		gde.frequency = "low";
		gde.showDialogs = false;
		String selectExpression = "select * from `" + "mosswords" + "` where itemName() = '"+gde.word+"'";
		SelectRequest selectRequest = new SelectRequest(selectExpression);

		for(Item item: sdb.select(selectRequest).getItems()) {
			if(item.getName().equals(gde.word)) {
				gde.displayExisting = DisplayExisting.initialize(item);
				gde.displayExisting.setVisible(false);
				gde.displayExisting.replaceButton.doClick();
				assertEquals(gde.flagValue,"Replace");
			}
		}
	}

	@Test
	public void testUseExistingImage() {
		GUI_DataEntry gde = new GUI_DataEntry();
		gde.word = "Cup";
		gde.categoryName = "NonLiving";
		gde.frequency = "high";
		gde.frequency = "low";
		gde.showDialogs = false;
		String selectExpression = "select * from `" + "mosswords" + "` where itemName() = '"+gde.word+"'";
		SelectRequest selectRequest = new SelectRequest(selectExpression);

		for(Item item: sdb.select(selectRequest).getItems()) {
			if(item.getName().equals(gde.word)) {
				gde.displayExisting = DisplayExisting.initialize(item);
				gde.displayExisting.setVisible(false);
				gde.displayExisting.useExistingButton.doClick();
				assertEquals(gde.flagValue,"Use Existing");

			}
		}
	}

	@Test
	public void testGetCategoryValue() {
		GUI_DataEntry gde = new GUI_DataEntry();
		String word = "Snake";
		String selectExpression = "select * from `" + "mosswords" + "` where itemName() = '"+word+"'";
		SelectRequest selectRequest = new SelectRequest(selectExpression);

		for(Item item: sdb.select(selectRequest).getItems()) {
			if(item.getName().equals(word)) {
				gde.displayExisting = DisplayExisting.initialize(item);
				gde.displayExisting.setVisible(false);
				assertEquals("Living",gde.displayExisting.getAttributeValue(item, "Category"));
			}
		}
	}

	@Test
	public void testGetFrequencyValue() {
		GUI_DataEntry gde = new GUI_DataEntry();
		String word = "Snake";
		String selectExpression = "select * from `" + "mosswords" + "` where itemName() = '"+word+"'";
		SelectRequest selectRequest = new SelectRequest(selectExpression);

		for(Item item: sdb.select(selectRequest).getItems()) {
			if(item.getName().equals(word)) {
				gde.displayExisting = DisplayExisting.initialize(item);
				gde.displayExisting.setVisible(false);
				assertEquals("high",gde.displayExisting.getAttributeValue(item, "Frequency"));
			}
		}
	}

	@Test
	public void testGetImageabilityValue() {
		GUI_DataEntry gde = new GUI_DataEntry();
		String word = "Snake";
		String selectExpression = "select * from `" + "mosswords" + "` where itemName() = '"+word+"'";
		SelectRequest selectRequest = new SelectRequest(selectExpression);

		for(Item item: sdb.select(selectRequest).getItems()) {
			if(item.getName().equals(word)) {
				gde.displayExisting = DisplayExisting.initialize(item);
				gde.displayExisting.setVisible(false);
				assertEquals("high",gde.displayExisting.getAttributeValue(item, "Imageability"));
			}
		}
	}

	@Test
	public void testGetLevelValue() {
		GUI_DataEntry gde = new GUI_DataEntry();
		String word = "Snake";
		String selectExpression = "select * from `" + "mosswords" + "` where itemName() = '"+word+"'";
		SelectRequest selectRequest = new SelectRequest(selectExpression);

		for(Item item: sdb.select(selectRequest).getItems()) {
			if(item.getName().equals(word)) {
				gde.displayExisting = DisplayExisting.initialize(item);
				gde.displayExisting.setVisible(false);
				assertEquals("1",gde.displayExisting.getAttributeValue(item, "Level"));
			}
		}
	}

	@Test
	public void testGetURLValue() {
		GUI_DataEntry gde = new GUI_DataEntry();
		String word = "Snake";
		String selectExpression = "select * from `" + "mosswords" + "` where itemName() = '"+word+"'";
		SelectRequest selectRequest = new SelectRequest(selectExpression);

		for(Item item: sdb.select(selectRequest).getItems()) {
			if(item.getName().equals(word)) {
				gde.displayExisting = DisplayExisting.initialize(item);
				gde.displayExisting.setVisible(false);
				assertEquals("https://s3.amazonaws.com/mosswords/Images/Living/snake.jpg",
						gde.displayExisting.getAttributeValue(item, "URL"));
			}
		}
	}
}
