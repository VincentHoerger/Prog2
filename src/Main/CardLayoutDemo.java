package Main;

/*
* CardLayoutDemo.java
*
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;






public class CardLayoutDemo implements ItemListener{
	
	static DB dbc = DB.getInstance();
	
	
    JPanel cards; //a panel that uses CardLayout
    
    final static String BUTTONPANEL = "Rezepte Anschauen/Suchen";
    final static String BUTTONPANEL1 = "Neues Rezept Anlegen";
    final static String BUTTONPANEL2 = "Rezepte Ändern/Löschen";
    
    
    public void addComponentToPane(Container pane) {
    	
        //Put the JComboBox in a JPanel to get a nicer look.
        JPanel comboBoxPane = new JPanel(); //use FlowLayout
        String comboBoxItems[] = { BUTTONPANEL, BUTTONPANEL1, BUTTONPANEL2};
        JComboBox cb = new JComboBox(comboBoxItems);
        cb.setEditable(false);
        cb.addItemListener(this);
        comboBoxPane.add(cb);
        
        //Create the "cards".
        JPanel card1 = new JPanel();
        final JTextField fieldSearch = new JTextField(30);
        card1.add(fieldSearch);
        JButton suchen = new JButton("Suchen");
        card1.add(suchen);
        
        suchen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	String txt = fieldSearch.getText();
                System.out.println("Hier wird gesucht: " + txt);
                Cocktail c  = dbc.getCocktailInfo(Integer.parseInt(txt));
                System.out.println(c.name);
                System.out.println(c.instructions);
            }
        });
        
        JPanel card2 = new JPanel();
        //final JTextField fieldSearch = new JTextField(30);
        //final JTextField fieldSearch = new JTextField(30);
        //final JTextField fieldSearch = new JTextField(30);
        card2.add(new JTextField("Name", 20));
        card2.add(new JTextField("Beschreibung", 20));
        card2.add(new JTextField("Zutaten", 20));
        JButton erstellen = new JButton("erstellen");
        card2.add(erstellen);
        
        erstellen.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Hier wird erstellt");
            }
        });
        
        
        JPanel card3 = new JPanel();
        JButton edit = new JButton("Ändern");
        card3.add(edit);
        
        edit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Hier wird geändert");
            }
        });
        
        
        JButton delete = new JButton("Löschen");
        card3.add(delete);
        
        delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Hier wird gelöscht");
            }
        });
        
        
        //Create the panel that contains the "cards".
        cards = new JPanel(new CardLayout());
        cards.add(card1, BUTTONPANEL);
        cards.add(card2, BUTTONPANEL1);
        cards.add(card3, BUTTONPANEL2);
        
        pane.add(comboBoxPane, BorderLayout.PAGE_START);
        pane.add(cards, BorderLayout.CENTER);
        
        
    }
    
    public void itemStateChanged(ItemEvent evt) {
        CardLayout cl = (CardLayout)(cards.getLayout());
        cl.show(cards, (String)evt.getItem());
        
    }
    
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
   private static void createAndShowGUI() {
	   
        //Create and set up the window.
        JFrame frame = new JFrame("COCKTAIL-REZEPTE");
        
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Create and set up the content pane.
        CardLayoutDemo demo = new CardLayoutDemo();
        demo.addComponentToPane(frame.getContentPane());
        
        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }
    
    public static void main(String[] args) {

        dbc.initDBConnection();
        /* Use an appropriate Look and Feel */
        try {
            //UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
        } catch (UnsupportedLookAndFeelException ex) {
            ex.printStackTrace();
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();
        } catch (InstantiationException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        /* Turn off metal's use of bold fonts */
        UIManager.put("swing.boldMetal", Boolean.FALSE);
        
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
                
            }
        });
    }
}
