package com.ahmetkca.engine;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Window {

    private JFrame frame;
    private BufferedImage image;
    private Canvas canvas;
    private BufferStrategy bs;
    private Graphics graphics;

    public Window(GameContainer gc) {
        image = new BufferedImage(gc.getWIDTH(), gc.getHEIGHT(), BufferedImage.TYPE_INT_ARGB);
        canvas = new Canvas();
        Dimension dimension = new Dimension((int) (gc.getWIDTH() * gc.getScale()),
                (int) (gc.getHEIGHT() * gc.getScale()));
        canvas.setPreferredSize(dimension);
        canvas.setMaximumSize(dimension);
        canvas.setMaximumSize(dimension);

        frame = new JFrame(gc.getTitle());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.add(canvas, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setVisible(true);

        canvas.createBufferStrategy(1);
        bs = canvas.getBufferStrategy();
        graphics = bs.getDrawGraphics();
    }

    public void update() {
        graphics.drawImage(image, 0, 0, canvas.getWidth(), canvas.getHeight(), null);
        bs.show();
    }

    public Canvas getCanvas() {
        return canvas;
    }

    public BufferedImage getImage() {
        return image;
    }

    public JFrame getFrame() {
        return frame;
    }
}
