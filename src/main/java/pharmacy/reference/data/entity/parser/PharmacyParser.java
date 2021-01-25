package pharmacy.reference.data.entity.parser;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pharmacy.reference.data.entity.Pharmacy;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class PharmacyParser {


    public List<Pharmacy> parse(File file) throws IOException {
        InputStream stream = null;
        List<Pharmacy> pharmacies = new ArrayList<>();
        try {
            stream = new FileInputStream(file);
            Workbook workbook = setWorkBook(stream, file);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> it = sheet.iterator();
            pharmacies = parseAllPharmaciesFromRows(it);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
        return pharmacies;
    }

    private List<Pharmacy> parseAllPharmaciesFromRows(Iterator<Row> iterator) {
        List<Pharmacy> pharmacies = new ArrayList<>();
        iterator.next();
        while (iterator.hasNext()) {
            Pharmacy pharmacy = parsePharmacyFromRowExel(iterator.next());
            if (pharmacy != null && pharmacy.getName() != null && !pharmacy.getName().equals("")) {
                pharmacies.add(pharmacy);
            }
        }
        return pharmacies;
    }

    private Workbook setWorkBook(InputStream stream, File file) throws IOException {
        return FilenameUtils.getExtension(file.getAbsolutePath()).toLowerCase().equals("xls") ? new HSSFWorkbook(stream) : new XSSFWorkbook(stream);
    }


    private Pharmacy parsePharmacyFromRowExel(Row row) {
        Iterator<Cell> cells = row.iterator();
        Pharmacy pharmacy = new Pharmacy();
//        while (cells.hasNext()) {

        try {
            pharmacy.setName(cells.next().getStringCellValue());
        } catch (NoSuchElementException e) {
            System.out.println("Empty pharmacy name");
        }
        try {
            pharmacy.setAddress(cells.next().getStringCellValue());
        } catch (NoSuchElementException e) {
            System.out.println("Empty pharmacy address");

        }
        try {
            pharmacy.setTelephoneNumbers(String.valueOf((long) cells.next().getNumericCellValue()));
        } catch (NoSuchElementException e) {
            System.out.println("Empty pharmacy telephone number");

        }
        try {
            pharmacy.setDistrict(cells.next().getStringCellValue());
        } catch (NoSuchElementException e) {
            System.out.println("Empty pharmacy district");

        }
        try {
//            System.out.println(cells.next().getCellType());
            pharmacy.setEmail(cells.next().getStringCellValue());
        } catch (NoSuchElementException e) {
            System.out.println("Empty pharmacy email");

        }

//        }
        return pharmacy;
    }


}
