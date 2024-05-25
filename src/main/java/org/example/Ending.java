package org.example;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class Ending extends GameEngine {
    public static void main(String[] args) {
        createGame(new Ending());
    }

    Image narrationImage;
    int currentTextIndex = -1;
    String[] texts = {
            "After successfully sealing the Demon King,",
            "Princess Celesta embarked on her journey back home.",
            "Upon returning to Nordria",
            "she was greeted with a hero's welcome by people",
            "(Press space to continue)"
    };
    boolean allTextsShown = false;
    double animTime;

    public void loadNarrationImage() {
        narrationImage = loadImage("Images/Ending/bg.png");
    }

    public void drawNarration() {
        drawImage(narrationImage, 0, 0, 1255, 700);
        String fontName = "Trebuchet MS";
        if (currentTextIndex >= 0) {
            changeColor(Color.white);
            for (int i = 0; i <= currentTextIndex; i++) {
                drawText(20, 400 + i * 50, texts[i], fontName, 26);
            }
        }
    }

    Image mainMap;
    public void drawMainMap() {
        mainMap = loadImage("Images/Ending/mainMap.png");
        drawImage(mainMap, 0, 0, 1255, 700);
    }

    // wizard
    Image wizard;
    public void drawWizard() {
        wizard = loadImage("Images/Ending/Wizard.png");
        drawImage(wizard, 300, 200, 61 * 1.5, 97 * 1.5);
    }

    // elf
    Image elf;
    public void drawElf() {
        elf = loadImage("Images/Ending/elf.png");
        drawImage(elf, 600, 150, 66 * 1.5, 97 * 1.5);
    }

    // dwarf
    Image dwarf;
    public void drawDwarf() {
        dwarf = loadImage("Images/Ending/dwarf.png");
        drawImage(dwarf, 800, 250, 67 * 1.5, 79 * 1.5);
    }
    //board
    Image board;
    public void drawBoard(){
        board = loadImage("Images/Ending/board.png");
        drawImage(board, 300, 200, 300, 150);
    }

    public double distance(double x1, double y1, double x2, double y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    Boolean checkWizard;
    Boolean checkElf;
    Boolean checkDwarf;
    public void checkCollision() {
        // check collision between celesta and wizard
        if (distance(300, 200, pos.getX(), pos.getY()) < 75) {
            checkWizard = true;
        } else {
            checkWizard = false;
        }

        //check collision between celesta and elf
        if (distance(600, 150, pos.getX(), pos.getY()) < 75) {
            checkElf = true;
        } else {
            checkElf = false;
        }

        //check collision between celesta and dwarf
        if (distance(800, 250, pos.getX(), pos.getY()) < 75) {
            checkDwarf = true;
        } else {
            checkDwarf = false;
        }
    }

    // princess
    Point2D pos = new Point2D.Double();
    Image princessSheet;
    Image[] frames_up;
    Image[] frames_down;
    Image[] frames_left;
    Image[] frames_right;
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

    // draw elf playing music
    Image elf1;
    Image elf2;
    boolean isPlayingMusic = false;
    double musicStartTime = 0;

    public void loadPlayMusic() {
        elf1 = loadImage("Images/Ending/elf1.png");
        elf2 = loadImage("Images/Ending/elf2.png");
    }

    // draw dancing
    Image dance1;
    Image dance2;
    boolean isDancing = false;
    double danceStartTime = 0;

    public void loadDancing() {
        dance1 = loadImage("Images/Ending/dance2.jpg");
        dance2 = loadImage("Images/Ending/dance1.png");
    }

    // draw dwarf eating
    Image dwarf1;
    Image dwarf2;
    Image dwarf3;
    boolean isEating = false;
    double eatStartTime = 0;

    public void loadEating() {
        dwarf1 = loadImage("Images/Ending/eat1.png");
        dwarf2 = loadImage("Images/Ending/eat2.png");
        dwarf3 = loadImage("Images/Ending/eat3.png");
    }

    boolean is_moving = false;
    boolean is_up = false;
    boolean is_down = true;
    boolean is_left = false;
    boolean is_right = false;
    int currentFrame;

    public void updatePrincess(double dt) {
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

            if (pos.getX() <= 0) {
                pos.setLocation(0, pos.getY());
            }
            if (pos.getX() >= 1197.4) {
                pos.setLocation(1197.4, pos.getY());
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

    public int getFrame(double d, int num_frames) {
        return (int) Math.floor(((animTime % d) / d) * num_frames);
    }

    AudioClip bgm;
    @Override
    public void init() {
        bgm = loadAudio("Audio/Ending/AWellEarnedCelebration.wav");
        playAudio(bgm);
        loadNarrationImage();
        initPrincess();
        loadDancing();
        loadPlayMusic();
        loadEating();
    }

    @Override
    public void update(double dt) {
        animTime += dt;
        if (!allTextsShown) {
            if (animTime >= 4 && currentTextIndex < texts.length - 1) {
                currentTextIndex++;
                animTime = 0;
                if (currentTextIndex == texts.length - 1) {
                    allTextsShown = true;
                }
                mFrame.repaint();
            }
        }

        if (isDancing) {
            if (animTime - danceStartTime >= 8) {
                isDancing = false;
            }
        } else if(isPlayingMusic) {
            if (animTime - musicStartTime >= 8) {
                isPlayingMusic = false;
            }
        } else if(isEating){
            if (animTime - eatStartTime >= 12) {
                isEating = false;
            }
        }
        else{
            updatePrincess(dt);
            checkCollision();

        }

    }

    @Override
    public void paintComponent() {
        if (!allTextsShown) {
            drawNarration();
        } else if (isDancing) {
            double danceTime = animTime - danceStartTime;
            if (danceTime < 4) {
                drawImage(dance1, 0, 0, 1255, 700);
            } else if (danceTime < 8) {
                drawImage(dance2, 0, 0, 1255, 700);
            }
        } else if(isPlayingMusic){
            double musicTime = animTime - musicStartTime;
            if (musicTime < 4) {
                drawImage(elf1, 0, 0, 1255, 700);
            } else if (musicTime < 8) {
                drawImage(elf2, 0, 0, 1255, 700);
            }
        }else if(isEating){
            double eatTime = animTime - eatStartTime;
            if (eatTime < 4) {
                drawImage(dwarf1, 0, 0, 1255, 700);
            } else if (eatTime < 8) {
                drawImage(dwarf2, 0, 0, 1255, 700);
            } else if(eatTime < 12){
                drawImage(dwarf3, 0, 0, 1255, 700);
            }
        } else {
            drawMainMap();
            drawWizard();
            drawElf();
            drawDwarf();
            drawPrincess();

            if (checkWizard) {
                changeColor(Color.white);
                drawText(300, 190, "Press F", "Arial", 20);
            }

            if (checkElf) {
                changeColor(Color.white);
                drawText(600, 140, "Press F", "Arial", 20);
            }

            if (checkDwarf) {
                changeColor(Color.white);
                drawText(800, 240, "Press F", "Arial", 20);
            }
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
        } else if (e.getKeyCode() == KeyEvent.VK_F) {
            if (checkWizard) {
                isDancing = true;
                danceStartTime = animTime;
                mFrame.repaint();
            }else if(checkElf){
                isPlayingMusic = true;
                musicStartTime = animTime;
                mFrame.repaint();
            }else if(checkDwarf){
                isEating = true;
                eatStartTime = animTime;
                mFrame.repaint();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // Not used
    }
}
