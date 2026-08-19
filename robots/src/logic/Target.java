package logic;

import gui.GameVisualizer;

import javax.swing.*;
import java.awt.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Target {
    private volatile int targetPositionX;
    private volatile int targetPositionY;
    private volatile int time;
    private volatile Color color;
    private final Random rnd;
    private final Color[] colors = new Color[]{Color.CYAN, Color.YELLOW, Color.ORANGE};
    private final Timer timer = new Timer();
    private final GameVisualizer window;
    private final Object lock = new Object();
    private boolean isBonus;
    private int typeOfTarget;

    public Target(GameVisualizer gameWindow) {
        rnd = new Random();
        window = gameWindow;
        typeOfTarget = rnd.nextInt(10);
        targetPositionX = rnd.nextInt(500);
        targetPositionY = rnd.nextInt(400);
        time = rnd.nextInt(15);
        setColor();
        setIsBonusState();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                changeTargetCondition();
            }
        }, time * 1000L);
    }

    protected void changeTargetCondition() {
        typeOfTarget = rnd.nextInt(10);
        targetPositionX = rnd.nextInt(window.getWidth() + window.getX() + 1);
        targetPositionY = rnd.nextInt(window.getHeight() + window.getY() + 1);
        time = rnd.nextInt(15);
        setColor();
        setIsBonusState();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                changeTargetCondition();
            }
        }, time * 1000L);
    }

    public int getTargetPositionX() {
        return targetPositionX;
    }

    public int getTargetPositionY() {
        return targetPositionY;
    }

    public int getTypeOfTarget() {
        return typeOfTarget;
    }

    public Color getColor() {
        return color;
    }

    public boolean getIsBonusState() {
        return isBonus;
    }

    private void setColor() {
        Color selectedColor = null;
        if (typeOfTarget == 1) {
            selectedColor = Color.RED;
        }
        if (typeOfTarget == 2) {
            selectedColor = Color.BLUE;
        }
        if (typeOfTarget == 3) {
            selectedColor = Color.GREEN;
        }
        if (selectedColor == null) {
            selectedColor = colors[rnd.nextInt(colors.length)];
        }
        color = selectedColor;
    }

    private void setIsBonusState() {
        isBonus = typeOfTarget >= 1 & typeOfTarget <= 3;
    }
}
