package dataEntry;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;

public class DisplayExistingImage extends JFrame {

	private String filePath;
	private File file;
	public int flag;
	private int IMG_WIDTH;
	private int IMG_HEIGHT;
	public boolean showDialogs = true;
	
	private JFileChooser fileChooser;
    public JFrame imageFrame;
    private JLabel jlblimage;
    private Item item;
	
    public DisplayExistingImage(Item imageItem) {

    	item = imageItem;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
            	
            	
            	JFrame imageFrame = new JFrame();
        		imageFrame.setBounds(50, 50, 662, 662);
        		imageFrame.setVisible(true);

        		BufferedImage bi;
        		try {
        			
        			URL imageURL = new URL(getAttributeValue(item,"url"));

        			URLConnection connection = imageURL.openConnection();
        			connection.setRequestProperty("User-Agent", "xxxxxx");
        			
        			bi = ImageIO.read(imageURL);
        			ImageIcon icon = new ImageIcon(bi.getScaledInstance(200, 200, Image.SCALE_SMOOTH));
        			jlblimage.setIcon(icon);
        			jlblimage.setBounds(340, 60, 200, 200);
        			imageFrame.add(jlblimage);
        		
        			JLabel imageName = new JLabel(item.getName());
        			imageName.setBounds(63, 46, 61, 16);
        			imageFrame.getContentPane().add(imageName);
        			
        			JLabel category = new JLabel(getAttributeValue(item, "Category"));
        			imageName.setBounds(63, 56, 61, 16);
        			imageFrame.getContentPane().add(category);
        			
        			JLabel frequency = new JLabel(getAttributeValue(item, "frequency"));
        			imageName.setBounds(63, 66, 61, 16);
        			imageFrame.getContentPane().add(frequency);
        			
        			JLabel imageability = new JLabel(getAttributeValue(item, "Imageability"));
        			imageName.setBounds(63, 76, 61, 16);
        			imageFrame.getContentPane().add(imageability);
        			
        		}
        		catch (Exception e) {
        			System.out.println("here "+e);
        		}		
        				
            }
        });

    }
    private String getAttributeValue(Item item, String string) {
		List<Attribute> attributes = item.getAttributes();
		
		for(int i=0; i<attributes.size(); i++) {
			if(attributes.get(i).getName()=="Category")
					return attributes.get(i).getValue();
		}
		return null;
	}

}
