package app;

import javax.swing.*;
import java.awt.*;
import java.util.LinkedList;

/**
 * Aceasta clasa extinde clasa JComponent pentru a suptrascrie functia paintComponent care raspunde de aspectul grafic al acesteia
 * Obiectul primeste prin constructor adresele listelor( de noduri ,de muchii si de muchii pentru mutare),dresea muchiei de concectare si adresa nodului de mutare
 */
public class MyComponent extends JComponent {
    /**
     * Lista de noduri
     */
    public LinkedList<Node> nodesList;
    /**
     * Lista de muchii
     */
    public LinkedList<Line> linesList;
    /**
     * lista de muchii, folosita in cadrul procesului de mutare a unui nod pentru a retine si actualiza pozitia muchiilor incidente la nodul mutat
     */
    public LinkedList<Line> movingLinesList;
    /**
     * Aceasta linie este utilizata la procesul de conectare fiind actualizata odata cu mutarea mouselui
     */
    public Line curentLine;
    /**
     * Variabila care retine tipul de optiune curent selectata
     */
    public MessageClass actionMessage;

    /**
     * constructorul clasei MyComponent
     * @param nodesList lista de noduri
     * @param linesList lista de muchii
     * @param movingLinesList lista de muchii folosita in cadrul mutarii unui nod
     * @param curentLine muchia actuala
     * @param actionMessage optiunea selectata
     */
    public MyComponent(LinkedList<Node> nodesList,LinkedList<Line> linesList,LinkedList<Line> movingLinesList,Line curentLine,MessageClass actionMessage){
        this.nodesList=nodesList;
        this.linesList=linesList;
        this.movingLinesList=movingLinesList;
        this.curentLine=curentLine;
        this.actionMessage=actionMessage;
    }

    /**
     * Functia de desenare a contextului grafic
     * @param g
     */
    public void paintComponent(Graphics g){
        Graphics2D G=(Graphics2D)g;

        G.setColor(Color.red);
        for(Line line: linesList){
            G.drawLine(line.x1,line.y1,line.x2,line.y2);
            G.drawString(String.valueOf(line.getWeight()),(line.x1+line.x2)/2+10,(line.y1+line.y2)/2+15);
        }
        if(actionMessage.messageCode==2){
            G.drawLine(curentLine.x1,curentLine.y1,curentLine.x2,curentLine.y2);
        }
        if(actionMessage.messageCode==6){
            G.setColor(Color.blue);
            for(Line line: movingLinesList){
                G.drawLine(line.x1,line.y1,line.x2,line.y2);
                G.drawLine(line.x1+1,line.y1+1,line.x2+1,line.y2+1);
                G.drawLine(line.x1+2,line.y1+2,line.x2+2,line.y2+2);
            }
        }
        for(Node node: nodesList){
            String type = node.getType();
            if (type.equalsIgnoreCase("stairs") || type.equalsIgnoreCase("elevator")) {
                G.setColor(Color.cyan);
                G.fillOval(node.xPoint, node.yPoint, 35, 35);
                G.setColor(Color.black);
                G.drawString(String.valueOf(node.id), node.xPoint + 15, node.yPoint + 15);
                G.setColor(Color.black);
                G.drawString(String.valueOf(node.getName()), node.xPoint, node.yPoint + 50);
            }
            else if(type.equalsIgnoreCase("classroom")){
                G.setColor(Color.red);
                G.fillOval(node.xPoint,node.yPoint,35,35);
                G.setColor(Color.black);
                G.drawString(String.valueOf(node.id),node.xPoint+15,node.yPoint+15);
                G.setColor(Color.black);
                G.drawString(String.valueOf(node.getName()),node.xPoint,node.yPoint+50);
            }
            else if(type.equalsIgnoreCase("exit")){
                G.setColor(Color.green);
                G.fillOval(node.xPoint,node.yPoint,35,35);
                G.setColor(Color.black);
                G.drawString(String.valueOf(node.id),node.xPoint+15,node.yPoint+15);
                G.setColor(Color.black);
                G.drawString(String.valueOf(node.getName()),node.xPoint,node.yPoint+50);
            }
            else if(type.equalsIgnoreCase("amphitheatre")){
                G.setColor(Color.orange);
                G.fillOval(node.xPoint,node.yPoint,35,35);
                G.setColor(Color.black);
                G.drawString(String.valueOf(node.id),node.xPoint+15,node.yPoint+15);
                G.setColor(Color.black);
                G.drawString(String.valueOf(node.getName()),node.xPoint,node.yPoint+50);
            }
            else if(type.equalsIgnoreCase("administration office")){
                G.setColor(Color.magenta);
                G.fillOval(node.xPoint,node.yPoint,35,35);
                G.setColor(Color.black);
                G.drawString(String.valueOf(node.id),node.xPoint+15,node.yPoint+15);
                G.setColor(Color.black);
                G.drawString(String.valueOf(node.getName()),node.xPoint,node.yPoint+50);
            }
            else{
                G.setColor(Color.gray);
                G.fillOval(node.xPoint,node.yPoint,35,35);
                G.setColor(Color.white);
                G.drawString(String.valueOf(node.id),node.xPoint+15,node.yPoint+15);
                G.setColor(Color.black);
                G.drawString(String.valueOf(node.getName()),node.xPoint,node.yPoint+50);
            }
        }
        G.setColor(Color.green);
        G.drawString("Exit",getWidth()-140,getHeight()-90);
        G.setColor(Color.cyan);
        G.drawString("Stairs",getWidth()-140,getHeight()-77);
        G.drawString("Elevator",getWidth()-140,getHeight()-64);
        G.setColor(Color.orange);
        G.drawString("Amphitheatre",getWidth()-140,getHeight()-51);
        G.setColor(Color.magenta);
        G.drawString("Administration office",getWidth()-140,getHeight()-37);
        G.setColor(Color.red);
        G.drawString("Classroom",getWidth()-140,getHeight()-24);
        G.setColor(Color.gray);
        G.drawString("Unidentified node",getWidth()-140,getHeight()-11);
    }
}
