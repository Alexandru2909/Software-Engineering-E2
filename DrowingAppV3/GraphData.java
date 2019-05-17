package app;

import com.google.gson.annotations.Expose;

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
    @Expose
    public LinkedList<Node> nodes;
    /**
     * Aceasta este lista de noduri
     */
    @Expose
    public LinkedList<Line> edges;

    /**
     * Instantiem cele doua liste cu valorile listelor folosite la mentinerea datelor pentru contextul grafic
     * @param nodesList este lista nodurilor
     * @param linesList este lista muchiilor
     */
    public GraphData(LinkedList<Node> nodesList,LinkedList<Line> linesList){
        this.nodes=nodesList;
        this.edges=linesList;
    }

    /**
     * Aceasta functie salveaza datele sub forma de fotografe a contextului grafic + un fisier ce contine cele doua liste create anterior
     * @param folderPath
     * @param fileName
     */
    public void saveData(String folderPath, String fileName){
        File filesFolder=new File(folderPath);
        if(filesFolder.exists()){
            if(filesFolder.isDirectory()){
                try {
                    File directory = new File("testGraph/graphFile");
                    if (! directory.exists())
                        directory.mkdir();
                    FileOutputStream output = new FileOutputStream(folderPath + "/" + "graphFile" + "/" + fileName + ".graph");
                    ObjectOutputStream outputObject = new ObjectOutputStream(output);
                    outputObject.writeObject(nodes);
                    outputObject.writeObject(edges);

                    BufferedImage image = new BufferedImage(1920, 1080, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D graphics = (Graphics2D) image.getGraphics();

                    for(Node node: nodes){
                        graphics.setColor(Color.red);
                        graphics.fillOval(node.xPoint,node.yPoint,25,25);
                        graphics.setColor(Color.white);
                        graphics.drawString(String.valueOf(node.id),node.xPoint+10,node.yPoint+15);
                    }
                    graphics.setColor(Color.red);
                    for(Line line: edges){
                        graphics.drawLine(line.x1,line.y1,line.x2,line.y2);
                        graphics.drawString(String.valueOf(line.getWeight()),(line.x1+line.x2)/2+10,(line.y1+line.y2)/2+10);
                    }

                    graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); //for "smooth" drawing
                    File directory2 = new File("testGraph/pngFormat");
                    if (! directory2.exists())
                        directory2.mkdir();
                    ImageIO.write(image, "png", new File(folderPath + "/" +"pngFormat" + "/" + fileName + ".png"));
                    outputObject.close();
                    output.close();
                }catch(Exception e){
                    System.out.println("exceptie la salvare");
                }
            }else{
                System.out.println("path-ul nu indica catre un director");
            }
        }else{
            System.out.println("path inexistent");
        }
    }

    /**
     * Aceasta functie incarca in listele programului listele din fisier
     * @param filePath
     */
    public void getData(String filePath){
        try {
            FileInputStream inputData = new FileInputStream(filePath);
            ObjectInputStream inputObjects = new ObjectInputStream(inputData);
            /*
             * Paul Reftu:
             *
             * <comment>
             *
             * Here the problem with the incorrect JSON output w.r.t the edges could be fixed, as you can see.
             * The problem was that - at the reading process of each .graph object, the attributes of the Line objects 'id_node1' and 'id_node2'
             * remained unchanged.
             *
             * Hence, if we add to every element of every set of incoming edge lists - namely to the 'id_node1' and 'id_node2' attributes -
             * the current size of our node list, this problem is fixed.
             */

            LinkedList<Node> inputNodes = (LinkedList<Node>) inputObjects.readObject();
            nodes.addAll(inputNodes);

            LinkedList<Line> inputLines = (LinkedList<Line>) inputObjects.readObject();

            for (Line line : inputLines) {
                line.setId_node1(line.getId_node1() + Node.getInstNumber());
                line.setId_node2(line.getId_node2() + Node.getInstNumber());
            }

            edges.addAll(inputLines);
            Node.setInstNumber(Node.getInstNumber() + inputNodes.size());

            /*
             * </comment>
             */

            inputObjects.close();
            inputData.close();
        }catch(Exception e){
            System.out.println("problema la citirea fisierului");
        }
    }
}
