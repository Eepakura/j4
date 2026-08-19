package logic;

import java.util.ArrayList;
import java.util.Observable;
import java.util.concurrent.atomic.AtomicBoolean;

import static logic.MathematicalOperations.round;
import static logic.RobotConstants.startCountOfLive1;
import static logic.RobotConstants.startCountOfLive2;

public class RobotCondition extends Observable {
    public static String KEY_POSITION_CHANGED = "position changed";

    private final int robotNumber; // 1 is yellow, 2 is green
    public NumberOfRobot numberOfRobot;
    private volatile int life;
    private double robotPositionX;
    private double robotPositionY;
    private double robotDirection;
    private int countCollectedPoints = 0;
    public boolean isAlive;
    public boolean isVelocityChanged = false;
    private double velocity;
    private AtomicBoolean isLocked = new AtomicBoolean(false);

    public RobotCondition(int number){
        isAlive = true;
        robotNumber = number;
        robotPositionX = 100 * number;
        robotPositionY = 100 * number;
        robotDirection = 0;
        setNumberOfRobot(number);
        setLifeFromNumber(number);
        setStartVelocity();
    }

    public RobotCondition(double x, double y, double direction, int number, int life){
        robotPositionX = x;
        robotDirection = direction;
        robotPositionY = y;
        robotNumber = number;
        setLife(life);
        setStartVelocity();
    }

    public double getRobotX() {
        return robotPositionX;
    }

    public double getRobotY() {
        return robotPositionY;
    }

    public double getRobotDirection() {
        return robotDirection;
    }

    public int getRobotNumber() {return robotNumber;}

    public int getRobotLife(){return life;}

    protected void setRobotPositionX(double robotPositionX) {
        this.robotPositionX = robotPositionX;
        setChanged();
        notifyObservers(KEY_POSITION_CHANGED);
        clearChanged();
    }

    protected void setRobotPositionY(double robotPositionY){
        this.robotPositionY = robotPositionY;
        setChanged();
        notifyObservers(KEY_POSITION_CHANGED);
        clearChanged();
    }

    protected void setRobotDirection(double robotDirection){
        this.robotDirection = robotDirection;
    }

    protected void updateTargetsCount(){
        this.countCollectedPoints += 1;
        setChanged();
        notifyObservers(KEY_POSITION_CHANGED);
        clearChanged();
    }

    public int getCountCollectedPoints() {
        return countCollectedPoints;
    }

    public Target getNearestTarget(ArrayList<Target> targets) {
        var nearestTarget = targets.get(0);
        var minDistance = Integer.MAX_VALUE;
        for (Target target : targets) {
            var distance = MathematicalOperations.distance(target.getTargetPositionX(),
                    target.getTargetPositionY(), getRobotX(), getRobotY());
            if (distance < minDistance) {
                minDistance = round(distance);
                nearestTarget = target;
            }
        }
        return nearestTarget;
    }

    private void setLife(int countOfLife){
        life = countOfLife;
        isAlive = countOfLife != 0;
    }

    private void setLifeFromNumber(int number){
        if (number == 1){setLife(startCountOfLive1);}
        else {setLife(startCountOfLive2);}
        setChanged();
        notifyObservers(KEY_POSITION_CHANGED);
        clearChanged();
    }

    public void decreaseLife(){
        while (!isLocked.compareAndSet(false,true)){}
        int newLife = life - 1;
        if (newLife == 0){
            setRobotPositionX(-2000);
            setRobotPositionY(-2000);
        }
        setLife(newLife);
        setChanged();
        notifyObservers(KEY_POSITION_CHANGED);
        clearChanged();
        isLocked.set(false);
    }

    private void increaseLife(){
        while (!isLocked.compareAndSet(false,true)){}
        life++;
        isLocked.set(false);
    }

    public void reactOnGameOver(){
        setRobotPositionX(100 * getRobotNumber());
        setRobotPositionY(100 * getRobotNumber());
        setLifeFromNumber(getRobotNumber());
        setRobotDirection(0);
        setChanged();
        notifyObservers(KEY_POSITION_CHANGED);
        clearChanged();
    }

    public void slowDown() {
        if (isVelocityChanged){return;}
        velocity = velocity / 3;
        isVelocityChanged = true;
    }

    private void velocityBoost(){
        velocity = velocity + (velocity / 20);
    }

    private void increasePoints(){
        this.countCollectedPoints += 2;
        setChanged();
        notifyObservers(KEY_POSITION_CHANGED);
        clearChanged();
    }

    public void restoreVelocity() {
        if (isVelocityChanged){
            velocity = velocity * 3;
            isVelocityChanged = false;
        }
    }

    private void setStartVelocity(){
        if (robotNumber == 1){velocity = 0.3;}
        else {velocity = 0.1;}
    }

    private void setNumberOfRobot(int number) {
        if (number == 1){numberOfRobot = NumberOfRobot.FirstRobot;}
        else {numberOfRobot = NumberOfRobot.SecondRobot;}
    }

    public double getVelocity() {
        return velocity;
    }

    public void collectBonus(int typeOfTarget) {
        if (typeOfTarget == 1){increaseLife();}
        if (typeOfTarget == 2){velocityBoost();}
        if (typeOfTarget == 3){increasePoints();}
    }
}
