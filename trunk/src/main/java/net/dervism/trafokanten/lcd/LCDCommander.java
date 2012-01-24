package net.dervism.trafokanten.lcd;

import java.util.Map;

import net.dervism.trafokanten.com.SerialWriter;

/**
 * 
 * @author Dervis M
 *
 */

public class LCDCommander extends SerialWriter {
	
	/**
	 * Send "21" followed by the following bytes:
	 * index
	 * character
	 * 
	 * index determines which of the 20 scrolling marquee characters gets set (\000 to \019 are valid).
	 * character is the value that this position in the scrolling marquee will be set to.
	 * 
	 * NOTE
	 * A section of memory holds 20 hidden characters. These 20 characters, along with the contents of one
	 * line of the display, can be rotated pixel by pixel across the display in a circular fashion. Use this command
	 * multiple times to set the 20 hidden characters. Use the normal display functions to set the other
	 * characters in the line you want to rotate and then enable rotation with Enable Scrolling Marquee command.
	 * The hidden characters are set to blanks at power-up, or loaded from the User Boot Screen.
	 * You will probably want to disable the scrolling marquee movement while you are setting the scrolling
	 * marquee characters or modifying characters on the rotating line. You will probably also want to move
	 * the cursor off the line or hide it.
	 * 
	 * If the first character of the line above the scrolling line on the CFA634 display has any pixels on its bottom
	 * row set, they will appear as �ghost� pixels in the upper right of the last character of the scrolling line.
	 * This will only happen on the first line or if the cursor or a custom character with its bottom row of pixels
	 * set is in the leftmost position of the line directly above the scrolling marquee line. These pixels are an
	 * artifact of the display controller�s memory architecture.
	 */
	public static int SET_SCROLLING_MARQUEE_CHARACTER = 21;
	
	public void setScrollingMarqueeCharacter(int index, char character) {
		write(SET_SCROLLING_MARQUEE_CHARACTER);
		write(index);
		write(character);
	}
	
	/**
	 * Sets the given text in scrolling marquee buffer.
	 * 
	 * @param text The text to scroll. Should not be longer than 20 characters, characters above the limit will 
	 * simply be ignored.
	 */
	public void setScrollingMarqueeText(String text) {
	        int length = text.length();
	        if (length > 20) length = 20;
	        
		for (int i = 0; i < length; i++) {
			setScrollingMarqueeCharacter(i, text.charAt(i));
		}
	}
	
	/**
	 * Enable Scrolling Marquee
	 * Send "22" followed by the following bytes:
	 * line
	 * scroll_step_size
	 * update_speed
	 * 
	 * line determines which line will scroll with the scrolling marquee or if the scrolling marquee is disabled.
	 * 
	 * \000 enable scrolling marquee on line 1
	 * \001 enable scrolling marquee on line 2
	 * \002 enable scrolling marquee on line 3
	 * \003 enable scrolling marquee on line 4
	 * \255 disable scrolling marquee (valid values for scroll_step_size and update_speed must still be sent)
	 * 
	 * scroll_step_size controls the number of pixels that the message is shifted by at each update:
	 * 
	 * \001 shift by one pixel, smooth but slow
	 * \002 shift by two pixels
	 * \003 shift by three pixels
	 * \004 shift by four pixels
	 * \005 shift by five pixels
	 * \006 shift by six pixels (equal to shifting by one character, fast)
	 * 
	 * update_speed determines how often updates will occur. The units are 1/96 of a second (about 10 mS). 
	 * 
	 * The valid range is \005 (52 mS) to \100 (1.042 S).
	 * 
	 * NOTE
	 * Since the liquid crystal fluid in the display takes some time to react, the minimum usable value is about
	 * 16 or 167 mS. The Enable Scrolling Marquee command supports a large range of speeds to accommodate
	 * future displays and user preference.
	 * 
	 * The following equations will allow you to determine the speed at which the message scrolls:
	 * Update Frequency = update_speed / 96 Hz
	 * Update Period = 96 / update_speed Seconds
	 * New Character Frequency = (scroll_step_size x update_speed) / (96 x 6) Hz
	 * New Character Period = (96 x 6) / (scroll_step_size x update_speed) Seconds
	 * Message Repeat Period = (40 x 96 x 6) / (scroll_step_size x update_speed) Seconds
	 */
	public static int ENABLE_SCROLLING_MARQUEE = 22;
	
	public void enableScrollingMarquee(int line, int scroll_step_size, int update_speed) {
		write(ENABLE_SCROLLING_MARQUEE);
		write(line);
		write(scroll_step_size);
		write(update_speed);
	}
	
	/**
         * Sets and activates some scrolling marquee text.
         * 
         * @param text The text to scroll. Should not be longer than 20 characters.
         * @param row Which line the text will be scrolled on. Lines are index from 0 -> 3.
         * @param params You should provide params 'scroll_step_size' and 'update_speed'. Default values are 5 & 60.
         */
        public void setScrollingMarqueeTextAndActivate(String text, int row, Map<String, Integer> params) {
            clearDisplay();
            setScrollingMarqueeText(text);
            enableScrollingMarquee(row, params.get("scroll_step_size") != null ? params.get("scroll_step_size") : 5, 
                                        params.get("update_speed") != null ? params.get("update_speed") : 60);
            write(HIDE_CURSOR);
        }
	
	/**
	 * Moves cursor to the top left character position. No data is changed. Identical to SET_CURSOR_POS=0,0.
	 */
	public static int CURSOR_HOME = 1;
	
	/**
	 * Moves the cursor back one space and erases the character in that space. Will wrap from the left-most column to the 
	 * right-most column of the line above. Will wrap from the left-most column of the top row to the right-most column of the 
	 * bottom row.
	 */
	public static int BACKSPACE = 8;
	
	/**
	 * Shows a blinking block cursor with underscore at the printing location. 
	 * This cursor inverts the character rather than replacing the character with a block. 
	 * This cursor style is the default cursor at power-up.
	 */
	public static int SHOW_INVERTED_BLOCK_CURSOR = 7;
	
	/**
	 * Cursor is not shown; nothing else is changed.
	 */
	public static int HIDE_CURSOR = 4;
	
	public void hideCursor() {
	    write(HIDE_CURSOR);
	}
	
	/**
	 * Clears the display and returns cursor to Home position (upper left). All data is erased.
	 */
	public static int CLEAR_DISPLAY = 12;
	
	public void clearDisplay() {
		write(CLEAR_DISPLAY);
	}
	
	/**
	 * Deletes the character at the current cursor position. Cursor is not moved.
	 */
	public static int DELETE_IN_PLACE = 11;
	
	/**
	 * Turns wrap feature on. When wrap is on, a printable character received when the 
	 * cursor is at the right-most column will cause the cursor to move down one row to the 
	 * left-most column. If the cursor is already at the right-most column of the bottom row,
	 * it will wrap to the top row if Scroll is off, or the display will scroll up one row if Scroll is on.
	 */
	public static int WRAP_ON = 23;
	
	/**
	 * Turns wrap feature off. When wrap is off, a printable character received when the cursor is at the right-most column will
	 * cause the cursor to disappear (as it will be off the right edge of the screen) and any subsequent characters will be ignored
	 * until some other command moves the cursor back onto the display. This function is independent of Scroll.
	 */
	public static int WRAP_OFF = 24;
	
	/**
	 * Turns scroll feature on. Then a Line Feed (Control+J) command from the bottom row will scroll the display up by one
	 * row, independent of Wrap. If Wrap is also on (Control+W), a wrap occurring on the bottom row will cause the display to
	 * scroll up one row. Scroll is on at power-up.
	 */
	public static int SCROLL_ON = 19;
	
	/**
	 * Turns scroll feature off. Then a Line Feed (Control+J) command from the bottom row will move the cursor to the top row
	 * of the same column, independent of wrap (Control+W for Wrap ON, Control+X for Wrap OFF). If wrap is on, a wrap
	 * occurring on the bottom row will also wrap vertically to the top row. Scroll is on at power-up.
	 */
	public static int SCROLL_OFF = 20;
	
	/**
	 * Send "Control+Q" followed by one byte for the column (0-19), and a second byte for the row (0-3). 
	 * The upper-left position is 0,0. The lower-right position is 19,3. 
	 * For example, to move the cursor to column 11 of the second line: 
	 *   \017\010\001
	 */
	public static int SET_CURSOR_POS = 17;
		
	public void setCursorPos(int row, int col) {
		write(SET_CURSOR_POS);
		write(col);
		write(row);
	}
	
	/**
	 * Deletes all characters on a row.
	 * @param row
	 */
	public void cleanRow(int row) {
		write(SET_CURSOR_POS);
		write(19); // places the cursor on the right-most column
		write(row);
		
		write(DELETE_IN_PLACE);
		for (int i = 18; i >= 0; --i) {
			write(BACKSPACE);
		}
	}
	
	/**
	 * Places cursor at start of the given row.
	 * @param row
	 */
	public void setCursorAt(int row) {
		write(SET_CURSOR_POS);
		write(0);
		write(row);
	}
	
	public static int ROW1 = 0;
	public static int ROW2 = 1;
	public static int ROW3 = 2;
	public static int ROW4 = 3;
	
	/**
	 * Four escape sequences are supported. These correspond to the escape sequences that are sent for the four arrows
	 * keys in HyperTerminal with an ANSI terminal selected (and also our WinTest). These sequences move the cursor only
	 * and do not wrap.
	 */
	public static int ESCAPTE_SEQUENCE = 27;
	public static int UP_KEY = 65;
	public static int DOWN_KEY = 66;
	public static int RIGHT_KEY = 67;
	public static int LEFT_KEY = 68;
	
	/**
	 * Moves the cursor up.
	 */
	public void up() {
		write(ESCAPTE_SEQUENCE);
		write(91);
		write(UP_KEY);
	}
	
	/**
	 * Moves the cursor down.
	 */
	public void down() {
		write(ESCAPTE_SEQUENCE);
		write(91);
		write(DOWN_KEY);
	}
	
	/**
	 * Moves the cursor right.
	 */
	public void right() {
		write(ESCAPTE_SEQUENCE);
		write(91);
		write(RIGHT_KEY);
	}
	
	/**
	 * Moves the cursor left.
	 */
	public void left() {
		write(ESCAPTE_SEQUENCE);
		write(91);
		write(LEFT_KEY);
	}
	
	/**
	 * See the included generator table file cgrom-character-generator-rom-map.png
	 * 
	 * @param lowerBits
	 * @param upperBits
	 */
	public void writeASCIISymbol(int lowerBits, int upperBits) {
		write(30);
		write(1);
		write(lowerBits + upperBits);
	}
	
	public void init() {
		write(SHOW_INVERTED_BLOCK_CURSOR);
		write(CLEAR_DISPLAY);
		setCursorAt(ROW1);
		write("Trafokanten starting");
		setCursorAt(ROW2);
		try {
			Thread.sleep(4000);
		} catch (InterruptedException e) {}
		write(CLEAR_DISPLAY);
		write(HIDE_CURSOR);
	}
}
