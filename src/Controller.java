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
 * The Controller handles all the events generated in the program.
 */

import java.awt.event.*;
import javax.swing.event.*;
import javax.swing.text.html.*;
import javax.swing.*;

public class Controller extends MouseAdapter implements ActionListener,
                                    HyperlinkListener, ComponentListener {

    // Action Listener

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("ab")) {
            // User presses return from address bar
            Main.changePage(Main.window.getABText());

            // Navigation
        } else if (e.getActionCommand().equals("back")) {
            Main.tab[Main.window.getCurrentTabID()].back();
        } else if (e.getActionCommand().equals("forwards")) {
            Main.tab[Main.window.getCurrentTabID()].forwards();
        } else if (e.getActionCommand().equals("refresh")) {
            Main.changePage(Main.window.getABText());
        } else if (e.getActionCommand().equals("home")) {
            Main.changePage(Main.homepage);
        } else if (e.getActionCommand().equals("go")) {
            Main.changePage(Main.window.getABText());
        } else if (e.getActionCommand().equals("stop")) {
            Main.stop();

            // Tabs
        } else if (e.getActionCommand().equals("tab")) {
            Main.openTab();
        } else if (e.getActionCommand().equals("cltab")) {
            Main.closeTab();

            // Bookmarks
        } else if (e.getActionCommand().equals("fav")) {
            Main.openBookmarksFrame();

            // Editing
        } else if (e.getActionCommand().equals("cut")) {
            Main.ccp('x');
        } else if (e.getActionCommand().equals("copy")) {
            Main.ccp('c');
        } else if (e.getActionCommand().equals("paste")) {
            Main.ccp('v');
            
            // Misc
        } else if (e.getActionCommand().equals("menubar")) {
            Main.setMenuBarVisibility(!Main.window.isMenuBarVisible());
        } else if (e.getActionCommand().equals("stbar")) {
            Main.setStatusBarVisibility(!Main.window.isStatusBarVisible());
        } else if (e.getActionCommand().equals("options")) {
            Main.openOptionsFrame();
        } else if (e.getActionCommand().equals("about")) {
            Main.openAboutFrame();
        } else if (e.getActionCommand().equals("exit")) {
            Main.window.dispose();
            System.exit(0);

            // Options Frame
        } else if (e.getActionCommand().equals("opHist")) {
            Main.of.clearHistory();
        } else if (e.getActionCommand().equals("opDone")) {
            Main.of.done();
        } else if (e.getActionCommand().equals("opCancel")) {
            Main.of.cancel();

            // Bookmarks Frame
        } else if (e.getActionCommand().equals("bmCreate")) {
            Main.bf.newBookmark();
        }

        System.out.println("Event trigger: " + e.getActionCommand());
    } // end actionPerformed


    // Hyperlink Listener

    public void hyperlinkUpdate(HyperlinkEvent e) {
        if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
            JEditorPane pane = (JEditorPane)e.getSource();
            if (e instanceof HTMLFrameHyperlinkEvent) {
                HTMLFrameHyperlinkEvent evt = (HTMLFrameHyperlinkEvent)e;
		HTMLDocument doc = (HTMLDocument)pane.getDocument();
		doc.processHTMLFrameHyperlinkEvent(evt);
            } else {
                try {
                    Main.changePage(e.getURL().toString());
		} catch (Throwable t) {
                    Main.errorMessage("Unable to move to " + e.getURL());
	        }
            }
        }
    } // end hyperlinkUpdate


    // ComponentListener

    public void componentResized(ComponentEvent e) {
        if (e.getComponent().getName().equals(Main.BROWSER_TITLE)) {
            // Move the background image to the top right of the frame again.
            Main.window.updateBGImageLocation();
        }
    }

    public void componentHidden(ComponentEvent e) {

    }

    public void componentShown(ComponentEvent e) {
        if (e.getComponent().getName().equals("Bookmarks")) {
            // Update the URL and Title for a suggested bookmark.
            Main.bf.updateText();
        }
    }

    public void componentMoved(ComponentEvent e) {

    }


    // MouseAdapter

    @Override
    public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);

        try {
            if (e.getComponent().getName().equals("bmTable")) {
                /**
                 * If the user clicks a cell in the bookmarks table, this attempts
                 * to load the associated URL in the currently selected tab.
                 */
                Main.bf.tableClick();
            }
        } catch (Exception ex) {
            // Component does not have a name
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            Main.window.showPopup(e.getComponent(), e.getX(), e.getY());
        }
    }

} // end class Controller

