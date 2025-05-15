package org.Mefju;

import javax.swing.*;
import java.awt.*;
import java.beans.Visibility;
import java.util.Objects;

public class GameFrame extends JFrame{
    String title ="Snake";

    public GameFrame()
    {
        this.add(new GamePanel());
        this.setTitle(title);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    public GameFrame(int delay)
    {
        ImageIcon snakeIMG = new ImageIcon(Objects.requireNonNull(getClass().getResource("/snakeimg.png")));
        this.add(new GamePanel(delay));
        this.setTitle(title);
        this.setIconImage(snakeIMG.getImage());
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

}
