package gui.conditionPreservators;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class Deserialization {
    protected String deserializeObject(String pathName) {
        try (FileInputStream fileInputStream = new FileInputStream(pathName);
             InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
             BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
            String string = bufferedReader.readLine();
            StringBuilder result = new StringBuilder();
            while (string != null) {
                result.append(string).append("\n");
                string = bufferedReader.readLine();
            }
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
