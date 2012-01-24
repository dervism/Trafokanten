package net.dervism.trafokanten.main;

import net.dervism.trafokanten.ws.Platform;

/**
 * 
 * @author Dervis M
 * 
 */

public class Trafokanten {


    public static void main(String[] args) throws Exception {

        String proxy = System.getProperty("proxy");
        System.getProperties().put("proxySet", "true");
        System.getProperties().put("proxyPort", 8080);
        System.getProperties().put("proxyHost", proxy);

        Platform p = new Platform();
        p.start();

    }
}
