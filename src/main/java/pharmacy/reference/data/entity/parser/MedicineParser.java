package pharmacy.reference.data.entity.parser;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pharmacy.reference.data.entity.Medicine;
import pharmacy.reference.data.entity.Pharmacy;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MedicineParser {

    private int columnMedicineNameNumber = -1;
    private int columnMedicinePriceNumber = -1;
    private int columnMedicineQuantityNumber = -1;
    private int columnMedicineManufactureNumber = -1;

    private Pharmacy pharmacy;

    public MedicineParser(Pharmacy pharmacy) {
        this.pharmacy = pharmacy;
    }


    public List<Medicine> parse(File file) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            if (FilenameUtils.getExtension(file.getAbsolutePath()).equalsIgnoreCase("xls")) {
                return parseExcel(file, new HSSFWorkbook(inputStream));
            } else if (FilenameUtils.getExtension(file.getAbsolutePath()).equalsIgnoreCase("xlsx")) {
                return parseExcel(file, new XSSFWorkbook(inputStream));
            } else if (FilenameUtils.getExtension(file.getAbsolutePath()).equalsIgnoreCase("csv")) {
                return parseCSV(file);
            }
        } catch (FileNotFoundException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
        return null;
    }

    private List<Medicine> parseExcel(File file, Workbook workBook) {
        InputStream inputStream = null;
        List<Medicine> medicines = new ArrayList<>();

        Sheet sheet = workBook.getSheetAt(0);
        Iterator<Row> it = sheet.iterator();
        setColumnTypeNumberExcel(it.next());

        while (it.hasNext()) {
            Medicine medicine = parseMedicineFromRowExel(it.next());
            if (!medicine.getName().equals("")) {
                medicines.add(medicine);
            }
        }
        return medicines;
    }

    private List<Medicine> parseCSV(File file) {
        InputStreamReader isr = null;
        List<Medicine> medicines = new ArrayList<>();
        try {
            isr = new InputStreamReader(new FileInputStream(file), "windows-1251");
            CSVReader cvsr = new CSVReader(isr, ';');
            List<String[]> allRows = cvsr.readAll();
            setColumnTypeNumberCSV(allRows.get(0));

            for (int row = 1; row < allRows.size(); row++) {
                Medicine medicine = parseMedicineFromRowCSV(allRows.get(row));
                if (!medicine.getName().equals("")) {
                    medicines.add(medicine);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return medicines;
    }

    private Medicine parseMedicineFromRowExel(Row row) {
        Iterator<Cell> cells = row.iterator();
        int column = 0;
        Medicine medicine = new Medicine();
        while (cells.hasNext()) {
            Cell cell = cells.next();

            if (column == columnMedicineNameNumber) {
                medicine.setName(cell.getStringCellValue());
            }
            if (column == columnMedicinePriceNumber) {
                medicine.setPrice((float) cell.getNumericCellValue());
            }
            if (column == columnMedicineQuantityNumber) {
                medicine.setQuantity((int) cell.getNumericCellValue());

            }
            if (column == columnMedicineManufactureNumber) {
                medicine.setManufacturer(cell.getStringCellValue());
            }
            medicine.setPharmacy(pharmacy);
            column++;
        }
        return medicine;
    }

    private Medicine parseMedicineFromRowCSV(String[] row) {
        Medicine medicine = new Medicine();
        for (int column = 0; column < row.length; column++) {
            if (column == columnMedicineNameNumber) {
                medicine.setName(row[column]);
            }
            if (column == columnMedicinePriceNumber) {
                medicine.setPrice(Float.parseFloat(row[column]));
            }
            if (column == columnMedicineQuantityNumber) {
                medicine.setQuantity(Integer.parseInt(row[column]));
            }
            if (column == columnMedicineManufactureNumber) {
                medicine.setManufacturer(row[column]);
            }
            medicine.setPharmacy(pharmacy);
        }
        return medicine;
    }


    private void setColumnTypeNumberExcel(Row row) {
        Iterator<Cell> cells = row.iterator();
        int columnNumber = 0;
        while (cells.hasNext()) {
            Cell cell = cells.next();
            setTableHeadNumbers(cell.getStringCellValue(), columnNumber++);
        }
    }

    private void setColumnTypeNumberCSV(String[] row) {
        int columnNumber = 0;
        for (String cell : row) {
            setTableHeadNumbers(cell, columnNumber++);
        }
    }

    private void setTableHeadNumbers(String cellValue, int columnNumber) {
        cellValue = cellValue.trim().toLowerCase().replaceAll("\\s+", "");
        switch (cellValue) {
            case "наименование":
            case "товар":
            case "наименованиетовара":
            case "названиетовара":
            case "название":
            case "номенклатура":
                columnMedicineNameNumber = columnNumber;
                break;
            case "производитель":
            case "фирма-производитель":
                columnMedicineManufactureNumber = columnNumber;
                break;
            case "количество":
            case "кол-во":
            case "количество товара":
                columnMedicineQuantityNumber = columnNumber;
                break;
            case "цена":
            case "цена,руб.":
            case "средниеценыценарозн":
            case "средняяцена":
                columnMedicinePriceNumber = columnNumber;
                break;
        }
    }
}
