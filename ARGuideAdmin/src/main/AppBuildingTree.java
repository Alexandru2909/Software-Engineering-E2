/**
 * 
 */
package main;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author Ioana-Balan
 *
 */
public class AppBuildingTree extends JPanel {

	/* Don't forget to generate comments that are able to be 
	 * parsed for the creation of a JavaDoc!
	 * Automatic comment generation shortcut: Alt + Shift + J
	*/
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 6240342843739611004L;
	public  AppFrame frame;
	JTree buildingTree;
	
	/* add tree nodes here
	 * this may be of help: https://docs.oracle.com/javase/tutorial/uiswing/components/tree.html
	 */
	
	DefaultMutableTreeNode buildings = new DefaultMutableTreeNode("Buildings");
	DefaultMutableTreeNode buildingA = new DefaultMutableTreeNode("Building A");
	DefaultMutableTreeNode buildingB = new DefaultMutableTreeNode("Building B");
	DefaultMutableTreeNode buildingC = new DefaultMutableTreeNode("Building C");
	DefaultMutableTreeNode buildingD = new DefaultMutableTreeNode("Building D");
	DefaultMutableTreeNode buildingPlanA = new DefaultMutableTreeNode("Building_Plan.json");
	DefaultMutableTreeNode buildingScheduleA = new DefaultMutableTreeNode("Building_Schedule.json");
	DefaultMutableTreeNode buildingPlanB = new DefaultMutableTreeNode("Building_Plan.json");
	DefaultMutableTreeNode buildingScheduleB = new DefaultMutableTreeNode("Building_Schedule.json");
	DefaultMutableTreeNode buildingPlanC = new DefaultMutableTreeNode("Building_Plan.json");
	DefaultMutableTreeNode buildingScheduleC = new DefaultMutableTreeNode("Building_Schedule.json");
	DefaultMutableTreeNode buildingPlanD = new DefaultMutableTreeNode("Building_Plan.json");
	DefaultMutableTreeNode buildingScheduleD = new DefaultMutableTreeNode("Building_Schedule.json");
	
	/**
	 * 
	 */
	public AppBuildingTree(AppFrame frame) {
		this.frame=frame;
		init();
	}
	
	private void init() {
		/* main interface implementation occurs here */
		
		buildingA.add(buildingPlanA);
		buildingA.add(buildingScheduleA);
		buildingB.add(buildingPlanB);
		buildingB.add(buildingScheduleB);
		buildingC.add(buildingPlanC);
		buildingC.add(buildingScheduleC);
		buildingD.add(buildingPlanD);
		buildingD.add(buildingScheduleD);
		
		buildings.add(buildingA);
		buildings.add(buildingB);
		buildings.add(buildingC);
		buildings.add(buildingD);
		
		buildingTree=new JTree(buildings);
		this.add(buildingTree);
	}

}
