package net.dervism.trafokanten.ws;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import net.dervism.trafokanten.com.SerialUtils;
import net.dervism.trafokanten.lcd.LCDCommander;

public class Passenger implements Observer{
	
	private LCDCommander lcd;
	
	private Map<Integer, Integer> current;
	
	public Passenger() {
	    current = new HashMap<Integer, Integer>();
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
		}
		
		List<Departure> top = new LinkedList<Departure>();
		List<Departure> rest = new LinkedList<Departure>();
		
		top = departures.size() < 3 ? departures.subList(0, departures.size()) : departures.subList(0, 3);
		
		if (departures.size() > 3 )rest = departures.subList(3, departures.size());
		
		if (isChanged(0, (int)top.get(0).getMinute())) {
		    lcd.clearDisplay();
		    int row = 0;
	            for (Departure departure : top) {
	                lcd.setCursorAt(row);
                        if (!isNow(departure)) {
                           lcd.write(departure.toDestinationWithMinuteString());
                        }
                        else {
                            lcd.write(departure.getDestinationName());
                            lcd.write(SerialUtils.spacer);
                            lcd.write("n");
                            lcd.writeASCIISymbol(15, 160);
                        }
                        row++;
	            }
		}
		
	}
	
	private boolean isNow(Departure departure) {
	    boolean now = (departure.getMinute() == 0) && departure.getSecond() < 30;
	     if (now) current.clear();
	     return now;
	}
	
	private boolean isChanged(int row, int minute) {
	    if (current.containsKey(row)) {
	        if (current.get(row) > minute) {
	            current.put(row, minute);
	            return true;
	        }
	    } else {
	        current.put(row, minute);
                return true;
	    }
	    return false;
	}
	
	private void debug(List<Departure> departures) {
	    for (Departure departure : departures) {
                System.out.println(departure.toDestinationWithMinuteString());
            }
	}
	private void debug(String s) {
	    System.out.println(s);
	}

}
