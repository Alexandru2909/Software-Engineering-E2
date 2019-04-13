import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Window;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


/**
 * @author Adrian-Gemeniuc
 *
 */

public class AppFilePane extends JPanel {
    
 
    private static final long serialVersionUID = -6663025488807576018L;
    public  AppFrame frame;
    
   
    public AppFilePane(AppFrame frame) {
    	super(new GridLayout(1, 1));
		this.frame = frame;
        init();
       // createAndShowGUI();
		
       
         
       JTabbedPane tabbedPane = new JTabbedPane();
       
        addPanel(tabbedPane,"Building1.json", "panel1QKWEQWEQKWJEHQKWJEHQKWJEHKJQWHEKQJWHEKQWJEHQKWJEHQWKJEHQWJKNNSZKNDKSJNDAMNSDASNDSAKJDASJKDASKJDSAJDKHQJKWEHQKJWHEQKWJEQWUEUIFHDUSDIHAIUSDHSAIHDUIASHQIUHEIQWHEQWIUHEQIWEHQUWIHEIQWHEQWIEHQIWHEQIH656464");   
        addPanel(tabbedPane,"Building2.json", "panel2");   
        addPanel(tabbedPane,"Building3.json", "panel3");  
        addPanel(tabbedPane,"Building4.json", "panel4");
       
        tabbedPane.setPreferredSize(new Dimension(100, 100));

        
        //Add the tabbed pane to this panel.
        add(tabbedPane);            
    }
    
    

    private void init() {
        /* main interface implementation occurs here */
    }



     /**
     * @param text
     * @return: displays the text from the file on every tab
     */
    protected JComponent makeTextPanel(String text) {
        
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        //Dimension d = new Dimension(30, 100);
		//filler.setHorizontalAlignment(JLabel.CENTER);
       
        filler.setVerticalAlignment(JLabel.TOP);
        filler.setPreferredSize(new Dimension(100, 100));
        panel.setLayout(new GridLayout(1,1));
       // panel.setSize(new Dimension(100, 100));
        
        panel.add(filler);
        return panel;
    }

    
	/**
	 * method to add new json files to the menu
	 * @param tabbedPane
	 * @param fileName
	 * @param content -- inside the panel
	 */
    
	public void addPanel(JTabbedPane tabbedPane,String fileName, String content)
	{
		JComponent panel = makeTextPanel(content);
        tabbedPane.addTab(fileName, panel);
        
	}

	

}
