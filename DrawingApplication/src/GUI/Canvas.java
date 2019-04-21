package GUI;

import Shapes.EdgeShape;
import Shapes.NodeShape;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class Canvas extends JPanel {

//    class Edge {
//        final Integer x1;
//        final Integer x2;
//        final Integer y1;
//        final Integer y2;
//
//        private double weight;
//
//        public Edge(Integer x1, Integer x2, Integer y1, Integer y2) {
//            this.x1 = x1;
//            this.x2 = x2;
//            this.y1 = y1;
//            this.y2 = y2;
//            this.weight = 0.00;
//        }
//
//        public double getWeight() {
//            return weight;
//        }
//
//        public void setWeight(double weight) {
//            this.weight = weight;
//        }
//    }


    ArrayList<NodeShape> nodes = new ArrayList<>();
    ArrayList<EdgeShape> edges = new ArrayList<>();

    private final GUI.DrawingFrame frame;
    private Graphics2D graphics;
    private BufferedImage image;

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int width = 800;
    private int height = 600;


    private Point mousePt;

    Canvas(GUI.DrawingFrame frame) {
        this.frame = frame;
        init();
    }

    private void drawNode(int x, int y) {
        Integer radius = 15;
        graphics.setColor(Color.BLACK);
        NodeShape shape = new NodeShape(x, y, radius);
        graphics.fill(shape);
        this.nodes.add(shape);
        repaint();
        this.frame.toolbar.nodeISpinner.setModel(new SpinnerNumberModel(1,1,nodes.size(), 1));
        this.frame.toolbar.nodeJSpinner.setModel(new SpinnerNumberModel(1,1,nodes.size(), 1));
        //this.frame.toolbar.nodeSpinner.setModel(new SpinnerNumberModel(1, 1, nodes.size(), 1));
    }

    void drawEdge(int i, int j) {
        Integer x1 = new Double(nodes.get(i).getCenterX()).intValue();
        Integer y1 = new Double(nodes.get(i).getCenterY()).intValue();
        Integer x2 = new Double(nodes.get(j).getCenterX()).intValue();
        Integer y2 = new Double(nodes.get(j).getCenterY()).intValue();

        EdgeShape newEdge = new EdgeShape(x1, y1, x2, y2);
        if (!edges.contains(newEdge)) {
            this.edges.add(newEdge);
            graphics.setColor(Color.BLACK);
            graphics.setStroke(new BasicStroke(5));
            graphics.drawLine(x1, y1, x2, y2);
            graphics.drawString(String.valueOf(newEdge.getWeight()), x1 + (x2 - x1)/2, y1 + (y2-y1)/2);
            repaint();
        }
    }

    private void init() {
        this.setPreferredSize(new Dimension(width, height));
        this.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1));
        this.image = new BufferedImage(800,600,BufferedImage.TYPE_INT_ARGB);
        this.graphics = image.createGraphics();

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //boolean ok=true;

                for (EdgeShape s: edges){
                    if (s.contains(e.getX(),e.getY()))
                        //ok=false;
                        break;
                }

                if(e.getButton() == MouseEvent.BUTTON3)
                {
                    JTextField weightTF = new JTextField(String.valueOf(0));
                    JPanel panel = new JPanel(new GridLayout(0, 1));
                    panel.add(new JLabel("Weight:"));
                    panel.add(weightTF);
                    int result = JOptionPane.showConfirmDialog(null, panel, "Edge info", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

                    if (result == JOptionPane.OK_OPTION) {
                        //System.out.println("OK");

                    }
                }

                if (e.getButton() == MouseEvent.BUTTON1) {
                    Integer radius = (Integer) 12;

                    drawNode(e.getX(), e.getY());
                    graphics.setColor(Color.LIGHT_GRAY);
                    graphics.setFont(new Font("Arial", Font.BOLD, 12));
                    graphics.drawString("" + nodes.size(), e.getX(), e.getY());
                }
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePt = e.getPoint();
                for (NodeShape s: nodes) {
                    s.select(mousePt.x, mousePt.y);
                }
                for (EdgeShape s: edges){
                    s.select(mousePt.x, mousePt.y);
                }
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                for (NodeShape s: nodes) {
                    s.unselect(e.getX(),e.getY());
                }

                for (EdgeShape s: edges){
                    s.unselect(e.getX(),e.getY());
                }
            }
        });


    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image == null) {
            image = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
            Graphics2D graphics2D = image.createGraphics();
            graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        }

        g.drawImage(image, 0, 0, null);
    }

//    void removeNode(Integer i) {
//        if (this.nodes.size() > i && i >= 0) {
//            this.nodes.remove(i);
//            this.frame.toolbar.nodeISpinner.setModel(new SpinnerNumberModel(0,0,this.nodes.size(), 1));
//            this.frame.toolbar.nodeJSpinner.setModel(new SpinnerNumberModel(0,0,this.nodes.size(), 1));
//            //this.frame.toolbar.nodeSpinner.setModel(new SpinnerNumberModel(0, 0, this.nodes.size(), 1));
//            repaint();
//        }
//    }

    void clear() {
        super.paintComponent(graphics);
        this.nodes.clear();
        this.edges.clear();
        this.frame.toolbar.nodeISpinner.setModel(new SpinnerNumberModel(0,0,0, 1));
        this.frame.toolbar.nodeJSpinner.setModel(new SpinnerNumberModel(0,0,0, 1));
        //this.frame.toolbar.nodeSpinner.setModel(new SpinnerNumberModel(0, 0, 0, 1));
        repaint();
    }

    public BufferedImage getImage() {
        return image;
    }
}
