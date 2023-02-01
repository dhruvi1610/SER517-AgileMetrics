package edu.asu.cassess.model.github;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class GitHubAnalytics {
    public static double calculateWeight(int linesOfCodeAdded, int linesOfCodeDeleted){
        Date now = new Date();

        SimpleDateFormat simpleDateformat = new SimpleDateFormat("E"); // the day of the week abbreviated
        System.out.println(simpleDateformat.format(now));

        simpleDateformat = new SimpleDateFormat("EEEE"); // the day of the week spelled out completely
        System.out.println(simpleDateformat.format(now));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_WEEK, 1);
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        int totalCodeAlteration;

        double weight;
        double preWeight;
        if(linesOfCodeAdded < linesOfCodeDeleted) {
            totalCodeAlteration = (linesOfCodeAdded + (linesOfCodeDeleted / 4))/4;
        } else {
            totalCodeAlteration = linesOfCodeAdded + (linesOfCodeDeleted / 4);
        }
        preWeight = totalCodeAlteration/(day * 16);

        if (preWeight > 13) {
            weight = 13;
        } else {
            weight = round(preWeight, 4);
        }
        //System.out.println("LOCD: " + linesOfCodeDeleted);
        //System.out.println("LOCA: " + linesOfCodeAdded);
        //System.out.println("TOTC: " + totalCodeAlteration);
        //System.out.println("Day: " + day);
        //System.out.println("Weight: " + weight);
        return weight;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
