/**
 * 
 */
package main;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author Ioana-Balan
 *
 */
public class AppControlPanel extends JPanel {
	
	/* Don't forget to generate comments that are able to be 
	 * parsed for the creation of a JavaDoc!
	 * Automatic comment generation shortcut: Alt + Shift + J
	*/
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public  AppFrame frame;
	JButton previewPlanBtn;
	JButton previewScheduleBtn;
	JButton computePathBtn;

	/**
	 * 
	 */
	public AppControlPanel(AppFrame frame) {
		this.frame=frame;
		init();
	}
	
	/**
	 * 
	 */
	private void init() {
		/* main interface implementation occurs here */
		
		previewPlanBtn=new JButton("Preview Plan");
		previewScheduleBtn=new JButton("Preview Schedule");
		computePathBtn=new JButton("Compute Shortest Path");
		
		this.add(previewPlanBtn);
		this.add(previewScheduleBtn);
		this.add(computePathBtn);
		
	}

}
