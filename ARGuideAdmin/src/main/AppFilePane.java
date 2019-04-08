
package main;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;


public class AppFilePane extends JPanel {
    
 
    private static final long serialVersionUID = -6663025488807576018L;
    
    
   
    public AppFilePane() {
        init();
        createAndShowGUI();
    }
    
    

    private void init() {
        /* main interface implementation occurs here */
    }


    /**
     * starting point
     */
    private static void createAndShowGUI() {
   
        //Add content to the window.
        frame.add(new TabbedPaneDemo(), BorderLayout.CENTER);
      
    }


     /**
     * @param text
     * @return: displays the text from the file on every tab
     */
    protected JComponent makeTextPanel(String text) {
        
        JPanel panel = new JPanel(false);
        JLabel filler = new JLabel(text);
        filler.setHorizontalAlignment(JLabel.CENTER);
        panel.setLayout(new GridLayout(1, 1));
        panel.add(filler);
        return panel;
    }



    public TabbedPaneDemo() {
        
        super(new GridLayout(1, 1));
         
       JTabbedPane tabbedPane = new JTabbedPane();
       
        addPanel(tabbedPane,"Building1.json", "panel1");   
        addPanel(tabbedPane,"Building2.json", "panel2");   
        addPanel(tabbedPane,"Building3.json", "panel3");  
        addPanel(tabbedPane,"Building4.json", "panel4");
      
      
        //Add the tabbed pane to this panel.
        add(tabbedPane);            
    }

}
