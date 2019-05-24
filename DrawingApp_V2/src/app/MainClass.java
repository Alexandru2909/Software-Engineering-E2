package app;


import javax.swing.*;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.LinkedList;

public class MainClass extends JFrame {
    /**
     * lista contine nodurile ce vor fi afisate pe suprafata de desenare
     */
    public LinkedList<Node> nodesList=new LinkedList<Node>();
    /**
     * lista contine toate muchiile folosite pentru conectarea nodurilor
     */
    public LinkedList<Line> linesList=new LinkedList<Line>();
    /**
     * Aceasta este linia care va fi afisata pe perioada conectarii a doua noduri
     * Odata ce primul nod este selectat pentru conectare, pana la selectarea celui de-al doilea nod, celalalt capat al liniei va indica catre pozitia mouse-ului
     */

    /*
     * Paul Reftu:
     *
     * the following instantiation causes the node counting to start at 3 on the boot-up of the application
     * the temporary fix is to set Node.instNumber := -2, instead of 0
     * I have personally made some tests after this modification -
     * and the numbering seems to be alright, but an eye has to be kept to see if this fix will spring up more problems later
     */
    public Line curentLine=new Line(0,0,0,0);

    /**
     * este clasa care retine tipurile de actiuni care se vor executa pe suprafata de desenare
     */
    public MessageClass actionMessage=new MessageClass();

    /**
     * verifica daca nodurile si muchiile au fost mutate
     */
    public boolean movedStatus=false;
    /**
     * aceasta lista este utila in cadrul procesului de mutare de noduri
     * odata ce un nod este selectat pentru operatia de mutare este necesara si selectarea muchiilor incidente la acesta pentru ,ca pe perioada mutarii nodului pozitia muchiilor sa se
     * actualizeze impreuna cu acesta
     */
    public LinkedList<Line> movingLinesList=new LinkedList<Line>();
    /**
     * acesta este nodul care va fi afisat pe perioada mutarii langa mouse
     */
    public Node movedNode=new Node(0,0,0);

    public boolean drawingStage=false;
    public boolean deleteEdgeStage=false;

    /**
     * functia reordoneza valorile din interiorul nodurilor dupa o operatie de stergere
     */
    public void repairOrder(){
        int index=1;
        Node.instNumber= nodesList.size();
        for(Node node:nodesList){

            node.id=index;
            index++;
        }
    }

    /**
     * Function to update "node editor panel"
     */
    private void nodeEditorUpdate(){
        if (nodesList.contains(firstNode)) {
            nodeNr.setText(String.valueOf(firstNode.id));
            nodeName.setText(firstNode.getName());
            nodeType.setText(firstNode.getType());
            nodeFloor.setText(String.valueOf(firstNode.getFloor()));
            nodeLatitude.setText(String.valueOf(firstNode.getLatitude()));
            nodeLongitude.setText(String.valueOf(firstNode.getLongitude()));
            drawingSurface.repaint();
        }
    }

    /**
     * pagina de unde selectam fisierele pe care dorim sa le exportam in format .json
     */
    JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
    JButton chooser = new JButton("Export");
    /**
     * butonul Open graph, de unde putem deschide un fisier .graph creat anterior
     */
    JFileChooser open = new JFileChooser(System.getProperty("user.dir"));
    JButton openGraph = new JButton("Open graph");
    /**
     * campul in care incarcam adresa fisierului ce contine reprezentarea inainte de apasarea butonului Open
     */
    JTextField inputFile=new JTextField(15);
    /**
     * Butonul pentru deschiderea unui fisier specificat in campul inputFile
     */
    JButton openButton=new JButton("Connect graphs");
    /**
     * Salveaza contextul grafic in fieiserul specificat in campul inputFile
     */
    JButton saveButton=new JButton("Save");
    /**
     * Acest buton curata contextul grafic al suptrafetei de desenare
     */
    JButton resetButton=new JButton("Reset");
    /**
     * Acest buton activeaza optiunea de desenare de noduri,orice click stanga efectuat pe suprafata de desenare va conduce la inserarea de noduri(cu exceptia suprapunerii)
     */
    JButton drawNodesButton=new JButton("Draw Nodes");
    /**
     * Acest buton activeaza optiunea de mutare de nooduri
     * odata selectat un nod acesta va fi mutat impreuna cu muchiile incidente
     */
    JButton moveNodeButton=new JButton("Move Nodes");
    /**
     * Acest buton activeaza optiunea de stergere de noduri
     * dupa activarea acestei optiuni orice nod selectat va fi sters impreuna cu muchiile incidente la acesta
     */
    JButton deleteNodeButton=new JButton("Delete Node");
    /**
     * Acest buton activeaza optiunea de conectare de noduri
     *
     */
    JButton connectNodesButton=new JButton("Connect Nodes");
    /**
     * Butonul care activeaza optiunea de stergere de muchii
     */
    JButton deleteEdgeButton=new JButton("Delete Edge");
    /**
     * Button that activates the option to edit node info
     */
    JButton editNodeButton=new JButton("Edit Node");
    /**
     * Button that activates the option to edit edge weight
     */
    JButton editEdgeButton=new JButton("Edit Edge");
    /**
     * Checkbox that toggles if node editor is activated as auto-popup (default is true)
     */
    JCheckBox popupNode=new JCheckBox("Node auto-popup", true);
    /**
     * Checkbox that toggles if line editor is activated as auto-popup (default is true)
     */
    JCheckBox popupLine=new JCheckBox("Line auto-popup", true);
    /**
     * Panel that contains the "Node auto-popup" checkbox
     */
    JPanel popupPanel=new JPanel();
    /**
     * Acesta este panoul care contine butoanele si campul de text
     */
    JPanel  buttonsPanel=new JPanel();
    /**
     * Acesta este panoul general
     */
    JPanel  generalPanel=new JPanel();//panoul general
    /**
     * Instantiem suprafata de desenare
     */
    MyComponent drawingSurface=new MyComponent(nodesList,linesList,movingLinesList,curentLine,actionMessage);//suprafata de desenare

    /**
     * node editor panel + textfields & checkboxes
     */
    JLabel nodeNr=new JLabel("0");
    JTextField nodeName=new JTextField("Node name");
    JTextField nodeType=new JTextField("Node type");
    JTextField nodeFloor=new JTextField("Node floor");
    JTextField nodeLatitude = new JTextField("Node latitude");
    JTextField nodeLongitude = new JTextField("Node longitude");
    JButton nodeSave = new JButton("Save node");
    JPanel nodeEditorPanel = new JPanel();

    private Node firstNode=null;
    private Node secondNode=null;
//    private Node firstNode=null;
    /**
     * Aceasta functie reseteaza flagurile de activitate
     * functia este apelata la fiecare apasare de buton pentru a ne asigura ca nu apar probleme intre operatii
     */
    public void resetStatus(){
        movingLinesList.clear();
        drawingSurface.repaint();
        movedStatus=false;
        drawingStage=false;
        deleteEdgeStage=false;
        firstNode=null;
        secondNode=null;
    }

    /**
     * Constructorul acestei clase
     */
    MainClass(){

        super("Drawing App");//setam titlul ferestrei

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        /**
         * setam layout-ul pentru buttonsPanel
         */
        buttonsPanel.setLayout(new FlowLayout());

        /**
         * atasam butoanele panoului buttonsPanel
         */
        buttonsPanel.add(chooser);
        buttonsPanel.add(openGraph);
        buttonsPanel.add(inputFile);
        buttonsPanel.add(openButton);
        buttonsPanel.add(saveButton);
        buttonsPanel.add(resetButton);
        buttonsPanel.add(drawNodesButton);
        buttonsPanel.add(moveNodeButton);
        buttonsPanel.add(deleteNodeButton);
        buttonsPanel.add(connectNodesButton);
        buttonsPanel.add(deleteEdgeButton);
        buttonsPanel.add(editNodeButton);
        buttonsPanel.add(editEdgeButton);

        /**
         * setting the layout for the popupPanel
         */
        popupPanel.setLayout(new FlowLayout());
        /**
         * adding the node checkbox to the popupPanel
         */
        popupPanel.add(popupNode);
        /**
         * adding the line checkbox to the popupPanel
         */
        popupPanel.add(popupLine);
        /**
         * addign the textfields + save button to the node editor panel
         */
        nodeEditorPanel.setLayout(new BoxLayout(nodeEditorPanel,BoxLayout.Y_AXIS));
        nodeEditorPanel.add(nodeNr);
        nodeEditorPanel.add(nodeName);
        nodeEditorPanel.add(nodeType);
        nodeEditorPanel.add(nodeFloor);
        nodeEditorPanel.add(nodeLatitude);
        nodeEditorPanel.add(nodeLongitude);
        nodeEditorPanel.add(nodeSave);
        /**
         * setam layout-ul general al ferestrei
         */
        generalPanel.setLayout(new BorderLayout());
        /**
         * adaugam panoul de butoane la panoul general
         */
        generalPanel.add(buttonsPanel,BorderLayout.NORTH);
        /**
         * adaugam suprafata de desenare la panoul general
         */
        generalPanel.add(drawingSurface,BorderLayout.CENTER);
        /**
         * attach the popup panel to the general panel
         */
        generalPanel.add(popupPanel,BorderLayout.SOUTH);
        /**
         * attach the node editor panel to the general panel
         */
        generalPanel.add(nodeEditorPanel,BorderLayout.EAST);
        /**
         * atasam listener butonului "Save node"
         */
        nodeSave.addActionListener(actionEvent -> {
            if (nodesList.contains(firstNode)) {
                firstNode.setName(nodeName.getText());
                firstNode.setType(nodeType.getText());
                try {
                    firstNode.setFloor(Integer.parseInt(nodeFloor.getText()));
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("Floor: 0");
                    firstNode.setFloor(0);
                }
                try {
                    firstNode.setLatitude(Double.parseDouble(nodeLatitude.getText()));
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("Latitude: 0.00");
                    firstNode.setLatitude(0.00);
                }
                try {
                    firstNode.setLongitude(Double.parseDouble(nodeLongitude.getText()));
                } catch (Exception e) {
                    System.out.println(e);
                    System.out.println("Longitude: 0.00");
                    firstNode.setLongitude(0.00);
                }
            }
        });
        /**
         * atasam listener butonului Export
         */
        chooser.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                linesList.forEach(line -> {
                    line.setNode1(line.getNode1());
                    line.setNode2(line.getNode2());
                });
                int returnVal = fileChooser.showOpenDialog((Component)e.getSource());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        GraphData myGraph=new GraphData(nodesList,linesList);

                        myGraph.getData(file.getPath());
                        Gson gson = new GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create();
                        String json = gson.toJson(myGraph);
                        BufferedWriter writer = new BufferedWriter(new FileWriter("testGraph/output.json"));
                        writer.write(json);
                        writer.close();
                    } catch (Exception ex) {
                        System.out.println("problem accessing file"+file.getAbsolutePath());
                    }
                }
                else {
                    System.out.println("File access cancelled by user.");
                }
            }
        });

        /**
         * atasam listener butonului Open graph
         */
        openGraph.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                linesList.forEach(line -> {
                    line.setNode1(line.getNode1());
                    line.setNode2(line.getNode2());
                });
                int returnVal = fileChooser.showOpenDialog((Component)e.getSource());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        GraphData myGraph=new GraphData(nodesList,linesList);

                        myGraph.getData(file.getPath());
                        Node.instNumber=nodesList.size();
                        drawingSurface.repaint();
                    } catch (Exception ex) {
                        System.out.println("problem accessing file"+file.getAbsolutePath());
                    }
                }
                else {
                    System.out.println("File access cancelled by user.");
                }
            }
        });

        /**
         * atasam listener butonului Open
         */
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String filePath=inputFile.getText();
                File inputFile=new File(filePath);
                if(inputFile.exists()){
                    GraphData myGraph=new GraphData(nodesList,linesList);
                    File folder = new File(filePath);
                    File[] listOfFiles = folder.listFiles();
                    nodesList.clear();
                    linesList.clear();
                    if (listOfFiles != null) {
                        for (File listOfFile : listOfFiles) {
                            if (listOfFile.isFile()) {
                                myGraph.getData(listOfFile.getPath());

                                System.out.println(listOfFile.getPath());
                            }
                        }
                    }
                    Node.instNumber=nodesList.size();
                    repairOrder();

                    for (Node node1 : nodesList) {
                        for (Node node2 : nodesList) {
                            if (node1.getType().equalsIgnoreCase("stairs") || node1.getType().equalsIgnoreCase("elevator")){
                                if (node2.getType().equalsIgnoreCase(node1.getType())){
                                    if (node1.getId() != node2.getId() && node1.getName().equals(node2.getName())) {
                                        if (node1.getFloor() == node2.getFloor() - 1 || node2.getFloor() == node1.getFloor() - 1) {
                                            curentLine = new Line(node1.xPoint + 10, node1.yPoint + 10, node2.xPoint + 10, node2.yPoint + 10);
                                            boolean exists=false;
                                            for (Line existentLine : linesList) {
                                                if ((existentLine.getNode1().id == node1.id && existentLine.getNode2().id == node2.id) ||
                                                        (existentLine.getNode2().id == node1.id && existentLine.getNode1().id == node2.id)){
                                                    exists=true;
                                                    break;
                                                }
                                            }

                                            if (!exists){
                                                curentLine.setNode1(node1);
                                                curentLine.setNode2(node2);
                                                linesList.add(curentLine);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    drawingSurface.repaint();
                }else{
                    System.out.println("documentul nu exista");
                }
            }
        });

        /**
         * atasam listener butonului Reset
         */
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetStatus();
                actionMessage.messageCode=-1;
                nodesList.clear();
                Node.instNumber=0;
                linesList.clear();
                drawingSurface.repaint();
            }
        });

        /**
         * atasam listener butonului Save
         */
        saveButton.addActionListener(new ActionListener() {
                                         @Override
                                         public void actionPerformed(ActionEvent e) {
                                             drawingSurface.repaint();
                                             GraphData outputData=new GraphData(nodesList,linesList);
                                             String filePath = inputFile.getText();
                                             File directory = new File("testGraph");
                                             if (! directory.exists())
                                                 directory.mkdir();
                                             outputData.saveData("testGraph",filePath);
                                         }
                                     }
        );

        /**
         * atasam listener butonului de stergere a nodurilor
         */
        deleteNodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetStatus();
                actionMessage.messageCode=5;
            }
        });

        /**
         * atasam listener butonului de desenare a nodurilor
         */
        drawNodesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetStatus();
                actionMessage.messageCode=1;//semnificatie : putem desena noduri
                drawingSurface.repaint();
            }
        });

        /**
         * atasam listener butonului de conectare a nodurilor
         */
        connectNodesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetStatus();
                actionMessage.messageCode=2;//semnificatia : putem desena muchii
                drawingStage=false;
            }
        });

        /**
         * atasam listener butonului de stergere a nodurilor
         */
        deleteNodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetStatus();
                actionMessage.messageCode=3;//semnificatia : putem sterge noduri
            }
        });

        /**
         * atasam listener butonului de mutare a nodurilor
         */
        moveNodeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetStatus();
                actionMessage.messageCode=4;//mutem muta un nod
            }
        });

        /**
         * atasam listener butonului de stergere a muchiilor
         */
        deleteEdgeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetStatus();
                actionMessage.messageCode=6;
            }
        });

        /**
         * atasam listener butonului de editare a nodurilor
         */
        editNodeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                resetStatus();
                actionMessage.messageCode=7;
            }
        });

        /**
         * atasam listener butonului de editare a muchiilor
         */
        editEdgeButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                resetStatus();
                actionMessage.messageCode=8;
            }
        });


        /**
         * atasam listener suprafetei de desenare
         */
        drawingSurface.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if(actionMessage.messageCode==1 && validPosition(e.getX(), e.getY())){
                    Node newNode = new Node(e.getX(),e.getY());
                    if (popupNode.isSelected()){
                        newNode.textBox();
                    }
                    nodesList.add(newNode);
                    firstNode=newNode;
                    nodeEditorUpdate();
                    drawingSurface.repaint();
                }else if(actionMessage.messageCode==2){
                    if(!drawingStage){
                        Node initNode=validNode(e.getX(),e.getY());
                        if(initNode!=null){
                            curentLine.x1=initNode.xPoint+10;
                            curentLine.y1=initNode.yPoint+10;
                            curentLine.setNode1(initNode);
                            drawingStage=true;
                        }
                    }else{
                        Node finalNode=validNode(e.getX(),e.getY());
                        if(finalNode!=null){
                            curentLine.x2=finalNode.xPoint+10;
                            curentLine.y2=finalNode.yPoint+10;
                            if(Line.availableLine(curentLine,linesList)){
                                curentLine.setNode2(finalNode);
                                Line newLine = new Line(curentLine);
                                if (popupLine.isSelected()){
                                    newLine.textBox();
                                }
                                linesList.add(newLine);
                                drawingSurface.repaint();
                                drawingStage = false;
                            }
                        }
                    }
                }else if(actionMessage.messageCode==4){
                    if(!movedStatus){
                        movedNode=validNode(e.getX(),e.getY());
                        firstNode=movedNode;
                        if(movedNode!=null){
                            movedStatus=true;
                            for(Line line:linesList){
                                if(line.x1==movedNode.xPoint+10 && line.y1== movedNode.yPoint+10 ||line.x2==movedNode.xPoint+10 && line.y2== movedNode.yPoint+10){
                                    if(line.x2==movedNode.xPoint+10 && line.y2== movedNode.yPoint+10){
                                        line.x2=line.x1;
                                        line.y2=line.y1;
                                        line.x1=movedNode.xPoint+10;
                                        line.y1=movedNode.yPoint+10;
                                    }
                                    movingLinesList.add(line);
                                }
                            }
                            drawingSurface.repaint();
                        }
                    }else{
                        if(!validPosition(e.getX(), e.getY())){
                            movedStatus=false;
                            movedNode.xPoint=e.getX();
                            movedNode.yPoint=e.getY();
                            for(Line line:movingLinesList){
                                line.x1=movedNode.xPoint+10;
                                line.y1=movedNode.yPoint+10;
                            }
                            movingLinesList.clear();
                            drawingSurface.repaint();
                        }
                        nodeEditorUpdate();
                    }
                }else if(actionMessage.messageCode==5){
                    Node node=validNode(e.getX(),e.getY());
                    if(node!=null){
                        Iterator<Line> myIterator=linesList.iterator();
                        Line line;
                        while(myIterator.hasNext()){
                            line=myIterator.next();
                            if(line.x1==node.xPoint+10 && line.y1==node.yPoint+10 ||line.x2==node.xPoint+10 && line.y2==node.yPoint+10){
                                myIterator.remove();
                            }
                        }
                        nodesList.remove(node);
                        repairOrder();
                        drawingSurface.repaint();
                    }
                }else if(actionMessage.messageCode==6){
                    if(!deleteEdgeStage){
                        firstNode=validNode(e.getX(),e.getY());
                        if (firstNode!=null) {
                            movingLinesList.clear();
                            for(Line line:linesList){
                                if(isAd(line, firstNode)){
                                    movingLinesList.add(line);
                                }
                            }
                            deleteEdgeStage=true;
                            drawingSurface.repaint();
                        }
                    }else{
                        secondNode=validNode(e.getX(),e.getY());
                        if(secondNode!=null && secondNode!=firstNode){
                            boolean lineFlag=false;
                            for(Line line:linesList){
                                if(isAd(line,firstNode) && isAd(line,secondNode)){
                                    linesList.remove(line);
                                    movingLinesList.remove(line);
                                    drawingSurface.repaint();
                                    lineFlag=true;
                                }
                            }
                            if(!lineFlag){
                                movingLinesList.clear();
                                drawingSurface.repaint();
                                drawingStage=false;
                                firstNode=secondNode;
                                secondNode=null;
                                for(Line line:linesList){
                                    if(isAd(line, firstNode)){
                                        movingLinesList.add(line);
                                    }
                                }
                            }
                            drawingSurface.repaint();
                        }else if(secondNode==null){
                            deleteEdgeStage=false;
                            movingLinesList.clear();
                            drawingSurface.repaint();
                        }
                    }
                }
                else if (actionMessage.messageCode==7){
                    firstNode=validNode(e.getX(),e.getY());
                    if (firstNode!=null){
                        firstNode.textBox();
                    }
                }
                else if (actionMessage.messageCode==8){
                    Line newValue=validLine(e.getX(),e.getY());
                    if (newValue!=null)
                        newValue.textBox();
                }
            }
        });
        drawingSurface.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e){
                if(drawingStage && actionMessage.messageCode==2){
                    curentLine.x2=e.getX();
                    curentLine.y2=e.getY();
                    drawingSurface.repaint();
                }else if(actionMessage.messageCode==4){
                    if(movedStatus){
                        movedNode.xPoint=e.getX();
                        movedNode.yPoint=e.getY();
                        for(Line line:movingLinesList){
                            line.x1=e.getX()+10;
                            line.y1=e.getY()+10;
                        }
                        drawingSurface.repaint();
                    }

                }
            }
        });

        /**
         * atasam panoul general ferestrei
         */
        this.getContentPane().add(generalPanel);
        /**
         * setam dimensiunea ferestrei
         */
        this.setSize(new Dimension(1400,968));
        /**
         * setam fereastra vizibila
         */
        this.setVisible(true);
    }

    /**
     * Functia verifica daca pozitia indicata de mouse se suprapune cu vreun nod din lista de noduri
     *
     * @param x desemneaza pozitia pe axa X
     * @param y desemneaza pozitia pe axa Y
     * @return daca pozitia specificata se suprapune cu vreun nod atunci acesta va fi returnat, altfel se returneaza null
     */
    public Node validNode(int x,int y){
        for(Node node: nodesList){
            if(Math.abs(x-node.xPoint)<25 && Math.abs(y-node.yPoint)<25){
                return node;
            }
        }
        return null;
    }

//    public Line validLine(Line curentLine){
//        for (Line line: linesList){
//            if ((line.x1==curentLine.x1 && line.y1==curentLine.y1 && line.x2==curentLine.x2 && line.y2==curentLine.y2) ||
//                    (line.x1==curentLine.x2 && line.y1==curentLine.y2 && line.x2==curentLine.x1 && line.y2==curentLine.y1)){
//                return line;
//            }
//        }
//        return null;
//    }

    /**
     *
     * @param x pozitia pe axa X
     * @param y pozitia pe axa Y
     * @return daca nu exista suprapuneri, se va returna muchia, altfel se va returna null
     */
    public Line validLine(int x, int y){
        for (Line line: linesList){
            if (isBetween(line, x, y)){
                return line;
            }
        }
        return null;
    }

    /**
     *
     * @param x1 pozitia pe axa X a primului click
     * @param y1 pozitia pe axa Y a primului click
     * @param x2 pozitia pe axa X a celui de-al doilea click
     * @param y2 pozitia pe axa Y a celui de-al doilea click
     * @return distanta dintre cele doua pozitii
     */
    private double distance(int x1, int y1, int x2, int y2){
        return Math.sqrt(Math.pow((x1-x2),2)+Math.pow((y1-y2),2));
    }

    /**
     *
     * @param line muchie
     * @param x pozitia pe axa X a mouse-lui
     * @param y pozitia pe axa Y a mouse-lui
     * @return
     */
    private boolean isBetween(Line line, int x, int y){
        double eps=0.001;
        return distance(line.x1,line.y1,x,y)+distance(line.x2,line.y2,x,y)<distance(line.x1,line.y1,line.x2,line.y2)+eps
                && distance(line.x1,line.y1,x,y)+distance(line.x2,line.y2,x,y)>distance(line.x1,line.y1,line.x2,line.y2)-eps;
    }

    /**
     * Functia pozitia indicata de mouse se suprapune cu vreun nod din lista de noduri
     *
     * @param x desemneaza pozitia pe axa X
     * @param y desemneaza pozitia pe axa Y
     * @return false daca pozitia specificata se suprapune cu vreun nod din lista, true altfel
     */
    public boolean validPosition(int x,int y){
        for(Node node: nodesList){
            if(Math.abs(x-node.xPoint)<25 && Math.abs(y-node.yPoint)<25){
                return false;
            }
        }
        return true;
    }

    /**
     *
     * @param line muchie
     * @param node nod
     * @return adauga muchia daca coordonatele coincid cu ale nodului
     */
    private boolean isAd(Line line,Node node){
        return ((line.x1 == (node.xPoint + 10)) && (line.y1 == (node.yPoint + 10))) || ((line.x2 == (node.xPoint + 10)) && (line.y2 == (node.yPoint + 10)));
    }

    public static void main(String...args){
        new MainClass();
    }
}
