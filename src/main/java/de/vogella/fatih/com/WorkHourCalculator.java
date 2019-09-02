package de.vogella.fatih.com;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

class WorkHourCalculator {


    private WorkHourCalculator() {
        throw new IllegalStateException("WorkHourCalculator Class");
    }


    static String calculate(String start, String end, List<String> list) throws ParseException {
        int keeper = 0;
        SimpleDateFormat df = new SimpleDateFormat("HH:mm");
        Calendar gc = new GregorianCalendar();

        Date d = df.parse(start);
        gc.setTime(d);
        gc.add(Calendar.HOUR, 2);
        Date d2 = gc.getTime();

        Date d3 = df.parse(end);
        gc.setTime(d3);
        gc.add(Calendar.HOUR, 2);
        Date d4 = gc.getTime();


        long diffInMillies = Math.abs(d2.getTime() - d4.getTime());
        long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
        if (!list.get(2).contains("0:0")) {
            keeper = Integer.parseInt(list.get(2));
            int hour = (keeper > 60) ? keeper : 60;
            diff -= hour;
        }
        long hours = diff / 60; //since both are ints, you get an int
        long minutes = diff % 60;

        return String.format("%d:%02d", hours, minutes);
    }
}