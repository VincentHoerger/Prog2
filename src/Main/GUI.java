package Main;

public class GUI {
	import javax.swing.JFrame;
	import javax.swing.JScrollPane;
	import javax.swing.JTable;
	import java.util.Vector;

	import javax.swing.table.DefaultTableModel;
	import javax.swing.table.TableColumn;

	public class SimpleTableClass extends JFrame {

	    private final int small = 50;
	    private final int big = 150;

	    private JTable table;

	    public SimpleTableClass() {
	        table = new JTable(new SimpleTableModel());
	        setColumnWidth();
	        JScrollPane sPane = new JScrollPane(table);

	        getContentPane().add(sPane);
	        this.setSize(600, 250);
	        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        this.setVisible(true);
	    }

	    private void setColumnWidth() {
	        for (int i = 0; i < table.getColumnCount(); i++) {
	            TableColumn c = table.getColumnModel().getColumn(i);
	            switch (i) {
	            case 1:
	                c.setPreferredWidth(small);
	                break;

	            case 4:
	                c.setPreferredWidth(big);
	                break;

	            default:
	                c.setPreferredWidth(JTable.AUTO_RESIZE_ALL_COLUMNS);
	            }
	        }
	    }

	    public static void main(String[] args) {
	        new SimpleTableClass();
	    }

	}

	class SimpleTableModel extends DefaultTableModel {

	    private int rows = 30, cols = 10;
	    
	    private Object[] rowData = new Object[cols];

	    public SimpleTableModel() {
	        super();
	        initModelData();
	    }

	    private void initModelData() {
	        
	        for (int i = 0; i < cols; i++) {
	            this.addColumn(Integer.toString(i));
	        }

	        for (int j = 0; j < rows; j++) {
	            for (int i = 0; i < cols; i++) {
	                rowData[i] = j + " | " + i;
	            }
	            this.addRow(rowData);
	        }
	    }
	}
