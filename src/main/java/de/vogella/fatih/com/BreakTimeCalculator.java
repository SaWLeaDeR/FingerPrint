package de.vogella.fatih.com;

import javafx.util.Pair;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

class BreakTimeCalculator {
    private BreakTimeCalculator() {
        throw new IllegalStateException("BreakTimeCalculator Class");
    }

    static Pair<List<List<String>>, List<Long>> calculate(JFileChooser excelled) throws IOException {
        File excelFile = excelled.getSelectedFile();
        FileInputStream fis = new FileInputStream(excelFile);

        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIt = sheet.iterator();

        String value = String.format("%s", sheet.getRow(2).getCell(3));

        List<Date> breaktimestart = new ArrayList<Date>();
        List<Date> breaktimeends = new ArrayList<Date>();
        List<List<String>> breaktime = new ArrayList<List<String>>();
        List<Long> list = new ArrayList<Long>();

        while (rowIt.hasNext()) {
            Row row = rowIt.next();
            if (row.getRowNum() != 0) {
                if (!row.getCell(3).toString().equals(value)) {
                    breaktime.add(FindBiggest.findBiggest(breaktimeends, breaktimestart));
                    list.add(FindBiggest.difference(breaktimeends, breaktimestart));

                    value = row.getCell(3).toString();

                    breaktimeends.clear();
                    breaktimestart.clear();
                    if (DateUtil.isCellDateFormatted(row.getCell(4)) && DateUtil.isCellDateFormatted(row.getCell(5))) {
                        breaktimestart.add(row.getCell(4).getDateCellValue());
                        breaktimeends.add(row.getCell(5).getDateCellValue());
                    }
                } else if (DateUtil.isCellDateFormatted(row.getCell(4)) && DateUtil.isCellDateFormatted(row.getCell(5))) {
                    breaktimestart.add(row.getCell(4).getDateCellValue());
                    breaktimeends.add(row.getCell(5).getDateCellValue());
                }
            }
        }
        breaktime.add(FindBiggest.findBiggest(breaktimeends, breaktimestart));
        list.add(FindBiggest.difference(breaktimeends, breaktimestart));

        fis.close();
        return new Pair<List<List<String>>, List<Long>>(breaktime, list);
    }
}
