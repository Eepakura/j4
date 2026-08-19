package gui.conditionPreservators;

import java.io.*;

public class Serialization {
    protected void serializeObject(String serializableObject, String pathName){

        try (FileOutputStream fileOutputStream = new FileOutputStream(pathName);
             OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream);
             PrintWriter printWriter = new PrintWriter(outputStreamWriter)) {

            printWriter.println(serializableObject);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
