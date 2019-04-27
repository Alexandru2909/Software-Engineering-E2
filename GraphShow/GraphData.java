package GraphShow;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.LinkedList;

/**
 * Clasa GraphData desemneaza structura de date in care va fi retinut continutul contextului grafic pe perioada stocarii
 */
public class GraphData  implements Serializable {
    /**
     * Aceasta este lista de muchii
     */
    public LinkedList<Node> nodesList;
    /**
     * Aceasta este lista de noduri
     */
    public LinkedList<Line> linesList;

    /**
     * Instantiem cele doua liste cu valorile listelor folosite la mentinerea datelor pentru contextul grafic
     * @param nodesList este lista nodurilor
     * @param linesList este lista muchiilor
     */
    public GraphData(LinkedList<Node> nodesList,LinkedList<Line> linesList){
        this.nodesList=nodesList;
        this.linesList=linesList;
    }

    /**
     * Aceasta functie salveaza datele sub forma de fotografe a contextului grafic + un fisier ce contine cele doua liste create anterior
     *
     * Pentru noul scop al programului nu mai este necesara crearea unei fotografii a contextului grafic
     *
     *
     *
     * @param folderPath
     * @param fileName
     */
    public void saveData(String folderPath, String fileName){
            File filesFolder=new File(folderPath);
            if(filesFolder.exists()==true){
                if(filesFolder.isDirectory()==true){
                    try {

                        FileOutputStream output = new FileOutputStream(folderPath + "\\" + fileName + ".graph");
                        ObjectOutputStream outputObject = new ObjectOutputStream(output);
                        outputObject.writeObject(nodesList);
                        outputObject.writeObject(linesList);

                        BufferedImage image = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
                        Graphics2D graphics = (Graphics2D) image.getGraphics();

                        for(Node node: nodesList){
                            graphics.setColor(Color.red);
                            graphics.fillOval(node.xPoint,node.yPoint,25,25);
                            graphics.setColor(Color.white);
                            graphics.drawString(String.valueOf(node.curentNumber),node.xPoint+10,node.yPoint+15);
                        }
                        graphics.setColor(Color.red);
                        for(Line line: linesList){
                            graphics.drawLine(line.x1,line.y1,line.x2,line.y2);
                            graphics.drawString(String.valueOf(line.getWeight()),(line.x1+line.x2)/2+10,(line.y1+line.y2)/2+10)
                        }

                        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //for "smooth" drawing
                        ImageIO.write(image, "png", new File(folderPath + "\\" + fileName + ".png"));
                        outputObject.close();
                        output.close();
                    }catch(Exception e){
                        System.out.print("expcetpie la salvare");
                    }
                }else{
                    System.out.print("path-ul nu indica catre un director");
                }
            }else{
                System.out.println("path inexistent");
            }
    }

    /**
     * Aceasta functie incarca in listele programului listele din fisier
     *
     * @param filePath
     */
    public void getData(String filePath){
        try {
            FileInputStream inputData = new FileInputStream(filePath);
            ObjectInputStream inputObjects = new ObjectInputStream(inputData);
            nodesList.clear();
            linesList.clear();
            nodesList.addAll((LinkedList<Node>) inputObjects.readObject());
            linesList.addAll((LinkedList<Line>) inputObjects.readObject());
            inputObjects.close();
            inputData.close();
        }catch(Exception e){
            System.out.println("problema la citirea fisierului");
        }
    }

}
