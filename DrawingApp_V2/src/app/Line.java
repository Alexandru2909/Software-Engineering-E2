package app;

import com.google.gson.annotations.Expose;

import javax.swing.*;
import java.awt.*;
import java.io.Serializable;

/**
 * Aceasta clasa retine datele necesare reprezentarii unei linii
 */
public class Line implements Serializable {
    public int x1;
    public int y1;
    public int x2;
    public int y2;

    @Expose
    private double cost;

    @Expose
    private int id_node1;

    private Node node1;

    @Expose
    private int id_node2;

    private Node node2;
    
    public Line() {
    	this.x1=0;
        this.y1=0;
        this.x2=0;
        this.y2=0;
    }

    public Line(int x1,int y1,int x2,int y2){
        this.x1=x1;
        this.y1=y1;
        this.x2=x2;
        this.y2=y2;
        this.cost=0.00;
        this.node1=new Node(0,0);
        this.node2=new Node(0,0);
        this.id_node1=0;
        this.id_node2=0;
    }

    public Line(Line line){
        this.x1=line.x1;
        this.y1=line.y1;
        this.x2=line.x2;
        this.y2=line.y2;
        this.cost=line.cost;
        this.node1=line.node1;
        this.node2=line.node2;
        this.id_node1=line.node1.id;
        this.id_node2=line.node2.id;
    }

    /**
	 * @return the id_node1
	 */
	public int getId_node1() {
		return id_node1;
	}

	/**
	 * @param id_node1 the id_node1 to set
	 */
	public void setId_node1(int id_node1) {
		this.id_node1 = id_node1;
	}

	/**
	 * @return the id_node2
	 */
	public int getId_node2() {
		return id_node2;
	}

	/**
	 * @param id_node2 the id_node2 to set
	 */
	public void setId_node2(int id_node2) {
		this.id_node2 = id_node2;
	}

	public void textBox() {

       JTextField weightTF = new JTextField(String.valueOf(this.getWeight()));
       JPanel panel = new JPanel(new GridLayout(0, 1));
       panel.add(new JLabel("Weight:"));
       panel.add(weightTF);
       int result = JOptionPane.showConfirmDialog(null, panel, "Edge info", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

       if (result == JOptionPane.OK_OPTION) {

           try {
               this.setWeight(java.lang.Double.parseDouble(weightTF.getText()));
           } catch (Exception e) {
               System.out.println(e);
               System.out.println("Weight: 0.00");
               this.setWeight(0.00);
           }
       }
   }

    public double getWeight() {
        return cost;
    }

    private void setWeight(double weight) {
        this.cost = weight;
    }
    
    public Node getNode1(){
        return node1;
    }
    
    public void setNode1(Node node){
        this.node1=node; this.id_node1=node.id;
    }
    
    public Node getNode2(){
        return node2;
    }
    
    public void setNode2(Node node){
        this.node2=node; this.id_node2=node.id;
    }
    
}