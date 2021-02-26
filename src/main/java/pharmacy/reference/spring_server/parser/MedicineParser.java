package pharmacy.reference.spring_server.parser;

import org.apache.commons.io.FilenameUtils;
import pharmacy.reference.spring_server.entitis.Medicine;
import pharmacy.reference.spring_server.entitis.Pharmacy;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MedicineParser {

    private final HashMap<String, Integer> columnNumberNames = new HashMap<>();

    private final Pharmacy pharmacy;

    public MedicineParser(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }


    public List<Medicine> parse(File file) {
        InputStream inputStream = null;
        List<Medicine> medicines = new ArrayList<>();
        try {
            inputStream = new FileInputStream(file);
            String fileType = FilenameUtils.getExtension(file.getAbsolutePath()).toLowerCase();
            if (fileType.equalsIgnoreCase("xls") ||
                    fileType.equalsIgnoreCase("xlsx")) {
                ExcelParser parser = new ExcelParser(pharmacy, file);
                medicines = parser.parse();
            } else if (FilenameUtils.getExtension(file.getAbsolutePath()).equalsIgnoreCase("csv")) {
                CSVParser parser = new CSVParser(pharmacy, file);
                medicines = parser.parse();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();

        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return medicines;
    }


}
