
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.JMenu;
import javax.swing.JPanel;

/**
 * @author Adrian-Gemeniuc
 *
 */

public class AppMenu extends JPanel {

	
	private static final long serialVersionUID = 199316383000490255L;
	AppFrame frame;
	
	public AppMenu(AppFrame frame) {
		this.frame = frame;
		init();
		
		// here I need the main frame of the application somehow
		frame.setJMenuBar(createMenuBar());
	}
	
	
	
	private void init() {
		/* main interface implementation occurs here */
		
	}
	
	
	/**
	 * @return: the new menu that had been created
	 */
	public static JMenuBar createMenuBar() {

		JMenuBar menuBar;
		JMenu menu, submenu;
		JMenuItem menuItem;

		// BARA MENIU
		menuBar = new JMenuBar();

		// FILE Menu.
		menu = new JMenu("File");	
		menuBar.add(menu);

		// MENIUL "NEW" -> URMAT DE UN SUBMENIU
		menu.addSeparator();
		submenu = new JMenu("New");
		
		menuItem = new JMenuItem(" Building");	
		submenu.add(menuItem);
		menu.add(submenu);

		// OPEN-ul simplu
		menu.addSeparator();
		menuItem = new JMenuItem("Open");
		menu.add(menuItem);

		// SAVE-ul simplu
		menu.addSeparator();
		menuItem = new JMenuItem("Save");
		menu.add(menuItem);

		// SAVE AS
		menu.addSeparator();
		menuItem = new JMenuItem("Save as ...");
		menu.add(menuItem);

		// CLOSE
		menu.addSeparator();
		menuItem = new JMenuItem("Close");
		menu.add(menuItem);

		// HELP-ul din MENIU
		menu = new JMenu("Help");
		menuBar.add(menu);
		
		return menuBar;

	}

}
