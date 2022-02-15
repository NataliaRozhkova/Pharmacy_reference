package pharmacy.reference.spring_server.util;

import com.sun.mail.pop3.POP3Folder;
import com.sun.mail.pop3.POP3SSLStore;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;
import org.slf4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeUtility;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Properties;

public class EmailDownloader {
    private final Logger logger;
    private Session session;
    private POP3SSLStore store;
    private String username;
    private String password;
    private POP3Folder folder;
    private URLName url;
    private String saveDirectory;

    public EmailDownloader(String username, String password, String saveDirectory, Logger logger) {
        this.username = username;
        this.password = password;
        this.saveDirectory = saveDirectory;
        this.logger = logger;
    }

    public void connect()
            throws Exception {
        String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        Properties pop3Props = new Properties();
        pop3Props.setProperty("mail.pop3.socketFactory.class", SSL_FACTORY);
        pop3Props.setProperty("mail.pop3.socketFactory.fallback", "false");
        pop3Props.setProperty("mail.pop3.port", "995");
        pop3Props.setProperty("mail.pop3.socketFactory.port", "995");
        url = new URLName("pop3", "pop.mail.ru", 995, "", username, password);
        session = Session.getInstance(pop3Props, null);
        store = new POP3SSLStore(session, url);
        store.connect();
    }

    public void openFolder(String folderName)
            throws Exception {
        folder = (POP3Folder) store.getFolder(folderName);
        if (folder == null)
            throw new Exception("Invalid folder");
        try {
            folder.open(Folder.READ_ONLY);
            logger.info((new StringBuilder("Folder name----")).append
                    (folder.getFullName()).toString());
        } catch (Exception ex) {
            logger.info((new StringBuilder("Folder Opening Exception..")).append(ex).toString());
        }
    }

    public void downloadFiles(LocalDate localDate)
            throws Exception {
        Message messages[] = folder.getMessages();
        for (int i = messages.length - 1; i >= 0; i--) {
            Message message = messages[i];
//            LocalDate messegeDate = message.getSentDate()
//                    .toInstant().atZone(ZoneId.systemDefault())
//                    .toLocalDate();
            Date dateStartLoad = new Date(System.currentTimeMillis() - 7500000);
            if (message.getSentDate().after(dateStartLoad) ) {

                dump(message);
            } else {
                break;
            }

        }

    }

    private void dump(Message message) throws Exception {
        String contentType = message.getContentType();
        InternetAddress address = (InternetAddress) message.getFrom()[0];
        Path filePath = Paths.get(saveDirectory + "/" + address.getAddress());
        if (!Files.exists(filePath)) {
            Files.createDirectory(filePath);
        }
        filePath = Paths.get(filePath.toString() + "/" + message.getSubject() + "/");
        if (!Files.exists(filePath)) {
            Files.createDirectory(filePath);
        }
        if (contentType.contains("multipart")) {
            readMessageMultipart(message, filePath);
        } else if (contentType.contains("text/plain") || contentType.contains("text/html")) {
            Object content = message.getContent();
            if (content != null) {
                String messageContent = content.toString();
            }
        } else if (contentType.contains("application/octet-stream")) {
            readMessageStream(message, filePath);
        }

    }

    private void readMessageMultipart(Message message, Path filePath) throws IOException, MessagingException {
        Multipart multiPart = (Multipart) message.getContent();
        int numberOfParts = multiPart.getCount();
        for (int partCount = 0; partCount < numberOfParts; partCount++) {
            MimeBodyPart part = (MimeBodyPart) multiPart.getBodyPart(partCount);
            try {
                String fileName = MimeUtility.decodeText(part.getFileName());
                part.saveFile(filePath + "/" + fileName);
            } catch (Exception exception){
                logger.error("Can't save file from email");

            }
        }
    }


    private void readMessageStream(Message message, Path filePath) throws IOException, MessagingException {
        FilterInputStream inputStream = (FilterInputStream) message.getContent();
        StringBuffer buffer = new StringBuffer();
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, getFileEncodingType((InputStream) message.getContent())));
        String inputLine = "";
        while ((inputLine = in.readLine()) != null) {
            buffer.append(inputLine).append("\n");
        }
        FileWriter writer = new FileWriter(filePath + "/" + MimeUtility.decodeText(message.getFileName()));
        writer.write(buffer.toString());
        writer.flush();
        writer.close();
        inputStream.close();
    }


    private String getFileEncodingType(InputStream inputStream) throws IOException, MessagingException {
        CharsetDetector charsetDetector = new CharsetDetector();
        charsetDetector.setText(PhFileUtils.readAllBytes(inputStream));
        CharsetMatch match = charsetDetector.detect();
        inputStream.close();
        return match.getName();
    }
}
