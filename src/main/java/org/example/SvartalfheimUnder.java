package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class SvartalfheimUnder extends GameEngine {
//    public static void main(String[] args) {
//        createGame(new SvartalfheimUnder());
//    }

    Point2D pos = new Point2D.Double();
    // princess
    Image princessSheet;
    Image[] frames_up;
    Image[] frames_down;
    Image[] frames_left;
    Image[] frames_right;

    // dwarf
    Image dwarf;
    Image[] framesDwarfLeft;
    Image[] framesDwarfRight;
    boolean dwarfLeft;
    int currentDwarfFrame;
    double dwarfPositionX = 200;
    boolean movingRight = true; // 矮人移动方向标志
    double animTime;
    boolean dwarfStop = false; // 矮人停止移动标志

    // Dialogue images
    Image[] dialogueImages = new Image[5];
    int currentDialogueIndex = -1; // 初始值为-1，表示没有显示任何对话

    // 添加对话完成标志
    boolean dialogueFinished = false;

    public void loadDialogueImages() {
        dialogueImages[0] = loadImage("Images/Svartalfheim/dwarfTalkUnder1.png");
        dialogueImages[1] = loadImage("Images/Svartalfheim/dwarfTalkUnder2.png");
        dialogueImages[2] = loadImage("Images/Svartalfheim/dwarfTalkUnder3.png");
        dialogueImages[3] = loadImage("Images/Svartalfheim/princessTalkUnder1.png");
        dialogueImages[4] = loadImage("Images/Svartalfheim/dwarfTalkUnder4.png");
    }

    public void drawDialogue() {
        if (currentDialogueIndex >= 0 && currentDialogueIndex < dialogueImages.length) {
            drawImage(dialogueImages[currentDialogueIndex], 0, 530, 1255, 200);
        }
    }

    public void initDwarf() {
        dwarf = loadImage("Images/Svartalfheim/dwarf.png");

        framesDwarfLeft = new Image[3];
        framesDwarfRight = new Image[3];

        for (int i = 0; i < 3; i++) {
            framesDwarfRight[i] = subImage(dwarf, 72 * i, 96, 72, 96);
            framesDwarfLeft[i] = subImage(dwarf, 72 * i, 288, 72, 96);
        }
    }

    public void drawDwarf() {
        if (dwarfLeft) {
            drawImage(framesDwarfLeft[currentDwarfFrame], dwarfPositionX + 200, 300, 72 * 1.5, 96 * 1.5);
        } else {
            drawImage(framesDwarfRight[currentDwarfFrame], dwarfPositionX + 200, 300, 72 * 1.5, 96 * 1.5);
        }
    }

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
            drawImage(frames_up[currentFrame], pos.getX(), pos.getY(), 57.6 * 1.7, 76.8 * 2);
        } else if (is_down) {
            drawImage(frames_down[currentFrame], pos.getX(), pos.getY(), 57.6 * 1.7, 76.8 * 2);
        } else if (is_left) {
            drawImage(frames_left[currentFrame], pos.getX(), pos.getY(), 57.6 * 1.7, 76.8 * 2);
        } else if (is_right) {
            drawImage(frames_right[currentFrame], pos.getX(), pos.getY(), 57.6 * 1.7, 76.8 * 2);
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

        // 让矮人走路，只有在 dwarfStop 为 false 时才移动
        if (!dwarfStop) {
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
        }

        // 公主走路
        updatePrincess(dt);

        CheckMission();
    }

    public int getFrame(double d, int num_frames) {
        return (int) Math.floor(((animTime % d) / d) * num_frames);
    }

    // 检查矮人和公主之间的距离是否小于75像素
    boolean checkMission = false;

    public void CheckMission() {
        if (distance(dwarfPositionX + 200, 300, pos.getX(), pos.getY()) < 75) {
            checkMission = true;
        } else {
            checkMission = false;
        }
    }

    // 计算两点之间的距离
    public double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    // 绘制naga‘s grace
    ImageIcon grace;
    Image board;
    public void drawGrace() {
        board = loadImage("Images/Svartalfheim/board.png");
        drawImage(board, 380, 190, 500, 300);
        String gracePath = "Images/Svartalfheim/crown.gif";
        drawGif(gracePath, 550, 310, 150, 150);
    }

    @Override
    public void init() {
        initDwarf();
        initPrincess();
        loadDialogueImages();
    }

    @Override
    public void paintComponent() {
        Image bg = loadImage("Images/Svartalfheim/bg.png");
        drawImage(bg, 0, 0, 1255, 700);

        drawDwarf();
        drawPrincess();

        // 在矮人头顶显示“Press F”提示
        if (checkMission) {
            changeColor(Color.white);
            drawText(dwarfPositionX + 230, 330, "Press F", "Arial", 20);
        }

        // 绘制对话
        // 绘制对话
        drawDialogue();

        // 如果对话完成，则绘制 Grace
        if (dialogueFinished) {
            drawGrace();
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
        } else if (e.getKeyCode() == KeyEvent.VK_F && distance(dwarfPositionX + 300, 350, pos.getX(), pos.getY()) < 75) {
            currentDialogueIndex = 0; // 按下 F 键时显示第一个对话框
            dwarfStop = true; // 按下 F 键时停止矮人的移动
            dwarfLeft = false; // 确保矮人面向右侧
            currentDwarfFrame = 0; // 固定矮人的精灵图为第一帧
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if ((e.getX() >= 380 && e.getX() <= 380 + 500 && e.getY() >= 190 && e.getY() <= 190 + 300) && dialogueFinished) {


            createGame(new GoldMiner());
        }

        // 点击对话框显示下一张对话图片
        if (e.getY() >= 530 && e.getY() <= 730) {
            if (currentDialogueIndex >= 0 && currentDialogueIndex < dialogueImages.length - 1) {
                currentDialogueIndex++; // 显示下一张对话图片
            } else {
                currentDialogueIndex = -1; // 重置为初始值，表示没有显示任何对话
                dialogueFinished = true; // 设置对话完成标志为 true
            }
        }
    }
}
