/**
 * 
 */
package main;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JPanel;

import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * @author Ioana-Balan
 * @author Adrian-Gemeniuc
 */
public class AppControlPanel extends JPanel {


	public static StringBuilder wholeFileToString = new StringBuilder();
	private static final long serialVersionUID = 1L;
	public AppFrame frame;
	JButton previewPlanBtn;
	JButton previewScheduleBtn;
	JButton computePathBtn;
	AppBuildingTree t;
	AppFilePane appFilePane;
	/**
	 * 
	 */

	public static AppBuildingTree beta;
	public AppControlPanel alpha;

	public void setBeta(AppBuildingTree newBeta) {
		this.beta = newBeta;
	}

	public AppControlPanel(AppFrame frame) {
		this.frame = frame;
		//***********************************************
			//	This is for using the method from another class	 //
		beta = new AppBuildingTree();
		alpha = new AppControlPanel();
		alpha.setBeta(beta);
		appFilePane = new AppFilePane();
		//************************************************
		init();
	}

	public AppControlPanel() {
		// TODO Auto-generated constructor stub
	}

	
	private void init() {
		/* main interface implementation occurs here */

		previewPlanBtn = new JButton("Preview Plan");
		previewScheduleBtn = new JButton("Preview Schedule");
		computePathBtn = new JButton("Compute Shortest Path");

		// 		Listenuri pentru butoane		//
		previewPlanBtnListener(previewPlanBtn);
		previewScheduleBtnListener(previewScheduleBtn);
		computePathBtnListener(computePathBtn);

		this.add(previewPlanBtn);
		this.add(previewScheduleBtn);
		this.add(computePathBtn);

	}

	private void computePathBtnListener(JButton computePathBtn) {
		// TODO Auto-generated method stub

	}

	private void previewScheduleBtnListener(JButton previewScheduleBtn) {

		previewScheduleBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				addListenerToJsonFile(AppBuildingTree.pathToFile);
			}

		});
	}

	public void addListenerToJsonFile(String pathToFile) {
		
		try {
			String line = null;
			FileReader fileReader = new FileReader(pathToFile);
			BufferedReader bufferReader = new BufferedReader(fileReader);

			try {
				while ((line = bufferReader.readLine()) != null) {
					System.out.println(line);
					wholeFileToString.append(line).append('\n');
					// aici ar trebui sa fie:
					// add(AppBuildingTree.shortFileName,line);
				}
			} catch (IOException e) {

			}
			try {
				bufferReader.close();
			} catch (IOException e) {

			}
		} catch (FileNotFoundException e) {

		}

		// System.out.println(wholeFileToString);
		// asta ar trebui sa creeze un nou tab
		appFilePane.makeTextPanel(wholeFileToString.toString());

		// appFilePane.addPanel("Building2.json",
		// AppControlPanel.wholeFileToString.toString());
	}

	private void previewPlanBtnListener(JButton previewPlanBtn) {
		// TODO Auto-generated method stub

	}

}
