package de.vogella.fatih.com;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.TimeUnit;

class WorkHourCalculator {

    private WorkHourCalculator(){
        throw new IllegalStateException("WorkHourCalculator Class");
    }


    static String Calculate(String start, String end) throws ParseException {

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

        diff -= 60;

        long hours = diff / 60; //since both are ints, you get an int
        long minutes = diff % 60;

        return String.format("%d:%02d", hours, minutes);
    }
}
