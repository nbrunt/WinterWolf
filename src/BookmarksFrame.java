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
 * The Bookmarks frame displays a table of all the current bookmarks and offers
 * the user the facilities to add more bookmarks.
 */

import java.awt.*;
import javax.swing.*;
import java.net.*;
import java.io.*;

public class BookmarksFrame extends JFrame {

    final static int DEFAULT_WIDTH = 450;
    final static int DEFAULT_HEIGHT = 200;
    final static int PADDING = 5;
    final static int MAX_BOOKMARKS = 500;
    final static String BOOKMARKS_ICON = "images/favourite-menu.png";

    JPanel     leftContent, rightContent = null;
    JTextField tfUrl, tfTitle = null;
    JTable     bookmarksTable = null;

    // Table data
    String[]   columns        = {"Title", "URL"};
    Object[][] bookmarks      = new Object[MAX_BOOKMARKS][2];
    int        numBookmarks   = 0;


    public BookmarksFrame() {
        this.setTitle("Bookmarks");

        // Set window behavious
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setIconImage(Main.icon);
        this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.setResizable(false);
        this.getContentPane().setLayout(new GridLayout(1,2));
        this.setName("Bookmarks");
        this.addComponentListener(Main.controller);

        // Add items to form
        createLeftPanel();
        createURLBox();
        createTitleBox();
        createButton();

        createRightPanel();
        createTitle();
        createTable();

        this.add(leftContent);
        this.add(rightContent);

    } // end constructor

    private void createLeftPanel() {
        leftContent = new JPanel();
        leftContent.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        leftContent.setLayout(new BoxLayout(leftContent, BoxLayout.PAGE_AXIS));
    }

    private void createURLBox() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("URL"));
        tfUrl = new JTextField(Main.window.getABText());
        tfUrl.setColumns(14);

        panel.setLayout(new FlowLayout());
        panel.add(tfUrl);
        leftContent.add(panel);
    }

    private void createTitleBox() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Title"));
        tfTitle = new JTextField();
        tfTitle.setColumns(14);

        panel.setLayout(new FlowLayout());
        panel.add(tfTitle);
        leftContent.add(panel);
    }

    private void createButton() {
        JPanel panel = new JPanel();
        JButton btnBookmark = new JButton("Create Bookmark");
        btnBookmark.setActionCommand("bmCreate");
        btnBookmark.addActionListener(Main.controller);

        panel.setLayout(new FlowLayout());
        panel.add(btnBookmark);
        leftContent.add(panel);
    }

    private void createRightPanel() {
        rightContent = new JPanel();
        rightContent.setLayout(new BoxLayout(rightContent, BoxLayout.PAGE_AXIS));
    }

    private void createTitle() {
        JPanel panel = new JPanel();
        JLabel lblIcon1 = new JLabel(new ImageIcon(BOOKMARKS_ICON));
        JLabel lblIcon2 = new JLabel(new ImageIcon(BOOKMARKS_ICON));
        JLabel lblTitle = new JLabel("Bookmarks");
        lblTitle.setFont(new Font("Verdana", Font.BOLD, 14));

        panel.setLayout(new FlowLayout());
        panel.add(lblIcon1);
        panel.add(lblTitle);
        panel.add(lblIcon2);
        rightContent.add(panel);
    }

    private void createTable() {
        bookmarksTable = new JTable(bookmarks, columns);
        bookmarksTable.setFillsViewportHeight(true);
        bookmarksTable.setRowSelectionAllowed(true);
        bookmarksTable.setColumnSelectionAllowed(false);
        bookmarksTable.setColumnSelectionAllowed(false);
        
        bookmarksTable.setName("bmTable");
        bookmarksTable.addMouseListener(Main.controller);
        rightContent.add(new JScrollPane(bookmarksTable));
    }


    // Public methods

    public void updateText() {
        /**
         * This is called whenever the Bookmarks frame is "shown" so that the
         * data is always up to date.
         */
        tfUrl.setText(Main.window.getABText());
        tfTitle.setText(Main.window.getTabTitle(Main.window.getCurrentTabSelectedID()));
    }

    public void updateTable() {
        // Re-load the data from the arrays into the table
        bookmarksTable.updateUI();
    }

    public void newBookmark() {
        try {
            // Check that URL is valid - throws IOExceptio if not
            URL temp = new URL(tfUrl.getText());

            // Set title
            bookmarks[numBookmarks][0] = tfTitle.getText();
            // Set Url
            bookmarks[numBookmarks][1] = tfUrl.getText();
            numBookmarks++;

            updateTable();
        } catch (IOException e) {
            Main.errorMessage("Invalid URL - cannot create bookmark");
        }
    }

    public void tableClick() {
        try {
            // If a cell is clicked, load the associated url
            String url = bookmarks[bookmarksTable.getSelectedRow()][1].toString();
            Main.changePage(url);
        } catch (Exception e) {
            // Blank cell was selected
        }
    }

} // end class AboutFrame