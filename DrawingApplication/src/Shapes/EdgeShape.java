package Shapes;

import java.lang.*;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;

public class EdgeShape extends Line2D.Double{

    private boolean isSelected;

    private double weight;

    public EdgeShape(Integer x1, Integer x2, Integer y1, Integer y2){
        super(x1,x2,y1,y2);
        this.isSelected = false;
        this.weight = 0.00;
    }

    public void select(int x, int y){
        if (this.contains(x,y)) {
            this.isSelected = false;
        }
    }

    public void unselect(int x, int y){
        if (this.contains(x,y)) {
            textBox();
        }
        this.isSelected=false;
    }

    private void textBox() {

        JTextField weightTF = new JTextField(String.valueOf(this.getWeight()));
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Weight:"));
        panel.add(weightTF);
        int result = JOptionPane.showConfirmDialog(null, panel, "Node info", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {

            try {
                this.setWeight(java.lang.Double.parseDouble(weightTF.getText()));
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Weight: 0.00");
                this.setWeight(0.00);
            }
            System.out.println();

        }
    }


    public double getWeight() {
        return weight;
    }

    private void setWeight(double weight) {
        this.weight = weight;
    }
}
