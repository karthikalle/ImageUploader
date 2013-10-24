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

public class TestFileChooser2 {

	public String filePath;
	
    public static void main(String[] args) {
        new TestFileChooser2();
    }

    public TestFileChooser2() {

        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {

                JFrame frame = new JFrame();
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new MainPane());
                frame.setSize(800, 400);
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);

            }
        });

    }

    protected class MainPane extends JPanel {

        private JFileChooser fileChooser;
        private JPanel filePane;

        private JTextField fileField;

        public MainPane() {

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
            
            JButton closeButton = new JButton("Exit");
            
            closeButton.addActionListener(new ActionListener() {
    			public void actionPerformed(ActionEvent e) {
    				filePane.setVisible(false);
    			}
            });
            
            closeButton.setBounds(100, 100, 25, 20);
            filePane.add(closeButton);
            add(filePane);
            
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