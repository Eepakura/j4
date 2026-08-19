package logic;

public class ModelCondition {
    protected double xPosition;
    protected double yPosition;
    protected double direction;

    public ModelCondition(double x, double y, double direction) {
        setXPosition(x);
        setYPosition(y);
        setDirection(direction);
    }

    public double getXPosition(){ return xPosition;}
    public double getYPosition(){ return yPosition;}
    public double getDirection(){ return direction;}

    protected void setXPosition(double x){ xPosition = x; }
    protected void setYPosition(double y){ yPosition = y; }
    protected void setDirection(double dir){ direction = dir; }
}
