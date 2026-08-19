package logic;


import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import static logic.RobotConstants.bulletVelocity;
import static logic.RobotConstants.walls;
import static logic.MathematicalOperations.*;

public class MovementImplementation {
    protected static void moveRobot(double direction, double duration, RobotCondition robot, JPanel thisWindow) {
        robot.setRobotDirection(direction);
        double[] newXY = getNewLinearXY(robot.getRobotX(), robot.getRobotY(), robot.getVelocity(), duration, direction);
        if (newXY[0] > thisWindow.getWidth() || newXY[1] > thisWindow.getHeight()) {
              return;
        }
        if (isWallInFront(round(newXY[0]), round(newXY[1])) == 1){robot.slowDown();} // снижам скорость врага при прохождении через преграду
        else {robot.restoreVelocity();}
        if (isWallInFront(round(newXY[0]), round(newXY[1])) == 2){return;} // ждем, пока между роботом и врагом не будет преград
        robot.setRobotPositionX(newXY[0]);
        robot.setRobotPositionY(newXY[1]);
    }

    protected static void moveBullets(double duration, List<BulletCondition> bullets, JPanel thisWindow) {
        if (bullets == null) {return;}
        int index = 0;
        int countOfDel = 0;
        ArrayList<Integer> indexToDelete = new ArrayList<>();
        for (BulletCondition bullet : bullets) {
            double[] newXY = getNewLinearXY(bullet.getXPosition(), bullet.getYPosition(), bulletVelocity, duration, bullet.getDirection());
            if (bullet.isHit(newXY)){
                indexToDelete.add(index);
            }
            if (newXY[0] > thisWindow.getWidth() || newXY[1] > thisWindow.getHeight()) {
                indexToDelete.add(index);
            }
            if (isWallInFront(round(newXY[0]), round(newXY[1])) == 1){indexToDelete.add(index);} // удаляем пулю
            if (isWallInFront(round(newXY[0]), round(newXY[1])) == 2){indexToDelete.add(index);} // удаляем пулю
            synchronized (bullets){
                bullet.setXPosition(newXY[0]);
                bullet.setYPosition(newXY[1]);
            }
            index++;
        }
        synchronized (bullets){
            for (Integer i : indexToDelete) {
                bullets.remove(i - countOfDel);
                countOfDel++;
            }
        }
    }

    protected static void moveEnemy(double velocity, double duration, EnemyCondition enemy, JPanel thisWindow) {
        if (enemy.number == 0){
            move1stEnemy(velocity, duration, enemy, thisWindow);
        }
        if (enemy.number == 1){
            move2ndEnemy(10, enemy);
        }
        if (enemy.number == 2){
            move3rdEnemy(velocity, duration, enemy);
        }
    }

    private static int isWallInFront(int newX, int newY) {
        for (int[] params: walls){
            int x = params[0];
            int y = params[1];
            int width = params[2];
            int height = params[3];
            if ((newX > x) & (newX < (x + width)) & (newY > y) & (newY < (y + height))){
                if (x == 300 | x == 500) {return 1;} //спереди стена 1
                return 2;} // спереди стены 2 или 3
        }
        return 0; // спереди нет стен
    }

    private static void move1stEnemy(double velocity, double duration, EnemyCondition enemy, JPanel thisWindow) {
        double[] newXY = getNewLinearXY(enemy.getXPosition(), enemy.getYPosition(), velocity, duration, enemy.getDirection());
        if (newXY[0] > thisWindow.getWidth() || newXY[1] > thisWindow.getHeight()) {
            return;
        }
        if (isWallInFront(round(newXY[0]), round(newXY[1])) == 1){enemy.slowDown();} // снижам скорость врага при прохождении через преграду
        else {enemy.restoreVelocity();}
        if (isWallInFront(round(newXY[0]), round(newXY[1])) == 2){return;} // ждем, пока между роботом и врагом не будет преград
        enemy.setXPosition(newXY[0]);
        enemy.setYPosition(newXY[1]);
    }

    private static void move2ndEnemy(double duration, EnemyCondition enemy){
        double newX = enemy.getXPosition() + enemy.velocity / enemy.angularVelocity *
                (Math.sin(enemy.getDirection()  + enemy.angularVelocity * duration) -
                        Math.sin(enemy.getDirection()));
        if (!Double.isFinite(newX))
        {
            newX = enemy.getXPosition() + enemy.velocity * duration * Math.cos(enemy.getDirection());
        }
        double newY = enemy.getYPosition() - enemy.velocity / enemy.angularVelocity *
                (Math.cos(enemy.getDirection()  + enemy.angularVelocity * duration) -
                        Math.cos(enemy.getDirection()));
        if (!Double.isFinite(newY))
        {
            newY = enemy.getYPosition() + enemy.velocity * duration * Math.sin(enemy.getDirection());
        }
        if (isWallInFront(round(newX), round(newY)) == 1){enemy.slowDown();} // снижам скорость врага при прохождении через преграду
        else {enemy.restoreVelocity();}
        if (isWallInFront(round(newX), round(newY)) == 2){return;} // ждем, пока между роботом и врагом не будет преград
        enemy.setXPosition(newX);
        enemy.setYPosition(newY);
        double newDirection = asNormalizedRadians(enemy.getDirection() + enemy.angularVelocity * duration);
        enemy.setDirection(newDirection);
    }

    private static void move3rdEnemy(double velocity, double duration, EnemyCondition enemy) {
        if (round(enemy.getXPosition()) == enemy.targetOneX){
            enemy.setDirection(angleTo(0, 0, 1, 0));
            double[] newXY = getNewLinearXY(enemy.getXPosition(), enemy.getYPosition(), velocity, duration, enemy.getDirection());
            enemy.setXPosition(newXY[0]);
            enemy.setYPosition(newXY[1]);
            return;
        }
        if (round(enemy.getXPosition()) == enemy.targetTwoX){
            enemy.setDirection(angleTo(0, 0, -1, 0));
            double[] newXY = getNewLinearXY(enemy.getXPosition(), enemy.getYPosition(), velocity, duration, enemy.getDirection());
            enemy.setXPosition(newXY[0]);
            enemy.setYPosition(newXY[1]);
            return;
        }
        double[] newXY = getNewLinearXY(enemy.getXPosition(), enemy.getYPosition(), velocity, duration, enemy.getDirection());
        if (isWallInFront(round(newXY[0]), round(newXY[1])) == 1){enemy.slowDown();} // снижам скорость врага при прохождении через преграду
        else {enemy.restoreVelocity();}
        if (isWallInFront(round(newXY[0]), round(newXY[1])) == 2){return;} // ждем, пока между роботом и врагом не будет преград
        enemy.setXPosition(newXY[0]);
        enemy.setYPosition(newXY[1]);
    }

    private static double[] getNewLinearXY(double x, double y, double velocity, double duration, double direction){
        double newX = x + velocity * duration * Math.cos(direction);
        double newY = y + velocity * duration * Math.sin(direction);
        return new double[]{newX, newY};
    }
}

