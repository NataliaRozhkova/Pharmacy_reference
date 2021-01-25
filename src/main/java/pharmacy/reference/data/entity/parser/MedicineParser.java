package pharmacy.reference.data.entity.parser;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;
import pharmacy.reference.data.entity.Medicine;
import pharmacy.reference.data.entity.Pharmacy;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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
