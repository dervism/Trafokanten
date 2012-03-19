package net.dervism.trafokanten.ws;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import net.dervism.trafokanten.com.SerialUtils;
import net.dervism.trafokanten.lcd.LCDCommander;

/**
 * 
 * @author Dervis M
 *
 */

public class Passenger implements Observer{
	
	private LCDCommander lcd;
	
	private long previous = 99;
	
	private boolean marquee = false;
	
	public Passenger() {
		lcd = new LCDCommander();
		try {
			lcd.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		lcd.init();
	}

	@Override
	public void update(Observable source, Object data) {
		List<Departure> departures = (List<Departure>)data;
		
		if (departures.isEmpty()) {
			lcd.clearDisplay();
			lcd.write("Ingen avganger.");
		} else {
		    List<Departure> top = new LinkedList<Departure>();
	                List<Departure> rest = new LinkedList<Departure>();
	                
	                top = departures.size() < 3 ? departures.subList(0, departures.size()) : departures.subList(0, 3);
	                
	                if (departures.size() > 3 ) rest = departures.subList(3, departures.size());
	                
	                long current = top.get(0).getMinute();
	                boolean changed = isChanged(current);
	                
	                if (changed) {
	                    previous = current > 0 ? current : 99;
	                    lcd.clearDisplay();
	                    int row = 0;
	                    for (Departure departure : top) {
	                        lcd.setCursorAt(row);
	                        if (!isNow(departure)) {
	                            lcd.write(departure.toDestinationWithAutoformattedMinuteString());
	                        }
	                        else {
	                            lcd.write(departure.toDepartingDestination());
	                            lcd.writeASCIISymbol(15, 160);
	                        }
	                        row++;
	                    }
	                    if (marquee){
	                        String restStr = "";
	                        for (Departure departure : rest) {
	                            restStr += departure.toDestinationWithShortMinuteString() + " ";
	                        }
	                        lcd.setScrollingMarqueeText(restStr);
	                        lcd.enableScrollingMarquee(3, 5, 30);	                        
	                    }
	                }
		}		
	}
	
	private boolean isNow(Departure departure) {
	    return (departure.getMinute() == 0) && departure.getSecond() < 30;
	}
	
	private boolean isChanged(long current) {
	    return current < previous;
	}

}
