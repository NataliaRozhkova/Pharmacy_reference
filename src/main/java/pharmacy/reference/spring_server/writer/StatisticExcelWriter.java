package pharmacy.reference.spring_server.writer;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import pharmacy.reference.spring_server.entitis.Pharmacy;
import pharmacy.reference.spring_server.entitis.Statistic;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class StatisticExcelWriter {
    private final String file;
    private final HashMap<Pharmacy, List<Statistic>> pharmacyPhoneCallCount;
    private final int month;
    private final HSSFWorkbook book;


    public StatisticExcelWriter(String fileName, HashMap<Pharmacy, List<Statistic>> pharmacyPhoneCallCount, int month, int year) {
        this.file = fileName + getMonth(month) + year+ ".xlsx";
        this.pharmacyPhoneCallCount = pharmacyPhoneCallCount;
        this.book = new HSSFWorkbook();
        this.month = month;
    }

    public void write() throws IOException {
        writeStatisticData();
        writeListCall();
        book.write(new FileOutputStream(file));
        book.close();

    }

    private void writeStatisticData() {
        Sheet sheet = book.createSheet("Данные");
        writeHeaderStatistic(sheet);
        int rowCount = 2;
        int callCount = 0;
        HSSFCellStyle style = setStyle();
        for (Pharmacy pharmacy : pharmacyPhoneCallCount.keySet()) {
            Row row = sheet.createRow(rowCount++);
            setCell(row, 0, pharmacy.getName() + " (" + pharmacy.getAddress() + ")", style);
            setCell(row, 1, Integer.toString(phoneCallCount(pharmacyPhoneCallCount.get(pharmacy))), style);
            callCount += phoneCallCount(pharmacyPhoneCallCount.get(pharmacy));
        }
        if (pharmacyPhoneCallCount.keySet().size() > 1) {
            Row row = sheet.createRow(rowCount);
            HSSFCellStyle styleBold = setBoldeStyle();
            setCell(row, 0, "Итого", styleBold);
            setCell(row, 1, Integer.toString(callCount), styleBold);

        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);

    }


    private void setCell(Row row, int column, String value, HSSFCellStyle style) {
        Cell newCell = row.createCell(column);
        newCell.setCellValue(value);
        newCell.setCellStyle(style);
    }

    private void setCellDate(Row row, int column, Date date) {
        Cell cell = row.createCell(column);
        cell.setCellValue(date);
        cell.setCellStyle(setDateStyle());
    }

    private void writeHeaderStatistic(Sheet sheet) {
        Row head = sheet.createRow(0);
        HSSFCellStyle style = setBoldeStyle();
        setCell(head, 0, "Статистика за " + getMonth(month), style);
        Row columnName = sheet.createRow(1);
        setCell(columnName, 0, "Аптека", style);
        setCell(columnName, 1, "Количество звонков", style);
        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row (0-based)
                0, //last row  (0-based)
                0, //first column (0-based)
                1  //last column  (0-based)
        ));
    }

    private void writeListCall() {
        Sheet sheet = book.createSheet("Звонки");
        writeHeaderCallList(sheet);
        int rowCount = 1;
        HSSFCellStyle style = setStyle();
        for (Pharmacy pharmacy : pharmacyPhoneCallCount.keySet()) {
            for (Statistic statistic : pharmacyPhoneCallCount.get(pharmacy)) {
                Row row = sheet.createRow(rowCount++);
                setCellDate(row, 0, statistic.getDate());
                setCell(row, 1, statistic.getMedicineName(), style);
                setCell(row, 2, statistic.getPharmacy().getName() + " (" + statistic.getPharmacy().getAddress() + ")", style);

            }

        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
    }

    private int phoneCallCount(List<Statistic> statistics) {
        int count = 0;
        if (statistics.size() > 0) {
            Statistic currentStat = statistics.get(0);
            count++;
            for (Statistic statistic : statistics) {
                if (statistic.getDate().getTime() - currentStat.getDate().getTime() > 1000) {
                    currentStat = statistic;
                    count++;
                }
            }
        }

        return count;
    }

    private void writeHeaderCallList(Sheet sheet) {
        Row head = sheet.createRow(0);
        HSSFCellStyle style = setBoldeStyle();
        setCell(head, 0, "Дата/Время", style);
        setCell(head, 1, "Название препарата", style);
        setCell(head, 2, "Аптека", style);

    }

    private HSSFCellStyle setStyle() {
        HSSFCellStyle cellStyle = book.createCellStyle();
        cellStyle = setBorder(cellStyle);
        return cellStyle;
    }

    private HSSFCellStyle setDateStyle() {
        HSSFCellStyle cellStyle = book.createCellStyle();
        cellStyle = setBorder(cellStyle);
        DataFormat format = book.createDataFormat();
        cellStyle.setDataFormat(format.getFormat("dd-MM-yyyy HH:mm:ss"));
        return cellStyle;
    }

    private HSSFCellStyle setBoldeStyle() {
        HSSFCellStyle cellStyle = book.createCellStyle();
        cellStyle = setBorder(cellStyle);
        HSSFFont font = book.createFont();
        font.setBold(true);
        cellStyle.setFont(font);
        return cellStyle;
    }

    private HSSFCellStyle setBorder(HSSFCellStyle cellStyle) {
        cellStyle.setBorderTop(HSSFColor.GREY_50_PERCENT.index);
        cellStyle.setBorderRight(HSSFColor.GREY_50_PERCENT.index);
        cellStyle.setBorderBottom(HSSFColor.GREY_50_PERCENT.index);
        cellStyle.setBorderLeft(HSSFColor.GREY_50_PERCENT.index);
        return cellStyle;
    }

    private String getMonth(int month) {
        String[] monthNames = {"Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
        return monthNames[month];
    }

}

