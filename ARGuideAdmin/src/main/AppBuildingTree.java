/**
 * 
 */
package main;


import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javafx.scene.control.RadioButton;

/**
 * @author Ioana-Balan
 * @author Adrian-Gemeniuc
 *
 */
public class AppBuildingTree extends JPanel {

	public DefaultMutableTreeNode copy, roote;
	public static DefaultMutableTreeNode buildings = new DefaultMutableTreeNode("Buildings");
	private static final long serialVersionUID = 6240342843739611004L;
	public AppFrame frame;
	public  JTree buildingTree, secondTree;
	public DefaultMutableTreeNode c;
	public AppBuildingTree copyOfCurrentObject;
	public DefaultTreeModel modele;
	public AppBuildingTree dc;
	public DefaultMutableTreeNode root;
	public JTree rootSubstitute;
	public int s = 0;
	public static String pathToFile;
	public static String shortFileName;
	// *********************************************
	public AppMenu beta;
	public AppBuildingTree alpha;

	public void setBeta(AppMenu newBeta) {
		this.beta = newBeta;
	}

	public AppBuildingTree(AppFrame frame) {
		this.frame = frame;

		beta = new AppMenu(frame);
		alpha = new AppBuildingTree();
		alpha.setBeta(beta);
		init();

	}
	// ****************************************************

	public AppBuildingTree() {

	}

	public void init() {
		/* main interface implementation occurs here */
		buildingTree = new JTree(buildings);
		//addRightClickListener(buildingTree);

		add(buildingTree);
		//addRightClickListener(buildingTree);
		// dc = this;
		 createBuilding("Faculty");
	}

	public void createBuilding(String name) {
		
		removeAll();
		root = new DefaultMutableTreeNode(name);
		
		// root -- Faculty

		buildings.add(root);
		buildingTree = new JTree(buildings);
		
		add(buildingTree);
		// now i can view the whole tree 
		
		addRightClickListener(buildingTree);
	
	
		
		//addListenerRoot(root);
		// dc = this;
		/*
		  removeAll();
		  modele = (DefaultTreeModel) buildingTree.getModel(); 
		  roote = (DefaultMutableTreeNode) modele.getRoot(); 
		  roote.add(new DefaultMutableTreeNode(name)); modele.reload();
		 
		  add(buildingTree); 
		  addRightClickListener(buildingTree);
		  modele.reload();
		 
*/
		
		  
		 

	}
 
	public void addRightClickListener(JTree buildingTree) {

		
		buildingTree.addTreeSelectionListener(new javax.swing.event.TreeSelectionListener() {
			public void valueChanged(javax.swing.event.TreeSelectionEvent evt) {
				
				jTreeValueChanged(evt);
			}
			
		});
	}

	public void jTreeValueChanged(TreeSelectionEvent tse) {

		String node = tse.getNewLeadSelectionPath().getLastPathComponent().toString();

		if (!node.contains(".") && (!node.equalsIgnoreCase("buildings")))
			createAddScheduleAndPanel();

	}

	private void createAddScheduleAndPanel() {
		class RadioButtonExample extends JFrame implements ActionListener {

			JRadioButton rb1, rb2;
			JButton b;

			public RadioButtonExample() {
				rb1 = new JRadioButton("Add Building Plan");
				rb1.setBounds(100, 50, 200, 30);
				rb2 = new JRadioButton("Add Building Schedule");
				rb2.setBounds(100, 100, 200, 30);

				ButtonGroup bg = new ButtonGroup();
				bg.add(rb1);
				bg.add(rb2);

				b = new JButton("GO!");
				b.setBounds(100, 150, 80, 30);
				b.addActionListener(this);
				add(rb1);
				add(rb2);
				add(b);

				setSize(300, 300);
				setLayout(null);
				setVisible(true);
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				if (rb1.isSelected()) {
					uploadPlan();
					s++;

				}
				if (rb2.isSelected()) {
					uploadSchedule();
					s++;
				}
				if (s == 2)
					this.dispose();

			}

			public void uploadSchedule() {

				JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
				int returnValue = jfc.showOpenDialog(null);

				if (returnValue == JFileChooser.APPROVE_OPTION) {
					// aici voi avea fisierul JSON
					File selectedFile = jfc.getSelectedFile();
					pathToFile = selectedFile.getAbsolutePath();

					beta.pathMenu = pathToFile;
					String file = selectedFile.getName().toString();
					shortFileName = selectedFile.getName().toString();

					addScheduleToTree(file);

					System.out.println(selectedFile.getAbsolutePath());
				}

			}

			private void addScheduleToTree(String file) {

				
				// ***********************************************
				// System.out.println("wtf coe");
				// copyOfCurrentObject.removeAll();
				// buildingTree=new JTree(buildings);

				// modele = (DefaultTreeModel) buildingTree.getModel();
				// roote = (DefaultMutableTreeNode) modele.getRoot();
				// roote.add(new DefaultMutableTreeNode(file));
				// modele.reload(roote);

				// addRightClickListener(buildingTree);
				// copyOfCurrentObject.add(buildingTree);

				// ****************************************************

				// DefaultMutableTreeNode child1 = new DefaultMutableTreeNode("else.json");
				DefaultMutableTreeNode child2 = new DefaultMutableTreeNode(file);
				// addListerToJsonFile(pathToFile);
				// root.add(child1);
				root.add(child2);

				buildings.add(root);
				buildingTree = new JTree(buildings);
				add(buildingTree);

				
			}

			private void uploadPlan() {
				System.out.println("apar in update plan");
			}

		}

		new RadioButtonExample();
	}

	/*
	 * public void createNewBuilding(String buildingName, String buildingPlan,
	 * String buildingSchedule) {
	 * 
	 * this.removeAll();
	 * 
	 * DefaultMutableTreeNode buildingA = new DefaultMutableTreeNode(buildingName);
	 * DefaultMutableTreeNode buildingPlanA = new
	 * DefaultMutableTreeNode(buildingPlan); DefaultMutableTreeNode
	 * buildingScheduleA = new DefaultMutableTreeNode(buildingSchedule);
	 * 
	 * // addPlanListener(buildingPlanA); //addScheduleListener(buildingScheduleA);
	 * 
	 * buildingA.add(buildingPlanA); buildingA.add(buildingScheduleA);
	 * buildings.add(buildingA);
	 * 
	 * buildingTree=new JTree(buildings); this.add(buildingTree);
	 * 
	 * }
	 * 
	 */
	public void addScheduleListener(DefaultMutableTreeNode buildingScheduleA) {

	}

	

}
