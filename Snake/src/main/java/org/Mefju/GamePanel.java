package org.Mefju;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private JButton restartBtn;
    private JButton menuBtn;
    private JButton wynikiBtn;

    static final int SCREEN_WIDTH = 800;
    static final int SCREEN_HEIGHT = 800;
    static final int UNIT_SIZE = 25;
    private boolean scoreSaved = false;
    static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
    static int delay;

    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    char direction = 'D';
    boolean running = false;
    Timer timer;
    Random random;
    private Image headImage;
    private Image bodyImage;
    private Image appleImage;

    public GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(400,400));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(this);
        //startGame();
    }
    public GamePanel(int delay) {
        random = new Random();
        this.setPreferredSize(new Dimension(800,800));
        this.setBackground(Color.black);
        this.setFocusable(true);
        this.addKeyListener(this);
        appleImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/appleIMG.jpg"))).getImage();
        startGame(delay);
    }
    private void startGame(int delay)
    {
        GamePanel.delay = delay;
        newApple();
        running = true;
        timer = new Timer(delay,this);
        timer.start();
    }
    private void newApple()
    {
        appleX = random.nextInt(SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }
    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {
            case 'U': y[0] -= UNIT_SIZE; break;
            case 'D': y[0] += UNIT_SIZE; break;
            case 'L': x[0] -= UNIT_SIZE; break;
            case 'R': x[0] += UNIT_SIZE; break;
        }
    }

    public void checkApple() {
        if (x[0] == appleX && y[0] == appleY) {
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void checkCollisions() {
        // zderzenie z własnym ciałem
        for (int i = bodyParts; i > 0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                running = false;
                break;
            }
        }

        if (x[0] < 0 || x[0] >= SCREEN_WIDTH || y[0] < 0 || y[0] >= SCREEN_HEIGHT) {
            running = false;
        }

        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        String text = "Game Over";
        g.setColor(Color.red);
        g.setFont(new Font("Arial", Font.BOLD, 40));
        FontMetrics metrics = g.getFontMetrics(g.getFont());
        int x =(getWidth() - metrics.stringWidth(text))/2;
        int y = getHeight() / 2-50;
        g.drawString(text, x, y);

    }
    public void showEndButtons()
    {
        int buttonWidth = 200;
        int buttonHeight = 40;
        int spacing = 15;
        restartBtn = new JButton("Zagraj ponownie");
        menuBtn = new JButton("Powrót do menu");
        wynikiBtn = new JButton("Ostatnie 5 wyników");
        this.setLayout(null);
        this.add(restartBtn);
        this.add(menuBtn);
        this.add(wynikiBtn);
        this.repaint();
        repositionButtons(buttonWidth, buttonHeight, spacing);
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repositionButtons(buttonWidth, buttonHeight, spacing);
            }
        });
        restartBtn.addActionListener(_ ->{
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.dispose();
            new GameFrame(delay);
        });
        menuBtn.addActionListener(_ ->{
            JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            topFrame.dispose();
            new SnakeMainMenu();
        });
        wynikiBtn.addActionListener(_ ->showLastScores());

    }
    private void repositionButtons(int buttonWidth, int buttonHeight, int spacing) {
        int x = (getWidth() - buttonWidth) / 2;
        int yStart = getHeight() / 2;

        if (restartBtn != null) restartBtn.setBounds(x, yStart, buttonWidth, buttonHeight);
        if (wynikiBtn != null) wynikiBtn.setBounds(x, yStart + buttonHeight + spacing, buttonWidth, buttonHeight);
        if (menuBtn != null) menuBtn.setBounds(x, yStart + 2 * (buttonHeight + spacing), buttonWidth, buttonHeight);
    }

    public void saveScore()
    {
        try{
            FileWriter file = new FileWriter("score.txt",true);
            file.write(STR."Wynik: \{applesEaten} o godzinie \{LocalDateTime.now()}\n");
            file.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void showLastScores()
    {
        try{
            List<String> allLines = Files.readAllLines(Paths.get("score.txt"));
            int size = allLines.size();
            int start = Math.max(0,size-5);
            List<String> lastScores = allLines.subList(start,size);
            StringBuilder sb = new StringBuilder("Ostatnie 5 wyników:\n");
            for(String line:lastScores)
                sb.append(line).append("\n");
            JOptionPane.showMessageDialog(this,sb.toString(),"Wyniki",JOptionPane.INFORMATION_MESSAGE);
        }catch (Exception e){
            JOptionPane.showMessageDialog(this,"Bład podczas odczytu","błąd",JOptionPane.INFORMATION_MESSAGE);
        }
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                if (direction != 'R') direction = 'L'; break;
            case KeyEvent.VK_RIGHT:
                if (direction != 'L') direction = 'R'; break;
            case KeyEvent.VK_UP:
                if (direction != 'D') direction = 'U'; break;
            case KeyEvent.VK_DOWN:
                if (direction != 'U') direction = 'D'; break;
        }
    }
    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.red);
            g.drawImage(appleImage,appleX, appleY, UNIT_SIZE, UNIT_SIZE,this);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                } else {
                    g.setColor(new Color(45, 180, 0));
                }
                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString(STR."Score: \{applesEaten}", 10, 20);
        } else {
            if(!scoreSaved)
            {
                saveScore();
                showEndButtons();
                scoreSaved = true;
            }
            gameOver(g);
        }
    }
    @Override
    public void keyReleased(KeyEvent e){}
}
