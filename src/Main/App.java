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
				
		        Cocktail[] cocktails = dbc.getAllCocktails();
		        System.out.println("All Cocktails: ");
		        for(Cocktail c: cocktails){
					System.out.println("cocktail_id = " + c.cocktail_id);
					System.out.println("name = " + c.name);
					//System.out.println("instructions = " + c.instructions);
					System.out.println("");
		    	}
		        
		        System.out.println("Usage Statistics: ");
		        UsageStatistic[] stat = dbc.getUsageStatistics();
		        
		        for(UsageStatistic s: stat){
					System.out.println("description = " + s.description);
					System.out.println("count = " + s.count);
					System.out.println("");
		        }
		        
				System.out.println("Cocktail: ");
				Cocktail c = dbc.getCocktailInfo(Integer.parseInt(txt));
                System.out.println("cocktail_id = " + c.cocktail_id);
                System.out.println("name = " + c.name);
                System.out.println("instructions = " + c.instructions);
                System.out.println("");
                
                Ingredient[] ingredients = dbc.getCocktailIngredients(Integer.parseInt(txt));
                System.out.println("Ingredients: ");
                for(Ingredient i: ingredients){
    				System.out.println("ingredient_id = " + i.ingredient_id);
    				System.out.println("cocktail_id = " + i.cocktail_id);
    				System.out.println("quantity = " + i.quantity);
    				System.out.println("unit = " + i.unit);
    				System.out.println("description = " + i.description);
    				System.out.println("");
            	}
                
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