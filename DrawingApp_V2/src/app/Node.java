package app;

import javax.swing.*;
import java.awt.*;

import java.io.Serializable;

/**
 * Aceasta clasa contine datele necesare reprezentarii unui nod
 *
 */
public class Node implements Serializable {
    public int xPoint;
    public int yPoint;
    public int curentNumber;//retine numarul nodului curent
    public static int instNumber=0;//retine numarul totat de noduri(instante de noduri)

    private String name;
    private String type;
    private Integer floor;

    public Node(int xPoint,int yPoint){
        this.xPoint=xPoint;
        this.yPoint=yPoint;
        this.name="";
        this.type="";
        this.floor=0;
        instNumber++;
        curentNumber=instNumber;
    }
    public Node(int xPoint,int yPoint,int dist){
        this.xPoint=xPoint;
        this.yPoint=yPoint;
    }

    public void textBox(){
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
