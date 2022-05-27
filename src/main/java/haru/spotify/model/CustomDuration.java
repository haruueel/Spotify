package haru.spotify.model;

public class CustomDuration {

    private String duration;
    private String display;

    // Constructor

    public CustomDuration(String duration){
        this.duration = duration;
        int totalSeconds = Integer.parseInt(this.duration);
        String minutes = String.valueOf(totalSeconds / 60);
        String seconds = String.valueOf(totalSeconds % 60);
        if (Integer.parseInt(seconds)<10) seconds = "0"+ seconds;
        display = minutes +":"+ seconds;
    }

    @Override
    public String toString(){
        return display;
    }

    public String getDuration() {
        return duration;
    }
}
