/**
 * 
 */
package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * @author Paul-Reftu
 *
 */
public class AppFrame extends JFrame {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8806044845373193215L;
	AppMenu menu;
	AppBuildingTree buildingTree;
	AppFilePane filePane;
	AppControlPanel controlPanel;

	/**
	 * 
	 */
	public AppFrame() {
		super("ARGuide Admin Tool");
		init();
	}
	
	private void init() {
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds(0, 0, screenSize.width, screenSize.height);
		
		BorderLayout layout = new BorderLayout();
		setLayout(layout);
		
		/* We may certainly need to pass certain components to others when we will provide the inner functionalities
		 * of the application - otherwise interoperability will not be possible.
		 */
		menu = new AppMenu(this);
		buildingTree = new AppBuildingTree();
		filePane = new AppFilePane(this);
		controlPanel = new AppControlPanel();
		
		add(menu, BorderLayout.NORTH);
		add(buildingTree, BorderLayout.WEST);
		add(filePane, BorderLayout.CENTER);
		add(controlPanel, BorderLayout.SOUTH);
	}

}
