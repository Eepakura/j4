import java.util.ListResourceBundle;

public class StringsResource_en_US extends ListResourceBundle {
    @Override
    protected Object[][] getContents() {
        return new Object[][]{
                {"question", "Are you sure you want to close the window?"},
                {"yes", "Yes"},
                {"no", "No"},
                {"confirmation", "Confirmation"},
                {"protocol", "Protocol is working"},
                {"mode", "Display mode"},
                {"modeDescription", "Application display control"},
                {"system", "System layout"},
                {"universal", "Universal layout"},
                {"operations", "Operations"},
                {"exit", "Exit"},
                {"tests", "Tests"},
                {"testsDescription", "Test commands"},
                {"message", "Log message"},
                {"newString", "New String"},
                {"gameField", "Playing field"},
                {"protocolWork", "Protocol of working"},
                {"language", "Language"},
                {"x info", "x position is "},
                {"y info", "y position is "},
                {"robot position", "Robot position"},
                {"points count", "count of collected points: "},
                {"lives1st", "Lives of 1st robot: "},
                {"lives2nd", "Lives of 2nd robot: "}
        };
    }
}
