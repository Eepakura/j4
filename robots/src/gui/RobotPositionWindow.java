package gui;

import logic.RobotCondition;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import static logic.MathematicalOperations.round;

public class RobotPositionWindow extends JInternalFrame implements Observer, LocaleChangeListener {
    public static String KEY_POSITION_CHANGED = "position changed";
    private RobotCondition robotModel1st;
    private RobotCondition robotModel2nd;
    private final String stringMessageFormat = "%s%s, %s%s, %s%s";
    JLabel coordinateInfo1st;
    JLabel coordinateInfo2nd;
    JLabel lives1st;
    JLabel lives2nd;

    private String xPosition = Localization.getValue("x info");
    private String yPosition = Localization.getValue("y info");
    private String targetsCountText = Localization.getValue("points count");
    private String lives1stText = Localization.getValue("lives1st");
    private String lives2ndText = Localization.getValue("lives2nd");
    private final String heart = "❤";


    public RobotPositionWindow(RobotCondition model1st, RobotCondition model2nd){
        super(Localization.getValue("robot position"), true, true, true, true);
        robotModel1st = model1st;
        robotModel2nd = model2nd;

        JPanel panel = new JPanel(new BorderLayout());


        coordinateInfo1st = new JLabel(String.format(stringMessageFormat, xPosition, 100, yPosition, 100, targetsCountText, 0));
        panel.add(coordinateInfo1st, BorderLayout.NORTH);

        coordinateInfo2nd = new JLabel(String.format(stringMessageFormat, xPosition, 200, yPosition, 200, targetsCountText, 0));
        panel.add(coordinateInfo2nd, BorderLayout.SOUTH);

        lives1st = new JLabel(lives1stText + heart.repeat(robotModel1st.getRobotLife()));
        panel.add(lives1st, BorderLayout.WEST);

        lives2nd = new JLabel(lives2ndText + heart.repeat(robotModel2nd.getRobotLife()));
        panel.add(lives2nd, BorderLayout.EAST);
        robotModel1st.addObserver(this);
        robotModel2nd.addObserver(this);

        getContentPane().add(panel);
        pack();
    }


    @Override
    public void update(Observable o, Object arg) {
        if (o.equals(robotModel1st) | o.equals(robotModel2nd)){
            if (arg.equals(KEY_POSITION_CHANGED)){
                onModelChanged();
            }
        }
    }

    private void onModelChanged() {
        coordinateInfo1st.setText(String.format(stringMessageFormat, xPosition, round(robotModel1st.getRobotX()), yPosition, round(robotModel1st.getRobotY()), targetsCountText, robotModel1st.getCountCollectedPoints()));
        coordinateInfo2nd.setText(String.format(stringMessageFormat, xPosition, round(robotModel2nd.getRobotX()), yPosition, round(robotModel2nd.getRobotY()), targetsCountText, robotModel2nd.getCountCollectedPoints()));
        lives1st.setText(lives1stText + heart.repeat(robotModel1st.getRobotLife()));
        lives2nd.setText(lives2ndText + heart.repeat(robotModel2nd.getRobotLife()));
    }

    @Override
    public void onLocaleChange() {
        this.setTitle(Localization.getValue("robot position"));
        xPosition = Localization.getValue("x info");
        yPosition = Localization.getValue("y info");
        targetsCountText = Localization.getValue("points count");
        lives1stText = Localization.getValue("lives1st");
        lives2ndText = Localization.getValue("lives2nd");
        onModelChanged();
    }
}
