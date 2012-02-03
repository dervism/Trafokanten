package net.dervism.trafokanten.ws;

import java.util.concurrent.TimeUnit;

import net.dervism.trafokanten.com.SerialUtils;

/**
 * 
 * @author Dervis M
 *
 */

public class Departure {

    private String destinationName;

    private long departureTime;

    private long minute;

    private long second;

    public Departure(Long departureTime) {
        this("", departureTime);
    }

    public Departure(String destinationName, long departureTime) {
        this.destinationName = destinationName;
        this.departureTime = departureTime;
        departureTime -= System.currentTimeMillis();
        this.minute = TimeUnit.MILLISECONDS.toMinutes(departureTime);
        this.second = TimeUnit.MILLISECONDS.toSeconds(departureTime) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(departureTime));
    }

    public void setDepartureTime(Long departureTime) {
        this.departureTime = departureTime;
    }

    public Long getDepartureTime() {
        return departureTime;
    }

    public long getMinute() {
        return minute;
    }

    public long getSecond() {
        return second;
    }

    public String toMinuteString() {
        return String.format("%d min", minute);
    }

    public String toDestinationWithMinuteString() {
        return String.format(destinationName + SerialUtils.spacer + "%d min", minute);
    }

    public String getDestinationName() {
        return destinationName;
    }

    public String toString() {
        return String.format("%d min, %d sec", minute, second);
    }

    public boolean equalsMinute(Departure other) {
        if (minute != other.minute)
            return false;
        return true;
    }

    public boolean after(Departure other) {
        return minute > other.minute;
    }

    public boolean before(Departure other) {
        return minute < other.minute;
    }

}
