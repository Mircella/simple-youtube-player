package net.vizja.youtube.model;

import net.vizja.youtube.utils.Common;

import org.ocpsoft.prettytime.PrettyTime;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Video implements Serializable {
    private String id;
    private String title;
    private String author;
    private String timePast;
    private String viewNumbers;
    private String imageUri;

    public Video() {
    }

    public Video(String id,String title, String author, String timePast, String viewNumbers,String imageUri) {
        this.id=id;
        this.title = title;
        this.author = author;
        this.timePast = timePast;
        this.viewNumbers = viewNumbers;
        this.imageUri=imageUri;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getTimePast() {

        return getPrettyAgoTime();
    }

    public String getViewNumbers() {
        return Common.getPrettyNumber(Long.valueOf(viewNumbers))+" views";
    }

    private String getPrettyAgoTime(){

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        try {
            Date date=inputFormat.parse(timePast);
            PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
            String ago = prettyTime.format(date);
            return ago;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
