package pharmacy.reference;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class PharmacyFileReader {

    public static String getFile(String path) {
        BufferedReader reader;
        StringBuilder file = new StringBuilder();
        try {
            reader = new BufferedReader(new java.io.FileReader(new File(path)));
            String line = reader.readLine();
            while (line != null) {
                file.append(line).append("\n");
                line = reader.readLine();
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.toString();
    }


}
