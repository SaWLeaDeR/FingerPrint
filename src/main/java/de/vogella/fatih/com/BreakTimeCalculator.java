package de.vogella.fatih.com;

import javafx.util.Pair;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

class BreakTimeCalculator {
    private BreakTimeCalculator(){
        throw new IllegalStateException("BreakTimeCalculator Class");
    }

    static Pair<List<List<String>>, List<Long>> calculate() throws IOException {
        File excelFile = new File("Book2.xlsx");
        FileInputStream fis = new FileInputStream(excelFile);

        XSSFWorkbook workbook = new XSSFWorkbook(fis);

        XSSFSheet sheet = workbook.getSheetAt(0);

        Iterator<Row> rowIt = sheet.iterator();
        String value = "";
        List<Date> breaktimestart = new ArrayList<Date>();
        List<Date> breaktimeends = new ArrayList<Date>();
        List<List<String>> breaktime = new ArrayList<List<String>>();
        List<String> zerobreaktime = new ArrayList<String>();
        zerobreaktime.add("0:0");
        zerobreaktime.add("0:0");
        List<Long> list = new ArrayList<Long>();

        while (rowIt.hasNext()) {
            Row row = rowIt.next();

            Iterator<Cell> cellIterator = row.cellIterator();
            if (row.getRowNum() != 0) {
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    if (cell.getColumnIndex() == 4) {
                        if (!row.getCell(3).toString().equals(value) && DateUtil.isCellDateFormatted(row.getCell(4)) && DateUtil.isCellDateFormatted(row.getCell(5))) {
                            value = row.getCell(3).toString();
                            breaktime.add(FindBiggest.findBiggest(breaktimeends, breaktimestart));
                            list.add(FindBiggest.difference(breaktimeends, breaktimestart));

                            breaktimeends.clear();
                            breaktimestart.clear();
                            breaktimestart.add(row.getCell(4).getDateCellValue());
                            breaktimeends.add(row.getCell(5).getDateCellValue());
                        } else if (DateUtil.isCellDateFormatted(row.getCell(4)) && DateUtil.isCellDateFormatted(row.getCell(5))) {
                            breaktimestart.add(row.getCell(4).getDateCellValue());
                            breaktimeends.add(row.getCell(5).getDateCellValue());
                        }
                    }
                }
                if (!DateUtil.isCellDateFormatted(row.getCell(4)) && !DateUtil.isCellDateFormatted(row.getCell(5))) {
                    breaktime.add(zerobreaktime);
                    list.add(0L);
                }
            }
        }
        list.remove(0);
        list.add(0L);
        for (int i = list.size() - 2; i > 0; i--) {
            if (list.get(i) == 0L) {
                Collections.swap(list, i + 1, i);
            }
        }
        breaktime.remove(0);
        breaktime.add(zerobreaktime);
        fis.close();
        return new Pair<List<List<String>>, List<Long>>(breaktime, list);
    }
}