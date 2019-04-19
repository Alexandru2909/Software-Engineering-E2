package GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;


import Shapes.NodeShape;
import org.w3c.dom.Node;

import static java.lang.StrictMath.abs;

public class Canvas extends JPanel {

    class Edge {
        final Integer x1;
        final Integer x2;
        final Integer y1;
        final Integer y2;

        private double weight;

        public Edge(Integer x1, Integer x2, Integer y1, Integer y2) {
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
            this.weight = 0.00;
        }

        public double getWeight() {
            return weight;
        }

        public void setWeight(double weight) {
            this.weight = weight;
        }
    }


    ArrayList<NodeShape> nodes = new ArrayList<>();
    ArrayList<Edge> edges = new ArrayList<>();

    private final DrawingFrame frame;
    private Graphics2D graphics;
    private BufferedImage image;

    private Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    private int width = 800;
    private int height = 600;

    private Point mousePt;

    Canvas(DrawingFrame frame) {
        this.frame = frame;
        init();
    }

    private void drawNode(int x, int y) {
            Integer radius = (Integer) 7;
            if (radius > 0) {
                graphics.setColor(Color.BLACK);
                NodeShape shape = new NodeShape(x, y, radius);
                graphics.fill(shape);
                this.nodes.add(shape);
                repaint();
                this.frame.toolbar.nodeISpinner.setModel(new SpinnerNumberModel(1,1,nodes.size(), 1));
                this.frame.toolbar.nodeJSpinner.setModel(new SpinnerNumberModel(1,1,nodes.size(), 1));
                //this.frame.toolbar.nodeSpinner.setModel(new SpinnerNumberModel(1, 1, nodes.size(), 1));
            }
    }

    void drawEdge(int i, int j) {
        Integer x1 = new Double(nodes.get(i).getCenterX()).intValue();
        Integer y1 = new Double(nodes.get(i).getCenterY()).intValue();
        Integer x2 = new Double(nodes.get(j).getCenterX()).intValue();
        Integer y2 = new Double(nodes.get(j).getCenterY()).intValue();

        Edge newEdge = new Edge(x1, x2, y1, y2);
        if (!edges.contains(newEdge)) {
            this.edges.add(newEdge);
            graphics.setColor(Color.BLACK);
            graphics.setStroke(new BasicStroke(3));
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
                drawNode(e.getX(), e.getY());
                graphics.setColor(Color.BLACK);
                graphics.setFont(new Font("Arial", Font.BOLD, 12));
                Integer radius = (Integer) 7;
                graphics.drawString("Node" + nodes.size(), e.getX() - (radius * 3 / 2), e.getY() - radius);
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                mousePt = e.getPoint();
                for (NodeShape s: nodes) {
                    s.select(mousePt.x, mousePt.y);
                }
            }
        });

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                for (NodeShape s: nodes) {
                    s.unselect();
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
