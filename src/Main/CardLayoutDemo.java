package Main;

/*
* CardLayoutDemo.java
*
*/
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;





public class CardLayoutDemo implements ItemListener{
    JPanel cards; //a panel that uses CardLayout
    final static String BUTTONPANEL = "Rezepte Anschauen/Suchen";
    final static String BUTTONPANEL1 = "Neues Rezept Anlegen";
    final static String BUTTONPANEL2 = "Rezepte Ändern/Löschen";
	private static final String TEXTPANEL = null;
    
    
    public void addComponentToPane(Container pane) {
        //Put the JComboBox in a JPanel to get a nicer look.
        JPanel comboBoxPane = new JPanel(); //use FlowLayout
        String comboBoxItems[] = { BUTTONPANEL, BUTTONPANEL1, BUTTONPANEL2, TEXTPANEL};
        JComboBox cb = new JComboBox(comboBoxItems);
        cb.setEditable(false);
        cb.addItemListener(this);
        comboBoxPane.add(cb);
        
        //Create the "cards".
        JPanel card1 = new JPanel();
        card1.add(new JTextField(30));
        card1.add(new JButton("Suchen"));
        
        
        
        JPanel card2 = new JPanel();
        card2.add(new JTextField("Name", 20));
        card2.add(new JTextField("Beschreibung", 20));
        card2.add(new JTextField("Zutaten", 20));
        card2.add(new JButton("Erstellen"));
        
        
        JPanel card3 = new JPanel();
        card3.add(new JButton("Ändern"));
        card3.add(new JButton("Löschen"));
        
        
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
