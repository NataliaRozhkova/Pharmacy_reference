package pharmacy.reference.spring_server.util;

import org.apache.tika.io.IOUtils;

import java.io.*;

public class PhFileUtils {

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

    public static byte[] readAllBytes(File file) {
        FileInputStream inputStream = null;
        byte[] bytes = new byte[(int) file.length()];
        DataInputStream dis = null;
        try {
            inputStream = new FileInputStream(file);
            dis = new DataInputStream(inputStream);
            dis.readFully(bytes);
            return bytes;
        } catch (IOException e) {
            return new byte[0];
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (dis != null) {
                try {
                    dis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static byte[] readAllBytes(InputStream is) throws IOException {
        return IOUtils.toByteArray(is);
    }

}
