package main;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;
import java.io.File;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

/**
 * @author Adrian-Gemeniuc
 *
 */
import java.awt.Dimension;
import java.awt.GridLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

/**
 * @author Adrian-Gemeniuc
 *
 */

public class AppFilePane extends JPanel{

	private static final long serialVersionUID = -6663025488807576018L;
	public AppFrame frame;
	public JTabbedPane tabbedPane = new JTabbedPane();
	public JPanel textPanel;
	public JTextArea textArea;
	public JScrollPane scrollPane;
	
	public AppFilePane(AppFrame frame) {
		
		super(new GridLayout(1, 1));
		this.frame = frame;
		init();

		// some examples of adding a tab name (Building1) and a tab content (panel1) to
		// the tabbedPane
		// addPanel("Building1.json", "panel1");

		// addPanel(tabbedPane,"Building3.json", "panel3");
		// addPanel(tabbedPane,"Building4.json", "panel4");

		tabbedPane.setPreferredSize(new Dimension(100, 100));

		makeTextPanel("This is the place for the Schedule && Plan, for now, the preview of the schedule is made in the console");
		

		// Add the tabbed pane to this panel.
		//add(tabbedPane);
	}

	public AppFilePane() {
		// TODO Auto-generated constructor stub
	}

	private void init() {
		/* main interface implementation occurs here */
	}

	/**
	 * @param text
	 * @return: displays the text from the file on every tab
	 */
	/*
	public JComponent makeTextPanel(String text) {

		JPanel panel = new JPanel(false);
		JLabel filler = new JLabel(text);

		filler.setVerticalAlignment(JLabel.TOP);
		filler.setPreferredSize(new Dimension(100, 100));
		panel.setLayout(new GridLayout(1, 1));
		// panel.setSize(new Dimension(100, 100));

		panel.add(filler);
		return panel;
	}
*/
	public void makeTextPanel(String file) {
		
		textPanel = new JPanel();
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setPreferredSize(new Dimension(1200, 575));
		
	//	scrollPane = new JScrollPane(textArea);
		//scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		//scrollPane.setBounds(10, 11, 455, 249);
		textArea.setText(file);
		textPanel.add(textArea);
		tabbedPane.add("tab1", textPanel);
		add(tabbedPane);
		//invalidate();
		//validate();
		//repaint();
		
		//setVisible(false);
		//setVisible(true);
		
		//textPanel.add(scrollPane);
	}

	/**
	 * method to add new json files to the menu
	 * @param         fileName, the one that you see on top
	 * @param content -- inside the panel
	 * @return
	 */
/*
	public void addPanel(String fileName, String content) {
		JComponent panel = makeTextPanel(content);
		// tabbedPane.addTab(fileName, panel);
		tabbedPane.insertTab(fileName, null, panel, null, 0);
		// return(add(tabbedPane));

	}
*/
	

}
