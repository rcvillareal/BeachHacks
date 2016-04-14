package rodney.myapplication;

/**
 * Created by rodney on 3/7/2016.
 */
public class SavedTimes {
    private int hours;
    private int minutes;
    private int seconds;
    private int centiseconds;

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }

    public int getCentiseconds() {
        return centiseconds;
    }

    public void setCentiseconds(int centiseconds) {
        this.centiseconds = centiseconds;
    }

    @Override
    public String toString() {
        return "SavedTimes{" +
                "hours=" + hours +
                ", minutes=" + minutes +
                ", seconds=" + seconds +
                ", centiseconds=" + centiseconds +
                '}';
    }
}
