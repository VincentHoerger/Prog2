package Main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class App {
	
	static PrintStream OUT = System.out;
	static DB dbc = DB.getInstance();
	public static void main(String[] args) {
        
        dbc.initDBConnection();
        
		setupOutStream();
		setupLookAndFeel();
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				startGUI();
			}
		});
//		SwingUtilities.invokeLater( ()->startGUI() ); // lambda equivalent for above
	}

	private static void startGUI() {
		// --- 1ter Teil - Fenster erstellen und starten
		JFrame window = new JFrame();
		window.setTitle("Text Writer");
		{
			setupWindowContent(window);
		}
		window.pack();
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
	}

	
	private static void setupWindowContent(JFrame window) {
		// --- 2ter Teil - Fenster Inhalt erstellen
		JPanel content = new JPanel();
		final JLabel lblEnterSmthng = new JLabel("Enter Something!");
		final JTextField txtField = new JTextField(30);
		JButton btnOk = new JButton("Ok");
		final JLabel labelLastText = new JLabel();
		content.add(lblEnterSmthng);
		content.add(txtField);
		content.add(btnOk);
		content.add(labelLastText);
		window.setContentPane(content);
		
		// --- 3ter Teil -Programm Logik
		ActionListener submitAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String txt = txtField.getText();
				dbc.handleDB(Integer.parseInt(txt));
				if(!txt.isEmpty()){
					OUT.println(txt);
					labelLastText.setText(txt);
					txtField.setText("");
				}
			}
		};
//		ActionListener submitAction = (e) -> {
//			String txt = txtField.getText();
//			if(!txt.isEmpty()){
//				OUT.println(txt);
//				labelLastText.setText(txt);
//				txtField.setText("");
//			}
//		}; // lambda equivalent for above
		btnOk.addActionListener(submitAction);
		txtField.addActionListener(submitAction);
	}
	
	
	private static void setupOutStream() {
		File logfile = new File("logFile.txt");
		try {
			if (!logfile.exists()) {
				logfile.createNewFile();
			}
			OUT = new PrintStream(logfile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static void setupLookAndFeel() {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
	}
}