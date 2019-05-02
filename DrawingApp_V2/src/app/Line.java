package app;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

import javax.swing.*;
import java.awt.*;

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
    private int node1;
    @Expose
    private int node2;

    public Line(int x1,int y1,int x2,int y2){
        this.x1=x1;
        this.y1=y1;
        this.x2=x2;
        this.y2=y2;
        this.weight=0.00;
        this.node1=0;
        this.node2=0;
    }

    public Line(Line x){
        this.x1=x.x1;
        this.y1=x.y1;
        this.x2=x.x2;
        this.y2=x.y2;
        this.weight=x.weight;
        this.node1=x.node1;
        this.node2=x.node2;
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
    
    public int getNode1(){
        return node1;
    }
    
    public void setNode1(int nodeId){
        this.node1=nodeId;
    }
    
    public int getNode2(){
        return node2;
    }
    
    public void setNode2(int nodeId){
        this.node2=nodeId;
    }
    
}