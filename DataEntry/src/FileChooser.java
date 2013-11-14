import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FileChooser {

	private String filePath;
	private File file;
	private int f;
	private int IMG_WIDTH;
	private int IMG_HEIGHT;
	
	private JFileChooser fileChooser;
    private JPanel filePane;
    private JFrame frame2;

    private JTextField fileField;
	
    public FileChooser(int img_width, int img_height) {
    	f=0;
    	IMG_WIDTH = img_width;
    	IMG_HEIGHT = img_height;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                frame2 = new JFrame();
                frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame2.setLayout(new BorderLayout());
                frame2.add(new MainPanel());
                frame2.setSize(800, 400);
                frame2.setVisible(true);
                if(f==1) {
                	
                	frame2.dispose();
	                GUI_DataEntry.flagImage.setText("Changed");
                    reScaleImage(file);

                }
            }
        });

    }

    protected class MainPanel extends JPanel {

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

            filePane = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.weightx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;

            fileField = new JTextField(10);
            filePane.add(fileField, gbc);
            

            add(filePane); 
            
            if(fileChooser.showSaveDialog(this)== JFileChooser.APPROVE_OPTION) {
            	if(file != null ) {
	            	System.out.println(filePath);	            	
	            	GUI_DataEntry.file=file;
            	}
            }
            else if(fileChooser.showSaveDialog(this)== JFileChooser.CANCEL_OPTION)
            	System.out.println("Cancelled");
            f=1;
            
        }

    }
    
    public void reScaleImage(File file) {
    	try {
    		
			BufferedImage originalImage = ImageIO.read(file);
			JPanel newPanel = new JPanel();
			newPanel.add(new JLabel( new ImageIcon(originalImage)));
			frame2.add(newPanel);
			
			Image resizedImage = originalImage.getScaledInstance(IMG_WIDTH, IMG_HEIGHT, Image.SCALE_SMOOTH);
			
			BufferedImage bsi = new BufferedImage(IMG_WIDTH, IMG_HEIGHT,BufferedImage.TYPE_INT_RGB);
			bsi.getGraphics().drawImage(resizedImage, 0, 0, null);
			FileOutputStream fos = new FileOutputStream(file);
			
			ImageIO.write(bsi, "jpeg", fos);

			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    public File getFile() {
    	return file;
    }
    
	public String getFilePath() {
		return filePath;
	}
}