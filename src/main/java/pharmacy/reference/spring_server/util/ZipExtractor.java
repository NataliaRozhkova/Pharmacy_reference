package pharmacy.reference.spring_server.util;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;


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

    public static void writeToZipFile(List<File> srcFiles, String zipName) throws IOException {
        FileOutputStream fos = new FileOutputStream(zipName);
        ZipOutputStream zipOut = new ZipOutputStream(fos);

        srcFiles.stream()
                .forEach(file -> {
                    FileInputStream fis = null;
                    try {
                        fis = new FileInputStream(file);
                        ZipEntry zipEntry = new ZipEntry(file.getName());
                        zipOut.putNextEntry(zipEntry);

                        byte[] bytes = new byte[1024];
                        int length;
                        while ((length = fis.read(bytes)) >= 0) {
                            zipOut.write(bytes, 0, length);
                        }
                        fis.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

        zipOut.close();
        fos.close();
    }


}
