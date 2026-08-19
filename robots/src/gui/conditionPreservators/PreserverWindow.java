package gui.conditionPreservators;

import java.util.HashMap;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PreserverWindow {
    private final Serialization serializer = new Serialization();
    private final Deserialization deserializer = new Deserialization();
    private static final Pattern compile = Pattern.compile("WindowCondition\\{ name: ([a-zA-Z]+?) x: -?(\\d+?) y: -?(\\d+?) width: (\\d+?) height: (\\d+?) isIconic: (true|false) }");
    private static final String stringWindowFormat = "WindowCondition{ name: %s x: %d y: %d width: %d height: %d isIconic: %s }";
    private static final String filePath = System.getProperty("user.home") + System.getProperty("file.separator") + "windows.txt";

    public void saveWindows(WindowData main, WindowData log, WindowData game) {
        serializer.serializeObject(windowDataToString(main) + "\n" + windowDataToString(log)
                + "\n" + windowDataToString(game), filePath);
    }

    private String windowDataToString(WindowData windowData){
        return String.format(stringWindowFormat, windowData.getName(), windowData.getPositionX(),
                windowData.getPositionY(), windowData.getWidth(), windowData.getHeight(), windowData.getIsIconic().toString());
    }

    public HashMap<String, WindowData> getSaveWindows(){
        var windowsStr = deserializer.deserializeObject(filePath);
        HashMap<String, WindowData> windows = new HashMap<>();
        for (String str: windowsStr.split("\n")){
            System.out.println(str);
            var wind = windowDataFromString(str);
            windows.put(wind.getName(), wind);
        }
        return windows;
    }

    private WindowData windowDataFromString(String windowData){

        Matcher matcher = compile.matcher(windowData);
        if (matcher.find()){
            String name = matcher.group(1);
            int positionX = Integer.parseInt(matcher.group(2));
            int positionY = Integer.parseInt(matcher.group(3));
            int width = Integer.parseInt(matcher.group(4));
            int height = Integer.parseInt(matcher.group(5));
            boolean isIconic = Objects.equals(matcher.group(6), "true");
            return new WindowData(name, positionX, positionY, width,
                    height, isIconic);
        }
        else return new WindowData("default", 0, 300, 300, 300, false);

    }
}

