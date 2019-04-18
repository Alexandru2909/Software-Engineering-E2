package GUI;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ControlPanel extends JPanel {

    private final DrawingFrame frame;
    JButton loadButton = new JButton("Load");
    JButton saveButton = new JButton("Save");
    JButton resetButton = new JButton("Reset");
    private BufferedImage paintImage = null;

    ControlPanel(DrawingFrame frame) {
        this.frame = frame;
        init();
    }

    private void init() {
        add(loadButton);
        loadButton.addActionListener((e) -> {
            String fileName = JOptionPane.showInputDialog(frame,"Choose a file");
            try{
                File imageFile = new File(fileName);
                paintImage = ImageIO.read(imageFile);
                repaint();
            } catch (IOException e2) {
                e2.printStackTrace();
            }
        });

        add(saveButton);
        saveButton.addActionListener((e) -> {
            String fileName = JOptionPane.showInputDialog(frame, "Choose a file name");
            try {
                ImageIO.write(frame.canvas.getImage(), "PNG", new File(fileName + ".png"));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        });

        add(resetButton);
        resetButton.addActionListener((e -> frame.canvas.clear()));

    }

}
