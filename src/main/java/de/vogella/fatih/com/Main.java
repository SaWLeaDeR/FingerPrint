package de.vogella.fatih.com;

import javafx.util.Pair;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, ParseException {
        calculator();
    }

    static void calculator() throws IOException, ParseException {
        JFrame frame = new JFrame("text");
        JFileChooser jFileChooser = new JFileChooser();
        jFileChooser.setCurrentDirectory(new File("."));
        FileFilter filter = new FileNameExtensionFilter("Excel file", "xls", "xlsx");
        jFileChooser.setFileFilter(filter);
        frame.add(jFileChooser);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        jFileChooser.showOpenDialog(frame);

        File excelFile = jFileChooser.getSelectedFile();
        File outputFile = new File(jFileChooser.getSelectedFile().getName().replaceFirst("[.][^.]+$", "") + "-output.xlsx");
        FileInputStream fis = new FileInputStream(excelFile);
        FileOutputStream fos = new FileOutputStream(outputFile);
        frame.dispose();

        String input = "ADI,SOYADI,HARTAR,GUN,UYARILAR,GIRIS,CIKIS,OGLEN CIKIS,OGLEN DONUS,NET\n";
        List<String> startrownumbers = new ArrayList<String>();
        List<String> endsrownumbers = new ArrayList<String>();

        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFWorkbook workbook1 = new XSSFWorkbook();

        XSSFSheet sheet = workbook.getSheetAt(0);
        XSSFSheet sheet1 = workbook1.createSheet("test");

        Iterator<Row> rowIt = sheet.iterator();

        String value = "";
        String lasthourkeepertemporarily = "";
        String keeper = "";
        while (rowIt.hasNext()) {
            Row row = rowIt.next();

            Iterator<Cell> cellIterator = row.cellIterator();

            if (row.getRowNum() != 0) {
                if (!row.getCell(3).toString().equals(value) && DateUtil.isCellDateFormatted(row.getCell(4))) {
                    value = row.getCell(3).toString();
                    startrownumbers.add(row.getCell(4).getDateCellValue().getHours() + ":" + row.getCell(4).getDateCellValue().getMinutes());

                    if (!lasthourkeepertemporarily.isEmpty() && keeper.equals(lasthourkeepertemporarily)) {
                        endsrownumbers.add(lasthourkeepertemporarily);
                    }
                    if (DateUtil.isCellDateFormatted(row.getCell(5))) {
                        lasthourkeepertemporarily = row.getCell(5).getDateCellValue().getHours() + ":" + row.getCell(5).getDateCellValue().getMinutes();
                        keeper = lasthourkeepertemporarily;
                    }
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        if (cell.getColumnIndex() == 0 || cell.getColumnIndex() == 1 || cell.getColumnIndex() == 2 || cell.getColumnIndex() == 3) {
                            input += cell.toString();
                            input += ",";
                        }
                    }
                    input += "\n";
                } else if (row.getCell(3).toString().equals(value)) {
                    if (DateUtil.isCellDateFormatted(row.getCell(4)) && !DateUtil.isCellDateFormatted(row.getCell(5))) {
                        lasthourkeepertemporarily = row.getCell(4).getDateCellValue().getHours() + ":" + row.getCell(4).getDateCellValue().getMinutes();
                        keeper = lasthourkeepertemporarily;
                    } else if (DateUtil.isCellDateFormatted(row.getCell(5))) {
                        lasthourkeepertemporarily = row.getCell(5).getDateCellValue().getHours() + ":" + row.getCell(5).getDateCellValue().getMinutes();
                        keeper = lasthourkeepertemporarily;
                    }
                } else if (lasthourkeepertemporarily.equals(keeper) && !row.getCell(3).toString().equals(value)) {
                    endsrownumbers.add(lasthourkeepertemporarily);
                    keeper = "000";
                }
                if (!DateUtil.isCellDateFormatted(row.getCell(5)) && !DateUtil.isCellDateFormatted(row.getCell(4))) {
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        if (cell.getColumnIndex() == 0 || cell.getColumnIndex() == 1 || cell.getColumnIndex() == 2 || cell.getColumnIndex() == 3) {
                            input += cell.toString();
                            input += ",";
                        }
                    }
                    startrownumbers.add("0:0");
                    endsrownumbers.add("0:0");
                    input += "\n";
                }
            }
        }
        endsrownumbers.add(lasthourkeepertemporarily);
        Pair<List<List<String>>, List<Long>> p = BreakTimeCalculator.calculate(jFileChooser);
        List<List<String>> breakTime = p.getKey();
        List<Long> list = p.getValue();
        CellStyle style = workbook1.createCellStyle();
        CellStyle style1 = workbook1.createCellStyle();
        style.setDataFormat(workbook1.getCreationHelper().createDataFormat().getFormat("HH:mm"));
        style1.setDataFormat(workbook1.getCreationHelper().createDataFormat().getFormat("HH:mm"));
        style1.setFillForegroundColor(IndexedColors.SKY_BLUE.getIndex());
        style1.setFillPattern(CellStyle.SOLID_FOREGROUND);
        String[] line = input.split("\n");
        int rownum = 0;
        for (String s : line) {
            String[] cell1 = line[rownum].split(",");
            Row row = sheet1.createRow(rownum++);
            int cellnum = 0;
            for (String obj : cell1) {
                if (rownum == 1) {
                    Cell cell = row.createCell(cellnum++);
                    cell.setCellValue(obj);
                } else {
                    Cell cell = row.createCell(cellnum++);
                    cell.setCellValue(obj);
                    if (cellnum == 4) {
                        Cell cell2 = row.createCell(cellnum++);
                        if (list.get(rownum - 2) > 30L) {
                            cell2.setCellStyle(style1);
                        } else {
                            cell2.setCellStyle(style);
                        }
                        cell2.setCellValue(String.format("%s dk ", list.get(rownum - 2)));
                    }

                    if (cellnum == 5) {
                        Cell cell6 = row.createCell(cellnum++);
                        cell6.setCellValue(startrownumbers.get(rownum - 2));
                    }
                    if (cellnum == 6) {
                        Cell cell3 = row.createCell(cellnum++);
                        cell3.setCellValue(endsrownumbers.get(rownum - 2));
                        cell3.setCellStyle(style);
                    }
                    if (cellnum == 7) {
                        Cell cell4 = row.createCell(cellnum++);
                        cell4.setCellValue(breakTime.get(rownum - 2).get(1));
                    }
                    if (cellnum == 8) {
                        Cell cell5 = row.createCell(cellnum++);
                        cell5.setCellValue(breakTime.get(rownum - 2).get(0));
                    }
                }
            }
            if (rownum == 1) {
                Cell cell = row.createCell(cellnum++);
                cell.setCellValue("");
            } else if (endsrownumbers.get(rownum - 2).contains("10:5") || startrownumbers.get(rownum - 2).contains("15:") ||
                    endsrownumbers.get(rownum - 2).contains("11:") || startrownumbers.get(rownum - 2).contains("14:") ||
                    endsrownumbers.get(rownum - 2).contains("12:") || startrownumbers.get(rownum - 2).contains("13:") ||
                    endsrownumbers.get(rownum - 2).contains("13:") || startrownumbers.get(rownum - 2).contains("12:")) {
                Cell cell = row.createCell(cellnum++);
                cell.setCellFormula("G" + rownum + "-F" + rownum);
                cell.setCellStyle(style);
            } else {
                Cell cell = row.createCell(cellnum++);
                if (!startrownumbers.get(rownum - 2).equals("0:0") && !endsrownumbers.get(rownum - 2).equals("0:0")) {
                    String a = WorkHourCalculator.calculate(startrownumbers.get(rownum - 2), endsrownumbers.get(rownum - 2), breakTime.get(rownum - 2));
                    cell.setCellValue(a);
                } else {
                    cell.setCellValue("0:0");
                }
                cell.setCellStyle(style);
            }
        }
        workbook1.write(fos);
        fos.close();

        fis.close();
    }
}