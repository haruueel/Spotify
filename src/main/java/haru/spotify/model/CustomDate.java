package haru.spotify.model;

public class CustomDate {

    private String fullDate;
    private String year;
    private String month;
    private String day;

    public CustomDate(String dateString){
        this.fullDate = dateString;
        String[] dateArgs = dateString.split("-");
        this.year = dateArgs[0];
        this.month = dateArgs[1];
        this.day = dateArgs[2];
    }

    @Override
    public String toString(){
        return year+"-"+month+"-"+day;
    }

    // Getters Setters

    public String getFullDate() {
        return fullDate;
    }

    public String getDay() {
        return day;
    }

    public String getMonth() {
        return month;
    }

    public String getYear() {
        return year;
    }

    public void setFullDate(String fullDate) {
        this.fullDate = fullDate;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
