package Shapes;

import jdk.nashorn.internal.scripts.JO;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class NodeShape extends Ellipse2D.Double {

    private Boolean isSelected;

    private String name;
    private String type;
    private Integer floor;

    public NodeShape(Integer x, Integer y, Integer radius) {
        super(x - radius, y - radius, radius * 2, radius * 2);
        this.isSelected = false;
        this.name = "";
        this.type = "";
        this.floor = 0;
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

    private void textBox(){
        JTextField nameTF = new JTextField(this.getName());
        JTextField typeTF = new JTextField(this.getType());
        JTextField floorTF = new JTextField(this.getFloor().toString());
        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Name:"));
        panel.add(nameTF);
        panel.add(new JLabel("Type:"));
        panel.add(typeTF);
        panel.add(new JLabel("Floor:"));
        panel.add(floorTF);
        int result = JOptionPane.showConfirmDialog(null, panel, "Node info", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {

            this.setName(nameTF.getText());
            this.setType(typeTF.getText());

            System.out.println("Name: " + nameTF.getText());
            System.out.println("Type: " + typeTF.getText());
            try {
                System.out.println(Integer.parseInt(floorTF.getText()));
                this.setFloor(Integer.parseInt(floorTF.getText()));
            } catch (Exception e) {
                System.out.println(e);
                System.out.println("Floor: 0");
                this.setFloor(0);
            }
            System.out.println();

        }
    }

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    private void setType(String type) {
        this.type = type;
    }

    public Integer getFloor() {
        return floor;
    }

    private void setFloor(Integer floor) {
        this.floor = floor;
    }
}
