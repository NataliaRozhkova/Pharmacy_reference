package pharmacy.reference.spring_server.parser;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pharmacy.reference.spring_server.entitis.Medicine;
import pharmacy.reference.spring_server.entitis.Pharmacy;

import java.io.*;
import java.util.*;

public class ExcelParser {
    private final HashMap<String, Integer> columnNumberNames = new HashMap<>();
    private final Pharmacy pharmacy;
    private final File file;

    public ExcelParser(Pharmacy pharmacy, File file) {
        this.pharmacy = pharmacy;
        this.file = file;

    }

    public List<Medicine> parse() throws IOException {
        InputStream stream = null;
        List<Medicine> medicines = new ArrayList<>();
        try {
            stream = new FileInputStream(file);
            Workbook workbook = setWorkBook(stream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();
            setColumnTypeNumberExcel(it);
            medicines = parseAllMedicinesFromRows(it);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return medicines;
    }

    private List<Medicine> parseAllMedicinesFromRows(Iterator<Row> iterator) {
        List<Medicine> medicines = new ArrayList<>();
        while (iterator.hasNext()) {
            Medicine medicine = parseMedicineFromRowExel(iterator.next());
            if (medicine != null && medicine.getName() != null && !medicine.getName().equals("")) {
                medicine.setPharmacy(pharmacy);
                medicine.setDate(new Date(System.currentTimeMillis()));
                medicines.add(medicine);
            }
        }
        return medicines;
    }

    private Workbook setWorkBook(InputStream stream) throws IOException {
        return FilenameUtils.getExtension(file.getAbsolutePath()).toLowerCase().equals("xls") ? new HSSFWorkbook(stream) : new XSSFWorkbook(stream);
    }


    private Medicine parseMedicineFromRowExel(Row row) {
        if (row.getLastCellNum() > 1) {
            Iterator<Cell> cells = row.iterator();
            int column = 0;
            Medicine medicine = new Medicine();
            while (cells.hasNext()) {
                Cell cell = cells.next();

                try {
                    if (columnNumberNames.containsKey(TableHeadVariants.NAME) && column == columnNumberNames.get(TableHeadVariants.NAME)) {
                        medicine.setName(cell.getStringCellValue());
                    }
                    if (columnNumberNames.containsKey(TableHeadVariants.PRICE) && column == columnNumberNames.get(TableHeadVariants.PRICE)) {
                        medicine.setPrice((float) cell.getNumericCellValue());
                    }
                    if (columnNumberNames.containsKey(TableHeadVariants.QUANTITY) && column == columnNumberNames.get(TableHeadVariants.QUANTITY)) {
                        medicine.setQuantity((int) cell.getNumericCellValue());
                    }
                    if (columnNumberNames.containsKey(TableHeadVariants.MANUFACTURE) && column == columnNumberNames.get(TableHeadVariants.MANUFACTURE)) {
                        medicine.setManufacturer(cell.getStringCellValue());
                    }
                    if (columnNumberNames.containsKey(TableHeadVariants.COUNTRY) && column == columnNumberNames.get(TableHeadVariants.COUNTRY)) {
                        medicine.setCountry(cell.getStringCellValue());
                    }
                } catch (RuntimeException e) {
                    medicine = null;
                    break;

                }
                column++;
            }

            return medicine;
        } else return null;
    }

    private void setColumnTypeNumberExcel(Iterator<Row> iterator) {
        do {
            setColumnTypeNumberExcelFromRow(iterator.next());
        } while (columnNumberNames.size() == 0 && iterator.hasNext());
        if (columnNumberNames.size() == 0) {
            columnNumberNames.put(TableHeadVariants.NAME, 0);
            columnNumberNames.put(TableHeadVariants.QUANTITY, 1);
            columnNumberNames.put(TableHeadVariants.PRICE, 2);
            columnNumberNames.put(TableHeadVariants.MANUFACTURE, 3);
            columnNumberNames.put(TableHeadVariants.COUNTRY, 4);
        }
    }


    private void setColumnTypeNumberExcelFromRow(Row row) {
        Iterator<Cell> cells = row.iterator();
        int columnNumber = 0;
        while (cells.hasNext()) {
            Cell cell = cells.next();
            try {
                String value = TableHeadVariants.tableHeadParse(cell.getStringCellValue());
                if (value != null) {
                    columnNumberNames.put(value, columnNumber);
                }
            } catch (IllegalStateException e) {
            }
            columnNumber++;

        }
    }
}
