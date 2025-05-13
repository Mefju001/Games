package org.Mefju;

import javax.swing.*;
import java.beans.Visibility;

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

}
