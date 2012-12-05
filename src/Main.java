/**
 * 
 * Copyright (C) 2010 Nick Brunt (nickbrunt.com)
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * 
 * @author Nick Brunt
 *
 * This is the Model class which controls all aspects of the program.
 */

import java.io.*;
import javax.imageio.*;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class Main {

    // Constants
    public  final static String BROWSER_TITLE = "WinterWolf";
    public  final static String ICON_FILENAME = "images/icon.png";
    private final static int    MAX_TABS      = 100;

    // Handle events
    public static Controller controller = new Controller();

    // Display windows
    public static BrowserWindow  window = null;
    public static AboutFrame     af     = null;
    public static OptionsFrame   of     = null;
    public static BookmarksFrame bf     = null;

    // Tabs
    public static Tab[] tab = new Tab[MAX_TABS];
    private static int numTabs = 0;

    // Misc. global variables
    public static BufferedImage icon     = null;
    public static String        homepage = "http://nickbrunt.com/google3.2";

    public static void main(String[] args) {

        // Load application icon
        loadIcon(Main.ICON_FILENAME);

        // Set up window layouts
        window = new BrowserWindow();
        af     = new AboutFrame();
        of     = new OptionsFrame();
        bf     = new BookmarksFrame();

        // Make main window visible
        window.setVisible(true);

        // Tell browser to use the system proxy settings
        System.setProperty("java.net.useSystemProxies", "true");

        // Load the home page
        openTab();
        changePage(homepage);

    } // end main

    public static void loadIcon(String filename) {
        try {
            icon = ImageIO.read(new File(filename));
        } catch (IOException e) {
            System.err.println("Could not load icon.");
        }
    }

    public static void changePage(String url) {
        /**
         * Set the address bar text in the main browser window and tell
         * the current tab to load the page.
         */
        window.setABText(url);
        tab[window.getCurrentTabID()].loadPage(url);
    }

    public static void statusMessage(String message) {
        // Set status bar message
        window.statusBar.setMessage(message);
    }

    public static void errorMessage(String message) {
        // Set status bar message and display a popup
        window.statusBar.setMessage(message);
        JOptionPane.showMessageDialog(window, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void openTab() {
        tab[numTabs] = new Tab();
        numTabs++;
    }

    public static void closeTab() {
        try {
            /**
             * Get the ID if the selected tab in the main window. Also get the
             * ID of the corresponding instance of the Tab class. These may be
             * different if several tabs have been opened and closed.
             */
            int currentTab = window.getCurrentTabID();
            tab[currentTab].close();
            window.closeTab(window.getCurrentTabSelectedID());
            tab[currentTab] = null;
        } catch (Exception e) {
            statusMessage("Could not close tab");
        }
    } // end closeTab

    public static void stop() {
        // Load a null page (has the effect of stopping any loading)
        tab[window.getCurrentTabID()].loadPage(null);
    }

    public static int getNumTabs() {
        return numTabs;
    }

    public static void setMenuBarVisibility(boolean visibility) {
        window.setMenuBarVisibility(visibility);
    }

    public static void setStatusBarVisibility(boolean visibility) {
        window.setStatusBarVisibility(visibility);
    }

    public static void ccp(char action) {
        try {
            /**
             * I spent a lot of time using MouseListeners to detect where the
             * cursor was (address bar or HTML pane) so that I knew where to
             * cut, copy or paste the selected text.  However, there were many
             * issues and if the cursor was in a text box within a web page,
             * it could not be detected at all.
             *
             * I decided a far simpler solution would be to simply generate the
             * keyboard shortcuts and let the operating system handle this
             * itself.
             */
            Robot robot = new Robot();

            switch (action) {
                case 'x': // Cut
                    robot.keyPress(KeyEvent.VK_CONTROL);
                    robot.keyPress(KeyEvent.VK_X);
                    break;
                case 'c': // Copy
                    robot.keyPress(KeyEvent.VK_CONTROL);
                    robot.keyPress(KeyEvent.VK_C);
                    break;
                case 'v': //Paste
                    robot.keyPress(KeyEvent.VK_CONTROL);
                    robot.keyPress(KeyEvent.VK_V);
                    break;
                default:
                    break;
            }
        } catch (AWTException e) {
            errorMessage("Could not cut, copy or paste");
        }
    } // end ccp

    public static void openAboutFrame() {
        af.setVisible(true);
    }

    public static void openOptionsFrame() {
        of.setVisible(true);
    }

    public static void openBookmarksFrame() {
        bf.setVisible(true);
    }

    public static void clearHistory() {
        for (int i = 0; i < numTabs; i++) {
            tab[i].clearHistory();
        }
    }

} // end class Main