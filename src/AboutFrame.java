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
 * The About frame consists of a single image which describes the browser and
 * contains the copyright information.
 */

import javax.swing.*;

public class AboutFrame extends JFrame {
    
    final static int DEFAULT_WIDTH = 350;
    final static int DEFAULT_HEIGHT = 350;
    final static String ABOUT_IMAGE = "images/about-screen.jpg";

    public AboutFrame() {
        this.setTitle("About " + Main.BROWSER_TITLE);

        // Set window behavious
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setIconImage(Main.icon);
        this.setResizable(false);

        // Add image
        JLabel pane = new JLabel(new ImageIcon(ABOUT_IMAGE));
        pane.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        this.getContentPane().add(pane);
        this.getContentPane().setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        pack();
    } // end constructor

} // end class AboutFrame