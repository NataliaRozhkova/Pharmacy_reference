package pharmacy.reference.spring_server.util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipExtractor {



    public static void unzip(String file) {

        try {
            File fSourceZip = new File(file);
            String zipPath = file.substring(0, file.length() - 4);
            File temp = new File(zipPath);
            temp.mkdir();
            System.out.println(zipPath + " created");


            Charset CP866 = Charset.forName("CP866");
            ZipFile zipFile = new ZipFile(fSourceZip, CP866);
            Enumeration e = zipFile.entries();

            while (e.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) e.nextElement();
                File destinationFilePath = new File(zipPath, entry.getName());

                destinationFilePath.getParentFile().mkdirs();

                if (entry.isDirectory()) {
                    continue;
                } else {
                    System.out.println("Extracting " + destinationFilePath);

                    BufferedInputStream bis = new BufferedInputStream(
                            zipFile.getInputStream(entry));

                    int b;
                    byte buffer[] = new byte[1024];


                    FileOutputStream fos = new FileOutputStream(
                            destinationFilePath);
                    BufferedOutputStream bos = new BufferedOutputStream(fos,
                            1024);

                    while ((b = bis.read(buffer, 0, 1024)) != -1) {
                        bos.write(buffer, 0, b);
                    }

                    bos.flush();
                    bos.close();
                    bis.close();
                }
                if (entry.getName().endsWith(".zip")) {
                    // found a zip file, try to open
                    unzip(destinationFilePath.getAbsolutePath());
                }
            }

        } catch (IOException ioe) {
            System.out.println("IOError :" + ioe);
        }
    }
}
