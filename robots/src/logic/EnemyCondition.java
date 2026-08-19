package logic;

import java.util.Observable;
import java.util.Observer;
import static logic.MathematicalOperations.angleTo;
import static logic.MathematicalOperations.round;
import static logic.MathematicalOperations.distance;
import static logic.RobotConstants.maxAngularVelocity;

public class EnemyCondition extends ModelCondition {
    private RobotCondition robotModel1st;
    private RobotCondition robotModel2nd;
    public double distanceToTarget;
    public int targetNumber;
    public int number;
    private NumberOfEnemy enemyNumber;
    public int stopIteration;
    public final int targetOneX = 400; //field for 3rd enemy
    public final int targetTwoX = 1000; //field for 3rd enemy
    public double velocity;
    public double angularVelocity;
    public boolean isVelocityChanged = false;


    public EnemyCondition(RobotCondition robotOne, RobotCondition robotTwo, int number) {
        super(1000,100 * number, 0);
        robotModel1st = robotOne;
        robotModel2nd = robotTwo;
        this.number = number;
        setEnemyNumber(number);
        stopIteration = 0;
        this.velocity = 0.1 * (number + 1);
    }

    private void setEnemyNumber(int number) {
        if (number == 0){enemyNumber = NumberOfEnemy.FirstEnemy;}
        if (number == 1){enemyNumber = NumberOfEnemy.SecondEnemy;}
        if (number == 2){enemyNumber = NumberOfEnemy.ThirdEnemy;}
    }

    public void update() {
        int targetX1 = round(robotModel1st.getRobotX()); // for n robot
        int targetY1 = round(robotModel1st.getRobotY());
        int targetX2 = round(robotModel2nd.getRobotX());
        int targetY2 = round(robotModel2nd.getRobotY());
        double distance1 = distance(xPosition, yPosition, targetX1, targetY1);
        double distance2 = distance(xPosition, yPosition, targetX2, targetY2);
        if (distance1 <= distance2){
            targetNumber = 1;
            if (distance1 < 7){catchRobot();}
            distanceToTarget = distance1;
            if (number == 2){return;} // так как третий враг ходит туда-обоатно, направление ему менять не нужно
            double newDirection = angleTo(getXPosition(), getYPosition(), targetX1, targetY1);
            setAngularVelocity(number, newDirection); // для второго врага нужно установить угловую скорость
            setDirection(newDirection);
        }
        else {
            targetNumber = 2;
            if (distance2 < 7){catchRobot();}
            distanceToTarget = distance2;
            if (number == 2){return;} // так как третий враг ходит туда-обоатно, направление ему менять не нужно
            double newDirection = angleTo(getXPosition(), getYPosition(), targetX2, targetY2);
            setAngularVelocity(number, newDirection); // для второго врага нужно установить угловую скорость
            setDirection(newDirection);        }
    }

    public void setAngularVelocity(int number, double newDirection){
        if (number == 1){
            angularVelocity = 0;
            if (newDirection > direction){
                angularVelocity = maxAngularVelocity;
            }
            if (newDirection < direction){
                angularVelocity = -maxAngularVelocity;
            }
        }
    }

    private void catchRobot(){
        stopIteration = 300;
        if (targetNumber == 0) {return;}
        if (targetNumber == 1){decreaseLife(1);}
        else {decreaseLife(2);}
    }

    public void decreaseLife(int number){
        if (number == 1){robotModel1st.decreaseLife();}
        else {robotModel2nd.decreaseLife();}
    }

    public void restart() {
        setXPosition(1000);
        setYPosition(number * 100);
        setDirection(0);
    }

    public void slowDown() {
        if (isVelocityChanged){return;}
        velocity = velocity / 3;
        isVelocityChanged = true;
    }

    public void restoreVelocity() {
        if (isVelocityChanged){
            velocity = velocity * 3;
            isVelocityChanged = false;
        }
    }

    public NumberOfEnemy getEnemyNumber() {
        return enemyNumber;
    }
}
