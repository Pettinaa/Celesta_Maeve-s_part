package org.example;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

public class GoldMiner extends GameEngine {
    public static void main(String[] args) {
        createGame(new GoldMiner());
    }

    // Line properties
    int startX;
    int startY;
    int currentX;
    int currentY;

    // State: 0 = swinging, 1 = extending, 2 = retracting
    int state;

    double lineLength;
    double maxLineLength = 500; // Maximum length the line can extend
    double angle = 0.5;
    double targetAngle;
    int direction = 1;

    // Gold properties
    int numGolds = 6; // 3 types * 2 each
    int[] goldX = new int[numGolds];
    int[] goldY = new int[numGolds];
    int goldWidth = 40;
    int goldHeight = 40;
    boolean[] goldGet = new boolean[numGolds];
    boolean[] goldCaptured = new boolean[numGolds];
    int[] goldTypes = new int[numGolds]; // 0: gold, 1: gold2, 2: gold3
    Image gold;
    Image gold2;
    Image gold3;

    // Gem properties
    int numGems = 4;
    int[] gemX = new int[numGems];
    int[] gemY = new int[numGems];
    int gemWidth = 32;
    int gemHeight = 32;
    boolean[] gemGet = new boolean[numGems];
    boolean[] gemCaptured = new boolean[numGems];
    Image gems;
    Image gem1;
    Image gem2;
    Image gem3;
    Image gem4;

    Image hook;
    Random rand = new Random();

    public void initLine() {
        lineLength = 100;
        startX = 630;
        startY = 330;
        currentX = startX;
        currentY = startY;
    }

    public void initGolds() {
        for (int i = 0; i < numGolds; i++) {
            goldX[i] = rand.nextInt(1200) + 50; // Random x position between 50 and 1250
            goldY[i] = rand.nextInt(300) + 400; // Random y position between 400 and 700
            goldTypes[i] = i / 2; // Assign type based on index (2 of each type)
        }
    }

    public void initGems() {
        for (int i = 0; i < numGems; i++) {
            gemX[i] = rand.nextInt(1200) + 50; // Random x position between 50 and 1250
            gemY[i] = rand.nextInt(300) + 400; // Random y position between 400 and 700
        }
    }

    public void updateHook(double dt) {
        if (state == 0) { // Swinging state
            if (angle < 0.1) {
                direction = 1;
            } else if (angle > 0.9) {
                direction = -1;
            }
            angle = angle + direction * 0.005;
            currentX = (int) (startX + lineLength * Math.cos(angle * Math.PI));
            currentY = (int) (startY + lineLength * Math.sin(angle * Math.PI));
        } else if (state == 1) { // Extending state
            lineLength += 200 * dt;
            if (lineLength >= maxLineLength) {
                lineLength = maxLineLength;
                state = 2; // Switch to retracting state
            }
            currentX = (int) (startX + lineLength * Math.cos(targetAngle));
            currentY = (int) (startY + lineLength * Math.sin(targetAngle));

            // Check for collision with gold
            for (int i = 0; i < numGolds; i++) {
                if (!goldCaptured[i] && checkCollision(currentX, currentY, goldX[i], goldY[i], goldWidth, goldHeight)) {
                    goldGet[i] = true;
                    goldCaptured[i] = true; // Mark the gold as captured
                    state = 2; // Switch to retracting state immediately
                    break;
                }
            }

            // Check for collision with gems
            for (int i = 0; i < numGems; i++) {
                if (!gemCaptured[i] && checkCollision(currentX, currentY, gemX[i], gemY[i], gemWidth, gemHeight)) {
                    gemGet[i] = true;
                    gemCaptured[i] = true; // Mark the gem as captured
                    state = 2; // Switch to retracting state immediately
                    break;
                }
            }
        } else if (state == 2) { // Retracting state
            lineLength -= 200 * dt;
            if (lineLength <= 100) {
                lineLength = 100;
                state = 0; // Switch to swinging state
                for (int i = 0; i < numGolds; i++) {
                    goldGet[i] = false; // Hide gold when hook is retracted
                }
                for (int i = 0; i < numGems; i++) {
                    gemGet[i] = false; // Hide gems when hook is retracted
                }
            }
            currentX = (int) (startX + lineLength * Math.cos(targetAngle));
            currentY = (int) (startY + lineLength * Math.sin(targetAngle));
        }
    }

    public void drawHook() {
        changeColor(Color.RED);
        drawLine(startX, startY, currentX, currentY, 3);
        hook = loadImage("Images/GoldMiner/hook.png");
        drawImage(hook, currentX - 35, currentY - 10, 72, 50);
    }

    public void drawGold() {
        if (gold == null) {
            gold = loadImage("Images/GoldMiner/gold0.gif");
            gold2 = loadImage("Images/GoldMiner/gold1.gif");
            gold3 = loadImage("Images/GoldMiner/gold2.gif");
            gems = loadImage("Images/GoldMiner/gems.png");

            //将每种宝石从精灵图中提取出来
            gem1 = subImage(gems, 0, 0, 32, 32);
            gem2 = subImage(gems, 32, 0, 32, 32);
            gem3 = subImage(gems, 0, 32, 32, 32);
            gem4 = subImage(gems, 32, 32, 32, 32);
        }

        //画金块
        for (int i = 0; i < numGolds; i++) {
            if (goldGet[i]) {
                // Draw gold at hook's current position
                if (goldTypes[i] == 0) {
                    drawImage(gold, currentX - goldWidth / 2, currentY - goldHeight / 2, goldWidth, goldHeight);
                } else if (goldTypes[i] == 1) {
                    drawImage(gold2, currentX - goldWidth / 2, currentY - goldHeight / 2, goldWidth, goldHeight);
                } else {
                    drawImage(gold3, currentX - goldWidth / 2, currentY - goldHeight / 2, goldWidth, goldHeight);
                }
            } else if (!goldCaptured[i]) {
                // Draw gold at its original position if it has not been captured
                if (goldTypes[i] == 0) {
                    drawImage(gold, goldX[i], goldY[i], goldWidth, goldHeight);
                } else if (goldTypes[i] == 1) {
                    drawImage(gold2, goldX[i], goldY[i], goldWidth, goldHeight);
                } else {
                    drawImage(gold3, goldX[i], goldY[i], goldWidth, goldHeight);
                }
            }
        }

        //画宝石
        for (int i = 0; i < numGems; i++) {
            if (gemGet[i]) {
                // Draw gem at hook's current position
                if (i == 0) {
                    drawImage(gem1, currentX - gemWidth / 2, currentY - gemHeight / 2, gemWidth, gemHeight);
                } else if (i == 1) {
                    drawImage(gem2, currentX - gemWidth / 2, currentY - gemHeight / 2, gemWidth, gemHeight);
                } else if (i == 2) {
                    drawImage(gem3, currentX - gemWidth / 2, currentY - gemHeight / 2, gemWidth, gemHeight);
                } else if (i == 3) {
                    drawImage(gem4, currentX - gemWidth / 2, currentY - gemHeight / 2, gemWidth, gemHeight);
                }
            } else if (!gemCaptured[i]) {
                // Draw gem at its original position if it has not been captured
                if (i == 0) {
                    drawImage(gem1, gemX[i], gemY[i], gemWidth, gemHeight);
                } else if (i == 1) {
                    drawImage(gem2, gemX[i], gemY[i], gemWidth, gemHeight);
                } else if (i == 2) {
                    drawImage(gem3, gemX[i], gemY[i], gemWidth, gemHeight);
                } else if (i == 3) {
                    drawImage(gem4, gemX[i], gemY[i], gemWidth, gemHeight);
                }
            }
        }
    }

    public boolean checkCollision(int x, int y, int rectX, int rectY, int rectWidth, int rectHeight) {
        return x >= rectX && x <= rectX + rectWidth && y >= rectY && y <= rectY + rectHeight;
    }

    // Game properties
    Image background;
    Image people;

    public void init() {
        setupWindow(1255, 700);
        background = loadImage("Images/GoldMiner/bg.png");
        people = loadImage("Images/GoldMiner/people.png");
        initLine();
        initGolds();
        initGems();
    }

    @Override
    public void update(double dt) {
        updateHook(dt);
    }

    @Override
    public void paintComponent() {
        clearBackground(1255, 700);
        drawImage(background, 0, 0, 1255, 700);
        drawImage(people, 580, 240, 100, 100);
        drawHook();
        drawGold();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && state == 0) {
            targetAngle = angle * Math.PI; // Save the current angle
            state = 1; // Switch to extending state
        }
    }
}
