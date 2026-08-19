package gui;

import logic.*;

import static logic.RobotConstants.walls;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.*;

import static logic.MathematicalOperations.round;

public class GameVisualizer extends JPanel
{
    private final Timer mTimer = initTimer();
    private final ArrayList<Target> targets = new ArrayList<>();
    protected RobotCondition robotCondition1st;
    protected RobotCondition robotCondition2nd;
    private final GameVisualizer gameViz;
    protected ArrayList<BulletCondition> bullets;
    protected ArrayList<EnemyCondition> enemies;
    private final BasicLogic basicLogic = new BasicLogic();
    private final int pointsCount = 8;
    private JInternalFrame gameWindow;



    private static Timer initTimer()
    {
        Timer timer = new Timer("events generator", true);
        return timer;
    }

    private volatile int targetPositionX;
    private volatile int targetPositionY;

    private static final int periodRedrawEvent = 30;
    private static final int periodModelUpdateEvent = 10;

    public GameVisualizer(RobotCondition robot1st, RobotCondition robot2nd, ArrayList<BulletCondition> bullets, ArrayList<EnemyCondition> enemies, JInternalFrame gameWindow)
    {
        this.gameViz = this;
        JPanel thisWindow = this;


        for (var i = 0; i < pointsCount; i++){
            targets.add(new Target(gameViz));
        }
        this.gameWindow = gameWindow;
        robotCondition1st = robot1st;
        robotCondition2nd = robot2nd;
        this.enemies = enemies;
        this.bullets = bullets;
        setEnemiesForBullets();
        targetPositionX = round(robot2nd.getRobotX());
        targetPositionY = round(robot2nd.getRobotY());

        mTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                onRedrawEvent();
            }
        }, 0, periodRedrawEvent);
        mTimer.schedule(new TimerTask()
        {
            @Override
            public void run()
            {
                basicLogic.onModelUpdateEvent(targets, new Point(targetPositionX, targetPositionY), robotCondition1st, robotCondition2nd, bullets, enemies, thisWindow, gameViz);

            }
        }, 0, periodModelUpdateEvent);
        addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                setTargetPosition(e.getPoint());
                repaint();
            }
        });
        setDoubleBuffered(true);
    }

    public void setEnemiesForBullets(){
        BulletCondition.enemies.addAll(enemies);
    }

    public void setTargetPosition(Point p){
        targetPositionX = p.x;
        targetPositionY = p.y;
    }

    protected void onRedrawEvent()
    {
        EventQueue.invokeLater(this::repaint);
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        Graphics2D g2d = (Graphics2D)g;
        drawWalls(g2d);
        drawRobot(g2d, round(robotCondition1st.getRobotX()), round(robotCondition1st.getRobotY()), robotCondition1st.getRobotDirection(), NumberOfRobot.FirstRobot);
        drawRobot(g2d, round(robotCondition2nd.getRobotX()), round(robotCondition2nd.getRobotY()), robotCondition2nd.getRobotDirection(), NumberOfRobot.SecondRobot);
        drawTarget(g2d, targetPositionX, targetPositionY);
        for (Target target : targets) {
            drawColorTarget(g2d, target);
        }
        drawBullets(g2d, bullets);
        drawEnemies(g2d, enemies);
    }

    private void drawWalls(Graphics2D g) {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0,0);
        g.setTransform(t);
        g.setColor(Color.BLACK);
        for (int[] wall : walls) {
            g.drawRect(wall[0], wall[1], wall[2], wall[3]);
            g.fillRect(wall[0], wall[1], wall[2], wall[3]);
        }
    }

    private static void fillOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.fillOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private static void drawOval(Graphics g, int centerX, int centerY, int diam1, int diam2)
    {
        g.drawOval(centerX - diam1 / 2, centerY - diam2 / 2, diam1, diam2);
    }

    private void drawRobot(Graphics2D g, int x, int y, double direction, NumberOfRobot robotNumber)
    {
        if (robotCondition1st == null & robotNumber == NumberOfRobot.FirstRobot) {return;}
        if (robotCondition2nd == null & robotNumber == NumberOfRobot.SecondRobot) {return;}
        int robotCenterX = round(x);
        int robotCenterY = round(y);
        AffineTransform t = AffineTransform.getRotateInstance(direction, robotCenterX, robotCenterY);
        g.setTransform(t);
        if (robotNumber == NumberOfRobot.FirstRobot){
            g.setColor(Color.YELLOW);
        }
        else {
            g.setColor(Color.GREEN);
        }
        int robotDiameter1 = 30;
        int robotDiameter2 = 10;
        fillOval(g, robotCenterX, robotCenterY, robotDiameter1, robotDiameter2);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX, robotCenterY, robotDiameter1, robotDiameter2);
        g.setColor(Color.WHITE);
        int robotHeadSize = 5;
        fillOval(g, robotCenterX  + 10, robotCenterY, robotHeadSize, robotHeadSize);
        g.setColor(Color.BLACK);
        drawOval(g, robotCenterX  + 10, robotCenterY, robotHeadSize, robotHeadSize);
    }

    private void drawTarget(Graphics2D g, int x, int y)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(Color.GREEN);
        int targetSize = 5;
        fillOval(g, x, y, targetSize, targetSize);
        g.setColor(Color.BLACK);
        drawOval(g, x, y, targetSize, targetSize);
    }


    private void drawColorTarget(Graphics2D g, Target target)
    {
        AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
        g.setTransform(t);
        g.setColor(target.getColor());
        int targetSize = 8;
        fillOval(g, target.getTargetPositionX(), target.getTargetPositionY(), targetSize, targetSize);
        g.setColor(Color.BLACK);
        drawOval(g, target.getTargetPositionX(), target.getTargetPositionY(), targetSize, targetSize);
    }

    private void drawBullets(Graphics2D g, ArrayList<BulletCondition> bullets){
        synchronized (bullets){
            if (bullets.isEmpty()) {return;}
            for (BulletCondition bullet: bullets){
                AffineTransform t = AffineTransform.getRotateInstance(0, 0, 0);
                g.setTransform(t);
                g.setColor(Color.ORANGE);
                int bulletSize = 4;
                fillOval(g, round(bullet.getXPosition()), round(bullet.getYPosition()), bulletSize, bulletSize);
                g.setColor(Color.BLACK);
                drawOval(g, round(bullet.getXPosition()), round(bullet.getYPosition()), bulletSize, bulletSize);
            }
        }
    }

    private void drawEnemies(Graphics2D g,ArrayList<EnemyCondition> enemies){
        for (EnemyCondition enemy: enemies){
            int number = enemy.number;
            int centerX = round(enemy.getXPosition());
            int robotCenterY = round(enemy.getYPosition());
            AffineTransform t = AffineTransform.getRotateInstance(enemy.getDirection(), centerX, robotCenterY);
            g.setTransform(t);
            g.setColor(choseColor(enemy.getEnemyNumber()));
            int enemyDiameter = choseDiameter(enemy.getEnemyNumber());
            fillOval(g, centerX, robotCenterY, enemyDiameter, enemyDiameter);
            g.setColor(Color.BLACK);
            drawOval(g, centerX, robotCenterY, enemyDiameter, enemyDiameter);
            g.setColor(Color.WHITE);
            int robotHeadSize = 5;
            fillOval(g, centerX  + 10, robotCenterY, robotHeadSize, robotHeadSize);
            g.setColor(Color.BLACK);
            drawOval(g, centerX  + 10, robotCenterY, robotHeadSize, robotHeadSize);
        }
    }

    private Color choseColor(NumberOfEnemy number){
        if (number == NumberOfEnemy.FirstEnemy){
            return Color.BLACK;
        }
        if (number == NumberOfEnemy.SecondEnemy){
            return Color.PINK;
        }
        return Color.MAGENTA;
    }

    private int choseDiameter(NumberOfEnemy number){
        if (number == NumberOfEnemy.FirstEnemy){return 30;}
        if (number == NumberOfEnemy.SecondEnemy){return 20;}
        return 15;
    }

    public void gameOver(){
        robotCondition1st.reactOnGameOver();
        robotCondition2nd.reactOnGameOver();
    }

}
