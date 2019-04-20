package Shapes;

import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class NodeShape extends Ellipse2D.Double {

    private Boolean isSelected;

    public NodeShape(Integer x, Integer y, Integer radius) {
        super(x - radius, y - radius, radius * 2, radius * 2);
        this.isSelected = false;
    }

    public void select(int x, int y) {
        if (this.contains(x, y)) {

            this.isSelected = false;
        }
    }

    public void unselect(int x, int y) {
        if (this.contains(x,y)) {
            textBox();
        }
                this.isSelected = false;
    }

    public void textBox(){
        JTextField name = new JTextField();
        JTextField type = new JTextField();
        JTextField floor = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(name);
        panel.add(new JLabel("Type:"));
        panel.add(type);
        panel.add(new JLabel("Floor:"));
        panel.add(floor);
        int result = JOptionPane.showConfirmDialog(null, panel, "Node info", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {

            System.out.println("Name: " + name.getText());
            System.out.println("Type: " + type.getText());
            try {
                System.out.println(Integer.parseInt(floor.getText()));
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Floor: 0");
            }
            System.out.println();

        }
    }
}
