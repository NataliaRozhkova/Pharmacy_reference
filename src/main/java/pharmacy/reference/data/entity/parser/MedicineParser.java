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
        try {
            inputStream = new FileInputStream(file);
            if (FilenameUtils.getExtension(file.getAbsolutePath()).equalsIgnoreCase("xls")) {
                return parseExcel(new HSSFWorkbook(inputStream));
            } else if (FilenameUtils.getExtension(file.getAbsolutePath()).equalsIgnoreCase("xlsx")) {
                return parseExcel(new XSSFWorkbook(inputStream));
            } else if (FilenameUtils.getExtension(file.getAbsolutePath()).equalsIgnoreCase("csv")) {
                return parseCSV(file, inputStream);
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
        return null;
    }

    private List<Medicine> parseExcel(Workbook workBook) {
        List<Medicine> medicines = new ArrayList<>();
        Sheet sheet = workBook.getSheetAt(0);
        Iterator<Row> it = sheet.iterator();
        do {
            setColumnTypeNumberExcel(it.next());
        } while (columnNumberNames.get(TableHeadVariants.NAME) == -1 && columnNumberNames.get(TableHeadVariants.PRICE) == -1 && it.hasNext());

        while (it.hasNext()) {
            Medicine medicine = parseMedicineFromRowExel(it.next());
            if (medicine != null && !medicine.getName().equals("")) {
                medicine.setPharmacy(pharmacy);
                medicines.add(medicine);
            }
        }
        return medicines;
    }

    private List<Medicine> parseCSV(File file, InputStream stream) {
        InputStreamReader isr = null;
        List<Medicine> medicines = new ArrayList<>();
        try {
            isr = new InputStreamReader(stream, getFileEncodingType(file));
            CSVReader reader = new CSVReader(isr, ';', ':', '\n');
            List<String[]> allRows = reader.readAll();
            setColumnTypeNumberCSV(allRows.get(0));

            for (int row = 1; row < allRows.size(); row++) {
                Medicine medicine = parseMedicineFromRowCSV(allRows.get(row));
                if (!medicine.getName().equals("")) {
                    medicine.setPharmacy(pharmacy);
                    medicines.add(medicine);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (isr != null) {

                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return medicines;
    }

    private String getFileEncodingType(File file) throws IOException {
        CharsetDetector charsetDetector = new CharsetDetector();
        FileInputStream inputStream = new FileInputStream(file);
        charsetDetector.setText(inputStream.readAllBytes());
        CharsetMatch match = charsetDetector.detect();
        inputStream.close();
        return match.getName();
    }

    private Medicine parseMedicineFromRowExel(Row row) {
        Iterator<Cell> cells = row.iterator();
        int column = 0;
        Medicine medicine = new Medicine();
        while (cells.hasNext()) {
            Cell cell = cells.next();
            try {
                if (column == columnNumberNames.get(TableHeadVariants.NAME)) {
                    medicine.setName(cell.getStringCellValue());
                }
                if (column == columnNumberNames.get(TableHeadVariants.PRICE)) {
                    medicine.setPrice((float) cell.getNumericCellValue());
                }
                if (column == columnNumberNames.get(TableHeadVariants.QUANTITY)) {
                    medicine.setQuantity((int) cell.getNumericCellValue());
                }
                if (column == columnNumberNames.get(TableHeadVariants.MANUFACTURE)) {
                    medicine.setManufacturer(cell.getStringCellValue());
                }
                medicine.setPharmacy(pharmacy);
            } catch (RuntimeException e) {
                medicine = null;
                break;

            }
            column++;
        }
        return medicine;
    }

    private Medicine parseMedicineFromRowCSV(String[] row) {
        Medicine medicine = new Medicine();
        for (int column = 0; column < row.length; column++) {
            try {
                if (column == columnNumberNames.get(TableHeadVariants.NAME)) {
                    medicine.setName(row[column]);
                }
                if (column == columnNumberNames.get(TableHeadVariants.PRICE)) {
                    try {
                        medicine.setPrice(Float.parseFloat(row[column]));
                    } catch (NumberFormatException e) {
                        medicine.setPrice(Float.parseFloat(row[column].replace(',', '.')));
                    }
                }
                if (column == columnNumberNames.get(TableHeadVariants.QUANTITY)) {
                    medicine.setQuantity(Integer.parseInt(row[column]));
                }
                if (column == columnNumberNames.get(TableHeadVariants.MANUFACTURE)) {
                    medicine.setManufacturer(row[column]);
                }
            } catch (RuntimeException e) {
                medicine = null;
                break;
            }
        }
        return medicine;
    }


    private void setColumnTypeNumberExcel(Row row) {
        Iterator<Cell> cells = row.iterator();
        int columnNumber = 0;
        while (cells.hasNext()) {
            Cell cell = cells.next();
            columnNumberNames.put(TableHeadVariants.tableHeadParse(cell.getStringCellValue()), columnNumber++);
        }
    }

    private void setColumnTypeNumberCSV(String[] row) {
        int columnNumber = 0;
        for (String cell : row) {
            columnNumberNames.put(TableHeadVariants.tableHeadParse(cell), columnNumber++);

        }
    }


}
