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
 * The Tab class contains all the information about each tab and handles the
 * loading of pages.
 */

import java.awt.Color;
import javax.swing.*;
import java.io.*;
import java.net.*;
import javax.swing.border.*;

public class Tab {

    final static int MAX_HISTORY_ITEMS = 100;

    private JEditorPane htmlPane;
    private URL[]       history = new URL[MAX_HISTORY_ITEMS];
    private int         currentHistoryPos = 0;
    private URL         url;
    private int         tabID;

    public Tab() {
        // Call the parent constructor
        super();

        // Set the information for this tab
        this.tabID    = Main.getNumTabs();
        this.htmlPane = new JEditorPane();
        this.htmlPane.setEditable(false);
        this.htmlPane.addHyperlinkListener(Main.controller);
        this.htmlPane.addMouseListener(Main.controller);

        // Remove border of ScrollPane
        JScrollPane sp = new JScrollPane(this.htmlPane);
        Border border = BorderFactory.createMatteBorder(1, 0, 1, 0, Color.lightGray);
        sp.setBorder(border);

        // The name of the tab is important when getting the ID in future
        sp.setName("Tab" + this.tabID);
        Main.window.addTab("New Tab", sp);
    }

    private void getData(String url) {
        try {
            Main.statusMessage("Loading " + url + "...");
            this.htmlPane.setPage(url);
            Main.window.setTabTitle(getTitle(), Main.window.getCurrentTabSelectedID());
            Main.statusMessage("Done");
        } catch (IOException e) {
            Main.errorMessage("Cannot load page");
        }
    }
    
    
    // Public methods

    public void loadPage(String url) {
        if (url != null) {
            try {
                // This will throw an exception if the URL is not valid
                this.history[++this.currentHistoryPos] = new URL(url);
                getData(url);
            } catch (IOException e) {
                // Try adding "http://" if the URL failed initially
                try {
                    url = "http://" + url;
                    this.history[this.currentHistoryPos] = new URL(url);
                    getData(url);
                    // Add "http://" to the address bar
                    Main.window.setABText(url);
                } catch (IOException ex) {
                    Main.errorMessage("Cannot understand URL");
                }
            }
        } else {
            try {
                /**
                 * This will throw an exception and will halt loading of any
                 * data. It is what is called when the stop button is pressed
                 */
                this.htmlPane.setPage("");
            } catch(IOException e) {
                Main.statusMessage("Stopped");
            }
        }
    } // end loadPage

    public String getTitle() {
        return this.history[this.currentHistoryPos].getHost();
    }

    public void back() {
        if (this.currentHistoryPos != 1) {
            this.currentHistoryPos--;
            String urlString = this.history[this.currentHistoryPos].toString();
            getData(urlString);
            Main.window.setABText(urlString);
        } else {
            Main.errorMessage("Cannot go back any further");
        }
    }

    public void forwards() {
        try {
            // Generate exception if we can't go forwards any further
            String temp = this.history[this.currentHistoryPos+1].toString();

            this.currentHistoryPos++;
            String urlString = this.history[this.currentHistoryPos].toString();
            getData(urlString);
            Main.window.setABText(urlString);
        } catch (Exception e) {
            Main.errorMessage("Cannot go forwards any further");
        }
    }

    public void close() {
        this.htmlPane = null;
        this.history = null;
        this.url = null;
    }

    public void clearHistory() {
        this.history = null;
        this.currentHistoryPos = 0;
    }

} // end class Tab