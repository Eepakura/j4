package gui;

import gui.conditionPreservators.PreserverWindow;
import gui.conditionPreservators.WindowData;

import java.awt.Frame;
import java.io.File;
import java.util.Locale;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RobotsProgram
{
  private static final PreserverWindow savingWindow = new PreserverWindow();

  public static void main(String[] args) {
      try {
        UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
      } catch (Exception e) {
        e.printStackTrace();
      }
      SwingUtilities.invokeLater(() -> {
        Locale currentLocale = Localization.getLocale();
        WindowData logInfo = WindowData.logWindowDefaultData();
        WindowData gameInfo = WindowData.gameWindowDefaultData();
        WindowData mainInfo = WindowData.mainWindowDefaultData();
        File fileWindows = new File((System.getProperty("user.home") + "\\windows.txt"));
        if (fileWindows.exists()) {
          var windows = savingWindow.getSaveWindows();
          if (windows.containsKey(WindowData.mainWindowName))
            mainInfo = windows.get(WindowData.mainWindowName);
          if (windows.containsKey(WindowData.logWindowName))
            logInfo = windows.get(WindowData.logWindowName);
          if (windows.containsKey(WindowData.gameWindowName))
            gameInfo = windows.get(WindowData.gameWindowName);
        }
        MainApplicationFrame frame = new MainApplicationFrame(logInfo, gameInfo, mainInfo);
        frame.setState(mainInfo.getIsIconic() ? Frame.ICONIFIED : Frame.NORMAL);
        frame.setBounds(mainInfo.getPositionX(), mainInfo.getPositionY(), mainInfo.getWidth(), mainInfo.getHeight());
      });
    }}
