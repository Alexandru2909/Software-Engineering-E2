package GUI;

import javax.swing.*;
import java.awt.*;

public class Toolbar extends JPanel {

    private final DrawingFrame frame;

    //JLabel strokeSizeLabel = new JLabel("Stroke size");
    JLabel radiusSizeLabel = new JLabel("Radius size");

    JSpinner strokeSizeSpinner = new JSpinner(new SpinnerNumberModel(10, 0, 100, 1));

    JSpinner radiusSizeSpinner = new JSpinner(new SpinnerNumberModel(20, 10, 100, 1));

    JButton colorChooseButton = new JButton("Color");

    JSpinner nodeISpinner = new JSpinner(new SpinnerNumberModel(0,0,0,1));
    JSpinner nodeJSpinner = new JSpinner(new SpinnerNumberModel(0,0,0,1));
    JButton addEdge = new JButton("Add edge");
    //JButton removeEdge = new JButton("Remove edge");

    //JSpinner nodeSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 0, 1));
    //JButton removeNode = new JButton("Remove node");

    Color color;

    Toolbar(DrawingFrame frame) {
        this.frame = frame;
//        this.setLayout(new GridLayout(2,5));
        init();
    }

    private void init() {
        //add(strokeSizeLabel);
        //add(strokeSizeSpinner);

        add(radiusSizeLabel);
        add(radiusSizeSpinner);

        add(colorChooseButton);
        colorChooseButton.addActionListener((e -> {
            color = JColorChooser.showDialog(frame, "Pick color", null);
            colorChooseButton.setBackground(color);
        }));

        add(nodeISpinner);
        add(nodeJSpinner);
        add(addEdge);
        addEdge.addActionListener((e) -> {
            // draw line between nodes
            Integer i = (Integer) nodeISpinner.getValue() - 1;
            Integer j = (Integer) nodeJSpinner.getValue() - 1;

            if (!i.equals(j)) {
                frame.canvas.drawEdge(i, j);
            }
        });

        //add(removeEdge);
//        removeEdge.addActionListener();

        //add(nodeSpinner);
//        add(removeNode);
//        removeNode.addActionListener((e) -> {
//            Integer index = (Integer) frame.toolbar.nodeSpinner.getValue();
//            this.frame.canvas.removeNode(index);
//        });
    }

}
