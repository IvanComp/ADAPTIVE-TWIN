/** 
 * This class was automatically generated  
 * using a Merode XML model and Apache Velocity
 * 
 * Merode Code Generator 2.0
 * @author MERODE Team-members
 */
package ui.tabs.tables;

import handlers.MerodeMainEventHandler;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.*;
import java.util.Map.Entry;

import javax.swing.*;

import ui.tabs.tables.views.ObjectTableCellEditor;
import ui.tabs.tables.views.ObjectViewRenderer;
import ui.tabs.tables.views.PropertyViewWindow;

import driver.MerodeMainEventHandlerGui;



/**
 * Class that represents a table of objects. The table has these columns:
 * - An object counter
 * - One column for each attribute (currently limited to one)
 * - The name of the current state
 * - A check symbol to show whether the object is in the final state or not
 */
@SuppressWarnings("serial")
public class Property_Table extends ObjectTable {
    private MerodeMainEventHandler main_handler;
    private MerodeMainEventHandlerGui parent_win;

    public Property_Table(MerodeMainEventHandler main_h) {
        this (null, main_h);
    }

    public Property_Table(MerodeMainEventHandlerGui par_win, 
    		MerodeMainEventHandler main_h, String object_id) {
        this (main_h, object_id);
        parent_win = par_win;
    }

    public Property_Table(MerodeMainEventHandlerGui par_win, 
    		MerodeMainEventHandler main_h) {
        super();
        main_handler = main_h;
        parent_win = par_win;
    
        createColumns();
        refresh();
    }

    public Property_Table(MerodeMainEventHandler main_h, String object_id) {
        super();
        main_handler = main_h;
    
        try {
            objects = new ArrayList();
            Object obj = null;
            
            try {
            	obj = main_handler.searchPropertyById (object_id);
            } catch (Exception ex){
            	System.out.println ("not an instance");
            } 
            
            objects.add (obj);
    
            createColumns();
            attachRenderers();
            createRows();
        } catch (Exception e) {
            JOptionPane.showMessageDialog( null, 
            	e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public Property_Table(MerodeMainEventHandler main_h, Collection col) {
        super();
        main_handler = main_h;
    
        try {
        	objects = new ArrayList (col);
            Collections.sort(objects, new Comparator<Object>() {
				@Override
				public int compare(Object arg0, Object arg1) {
					return (((dao.Property) arg0).getId()
						.compareTo(((dao.Property) arg1).getId()));
				}
            });
            createColumns();
            attachRenderers();
            createRows();
        } catch (Exception e) {
            JOptionPane.showMessageDialog( null,
            	e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
	}

	protected void getAllObjects() {
        try {
            Collection c = main_handler.getAllProperty();
            objects = new ArrayList (c);
        } catch (Exception e) {
            JOptionPane.showMessageDialog( null,
            	e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    Map <Integer, String> keys = new HashMap();
    protected void createRows() {
        	ArrayList all = null;
    	try {
			Collection c = main_handler.getAllProperty();
        	all = new ArrayList (c);
            Collections.sort(all, new Comparator<Object>() {
				@Override
				public int compare(Object arg0, Object arg1) {
					return (((dao.Property) arg0).getId()
						.compareTo(((dao.Property) arg1).getId()));
				}
            });

            for (int i = 0; i < all.size(); i++){
            	keys.put(new Integer(i), ((dao.Property)all.get(i)).getId());
            }

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if ( all != null ){
			if(all.size() > objects.size() && keys != null){
				for (int i = 0; i < objects.size(); i++) {
					Vector v = new Vector();
					for (Entry<Integer, String> entry : keys.entrySet()) {
						if (entry.getValue().equals(
								((dao.Property) objects.get(i)).getId())) {
							v.add(entry.getKey());
						}
					}
					getRowData((dao.Property) objects.get(i), v);
					model.addRow(v);
				}
			} else {
    
		        for (int i = 0; i < objects.size(); i++) {
		            Vector v = new Vector();
		            v.add (new Integer(i)
		            	+ ":" + ((dao.Property) objects.get(i)).getMandatoryInconsistency());
		            getRowData((dao.Property)objects.get(i), v);
		            model.addRow(v);
		        }
        	}
		}
    }

    protected void createColumns() {
        model.addColumn("<html><b>ID</b></html>");
        model.addColumn("<html><b>Featureofinterestname</b></html>");
        model.addColumn("<html><b>state</b></html>");
        model.addColumn(messages.MessageProperties.COLUMN_FINAL);
        model.addColumn("more...");
        getColumnModel().getColumn(0).setPreferredWidth(5); //index
        getColumnModel().getColumn(4 - 1).setPreferredWidth(15); //final state checkbox
        getColumnModel().getColumn(4).setPreferredWidth(25);
    }

    protected void attachRenderers() {
    	model.editableColumns.add(new Integer(4));
        getColumnModel().getColumn(4).setCellRenderer(
        		new ObjectViewRenderer( main_handler, objects, this )
        );
    	getColumnModel().getColumn(4).setCellEditor(
        		new propertyAllCollectionsEditor( parent_win, 
        			main_handler, objects, this)
        );
    	
    }

    private void getRowData (dao.Property object, Vector vector) {
    
        try {
            	object = main_handler.searchPropertyById (object.getId());
            	vector.add (object.getFeatureofinterestname()); 
            } catch (Exception ex){
            	System.out.println ("not an instance");
            } 
    
                        vector.add (object.getState().getName());
        vector.add (new Boolean(object.getState().isFinalState()));
    }
}

  /**
   * Class to edit cells that represent a reference to an object view
   * It contains:
   * - A button to invoke view window
   */
    class propertyAllCollectionsEditor extends ObjectTableCellEditor {
      private ArrayList objects;
      private MerodeMainEventHandlerGui parent_win;
      private MerodeMainEventHandler main_handler;
      private JTable table;
      
      
      
      public propertyAllCollectionsEditor(
      		MerodeMainEventHandlerGui par_win, 
      			MerodeMainEventHandler main_h, 
      				ArrayList obj, JTable tab) {
          super();
          objects = obj;
          parent_win = par_win;
          main_handler = main_h;
          table = tab;
      	  setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

      	  final JButton but = new JButton(messages.MessageProperties.VIEW);
  		  Icon i = new ImageIcon(this.getClass().getResource("views/magnifier.png"));
  		  if (i != null) {
  	  		  but.setIcon(i);  			  
  		  }
  		  but.setIcon(i);
      	  but.setPreferredSize( new Dimension(20, but.getHeight()));
      	  
      	  but.addActionListener( new ActionListener() {
      		  public void actionPerformed (ActionEvent event) {
      			  System.out.println("Index: " + table.getSelectedRow());
      	    	  //but.setEnabled(false);
      	     		dao.Property object = (dao.Property)objects.get(table.getSelectedRow());
    	    		String id = object.getId();
    	    		//FIXME - object needs to come fresh
    	    		if (main_handler != null){
    	        		try {
    	    				object = main_handler.searchPropertyById (id);
    	    			} catch (Exception e) {
    	    				// TODO Auto-generated catch block
    	    				e.printStackTrace();
    	    			}
    	    	}
    	    	PropertyViewWindow thePropertyViewWindow =
    	      		new PropertyViewWindow (parent_win, main_handler, object);

    	        final JFrame frame = new JFrame();
    	        frame.setTitle("Property "); /*+ (table.getSelectedRow())*/;
    	        frame.setPreferredSize(new Dimension(660, 600));
    	        Toolkit tk = Toolkit.getDefaultToolkit();
    	        Dimension screenSize = tk.getScreenSize();
    	        final int WIDTH = screenSize.width;
    	        final int HEIGHT = screenSize.height;
    	        // Setup the frame accordingly
    	        frame.setLocation(WIDTH /2, HEIGHT/ 8);
    	        JScrollPane scrollpane = new JScrollPane(thePropertyViewWindow);
    	        frame.add(scrollpane);
    	        
    			// this centers the frame on the screen
    			//frame.setLocationRelativeTo(null);
    	        frame.pack();
       			validateAndShowDetailWindow(frame, (dao.Property)object);
      			
      		  }
      	  });
      	  
      	  add (but);
      }

      private void validateAndShowDetailWindow(JFrame frame, 
      			final dao.Property object) {
      	  final Map <String, JFrame> openViewInstances = MerodeMainEventHandlerGui.openViewInstances;
     	  if (openViewInstances.containsKey(object.getId())){
    		  System.out.println("open dialog detected for this object");
    	        for (Object entry : openViewInstances.keySet()) {
    	            if (entry.equals(object.getId())) {
    	                //System.out.println(openViewInstances.get(index));
    	                frame = openViewInstances.get(object.getId());
    	                frame.toFront();
    	                frame.setState(JFrame.NORMAL);

    	            } 
    	        }

    	  } else {
    	        frame.addWindowListener(new WindowAdapter() {
    	            public void windowClosing(WindowEvent e) {
    	            	//b.setEnabled(true);
    	            	openViewInstances.remove(object.getId());
    	            }
    	        });
    	        frame.setVisible(true);
    	        openViewInstances.put(object.getId(), frame);
    	        
    	  }
       }

 }



