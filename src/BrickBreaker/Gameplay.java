package BrickBreaker;

import javax.swing.JPanel;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

public class Gameplay extends JPanel implements KeyListener, ActionListener {
    private boolean play = false;
    private int score = 0;
    private int totalbricks = 21;
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

        map=new MapGenerator(3,7);

        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g) {
        // Clear the panel with a black background.
        super.paint(g);

        g.setColor(Color.black);
        g.fillRect(1,1,692, 592);

        g.setColor(Color.yellow);
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(683, 0, 3, 592);

        g.setColor(Color.green);
        g.fillRect(playerX, 550, 100, 8);

        g.setColor(Color.yellow);
        g.fillOval(ballposX, ballposY, 20, 20);

        map.draw((Graphics2D)g);

        g.dispose();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        timer.start();
        if (play) {

            ballposX += balldirX;
            ballposY += balldirY;

            A: for(int i=0;i<map.map.length;i++){ // first map is from line 26 creating map object of MapGenerator
                for(int j=0;j<map.map[0].length;j++){
                    if(map.map[i][j]>0){
                        int brickX = j* map.brickwidth + 80;
                        int brickY = i* map.brickheight + 50;
                        int brickwidth=map.brickwidth;
                        int brickheight=map.brickheight;
                        Rectangle brickrect = new  Rectangle(brickX, brickY, brickwidth, brickheight);
                        Rectangle ballrect = new  Rectangle(ballposX, ballposX, balldirX, balldirY);
                        if(ballrect.intersects(brickrect)){
                            map.setBrickValue(0, i, j);
                            totalbricks--;
                            score+=5;
                            if(ballposX+19>=brickrect.x || ballposX+1>=brickrect.x+brickrect.width){
                                balldirX *= -1;
                            }
                            else{
                                balldirY *= -1;
                            }
                            break A;
                        }
                    }
                }
            }

            if(new Rectangle(ballposX,ballposY,20,20).intersects(new Rectangle(playerX, 550, 100, 8))){
                balldirY *= -1;
            }
            
            if (ballposX < 0) {
                balldirX *= -1;
            }
            if (ballposY < 0) {
                balldirY *= -1;
            }
            if (ballposX > 670) {
                balldirX *= -1;
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
