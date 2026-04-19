package cinema;

import javax.swing.*;
import java.util.Date;

public class Movie {
    private String title;
    private String genre;
    private String language;
    private Date date;
    private String rating;
    private int duration;
    private String director;
    private String cast;
    private String subtitles;
    private String description;
    private ImageIcon poster;

    public Movie(String title, String genre, String language,String rating,Date date,int duration, String director, String cast, String subtitles, String description, ImageIcon poster){
        this.title = title;
        this.genre = genre;
        this.language = language;
        this.date = date;
        this.rating = rating;
        this.duration = duration;
        this.director = director;
        this.cast = cast;
        this.subtitles = subtitles;
        this.description = description;
        this.poster = poster;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getSubtitles() {
        return subtitles;
    }

    public void setSubtitles(String subtitles) {
        this.subtitles = subtitles;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ImageIcon getPoster() {
        return poster;
    }

    public void setPoster(ImageIcon poster) {
        this.poster = poster;
    }
}
