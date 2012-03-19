package net.dervism.trafokanten.ws;

import java.util.concurrent.TimeUnit;

import net.dervism.trafokanten.com.SerialUtils;

/**
 * 
 * @author Dervis M
 *
 */

public class Departure {
    private String publishedLineName;
    
    private String destinationName;

    private long departureTime;

    private long minute;

    private long second;

    public Departure(Long departureTime) {
        this("", "", departureTime);
    }

    public Departure(String publishedLineName, String destinationName, long departureTime) {
        this.publishedLineName = publishedLineName;
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

    public String toDestinationWithShortMinuteString() {
        return String.format("L" + getPublishedLineName() + " " + "%dm", minute);
    }
    
    /**
     * Use this for departures not departing at the current 
     * moment.
     * 
     * @return
     */
    public String toDestinationWithAutoformattedMinuteString() {
        String str = "";
        int destinationNameLen = destinationName.length();
        int timeLen = String.format("%d min", minute).length();
        int tlen = destinationNameLen + timeLen;
        
        if (tlen > 20) {
            int overflow = (tlen - 20) + 1;
            int cut = destinationNameLen - overflow;
            String dstr = destinationName.substring(0, cut);
            str = String.format(SerialUtils.replaceNoneAsciiChars(dstr) + SerialUtils.tabulator(overflow) + "%d min", minute);
        } else {
            int overflow = (20 - tlen);
            str = String.format(SerialUtils.replaceNoneAsciiChars(destinationName) + SerialUtils.tabulator(overflow) + "%d min", minute);
        }
        
        return str;
    }
    
    /**
     * Use this for displaying departure info with Norwegian keyword
     * "nå". When using English keyword "now", increase the 'destinationNameLen'
     * to 3. In this version the Norwegian letter 'å' is written through
     * the LCDCommander class.
     *  
     * @return
     */
    public String toDepartingDestination() {
        String str = "";
        int destinationNameLen = destinationName.length();
        int tlen = destinationNameLen + 2;
        
        if (tlen > 20) {
            int overflow = (tlen - 20) + 1;
            int cut = destinationNameLen - overflow;
            String dstr = destinationName.substring(0, cut);
            str = SerialUtils.replaceNoneAsciiChars(dstr) + SerialUtils.tabulator(overflow) + "n";
        } else {
            int overflow = (20 - tlen);
            str = SerialUtils.replaceNoneAsciiChars(destinationName) + SerialUtils.tabulator(overflow) + "n";
        }
        
        return str;
    }
    
    private String getPublishedLineName() {
        return publishedLineName;
    }

    public String getDestinationName() {
        return destinationName;
    }

    public String toString() {
        return String.format("%d min, %d sec", minute, second);
    }

    public boolean after(Departure other) {
        return minute > other.minute;
    }

    public boolean before(Departure other) {
        return minute < other.minute;
    }

}
