package org.Mefju;

import javax.swing.*;
import java.awt.*;

public class SnakeMainMenu extends JFrame {
    public SnakeMainMenu()
    {
        setTitle("Snake-Menu");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(300, 200);
        setLayout(new GridLayout(4, 1));

        JLabel label = new JLabel("Wybierz poziom trudności", SwingConstants.CENTER);
        add(label);

        JButton easy = new JButton("Łatwy");
        JButton medium = new JButton("Średni");
        JButton hard = new JButton("Trudny");

        add(easy);
        add(medium);
        add(hard);

        easy.addActionListener(e -> startGame(150));
        medium.addActionListener(e -> startGame(100));
        hard.addActionListener(e -> startGame(60));

        setLocationRelativeTo(null);
        setVisible(true);
    }
    private void startGame(int delay)
    {
        dispose();
        new GameFrame(delay);
    }
}
