package de.vogella.fatih.com;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

class FindBiggest {

    private FindBiggest() {
        throw new IllegalStateException("FindBiggest Class");
    }

    static List<String> findBiggest(List<Date> endsrownumbers, List<Date> startrownumbers) {
        long biggestTimeZone = 0;
        List<String> list = new ArrayList<String>();
        String starthour = "";
        String endhour = "";

        for (int i = 0; i < endsrownumbers.size() - 1; i++) {
            long diffInMillies = Math.abs(endsrownumbers.get(i).getTime() - startrownumbers.get(i + 1).getTime());
            long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if (diff > biggestTimeZone) {
                biggestTimeZone = diff;
                starthour = startrownumbers.get(i + 1).getHours() + ":" + startrownumbers.get(i + 1).getMinutes();
                endhour = endsrownumbers.get(i).getHours() + ":" + endsrownumbers.get(i).getMinutes();
            }
        }
        list.add(starthour);
        list.add(endhour);
        list.add(Long.toString(biggestTimeZone));
        return list;
    }

    static Long difference(List<Date> endsrownumbers, List<Date> startrownumbers) {
        long biggestTimeZone = 0;
        int ikeeper = -1;
        long biggestTimeZone1 = 0;

        for (int i = 0; i < endsrownumbers.size() - 1; i++) {
            long diffInMillies = Math.abs(endsrownumbers.get(i).getTime() - startrownumbers.get(i + 1).getTime());
            long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if (diff > biggestTimeZone) {
                biggestTimeZone = diff;
                ikeeper = i;
            }
        }
        if (ikeeper != -1 && endsrownumbers.size() >= 2) {
            endsrownumbers.remove(ikeeper);
            startrownumbers.remove(ikeeper + 1);
        }
        for (int i = 0; i < endsrownumbers.size() - 1; i++) {
            long diffInMillies = Math.abs(endsrownumbers.get(i).getTime() - startrownumbers.get(i + 1).getTime());
            long diff = TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
            if (diff > biggestTimeZone1) {
                biggestTimeZone1 = diff;
            }
        }
        return biggestTimeZone1;
    }
}