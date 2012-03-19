package net.dervism.trafokanten.ws;

import static org.junit.Assert.*;

import java.util.Calendar;

import org.junit.Test;

public class DepartureTest {
    

    @Test
    public void testToDestinationWithAutoformattedMinuteString() {
        Departure d = new Departure("6", "Ringen", System.currentTimeMillis() + (10*60*1000));
        assertEquals("Ringen        10 min", d.toDestinationWithAutoformattedMinuteString());
    }

}
