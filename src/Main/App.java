package Main;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class App {
	
	static DB dbc = DB.getInstance();
	public static void main(String[] args) {
        
        dbc.initDBConnection();
        
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
		GridBagLayout layout = new GridBagLayout();
		GridBagConstraints c = new GridBagConstraints();
		content.setLayout(layout);
		
		final JLabel lblEnterSmthng = new JLabel("Enter Something!");
		final JTextField txtField = new JTextField(30);
		JButton btnOk = new JButton("Ok");
		
		final JLabel lblCocktailName = new JLabel("Cocktail Name:");
		final JTextField fieldCocktailName = new JTextField(30);
		final JLabel lblInstructions = new JLabel("Instructions");
		final JTextArea txtaInstructions = new JTextArea();
		JButton btnCreateCocktail = new JButton("Anlegen");
		
		content.add(lblEnterSmthng,c);
		content.add(txtField,c);
		content.add(btnOk,c);
		content.add(lblCocktailName,c);
		content.add(fieldCocktailName,c);
		content.add(lblInstructions,c);
		content.add(txtaInstructions,c);
		content.add(btnCreateCocktail,c);
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
			}
		};
		ActionListener createCocktailAction = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Cocktail c = new Cocktail();
				c.name = fieldCocktailName.getText();
				c.instructions = txtaInstructions.getText();
				/*
				 * Assemble all Ingredients in Array
				 */
				Ingredient[] i = null;
				
				// Only for ze lulz, replace with actual data
				i = new Ingredient[3];
				i[0] = new Ingredient();
				i[0].description = "Ice";
				//i[0].quantity = 1;
				//i[0].unit = "";
				i[1] = new Ingredient();
				i[1].description = "Vodka (two Shots)";
				i[1].quantity = 8;
				i[1].unit = "cl";
				i[2] = new Ingredient();
				i[2].description = "Orange Juice";
				i[2].quantity = 10;
				i[2].unit = "cl";
				// The fun ends here
				
				createCocktailWithIngredients(c,i);
				System.out.println("id: " + c.cocktail_id);
				System.out.println("name: " + c.name);
				System.out.println("instructions: " + c.instructions);
			}
		};
		btnCreateCocktail.addActionListener(createCocktailAction);
		btnOk.addActionListener(submitAction);
		txtField.addActionListener(submitAction);
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
	
	static void createCocktailWithIngredients(Cocktail cocktail, Ingredient[] ingredients){
		Cocktail c = cocktail;
		Ingredient i[] = ingredients;
		dbc.createCocktail(c);
        for(Ingredient elem: i){
        	elem.cocktail_id = c.cocktail_id;
        	dbc.createIngredient(elem);
    	}
        
	}
}