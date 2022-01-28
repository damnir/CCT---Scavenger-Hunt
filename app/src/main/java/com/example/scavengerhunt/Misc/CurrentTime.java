package com.example.scavengerhunt.Misc;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CurrentTime {

    public String time;
    private Date startDate;

    public CurrentTime() {
        startDate = new Date();
    }

    public String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }

    public String getElapsed() {
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        long start = startDate.getTime();
        long current = date.getTime();
        date.setTime(current-start-3600000);
        return formatter.format(date);
    }
}
