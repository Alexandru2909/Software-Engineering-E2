package MyPackage;

import javax.swing.*;
import java.awt.*;
import java.util.Iterator;
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
     * Lista de muchii pentru mutare
     *
     * Aceasta lista este folosita in cadrul procesului de mutare a unui nod pentru a retine si actualiza pozitia muchiilor incidente la nodul mutat
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
        }
        if(actionMessage.messageCode==2){
            G.drawLine(curentLine.x1,curentLine.y1,curentLine.x2,curentLine.y2);
        }
        if(actionMessage.messageCode==6){
            G.setColor(Color.blue);
            for(Line line: movingLinesList){
                System.out.println("redesenare muchii in galben");
                G.drawLine(line.x1,line.y1,line.x2,line.y2);
                G.drawLine(line.x1+1,line.y1+1,line.x2+1,line.y2+1);
                G.drawLine(line.x1+2,line.y1+2,line.x2+2,line.y2+2);
            }
        }
        for(Node node: nodesList){
            G.setColor(Color.red);
            G.fillOval(node.xPoint,node.yPoint,25,25);
            G.setColor(Color.white);
            G.drawString(String.valueOf(node.curentNumber),node.xPoint+10,node.yPoint+15);
        }

    }

}
