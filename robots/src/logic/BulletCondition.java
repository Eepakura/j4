package logic;

import java.util.ArrayList;

public class BulletCondition extends ModelCondition{
    public static ArrayList<EnemyCondition> enemies = new ArrayList<>();
    public BulletCondition(double x, double y, double direction){
        super(x, y, direction);
    }

    public boolean isHit(double[] newXY){
        for (EnemyCondition enemy: enemies){
            if (Math.abs(newXY[0] - enemy.getXPosition()) < 3 & Math.abs(newXY[1] - enemy.getYPosition()) < 3){
                enemy.restart();
                return true;
            }
        }
        return false;
    }
}
