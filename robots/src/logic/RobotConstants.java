package logic;

public class RobotConstants {
    public static final double maxVelocity1st = 0.3;
    public static final double maxVelocity2nd = 0.1;
    public static final double bulletVelocity = 0.3;
    public static final int startCountOfLive1 = 5;
    public static final int startCountOfLive2 = 3;
    public static final double maxAngularVelocity = 0.002; //field for 2nd enemy

    public static final int [][] walls = new int[][]{
            new int[]{300, 400, 250, 50}, // (x1, x2) - координаты точки начала фигурыб, x3 - ширина, х4 - высота фигуры
            new int[]{500, 450, 50, 0},
            new int[]{600, 600, 200, 50},
            new int[]{900, 300, 150, 150}
    };
}
