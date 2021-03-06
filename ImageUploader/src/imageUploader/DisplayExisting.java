package imageUploader;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;

import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

@SuppressWarnings("serial")
public class DisplayExisting extends JDialog {

	private final JPanel contentPanel = new JPanel();
	public static Item item;
	public static DisplayExisting dialog;
	public JButton replaceButton;
	public JButton useExistingButton;

	/**
	 * Launch the application.
	 */
	public static DisplayExisting initialize(Item image) {
		item = image;
		try {
			dialog = new DisplayExisting();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dialog;
	}

	/**
	 * Create the dialog.
	 */
	public DisplayExisting() {
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 450, 36);
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);

		JLabel heading = new JLabel("Image Already Exists in the Database");
		contentPanel.add(heading);
		heading.setFont(new Font("Serif", Font.BOLD, 18));

		JPanel buttonPane = new JPanel();
		buttonPane.setBounds(0, 239, 450, 39);
		getContentPane().add(buttonPane);

		replaceButton = new JButton("Replace Original");
		replaceButton.setBounds(87, 5, 129, 29);
		replaceButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUI_DataEntry.flag.setText("Replace");
			}
		});
		buttonPane.setLayout(null);
		buttonPane.add(replaceButton);
		getRootPane().setDefaultButton(replaceButton);

		useExistingButton = new JButton("Use Existing");
		useExistingButton.setBounds(237, 5, 116, 29);
		useExistingButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				GUI_DataEntry.flag.setText("Use Existing");
			}
		});
		buttonPane.add(useExistingButton);

		JLabel imageNameLabel = new JLabel("Image Name");
		imageNameLabel.setBounds(10, 48, 100, 16);
		getContentPane().add(imageNameLabel);

		JLabel categoryLabel = new JLabel("Category");
		categoryLabel.setBounds(10, 89, 100, 16);
		getContentPane().add(categoryLabel);

		JLabel frequencyLabel = new JLabel("Frequency");
		frequencyLabel.setBounds(10, 134, 100, 16);
		getContentPane().add(frequencyLabel);

		JLabel imageabilityLabel = new JLabel("Imageability");
		imageabilityLabel.setBounds(10, 176, 100, 16);
		getContentPane().add(imageabilityLabel);

		JLabel imageName = new JLabel(item.getName());
		imageName.setBounds(114, 48, 100, 16);
		getContentPane().add(imageName);

		JLabel category = new JLabel(getAttributeValue(item,"Category"));
		category.setBounds(114, 89, 100, 16);
		getContentPane().add(category);

		JLabel frequency = new JLabel(getAttributeValue(item,"Frequency"));
		frequency.setBounds(114, 134, 100, 16);
		getContentPane().add(frequency);

		JLabel imageability = new JLabel(getAttributeValue(item,"Imageability"));
		imageability.setBounds(114, 176, 100, 16);
		getContentPane().add(imageability);

		JLabel imageLabel = new JLabel();
		imageLabel.setBounds(226, 42, 200, 200);

		/* Display Image */
		try {
			URL imageURL = new URL(getAttributeValue(item,"URL"));
			URLConnection connection = imageURL.openConnection();
			connection.setRequestProperty("User-Agent", "xxxxxx");
			BufferedImage bi = ImageIO.read(imageURL);
			ImageIcon icon = new ImageIcon(bi.getScaledInstance(200, 200, Image.SCALE_SMOOTH));
			imageLabel.setIcon(icon);
			getContentPane().add(imageLabel);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/* Get value of the attribute for that item */
	public String getAttributeValue(Item item, String attribute) {

		List<Attribute> attributes = item.getAttributes();
		for(int i=0; i<attributes.size(); i++) {
			if(attributes.get(i).getName().equals(attribute))
				return attributes.get(i).getValue();
		}
		return null;
	}

}
