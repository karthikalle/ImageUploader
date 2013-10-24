import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class FileChooser {

	public String filePath;
	private int f;
	
    public FileChooser(final filePathOnly fp) {
    	f=0;
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                JFrame frame2 = new JFrame();
                frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame2.setLayout(new BorderLayout());
                frame2.add(new MainPanel());
                frame2.setSize(800, 400);
                frame2.setLocationRelativeTo(null);
                frame2.setVisible(true);
                if(f==1) {
                	fp.setFilePath(filePath);
                	frame2.dispose();
                }
            }
        });

    }

    protected class MainPanel extends JPanel {

        private JFileChooser fileChooser;
        private JPanel filePane;

        private JTextField fileField;

        public MainPanel() {

            setLayout(new BorderLayout());

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
            
 /*           JButton closeButton = new JButton("Exit");
            
            closeButton.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				filePath = fileField.getText();
    				filePane.setVisible(false);
    			}
            });
            
            closeButton.setBounds(100, 100, 25, 20);
            filePane.add(closeButton); */
            add(filePane); 
            
            if(fileChooser.showSaveDialog(this)== JFileChooser.APPROVE_OPTION)
            	System.out.println(filePath);
            else if(fileChooser.showSaveDialog(this)== JFileChooser.CANCEL_OPTION)
            	System.out.println("Cancelled");
            f=1;
            
        }

        protected void setFile(File file) {

            fileField.setText(file.getPath());
            filePath = fileField.getText();
            
        }

    }

	public String getFilePath() {
		return filePath;
	}
}