import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class FileChooserPanel extends JPanel {


	private JFileChooser fileChooser;
    private JPanel filePane;

    private JTextField fileField;

    public FileChooserPanel() {

        setLayout(new BorderLayout());
        setVisible(true);
        
        fileChooser = new JFileChooser();
        fileChooser.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (evt.getPropertyName().equals("SelectedFileChangedProperty")) {
                    File file = fileChooser.getSelectedFile();
                    if (file != null) {
                        setFile(file);
                    }
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

    }

    protected void setFile(File file) {

        fileField.setText(file.getPath());

    }
    
    public String getFilePath() {
    	
    	return fileField.getText();
    	
    }

}
