package MyPackage;

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
    public Node(int xPoint,int yPoint){
        this.xPoint=xPoint;
        this.yPoint=yPoint;
        instNumber++;
        curentNumber=instNumber;
    }
    public Node(int xPoint,int yPoint,int dist){
        this.xPoint=xPoint;
        this.yPoint=yPoint;
    }


}
