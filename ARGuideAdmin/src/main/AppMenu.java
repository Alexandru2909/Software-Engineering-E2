package main;


import java.awt.*;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import java.awt.event.*;

/**
 * @author Adrian-Gemeniuc
 *
 */

public class AppMenu extends JPanel {

	// alpha -- app menu --
	// beta -- app builidng tree -- fore
	private static final long serialVersionUID = 199316383000490255L;
	AppFrame frame;
	public AppBuildingTree fore;
	public DefaultMutableTreeNode roote, buildings = new DefaultMutableTreeNode("Buildings");
	public static JTree buildingTree, secondTree;
	public DefaultMutableTreeNode c;
	public AppBuildingTree copyOfCurrentObject;
	public DefaultTreeModel modele;

	// *****************************************************
	public static AppBuildingTree beta;
	public AppMenu alpha;
	public String pathMenu;

	public void setBeta(AppBuildingTree newBeta) {
		this.beta = newBeta;
	}

	public AppMenu(AppFrame frame) {
		this.frame = frame;
		// fore = new AppBuildingTree();
		// fore.init();

		beta = new AppBuildingTree();
		alpha = new AppMenu();
		alpha.setBeta(beta);

		init();

		frame.setJMenuBar(createMenuBar());
	}

	public AppMenu() {
		// TODO Auto-generated constructor stub
	}

	private void init() {
		/* main interface implementation occurs here */

	}

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
		addBuildingListener(menuItem);

		submenu.add(menuItem);
		menu.add(submenu);

		// OPEN-ul simplu
		menu.addSeparator();
		menuItem = new JMenuItem("Open");
		addOpenListener(menuItem);
		menu.add(menuItem);

		// SAVE-ul simplu
		menu.addSeparator();
		menuItem = new JMenuItem("Save");
		addSaveListener(menuItem);
		menu.add(menuItem);

		// SAVE AS
		menu.addSeparator();
		menuItem = new JMenuItem("Save as ...");

		menu.add(menuItem);

		// CLOSE
		menu.addSeparator();
		menuItem = new JMenuItem("Close");
		addCloseListener(menuItem);
		menu.add(menuItem);

		// HELP-ul din MENIU
		menu = new JMenu("Help");
		addHelpListener(menu);
		menuBar.add(menu);

		return menuBar;

	}

	private static void addHelpListener(JMenu menu) {
		// TODO Auto-generated method stub

	}

	private static void addCloseListener(JMenuItem menuItem) {
		// TODO Auto-generated method stub

	}

	private static void addSaveListener(JMenuItem menuItem) {
		// TODO Auto-generated method stub

	}

	private static void addOpenListener(JMenuItem menuItem) {
		// TODO Auto-generated method stub

	}

	private static void addBuildingListener(JMenuItem menuItem) {

		menuItem.addActionListener(new ActionListener() {

			@Override

			public void actionPerformed(ActionEvent e) {

				JFrame insertName = new JFrame();
				Object result = JOptionPane.showInputDialog(insertName, "Enter building name:");
				String buildingName = result.toString();
				beta.createBuilding(buildingName);

				// AppBuildingTree.createBuilding(buildingName);
			}
		});
	}

	public void doNothing() {
		System.out.println("o afacere");
	}

}

