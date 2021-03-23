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
import pharmacy.reference.spring_server.entitis.reports.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;

public class ExcelWriter {
    private final HSSFWorkbook book;
    private final String file;
    private final int month;

    public ExcelWriter(String fileName, int month, int year) {
        this.file = fileName + getMonth(month) + year + ".xlsx";
        this.month = month;
        this.book = new HSSFWorkbook();

    }

    public void write() throws IOException {
        book.write(new FileOutputStream(file));
        book.close();
    }


    public void addPharmacyReport(PharmacyReport report) {
        Sheet sheet = book.createSheet("Данные");
        int rowCount = writeHeaderStatistic(sheet);
        setPharmacyReport(sheet.createRow(rowCount), report);
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    public void addPharmacyChainReport(PharmacyChainReport report) {
        Sheet sheet = book.createSheet("Данные");
        int rowCount = writeHeaderStatistic(sheet);
        setPharmaciesFromReport(report, sheet, rowCount);
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
    }

    public void addOverallPharmacyChainReport(OverallPharmacyChainReport report) {
        Sheet sheet = book.createSheet("Данные");
        int rowCount = writeOverallHeaderStatistic(sheet);
        for (PharmacyChainReport chainReport : report.pharmacyChainReports) {
            rowCount = setPharmaciesFromReportWithPrice(chainReport, sheet, rowCount);
        }
        setOverallResultCallsAndPrice(sheet.createRow(rowCount), report.overall);
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
    }

    public void addCallsReport(List<CallsReport> reports) {
        Sheet sheet = book.createSheet("Звонки");
        int rowCount = writeHeaderCalls(sheet);
        for (CallsReport report : reports) {
            Row row = sheet.createRow(rowCount++);
            setCellDate(row, 0, report.date, setDateStyle());
            setCell(row, 1, report.medicinesName, setStyle());
            setCell(row, 2, report.pharmacyName, setStyle());
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);

    }


    public void addCallsReportWithPrice(List<CallsReport> reports) {
        Sheet sheet = book.createSheet("Звонки");
        int rowCount = writeHeaderCallsWithPrice(sheet);
        for (CallsReport report : reports) {
            Row row = sheet.createRow(rowCount++);
            setCellDate(row, 0, report.date, setDateStyle());
            setCell(row, 1, report.medicinesName, setStyle());
            setCell(row, 2, report.pharmacyName, setStyle());
            setCell(row, 3, Float.toString(report.price), setStyle());
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
    }

    public void addDefectureReports(List<String> defectures) {
        Sheet sheet = book.createSheet("Дефектура");
        setCell(sheet.createRow(0), 0, "Наименование препарата", setBoldStyle());
        int rowCount = 1;
        for (String defecture : defectures) {
            setCell(sheet.createRow(rowCount++), 0, defecture, setStyle());
        }
        sheet.autoSizeColumn(0);

    }

    public void addCallCountReports(OverallCallsCountReport callsCountReports) {
        Sheet sheet = book.createSheet("Учет звонков");
        Row head = sheet.createRow(0);
        setCell(head, 0, "Дата", setBoldStyle());
        setCell(head, 1, "Звонки", setBoldStyle());
        int rowCount = 1;
        for (CallsCountReport report : callsCountReports.callsCountReports) {
            Row row = sheet.createRow(rowCount++);
            setCellDate(row, 0, report.date, setDateStyleWithoutTime());
            setCell(row, 1, Integer.toString(report.callCount), setStyle());
        }
        setOverall(sheet.createRow(rowCount), callsCountReports.overall);
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);

    }

    private int setPharmaciesFromReportWithPrice(PharmacyChainReport reports, Sheet sheet, int rowCount) {
        for (PharmacyReport report : reports.pharmacyReports) {
            setPharmacyReportWithPrice(sheet.createRow(rowCount++), report);
        }
        setOverallCallsAndPrice(sheet.createRow(rowCount++), reports.overall);
        return rowCount;
    }

    private int setPharmaciesFromReport(PharmacyChainReport reports, Sheet sheet, int rowCount) {
        for (PharmacyReport report : reports.pharmacyReports) {
            setPharmacyReport(sheet.createRow(rowCount++), report);
        }
        setOverall(sheet.createRow(rowCount++), reports.overall);
        return rowCount;
    }

    private void setPharmacyReport(Row row, PharmacyReport report) {
        setCell(row, 0, report.name, setStyle());
        setCell(row, 1, Integer.toString(report.callCount), setStyle());
    }

    private void setPharmacyReportWithPrice(Row row, PharmacyReport report) {
        setCell(row, 0, report.name, setStyle());
        setCell(row, 1, Integer.toString(report.callCount), setStyle());
        setCell(row, 2, Float.toString(report.sumPrice), setStyle());
    }

    private void setOverall(Row row, Overall overall) {
        setCell(row, 0, "Итог", setBoldStyle());
        setCell(row, 1, Integer.toString(overall.callCount), setBoldStyle());
    }

    private void setOverallCallsAndPrice(Row row, Overall overall) {
        setCell(row, 0, "Итог", setBoldStyle());
        setCell(row, 1, Integer.toString(overall.callCount), setBoldStyle());
        setCell(row, 2, Float.toString(overall.sumPrice), setBoldStyle());

    }

    private void setOverallResultCallsAndPrice(Row row, Overall overall) {
        setCell(row, 0, "Общий итог", setBoldStyle());
        setCell(row, 1, Integer.toString(overall.callCount), setBoldStyle());
        setCell(row, 2, Float.toString(overall.sumPrice), setBoldStyle());

    }


    private void setCell(Row row, int column, String value, HSSFCellStyle style) {
        Cell newCell = row.createCell(column);
        newCell.setCellValue(value);
        newCell.setCellStyle(style);
    }

    private int writeHeaderStatistic(Sheet sheet) {
        int rowCount = 0;
        Row head = sheet.createRow(rowCount++);
        HSSFCellStyle style = setBoldStyle();
        setCell(head, 0, "Статистика за " + getMonth(month), style);
        Row columnName = sheet.createRow(rowCount++);
        setCell(columnName, 0, "Аптека", style);
        setCell(columnName, 1, "Количество звонков", style);
        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row (0-based)
                0, //last row  (0-based)
                0, //first column (0-based)
                1  //last column  (0-based)
        ));
        return rowCount;
    }

    private int writeHeaderCalls(Sheet sheet) {
        int rowCount = 0;
        Row head = sheet.createRow(rowCount++);
        HSSFCellStyle style = setBoldStyle();
        setCell(head, 0, "Дата/Время", style);
        setCell(head, 1, "Название препарата", style);
        setCell(head, 2, "Апетка", style);
        return rowCount;
    }

    private int writeHeaderCallsWithPrice(Sheet sheet) {
        int rowCount = 0;
        Row head = sheet.createRow(rowCount++);
        HSSFCellStyle style = setBoldStyle();
        setCell(head, 0, "Дата/Время", style);
        setCell(head, 1, "Название препарата", style);
        setCell(head, 2, "Апетка", style);
        setCell(head, 3, "Стоимость препарата", style);
        return rowCount;
    }


    private int writeOverallHeaderStatistic(Sheet sheet) {
        int rowCount = 0;
        Row head = sheet.createRow(rowCount++);
        HSSFCellStyle style = setBoldStyle();
        setCell(head, 0, "Статистика за " + getMonth(month), style);
        Row columnName = sheet.createRow(rowCount++);
        setCell(columnName, 0, "Аптека", style);
        setCell(columnName, 1, "Количество звонков", style);
        setCell(columnName, 2, "Сумма по звонкам", style);
        sheet.addMergedRegion(new CellRangeAddress(
                0, //first row (0-based)
                0, //last row  (0-based)
                0, //first column (0-based)
                1  //last column  (0-based)
        ));
        return rowCount;
    }


    private void setCellDate(Row row, int column, Date date, HSSFCellStyle dateStyle) {
        Cell cell = row.createCell(column);
        cell.setCellValue(date);
        cell.setCellStyle(dateStyle);
    }

    private HSSFCellStyle setStyle() {
        HSSFCellStyle cellStyle = book.createCellStyle();
        cellStyle = setBorder(cellStyle);
        return cellStyle;
    }

    private HSSFCellStyle setDateStyleWithoutTime() {
        HSSFCellStyle cellStyle = book.createCellStyle();
        cellStyle = setBorder(cellStyle);
        DataFormat format = book.createDataFormat();
        cellStyle.setDataFormat(format.getFormat("dd-MM-yyyy"));
        return cellStyle;
    }

    private HSSFCellStyle setDateStyle() {
        HSSFCellStyle cellStyle = book.createCellStyle();
        cellStyle = setBorder(cellStyle);
        DataFormat format = book.createDataFormat();
        cellStyle.setDataFormat(format.getFormat("dd-MM-yyyy HH:mm:ss"));
        return cellStyle;
    }

    private HSSFCellStyle setBoldStyle() {
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
        String[] monthNames = {"", "Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"};
        return monthNames[month];
    }
}
