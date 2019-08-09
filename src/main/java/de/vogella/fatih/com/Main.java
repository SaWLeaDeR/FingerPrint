package de.vogella.fatih.com;

import javafx.util.Pair;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        BreakTimeCalculator breakTimeCalculator = null;
        String inputFileName = JOptionPane.showInputDialog("Please Enter the Input File Name (without File Extension)");
        JFrame frame = new JFrame("text");
        JFileChooser jFileChooser = new JFileChooser();
//        jFileChooser.setCurrentDirectory(new File(System));
        jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        frame.add(jFileChooser);
        frame.setVisible(true);
        jFileChooser.showOpenDialog(frame);

//        jFileChooser.addActionListener(new ActionListener() {
//            @Override
//            public void actionPerformed(ActionEvent e) {
//                if (e.ge)
//            }
//        });
        File excelFile = new File(inputFileName + ".xlsx");
        File outputFile = new File(inputFileName + "_output.xlsx");
        FileInputStream fis = new FileInputStream(excelFile);
        FileOutputStream fos = new FileOutputStream(outputFile);

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
                if (!row.getCell(3).toString().equals(value) && DateUtil.isCellDateFormatted(row.getCell(4)) && DateUtil.isCellDateFormatted(row.getCell(5))) {
                    value = row.getCell(3).toString();
                    startrownumbers.add(row.getCell(4).getDateCellValue().getHours() + ":" + row.getCell(4).getDateCellValue().getMinutes());
                    if (!lasthourkeepertemporarily.isEmpty() && keeper == lasthourkeepertemporarily) {
                        endsrownumbers.add(lasthourkeepertemporarily);
                    }
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        if (cell.getColumnIndex() == 5) {
                            continue;
                        } else if (cell.getColumnIndex() == 4) {
                            if (DateUtil.isCellDateFormatted(cell)) {
                                input += cell.getDateCellValue().getHours();
                                input += ":";
                                input += cell.getDateCellValue().getMinutes();
                                input += ",";
                            } else {
                                input += cell.toString();
                            }
                        } else {
                            input += cell.toString();
                            input += ",";
                        }
                    }
                    input += "\n";

                } else if (row.getCell(3).toString().equals(value) && DateUtil.isCellDateFormatted(row.getCell(5))) {
                    lasthourkeepertemporarily = row.getCell(5).getDateCellValue().getHours() + ":" + row.getCell(5).getDateCellValue().getMinutes();
                    keeper = lasthourkeepertemporarily;
                } else if (lasthourkeepertemporarily == keeper && !row.getCell(3).toString().equals(value)) {
                    endsrownumbers.add(lasthourkeepertemporarily);
                    keeper = "000";
                }
                if (!DateUtil.isCellDateFormatted(row.getCell(5)) && !DateUtil.isCellDateFormatted(row.getCell(4))) {
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();
                        input += cell.toString();
                        input += ",";
                    }
                    startrownumbers.add("0:0");
                    endsrownumbers.add("0:0");
                    input += "00,";
                    input += "\n";
                }
            }
        }
        Pair<List<List<String>>, List<Long>> p = breakTimeCalculator.calculate();
        List<List<String>> breakTime = p.getKey();
        List<Long> list = p.getValue();
        for (int i = breakTime.size() - 2; i > 0; i--) {
            if (breakTime.get(i).get(0).equals("0:0")) {
                Collections.swap(breakTime, i + 1, i);
            }
        }
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
                    if (cellnum == 4) {
                        Cell cell = row.createCell(cellnum++);
                        if (list.get(rownum - 2) > 30l) {
                            cell.setCellStyle(style1);
                        } else {
                            cell.setCellStyle(style);
                        }

                        cell.setCellValue(String.format("%s dk ",list.get(rownum-2)));
                    }
                    Cell cell = row.createCell(cellnum++);
                    cell.setCellValue(obj);
                    if (cellnum == 6) {
                        Cell cell3 = row.createCell(cellnum++);
                        cell3.setCellValue(endsrownumbers.get(rownum - 2));
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
            Cell cell = row.createCell(cellnum++);
            cell.setCellFormula("G" + rownum + "-F" + rownum + "-I" + rownum + "+H" + rownum);
            cell.setCellStyle(style);
        }
        workbook1.write(fos);
        fos.close();

        fis.close();
    }
}