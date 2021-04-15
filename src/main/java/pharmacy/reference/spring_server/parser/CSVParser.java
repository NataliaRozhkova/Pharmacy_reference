package pharmacy.reference.spring_server.parser;

import au.com.bytecode.opencsv.CSVReader;
import org.apache.tika.parser.txt.CharsetDetector;
import org.apache.tika.parser.txt.CharsetMatch;
import pharmacy.reference.spring_server.entitis.Medicine;
import pharmacy.reference.spring_server.entitis.Pharmacy;
import pharmacy.reference.spring_server.util.PhFileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class CSVParser {

    private final HashMap<String, Integer> columnNumberNames = new HashMap<>();
    private final Pharmacy pharmacy;
    private final File file;

    public CSVParser(Pharmacy pharmacy, File file) {
        this.pharmacy = pharmacy;
        this.file = file;
    }

    public List<Medicine> parse() {
        InputStreamReader isr = null;
        List<Medicine> medicines = new ArrayList<>();
        try {
            InputStream stream = new FileInputStream(file);

            isr = new InputStreamReader(stream, getFileEncodingType(file));
            CSVReader reader = new CSVReader(isr, ';', '\n');
            List<String[]> allRows = reader.readAll();
            if (allRows.get(allRows.size() / 2).length == 1) {
                stream.close();
                stream = new FileInputStream(file);
                isr = new InputStreamReader(stream, getFileEncodingType(file));
                reader = new CSVReader(isr, '\t', '\n');
                allRows = reader.readAll();
                stream.close();
            }
//            if (allRows.size() < 5) {
//                stream.close();
//                String newTransformFile = transformFile(file);
//                stream = new FileInputStream(newTransformFile);
//                isr = new InputStreamReader(stream);
//                reader = new CSVReader(isr, ';', '\n');
//                allRows = reader.readAll();
//                stream.close();
//            }
            int table_start = setColumnTypeNumberCSV(allRows);
            medicines = parseAllMedicinesFromRows(table_start, allRows);

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

//    public String transformFile(File file) throws IOException {
//        InputStream stream = new FileInputStream(file);
//        String fileToString = IOUtils.toString(stream, getFileEncodingType(file));
//        fileToString = fileToString
//                .substring(fileToString.indexOf("А"))
//                .replaceAll("(?<=[A-Za-zа-яА-Я])(?=[0-9])|(?<=[0-9])(?=[A-Za-zа-яА-Я])", " ")
//        .replaceAll("(?<=(\\d[.]\\d\\d\\d\\d\\d))", "           ")
////                .replaceAll("000000", "0      0")
//                .replaceAll("\\d{8,17}", "")
//                .replaceAll("(\\s{5})+", "@")
////                .replaceAll("\u00AD", "!!!!")
//
//////                .replaceAll("\\d{9}", "@")
////                .replaceAll("@@", "@")
////                .replaceAll("@ @", "@")
////
////
//                .replaceAll("((.*?\\@){5})", "$1\n\n")
//
//
//        ;
//
//        String newFileName = file.getAbsolutePath().replace(".", "1.");
//        FileWriter writer = new FileWriter(new File(newFileName));
//        writer.write(fileToString);
//        writer.close();
//        stream.close();
//        return newFileName;
//    }

    private List<Medicine> parseAllMedicinesFromRows(int table_start, List<String[]> allRows) {
        List<Medicine> medicines = new ArrayList<>();
        for (int row = table_start; row < allRows.size(); row++) {
            Medicine medicine = parseMedicineFromRowCSV(allRows.get(row));
            if (medicine != null && !medicine.getName().equals("")) {
                medicine.setPharmacy(pharmacy);
                medicine.setDate(new Date(System.currentTimeMillis()));
                medicines.add(medicine);
            }
        }
        return medicines;
    }

    private int setColumnTypeNumberCSV(List<String[]> allRows) {
        int table_start = 0;
        do {
            setColumnTypeNumberCSVFromRow(allRows.get(table_start++));
        } while (columnNumberNames.size() == 0 && table_start != allRows.size() - 1);
        if (columnNumberNames.size() == 0) {
            columnNumberNames.put(TableHeadVariants.NAME, 0);
            columnNumberNames.put(TableHeadVariants.QUANTITY, 1);
            columnNumberNames.put(TableHeadVariants.PRICE, 2);
            columnNumberNames.put(TableHeadVariants.MANUFACTURE, 3);
            columnNumberNames.put(TableHeadVariants.COUNTRY, 4);
        } else return table_start;
        return 0;
    }

    private String getFileEncodingType(File file) throws IOException {
        CharsetDetector charsetDetector = new CharsetDetector();
        charsetDetector.setText(PhFileUtils.readAllBytes(file));
        CharsetMatch match = charsetDetector.detect();
        return match.getName();
    }


    private Medicine parseMedicineFromRowCSV(String[] row) {
        Medicine medicine = new Medicine();
        for (int column = 0; column < row.length; column++) {
            try {
                if (columnNumberNames.containsKey(TableHeadVariants.NAME) && column == columnNumberNames.get(TableHeadVariants.NAME)) {
                    medicine.setName(row[column]);
                }
                if (columnNumberNames.containsKey(TableHeadVariants.PRICE) && column == columnNumberNames.get(TableHeadVariants.PRICE)) {
                    try {
                        medicine.setPrice(Float.parseFloat(row[column]));
                    } catch (NumberFormatException e) {
                        medicine.setPrice(Float.parseFloat(row[column].replace(',', '.')));
                    }
                }
                if (columnNumberNames.containsKey(TableHeadVariants.QUANTITY) && column == columnNumberNames.get(TableHeadVariants.QUANTITY)) {
                    try {
                        medicine.setQuantity(Integer.parseInt(row[column]));
                    } catch (NumberFormatException e) {
                        medicine.setQuantity((int) Float.parseFloat(row[column].replace(',', '.')));
                    }
                }
                if (columnNumberNames.containsKey(TableHeadVariants.MANUFACTURE) && column == columnNumberNames.get(TableHeadVariants.MANUFACTURE)) {
                    medicine.setManufacturer(row[column]);
                }
                if (columnNumberNames.containsKey(TableHeadVariants.COUNTRY) && column == columnNumberNames.get(TableHeadVariants.COUNTRY)) {
                    medicine.setCountry(row[column]);
                }
            } catch (RuntimeException e) {
                medicine = null;
                break;
            }
        }
        return medicine;
    }

    private void setColumnTypeNumberCSVFromRow(String[] row) {
        int columnNumber = 0;
        for (String cell : row) {
            String value = TableHeadVariants.tableHeadParse(cell);
            if (value != null) {
                columnNumberNames.put(value, columnNumber);
            }
            columnNumber++;
        }
    }


}
