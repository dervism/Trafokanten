package net.dervism.trafokanten.com;

import static org.junit.Assert.*;

import org.junit.Test;

public class SerialUtilsTest {

    @Test
    public void testTabulator() {
        assertTrue(SerialUtils.tabulator(-1).length() == 0);
        assertTrue(SerialUtils.tabulator(0).length() == 0);
        assertTrue(SerialUtils.tabulator(3).length() == 3);
    }

    @Test
    public void testReplaceNoneAsciiChars() {
        assertEquals(SerialUtils.replaceNoneAsciiChars("Ellingsrudåsen"), "Ellingsrudasen");
        assertEquals(SerialUtils.replaceNoneAsciiChars("Østerås"), "Osteras");
        assertEquals(SerialUtils.replaceNoneAsciiChars("ÅøåØ"), "AoaO");
    }

}
