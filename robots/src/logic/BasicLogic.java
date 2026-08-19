package logic;

import gui.GameVisualizer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


import static logic.MathematicalOperations.angleTo;
import static logic.MathematicalOperations.distance;
import static logic.MovementImplementation.moveRobot;
import static logic.MovementImplementation.moveBullets;
import static logic.MovementImplementation.moveEnemy;

public class BasicLogic {
    private final Object lock = new Object();

    private static int countOfCall = 0;

    public void onModelUpdateEvent(ArrayList<Target> targets, Point targetMouse, RobotCondition robot1st, RobotCondition robot2nd, ArrayList<BulletCondition> bullets, ArrayList<EnemyCondition> enemies, JPanel thisWindow, GameVisualizer gameViz) {
        if (!robot1st.isAlive & !robot2nd.isAlive) {
            gameViz.gameOver();
        }
        updateEnemies(enemies, thisWindow);
        if (robot1st.isAlive) {
            update(robot1st, targets, targetMouse, thisWindow, gameViz);
        }
        if (robot2nd.isAlive) {
            update(robot2nd, targets, targetMouse, thisWindow, gameViz);
        }
        updateBullets(bullets, robot2nd, thisWindow);
    }

    private void update(RobotCondition robot, ArrayList<Target> targets, Point targetMouse, JPanel thisWindow,GameVisualizer gameViz) {
        var target = robot.getNearestTarget(targets);
        var point = new Point(target.getTargetPositionX(), target.getTargetPositionY());
        var distance = distance(robot.getRobotX(), robot.getRobotY(), target.getTargetPositionX(), target.getTargetPositionY());
        var distanceToMousePoint = distance(targetMouse.x, targetMouse.y, robot.getRobotX(), robot.getRobotY());
        if (distance > distanceToMousePoint) {
            point = targetMouse;
            distance = distanceToMousePoint;
        }
        if (distance < 5) {
            if (point.equals(targetMouse)) {
                gameViz.setTargetPosition(new Point(-1990, -1990));
            } else {
                if (target.getIsBonusState()) {
                    robot.collectBonus(target.getTypeOfTarget());
                }
                target.changeTargetCondition();
            }
            robot.updateTargetsCount();
        } else updateRobot(robot, point, thisWindow);
    }

    private void updateEnemies(ArrayList<EnemyCondition> enemies, JPanel window) {
        for (EnemyCondition enemy : enemies) {
            if (enemy.stopIteration != 0) {
                enemy.stopIteration--;
                continue;
            }
            enemy.update();
            if (enemy.distanceToTarget >= 7) {
                moveEnemy(0.1, 10, enemy, window);
            }
        }
    }

    private void updateRobot(RobotCondition robot, Point target, JPanel thisWindow) {
        double angleToTarget1st = angleTo(robot.getRobotX(), robot.getRobotY(), target.x, target.y);
        robot.setRobotDirection(angleToTarget1st);
        if (robot.getRobotNumber() == 2) {
            moveRobot(angleToTarget1st, 10, robot, thisWindow);
            countOfCall++;
        } else {
            moveRobot(angleToTarget1st, 10, robot, thisWindow);
        }
    }

    private void updateBullets(ArrayList<BulletCondition> bullets, RobotCondition robot2nd, JPanel thisWindow) {
        if (countOfCall == 50) {
            countOfCall = 0;
            if (bullets.size() == 25) {
                return;
            }
            BulletCondition bullet = new BulletCondition(robot2nd.getRobotX(), robot2nd.getRobotY(), robot2nd.getRobotDirection());
            synchronized (bullets) {
                bullets.add(bullet);
            }
        }
        moveBullets(10, bullets, thisWindow);
    }
}