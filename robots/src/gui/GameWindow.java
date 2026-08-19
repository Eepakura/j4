package gui;

import gui.conditionPreservators.WindowData;
import logic.BulletCondition;
import logic.EnemyCondition;
import logic.RobotCondition;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.JPanel;

public class GameWindow extends JInternalFrame implements LocaleChangeListener
{
    protected final GameVisualizer mVisualizer;
    public GameWindow(RobotCondition robot1st, RobotCondition robot2sd, ArrayList<BulletCondition> bullets, ArrayList<EnemyCondition> enemies, WindowData gameWindow)
    {
        super(Localization.getValue("gameField"), true, true, true, true);
        mVisualizer = new GameVisualizer(robot1st, robot2sd, bullets, enemies, this);
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(mVisualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        setResizable(true);
        setName("gameWindow");
        setPreferredSize(new Dimension(300, 200));
    }

    @Override
    public void onLocaleChange() {
        this.setTitle(Localization.getValue("gameField"));
    }
}
