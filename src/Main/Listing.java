package Main;

	 import java.awt.*;
	 import java.awt.event.*;
	 import javax.swing.*;
	 
	 @SuppressWarnings("serial")
	public class Listing
	 extends JFrame
	 implements TableData
	 {
	   public Listing()
	   {
	     super("JTable 1");
	     //addWindowListener(new WindowClosingAdapter(true));
	     JTable table = new JTable(DATA, COLHEADS);
	     Container cp = getContentPane();
	     cp.add(new JLabel("Alte c\'t-Ausgaben:"), BorderLayout.NORTH);
	     cp.add(new JScrollPane(table), BorderLayout.CENTER);
	   }
	 
	   public static void main(String[] args)
	   {
	    Listing frame = new Listing();
	     frame.setLocation(100, 100);
	     frame.setSize(300, 200);
	     frame.setVisible(true);
	   }
	 }

