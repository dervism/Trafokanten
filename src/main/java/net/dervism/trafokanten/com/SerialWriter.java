package net.dervism.trafokanten.com;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 * @author Dervis M
 *
 */

public class SerialWriter {
	
	protected OutputStream outputStream;
	
	public SerialWriter() {
		
	}
	
	public void connect() throws Exception {
		connect("COM7");
	}
	
	/**
	 * Connects to a serial port.
	 * @param portName
	 * @throws Exception
	 */
	public void connect(String portName) throws Exception {
		CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        if ( portIdentifier.isCurrentlyOwned() )
        {
        	throw new RuntimeException("Port is currently in use");
        }
        else
        {
            CommPort commPort = portIdentifier.open(this.getClass().getName(), 2000);
            
            if ( commPort instanceof SerialPort )
            {
                SerialPort serialPort = (SerialPort) commPort;
                serialPort.setSerialPortParams(19200, SerialPort.DATABITS_8, 
                		SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
                
                // InputStream in = serialPort.getInputStream();
                outputStream = serialPort.getOutputStream();
            }
            else
            {
                throw new IllegalArgumentException("Expected a serial port, but found " + commPort.getName());
            }
        }     
    }
	
	public void write(byte[] b) {
		try {                
			outputStream.write(b);               
	     }
	     catch ( IOException e ) {
	         e.printStackTrace();
	     }
	}
	 
	public void write(int c) {
		try {                
			outputStream.write(c);               
		}
		catch ( IOException e ) {
			e.printStackTrace();
		}            
     }
	 
	 public void write(String text) {
		 write(text.getBytes());          
     }
	 
	 /**
	  * Must close in order to properly release the serial port.
	  */
	 public void close() {
		 try {
			outputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	 }
	 
	/**
	 * For testing purposes.
	 * @param outputStream
	 */
	public void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	
}
