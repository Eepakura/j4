package gui;

import java.util.Arrays;
import java.util.Locale;
import java.util.ResourceBundle;

public final class Localization {
    private final static String resource = "StringsResource";
    private Localization(){}
    private static ResourceBundle bundle;

    public static Locale getLocale(){
        return Locale.getDefault();
    }
    public static boolean isSupported(Locale locale){
        Locale[] availableLocales = Locale.getAvailableLocales();
        return Arrays.asList(availableLocales).contains(locale);
    }
    public static void setLocale(Locale locale){
        Locale.setDefault(locale);
    }
    public static String getValue(String key){
        bundle = ResourceBundle.getBundle(resource, getLocale());
        var string = bundle.getString(key);
        if (string.isEmpty())
            return "Default";
        return string;
    }
}
