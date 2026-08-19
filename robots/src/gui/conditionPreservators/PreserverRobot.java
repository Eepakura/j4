package gui.conditionPreservators;

import logic.RobotCondition;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PreserverRobot {
    private static Boolean is1stRobotCreated = false;
    private final Serialization serializer = new Serialization();
    private final Deserialization deserializer = new Deserialization();
    private static final Pattern compile = Pattern.compile("RobotCondition\\{ x: (-?\\d+?\\.?\\d*?) y: (-?\\d+?\\.?\\d*?) direction: (-?\\d+?\\.?\\d*?) number: ([1-2]) life: ([0-5])}");
    private static final String stringRobotFormat = "RobotCondition{ x: %s y: %s direction: %s number: %s life: %s}";
    private static final String filePath = System.getProperty("user.home") + System.getProperty("file.separator") + "robot.txt";

    public void saveRobot(RobotCondition robot1st, RobotCondition robot2sd) {
        serializer.serializeObject(robotDataToString(robot1st) + "\n" + robotDataToString(robot2sd), filePath);
    }

    private String robotDataToString(RobotCondition robotD){
        return String.format(stringRobotFormat, robotD.getRobotX(), robotD.getRobotY(), robotD.getRobotDirection(), robotD.getRobotNumber(), robotD.getRobotLife());
    }

    public RobotCondition[] getSaveRobot(){
        var robotsStr = deserializer.deserializeObject(filePath);
        var robotsStrArr = robotsStr.split("\n");
        var robot1st = robotDataFromString(robotsStrArr[0]);
        var robot2nd = robotDataFromString(robotsStrArr[1]);
        return new RobotCondition[]{robot1st, robot2nd};
    }

    private int getRobotNumber(){
        if (is1stRobotCreated) {
            is1stRobotCreated = false;
            return 2;}
        else {
            is1stRobotCreated = true;
            return 1;}
    }

    private RobotCondition robotDataFromString(String robotData){
        Matcher matcher = compile.matcher(robotData);
        if (matcher.find()){
            double x = Double.parseDouble(matcher.group(1));
            double y = Double.parseDouble(matcher.group(2));
            double direction = Double.parseDouble(matcher.group(3));
            int number = Integer.parseInt(matcher.group(4));
            int life = Integer.parseInt(matcher.group(5));
            return new RobotCondition(x, y, direction, number, life);
        }
        else return new RobotCondition(getRobotNumber());
    }
}
