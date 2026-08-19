package gui.conditionPreservators;

import gui.MainApplicationFrame;

import javax.swing.*;
import java.awt.*;

public class WindowData {
    private final String name;
    private final int positionX;
    private final int positionY;
    private final int height;
    private final int width;
    private final Boolean isIconic;
    public static final String gameWindowName = "gameWindow";
    public static final String logWindowName = "logWindow";
    public static final String mainWindowName = "mainWindow";


    public WindowData(String name, int positionX, int positionY, int width, int height, boolean isIconic) {
        this.name = name;
        this.positionX = positionX;
        this.positionY = positionY;
        this.height = height;
        this.width = width;
        this.isIconic = isIconic;
    }

    public WindowData(JInternalFrame frame){
        this.name = frame.getName();
        this.positionX = frame.getX();
        this.positionY = frame.getY();
        this.height = frame.getHeight();
        this.width = frame.getWidth();
        this.isIconic = frame.isIcon();
    }

    public WindowData(MainApplicationFrame frame) {
        this.name = mainWindowName;
        this.positionX = frame.getX();
        this.positionY = frame.getY();
        this.height = frame.getHeight();
        this.width = frame.getWidth();
        this.isIconic = frame.getState() == 1;
    }

    public static WindowData logWindowDefaultData(){
        return new WindowData(logWindowName, 10, 10, 300, 800, false);
    }

    public static WindowData gameWindowDefaultData(){
        return new WindowData(gameWindowName, 400, 20, 900, 600, false);

    }

    public static WindowData mainWindowDefaultData(){
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        return new WindowData(mainWindowName, 50, 50, screenSize.width - 100, screenSize.height - 100, false);

    }

    public int getPositionX() {
        return positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public String getName() {
        return name;
    }

    public int getWidth() {
        return width;
    }

    public Boolean getIsIconic() {
        return isIconic;
    }

    public int getHeight() {
        return height;
    }
}
