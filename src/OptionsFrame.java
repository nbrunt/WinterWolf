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
 * The Options frame displays all the things that the user can modify.
 */

import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.URL;

public class OptionsFrame extends JFrame {

    final static int DEFAULT_WIDTH = 260;
    final static int DEFAULT_HEIGHT = 220;
    final static int DEFAULT_PANEL_WIDTH = 300;
    final static String HOMEPAGE_ICON = "images/home-menu.png";
    // The frame whitespace border
    final static int PADDING = 5;

    public  JTextField tfHomepage   = null;
    private JPanel     content      = null;

    /*
     * This flag is set to true when the "Clear History" button is pressed.
     * When "Done" this is queried and then the history is either deleted
     * or not.
     */
    public boolean clearHistory = false;

    public OptionsFrame() {
        this.setTitle("Options");

        // Set window behavious
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setIconImage(Main.icon);
        this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        this.setResizable(false);

        // Add items to form
        createContentPanel();
        createHomepageSection();
        createHistorySection();
        createButtonsSection();
        
        this.add(content);

    } // end constructor

    private void createContentPanel() {
        content = new JPanel();
        // This gives padding to the frame so that the boxes aren't right up to the edge
        content.setBorder(BorderFactory.createEmptyBorder(PADDING, PADDING, PADDING, PADDING));
        content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
    }

    private void createHomepageSection() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Set the homepage"));
        tfHomepage = new JTextField(Main.homepage);
        tfHomepage.setColumns(15);
        JLabel lblIcon = new JLabel(new ImageIcon(HOMEPAGE_ICON));

        panel.setLayout(new FlowLayout());
        panel.add(lblIcon);
        panel.add(tfHomepage);
        content.add(panel);
    }

    private void createHistorySection() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Browsing history"));
        JButton btnHistory = new JButton("Clear History");

        btnHistory.setActionCommand("opHist");
        btnHistory.addActionListener(Main.controller);

        panel.add(btnHistory);
        content.add(panel);
    }

    private void createButtonsSection() {
        JPanel panel = new JPanel();
        JButton btnDone = new JButton("Done");
        JButton btnCancel = new JButton("Cancel");

        btnDone.setActionCommand("opDone");
        btnCancel.setActionCommand("opCancel");

        btnDone.addActionListener(Main.controller);
        btnCancel.addActionListener(Main.controller);

        panel.add(btnDone);
        panel.add(btnCancel);
        content.add(panel);
    }


    // Public Methods

    public void clearHistory() {
        clearHistory = true;
        JOptionPane.showMessageDialog(this,
                "Your current browsing history will be deleted once you click \"Done\"",
                "Clear Browsing History", JOptionPane.INFORMATION_MESSAGE);
    }

    public void cancel() {
        // Clear all changes
        clearHistory = false;
        tfHomepage.setText(Main.homepage);

        this.setVisible(false);
    }

    public void done() {
        try {
            // Check that the given homepage is valid
            URL testUrl = new URL(tfHomepage.getText());

            // Change homepage
            Main.homepage = testUrl.toString();
        } catch (IOException ex) {
            Main.errorMessage("Invalid URL - cannot change homepage");
            // Reset homepage url
            tfHomepage.setText(Main.homepage);
        }

        // Remove history
        Main.clearHistory();

        this.setVisible(false);
    } // end done

} // end class AboutFrame