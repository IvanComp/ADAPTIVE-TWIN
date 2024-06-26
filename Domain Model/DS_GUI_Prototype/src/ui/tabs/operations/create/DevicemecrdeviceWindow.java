/** 
 * This class was automatically generated  
 * using a Merode XML model and Apache Velocity
 * 
 * Merode Code Generator 2.0
 * @author MERODE Team-members
 */

package ui.tabs.operations.create;

import handlers.MerodeMainEventHandler;

import javax.swing.*;

import validation.ErrorValidator;

import java.awt.*;

import ui.tabs.lists.ObjectListWindow;
import ui.tabs.operations.DeviceOperationWindow;

import java.lang.reflect.Field;
import java.text.*;

import com.toedter.calendar.JDateChooser;

import dao.MerodeException;
import driver.MerodeMainEventHandlerGui;


/**
 * Windows to create objects. They have:
 * - One textfield for each attribute
 * - One object table per master object
 * - One button to execute the operation
 */
@SuppressWarnings("serial")
public class DevicemecrdeviceWindow extends DeviceOperationWindow {
    
    public DevicemecrdeviceWindow (MerodeMainEventHandlerGui main_frame, MerodeMainEventHandler main_handler, ObjectListWindow list_frame) {
        super(main_frame, main_handler, list_frame, "mecrdevice");
        buildFrame(getContentPane());
    }


    protected void executeMethod() {
        try {
            boolean hasErrors = false;
            String errors = "";

            DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
            if (t_Name.getText().trim().length()==0) {
                hasErrors = true;
                errors = errors + "Please fill in attribute: Name\n";
            }
 
            if (t_Description.getText().trim().length()==0) {
                hasErrors = true;
                errors = errors + "Please fill in attribute: Description\n";
            }
 
            if (t_Status.getText().trim().length()==0) {
                hasErrors = true;
                errors = errors + "Please fill in attribute: Status\n";
            }
             else {
            	if (!
                	 ("true".equals(t_Status.getText().toLowerCase()) 
                		|| "false".equals(t_Status.getText().toLowerCase())
                	  )
                	) {        		
            		//not Status input
                    hasErrors = true;
                    errors = errors + "Please provide a valid BOOLEAN input, e.g. TRUE or FALSE: Status\n";
            	}
	         }
 
            if (hasErrors) {
                Field[] privateMembers = this.getClass().getDeclaredFields();
                String objName = this.getTitle().substring(0, this.getTitle().lastIndexOf(":")).toUpperCase();
            	ErrorValidator.processErrors(errors, privateMembers, objName);
            } else {
                boolean success = main_handler.mecrdevice (
                 t_Name.getText(), t_Description.getText(),t_Status.getText());
                if (success){
                    //list_frame.table.refresh();
                    main_frame.refreshTables();
                    
                    dispose();
                    list_frame.setVisible(true);              	
                }
            }
        } catch (MerodeException me) {
        	ErrorValidator.processExceptions(me);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}