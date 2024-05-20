package org.example;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class SvartalfheimUpper extends GameEngine {
    public static void main(String[] args) {
        createGame(new SvartalfheimUpper());
    }

    // mission
    Image mission;
    Point2D pos = new Point2D.Double();
    // princess
    Image princessSheet;
    Image[] frames_up;
    Image[] frames_down;
    Image[] frames_left;
    Image[] frames_right;

    public void drawMission() {
        mission = loadImage("Images/Svartalfheim/mission.png");
        if (getMission) {
            drawImage(mission, 380, 220, 520, 350);
        }
    }

    // dwarf
    Image dwarf;
    Image[] framesDwarfLeft;
    Image[] framesDwarfRight;
    boolean dwarfLeft;
    int currentDwarfFrame;
    double dwarfPositionX = 200;
    double animTime;
    boolean movingRight = true; // 新增：矮人移动方向标志

    public void initDwarf() {
        dwarf = loadImage("Images/Svartalfheim/dwarf.png");

        framesDwarfLeft = new Image[3];
        framesDwarfRight = new Image[3];

        for (int i = 0; i < 3; i++) {
            framesDwarfRight[i] = subImage(dwarf, 72 * i, 96, 72, 96);
            framesDwarfLeft[i] = subImage(dwarf, 72 * i, 288, 72, 96);
        }

        dwarfLeft = false;
    }

    public void drawDwarf() {
        if (dwarfLeft) {
            drawImage(framesDwarfLeft[currentDwarfFrame], dwarfPositionX + 300, 350, 72 * 0.7, 96 * 0.7);
        } else {
            drawImage(framesDwarfRight[currentDwarfFrame], dwarfPositionX + 300, 350, 72 * 0.7, 96 * 0.7);
        }
    }

    // princess
    public void initPrincess() {
        frames_up = new Image[3];
        frames_down = new Image[3];
        frames_left = new Image[3];
        frames_right = new Image[3];

        princessSheet = loadImage("Images/Svartalfheim/spritesheet_princess.png");

        for (int i = 0; i < 3; i++) {
            frames_up[i] = subImage(princessSheet, 72 * i, 0, 72, 96);
            frames_right[i] = subImage(princessSheet, 72 * i, 96, 72, 96);
            frames_down[i] = subImage(princessSheet, 72 * i, 192, 72, 96);
            frames_left[i] = subImage(princessSheet, 72 * i, 288, 72, 96);
        }

        pos.setLocation(627.5, 350);
    }

    public void drawPrincess() {
        if (is_up) {
            drawImage(frames_up[currentFrame], pos.getX(), pos.getY(), 57.6, 76.8);
        } else if (is_down) {
            drawImage(frames_down[currentFrame], pos.getX(), pos.getY(), 57.6, 76.8);
        } else if (is_left) {
            drawImage(frames_left[currentFrame], pos.getX(), pos.getY(), 57.6, 76.8);
        } else if (is_right) {
            drawImage(frames_right[currentFrame], pos.getX(), pos.getY(), 57.6, 76.8);
        }
    }

    boolean is_moving = false;
    boolean is_up = false;
    boolean is_down = true;
    boolean is_left = false;
    boolean is_right = false;

    int currentFrame;

    public void updatePrincess(double dt) {
        // 让公主走路
        if (is_moving) {
            if (is_up) {
                pos.setLocation(pos.getX(), pos.getY() - 5);
            } else if (is_down) {
                pos.setLocation(pos.getX(), pos.getY() + 5);
            } else if (is_left) {
                pos.setLocation(pos.getX() - 5, pos.getY());
            } else if (is_right) {
                pos.setLocation(pos.getX() + 5, pos.getY());
            }

            // Ensure the princess stays within the bounds of the background
            if (pos.getX() <= 0) {
                pos.setLocation(0, pos.getY());
            }
            if (pos.getX() >= 1197.4) {
                pos.setLocation(1197.4, pos.getY()); // Ensure princess stays at the right boundary
            }
            if (pos.getY() <= 0) {
                pos.setLocation(pos.getX(), 0);
            }
            if (pos.getY() >= 623.3) {
                pos.setLocation(pos.getX(), 623.3);
            }

            currentFrame = getFrame(0.3, 3);
        } else {
            currentFrame = 0;
        }
    }

    @Override
    public void update(double dt) {
        animTime += dt;

        // 让矮人走路
        if (movingRight) {
            dwarfPositionX += 1;
            if (dwarfPositionX >= 270) { // 修改这里，矮人应该在合适的位置调转方向
                movingRight = false;
                dwarfLeft = true;
            }
        } else {
            dwarfPositionX -= 1;
            if (dwarfPositionX <= 200) { // 修改这里，矮人应该在合适的位置调转方向
                movingRight = true;
                dwarfLeft = false;
            }
        }

        currentDwarfFrame = getFrame(0.3, 3);

        // 公主走路
        updatePrincess(dt);
    }

    public int getFrame(double d, int num_frames) {
        return (int) Math.floor(((animTime % d) / d) * num_frames);
    }

    // game
    Image bg;
    boolean getMission = false;

    @Override
    public void init() {
        initDwarf();
        initPrincess();
    }

    @Override
    public void paintComponent() {
        bg = loadImage("Images/Svartalfheim/dwarfHome.png");
        drawImage(bg, 0, 0, 1255, 700);
        getMission = true;

        // drawMission();
        drawDwarf();
        drawPrincess();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getX() >= 380 && e.getX() <= 380 + 520 && e.getY() >= 220 && e.getY() <= 220 + 350) {
            //createGame(new GoldMiner());
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        is_moving = false;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP) {
            is_moving = true;
            is_up = true;
            is_down = false;
            is_left = false;
            is_right = false;
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            is_moving = true;
            is_down = true;
            is_up = false;
            is_left = false;
            is_right = false;
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            is_moving = true;
            is_left = true;
            is_up = false;
            is_down = false;
            is_right = false;
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            is_moving = true;
            is_right = true;
            is_up = false;
            is_down = false;
            is_left = false;
        }
    }
}
