package net.dervism.trafokanten.main;

import net.dervism.trafokanten.lcd.LCDCommander;
import net.dervism.trafokanten.ws.Platform;

/**
 * 
 * @author Dervis M
 *
 */

public class Trafokanten {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		LCDCommander lcd = new LCDCommander();
		
		System.getProperties().put("proxySet", "true");
		System.getProperties().put("proxyPort", 8080 );
		System.getProperties().put("proxyHost","proxy.statnett.no");
	    
		Platform p = new Platform();
		p.start();
		
		//lcd.connect();
		
//		lcd.clearDisplay();
//		lcd.setScrollingMarqueeText("Hello, world!");
//		lcd.enableScrollingMarquee(1, 5, 60);
//		lcd.write(lcd.HIDE_CURSOR);
		
		//System.exit(0);
	}
	
	

}
