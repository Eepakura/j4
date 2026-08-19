package gui;

import gui.conditionPreservators.PreserverRobot;
import gui.conditionPreservators.PreserverWindow;
import gui.conditionPreservators.WindowData;
import gui.menu.MenuBar;

import java.awt.*;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;


import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import log.Logger;
import logic.BulletCondition;
import logic.EnemyCondition;
import logic.RobotCondition;

public class MainApplicationFrame extends JFrame implements LocaleChangeListener {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final RobotCondition robot1st;
    private final RobotCondition robot2nd;
    protected ArrayList<EnemyCondition> enemies = new ArrayList<>();
    protected ArrayList<BulletCondition> bullets = new ArrayList<>();
    private final GameWindow gameWindow;
    private final LogWindow logWindow;
    private final RobotPositionWindow positionWindow;
    private final MenuBar jMenuBar;
    private final PreserverRobot savingRobot = new PreserverRobot();
    private final PreserverWindow savingWindow = new PreserverWindow();
    private final int positionWindowWidth = 500;
    private final int positionWindowHeight = 100;



    public MainApplicationFrame(WindowData logInfo, WindowData gameInfo, WindowData mainInfo) {
        setContentPane(desktopPane);
        File fileRobot = new File((System.getProperty("user.home") + "\\robot.txt"));
        if (fileRobot.exists()) {
            var robots = savingRobot.getSaveRobot();
            robot1st = robots[0];
            robot2nd = robots[1];
        } else {
            robot1st = new RobotCondition(1);
            robot2nd = new RobotCondition(2);
        }

        for(int x = 0; x < 3; x++){
            EnemyCondition enemy = new EnemyCondition(robot1st, robot2nd, x);
            enemies.add(enemy);
        }

        gameWindow = new GameWindow(robot1st, robot2nd, bullets, enemies, gameInfo);
        logWindow = createLogWindow();
        positionWindow = new RobotPositionWindow(robot1st, robot2nd);

        addWindow(logWindow, logInfo);
        addWindow(gameWindow, gameInfo);
        addWindow(positionWindow);

        positionWindow.setBounds(0, mainInfo.getHeight() - positionWindowHeight * 2, positionWindowWidth, positionWindowHeight);
        jMenuBar = new MenuBar(this);
        setJMenuBar(jMenuBar.menuBar);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        closeProcessing(this);
        this.setVisible(true);
    }

    public static int getConfirmOfClosing() {
        return JOptionPane.showOptionDialog(
                null, Localization.getValue("question"),
                Localization.getValue("confirmation"), JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, null, null);
    }

    // обработка закрытия окна
    private void closeProcessing(Container frame) {
        UIManager.put("OptionPane.yesButtonText", Localization.getValue("yes"));
        UIManager.put("OptionPane.noButtonText", Localization.getValue("no"));
        if (frame instanceof MainApplicationFrame) {
            WindowListener exitListener = new WindowAdapter() {
                @Override
                public void windowClosing(WindowEvent e) {
                    if (getConfirmOfClosing() == 0) {
                        savingRobot.saveRobot(robot1st, robot2nd);
                        savingWindow.saveWindows(new WindowData(((MainApplicationFrame) frame)),
                                new WindowData(logWindow),
                                new WindowData(gameWindow));
                        frame.setVisible(false);
                        ((MainApplicationFrame) frame).dispose();
                        System.exit(0);
                    }
                }
            };
            this.addWindowListener(exitListener);
        } else if (frame instanceof JInternalFrame) {
            InternalFrameListener exitListener = new InternalFrameAdapter() {
                @Override
                public void internalFrameClosing(InternalFrameEvent e) {
                    if (getConfirmOfClosing() == 0) {
                        savingRobot.saveRobot(robot1st, robot2nd);
                        frame.setVisible(false);
                        ((JInternalFrame) frame).dispose();
                    }
                }
            };
            ((JInternalFrame) frame).addInternalFrameListener(exitListener);
        }
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setName("logWindow");
        Logger.debug("protocol");
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        return logWindow;
    }

    protected void addWindow(JInternalFrame frame, WindowData windowData) {
        desktopPane.add(frame);
        frame.pack();
        frame.setBounds(windowData.getPositionX(), windowData.getPositionY(), windowData.getWidth(), windowData.getHeight());
        try {
            frame.setIcon(windowData.getIsIconic());
        } catch (Exception e) {
            e.printStackTrace();
        }
        frame.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        frame.setVisible(true);
        this.closeProcessing(frame);
    }


    private void changeLanguage() {
        UIManager.put("OptionPane.yesButtonText", Localization.getValue("yes"));
        UIManager.put("OptionPane.noButtonText", Localization.getValue("no"));
        logWindow.onLocaleChange();
        gameWindow.onLocaleChange();
        jMenuBar.onLocaleChange();
        positionWindow.onLocaleChange();
    }

    private void addWindow(RobotPositionWindow positionWindow){
        desktopPane.add(positionWindow); // добавляется JInternalFrame а не JDialog
        positionWindow.pack();
        positionWindow.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        positionWindow.setVisible(true);
        this.closeProcessing(positionWindow);
        }

    @Override
    public void onLocaleChange() {
        EventQueue.invokeLater(this::changeLanguage);
    }
}
