package app;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * Aceasta clasa retine datele necesare reprezentarii unei linii
 */
public class Line implements Serializable {
    public int x1;
    public int y1;
    public int x2;
    public int y2;

    @Expose
    private double weight;

    @Expose
    private Node node1;
    @Expose
    private Node node2;

    public Line(int x1,int y1,int x2,int y2){
        this.x1=x1;
        this.y1=y1;
        this.x2=x2;
        this.y2=y2;
        this.weight=0.00;
        this.node1=new Node(0,0);
        this.node2=new Node(0,0);
    }

    public Line(Line line){
        this.x1=line.x1;
        this.y1=line.y1;
        this.x2=line.x2;
        this.y2=line.y2;
        this.weight=line.weight;
        this.node1=line.node1;
        this.node2=line.node2;
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
        return weight;
    }

    private void setWeight(double weight) {
        this.weight = weight;
    }
    
    public Node getNode1(){
        return node1;
    }
    
    public void setNode1(Node node){
        this.node1=node;
    }
    
    public Node getNode2(){
        return node2;
    }
    
    public void setNode2(Node node){
        this.node2=node;
    }
    
}