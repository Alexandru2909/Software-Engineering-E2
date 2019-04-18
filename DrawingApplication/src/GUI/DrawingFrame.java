package GUI;

import javax.swing.*;
import java.awt.*;

public class DrawingFrame extends JFrame {

    Canvas canvas;
    Toolbar toolbar;
    ControlPanel controlPanel;

    public DrawingFrame() {
        super("Graph Draw");
        init();
        addComponents();
        this.pack();
    }

    private void init() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        this.canvas = new Canvas(this);
        this.toolbar = new Toolbar(this);
        this.controlPanel = new ControlPanel(this);
    }

    private void addComponents() {
        add(canvas, BorderLayout.CENTER);
        add(toolbar, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.SOUTH);
    }

}
