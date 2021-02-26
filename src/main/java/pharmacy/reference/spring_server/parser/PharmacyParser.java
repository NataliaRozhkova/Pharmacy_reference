package pharmacy.reference.spring_server.parser;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import pharmacy.reference.spring_server.entitis.Pharmacy;
import pharmacy.reference.spring_server.entitis.PharmacyChain;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class PharmacyParser {

    private List<PharmacyChain> chains;

    public PharmacyParser(List<PharmacyChain> chains) {
        this.chains = chains;
    }

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

        try {
            pharmacy.setName(cells.next().getStringCellValue());
        } catch (NoSuchElementException e) {
            System.out.println("Empty pharmacy name");
        }
        try {
//            pharmacy.setPharmacyChain(cells.next().getStringCellValue());
//            Long num = Long.parseLong(cells.next().getStringCellValue());
            Long num = (long) cells.next().getNumericCellValue();
            pharmacy.setPharmacyChain(findChain(num));
        } catch (NoSuchElementException e) {
            System.out.println("Empty pharmacy chain");
        }
        try {
            pharmacy.setAddress(cells.next().getStringCellValue());
        } catch (NoSuchElementException e) {
            System.out.println("Empty pharmacy address");

        }
        try {
//            pharmacy.setTelephoneNumbers(String.valueOf((long) cells.next().getNumericCellValue()));
            pharmacy.setTelephoneNumbers(cells.next().getStringCellValue());
        } catch (NoSuchElementException e) {
            System.out.println("Empty pharmacy telephone number");

        }
        try {
            pharmacy.setTown(cells.next().getStringCellValue());
        } catch (NoSuchElementException e) {
            System.out.println("Empty pharmacy town");
        }
        try {
            pharmacy.setDistrict(cells.next().getStringCellValue());
        } catch (NoSuchElementException e) {
            System.out.println("Empty pharmacy district");

        }
        try {
            pharmacy.setEmail(cells.next().getStringCellValue());
        } catch (NoSuchElementException e) {
            System.out.println("Empty pharmacy email");

        }
        return pharmacy;
    }

    private PharmacyChain findChain(long id) {
        for (PharmacyChain chain : chains) {
            if (chain.getId() == id) {
                return chain;
            }
        }
        return null;
    }


}
