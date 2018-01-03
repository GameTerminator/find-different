package com.githang.finddifferent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * @author 黄浩杭 (huanghaohang@parkingwang.com)
 */
public class Main {
    private static final int WIDTH = 550;
    private static final int HEIGHT = 550;

    private static final int START_X = 134;
    private static final int START_Y1 = 66;
    private static final int START_Y2 = 666;

    public static void main(String[] args) throws IOException {
        AdbHelper helper = new AdbHelper();

        ImagePanel panel = new ImagePanel();
        panel.setSize(WIDTH, HEIGHT);
        panel.setBackground(Color.BLACK);
        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int x = START_X + e.getX();
                int y = START_Y1 + e.getY();
                helper.click(x, y);
                refreshSnapshot(helper, panel);
            }
        });

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttons.setSize(WIDTH, 100);

        Button connect = new Button("connect");
        connect.setSize(100, 50);
        connect.addActionListener(e -> {
            helper.waitForConnection();
            refreshSnapshot(helper, panel);
        });
        buttons.add("connect", connect);

        Button disconnect = new Button("disconnect");
        disconnect.addActionListener(e -> helper.disconnect());
        disconnect.setSize(100, 50);
        buttons.add("disconnect", disconnect);

        Button snapshot = new Button("snapshot");
        snapshot.setSize(100, 50);
        snapshot.addActionListener(e -> {
            refreshSnapshot(helper, panel);
        });
        buttons.add(snapshot.getLabel(), snapshot);

        JFrame frame = new JFrame();
        frame.setLayout(new BorderLayout());
        frame.add("North", buttons);
        frame.add("Center", panel);
        frame.setSize(WIDTH, HEIGHT + 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    private static void refreshSnapshot(AdbHelper helper, ImagePanel panel) {
        BufferedImage snapshot1 = helper.snapshot();
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Color a = new Color(snapshot1.getRGB(START_X + x, START_Y1 + y));
                Color b = new Color(snapshot1.getRGB(START_X + x, START_Y2 + y));
                if (isDifferentColor(a, b)) {
                    image.setRGB(x, y, a.getRGB());
                } else {
                    image.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }
        panel.setImage(image);
        panel.repaint();
    }

    private static boolean isDifferentColor(Color a, Color b) {
        return Math.abs(b.getRed() - a.getRed()) > 10
                || Math.abs(b.getGreen() - a.getGreen()) > 10
                || Math.abs(b.getBlue() - a.getBlue()) > 10;
    }
}
