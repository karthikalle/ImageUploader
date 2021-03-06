package imageUploader;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class FileChooser {

	private String filePath;
	private File file;
	public int flag;
	private int IMG_WIDTH;
	private int IMG_HEIGHT;
	public boolean showDialogs = true;

	private JFileChooser fileChooser;
	public JFrame frame2;

	public FileChooser(int img_width, int img_height) {
		flag=0;
		IMG_WIDTH = img_width;
		IMG_HEIGHT = img_height;
		GUI_DataEntry.flagImage.setText("");

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {

				frame2 = new JFrame();
				frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame2.setLayout(new BorderLayout());
				if(showDialogs)
					frame2.add(new MainPanel());
				frame2.setSize(800, 400);
				if(flag==1) {
					frame2.dispose();
					if(reScaleImage(file))
						GUI_DataEntry.flagImage.setText("Changed");
				}
			}
		});

	}

	@SuppressWarnings("serial")
	public class MainPanel extends JPanel {

		public MainPanel() {

			setLayout(new BorderLayout());

			fileChooser = new JFileChooser();
			fileChooser.addPropertyChangeListener(new PropertyChangeListener() {

				@Override
				public void propertyChange(PropertyChangeEvent evt) {
					if (evt.getPropertyName().equals("SelectedFileChangedProperty")) {
						file = fileChooser.getSelectedFile();
					}
				}
			});

			add(fileChooser, BorderLayout.WEST);

			if(fileChooser.showSaveDialog(this)== JFileChooser.APPROVE_OPTION) {
				if(file != null ) {
					flag = 1;
					System.out.println(filePath);	            	
					GUI_DataEntry.file=file;
				}
			}
			else if(fileChooser.showSaveDialog(this)== JFileChooser.CANCEL_OPTION)
				System.out.println("Cancelled");
		}

	}

	public boolean reScaleImage(File file) {
		if(file == null)
			return false;
		try {
			BufferedImage originalImage = ImageIO.read(file);		
			int originalWidth = originalImage.getWidth();
			int originalHeight = originalImage.getHeight();

			if(originalWidth < 600 || originalHeight < 600) {
				if(showDialogs)
					JOptionPane.showMessageDialog(frame2, "Please upload images with resolution higher than 600 X 600");
				return false;
			}

			Image resizedImage = originalImage.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH);

			BufferedImage bsi = new BufferedImage(IMG_WIDTH, IMG_HEIGHT,BufferedImage.TYPE_INT_RGB);
			bsi.getGraphics().drawImage(resizedImage, 0, 0, null);
			FileOutputStream fos = new FileOutputStream(file);
			ImageIO.write(bsi, "jpeg", fos);


		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}

}