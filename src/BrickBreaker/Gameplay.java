package BrickBreaker;

import javax.swing.JPanel;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;

    private int row=4; // assigning brick data
    private int col=8;
    private int totalbricks = row*col;

    private Timer timer;
    private int delay = 8;
    private int playerX = 310;
    private int ballposX = 120;
    private int ballposY = 350;
    private int balldirX = -1;
    private int balldirY = -2;

    private MapGenerator map;

    public Gameplay() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);


        map = new MapGenerator(row,col); // Inserting bricks

        timer = new Timer(delay, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        // Clear the panel with a black background.
        super.paintComponent(g);

        g.setColor(Color.black);
        g.fillRect(1, 1, 692, 592);

        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(683, 0, 3, 592);

        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY, 20, 20);

        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.ROMAN_BASELINE, 25));
        g.drawString("Your Score: " + score, 490, 30);

        if(totalbricks==0){
            g.setColor(Color.green);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("YOU WON", 279, 330);
            g.setFont(new Font("serif", Font.ITALIC, 30));
            g.drawString("Your Score: " + score, 258, 390);
            g.setFont(new Font("serif", Font.ROMAN_BASELINE, 25));
            g.drawString("Press ENTER to restart", 235, 450);
        }
            
        if(ballposY>570){
            play=false;
            balldirX=0;
            balldirY=0;
            g.setColor(Color.red);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("GAME OVER", 260, 330);
            g.setFont(new Font("serif", Font.ITALIC, 30));
            g.drawString("Your Score: " + score, 258, 390);
            g.setColor(Color.green);
            g.setFont(new Font("serif", Font.ROMAN_BASELINE, 25));
            g.drawString("Press ENTER to restart", 235, 450);
        }

        map.draw((Graphics2D) g);

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {
            ballposX += balldirX;
            ballposY += balldirY;

            // Ball collision with walls
            if (ballposX < 0) {
                balldirX = -balldirX;
            }
            if (ballposY < 0) {
                balldirY = -balldirY;
            }
            if (ballposX > 650) {
                balldirX = -balldirX;
            }

            // Ball collision with paddle
            if (new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
                balldirY = -balldirY;
            }

            // Ball collision with bricks
            for (int i = 0; i < map.map.length; i++) {
                for (int j = 0; j < map.map[0].length; j++) {
                    if (map.map[i][j] > 0) {
                        int brickX = j * map.brickwidth + 80;
                        int brickY = i * map.brickheight + 50;
                        int brickwidth = map.brickwidth;
                        int brickheight = map.brickheight;
                        Rectangle brickrect = new Rectangle(brickX, brickY, brickwidth, brickheight);

                        if (brickrect.intersects(new Rectangle(ballposX, ballposY, 20, 20))) {
                            map.setBrickValue(0, i, j);
                            totalbricks--;
                            score += 5;

                            // Ball direction change based on collision
                            if (ballposX + 19 <= brickrect.x || ballposX + 1 >= brickrect.x + brickrect.width) {
                                balldirX = -balldirX;
                            } else {
                                balldirY = -balldirY;
                            }
                        }
                    }
                }
            }

            // Check for game over
            if (totalbricks == 0) {
                play = false;
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            if (playerX >= 600) {
                playerX = 600;
            } else {
                moveRight();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            if (playerX <= 10) {
                playerX = 10;
            } else {
                moveLeft();
            }
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!play) {
                score = 0;
                totalbricks = row*col;
                playerX = 310;
                ballposX = 120;
                ballposY = 350;
                balldirX = -1;
                balldirY = -2;
                map = new MapGenerator(row, col);

                repaint();
            }
        }
    }

    public void moveRight() {
        play = true;
        playerX += 20;
    }

    public void moveLeft() {
        play = true;
        playerX -= 20;
    }
}
