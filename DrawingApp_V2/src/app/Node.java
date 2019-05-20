package app;

import com.google.gson.annotations.Expose;

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
    /**
     * numarul nodului curent, va fi afisat in fisierul json
     */
    @Expose
    public int id;//retine numarul nodului curent
    public static int instNumber=-2;//retine numarul totat de noduri(instante de noduri)

    /**
     * numele nodului, va fi afisat in fisierul json
     */
    @Expose
    private String name;

    /**
     * tipul nodului, va fi afisat in fisierul json
     */
    @Expose
    private String type;

    /**
     * etajul nodului, va fi afisat in fisierul json
     */
    @Expose
    private Integer floor;

    /**
     * constructorul clasei Node
     * @param xPoint pozitia pe axa X a nodului
     * @param yPoint pozitia pe axa Y a nodului
     */
    public Node(int xPoint,int yPoint){
        this.xPoint=xPoint;
        this.yPoint=yPoint;
        this.name="";
        this.type="";
        this.floor=0;
        instNumber++;
        id=instNumber;
    }
    public Node(int xPoint,int yPoint,int dist){
        this.xPoint=xPoint;
        this.yPoint=yPoint;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }
    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * meniul afisat pe ecran in momentul editarii unui nod; permite selectarea numelui, tipului si etajului
     */
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
        /**
         * Auto focus implementation
         */
        JOptionPane pane = new JOptionPane(panel, JOptionPane.PLAIN_MESSAGE, JOptionPane.OK_CANCEL_OPTION) {
            /**
            * Auto-focus on the first field of the text box
            */
            @Override
            public void selectInitialValue(){
                nameTF.requestFocusInWindow();
                nameTF.selectAll();
            }
        };
        pane.createDialog(null, "Node info").setVisible(true);

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
    /**
     * @return the instNumber
     */
    public static int getInstNumber() {
        return instNumber;
    }
    /**
     * @param instNumber the instNumber to set
     */
    public static void setInstNumber(int instNumber) {
        Node.instNumber = instNumber;
    }
}
