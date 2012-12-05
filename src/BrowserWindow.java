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
 * BrowserWindow is the View for the browser.  It creates all the components
 * and manages any changes made to what the user can see.
 */

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class BrowserWindow extends JFrame {

    final static int    DEFAULT_WIDTH       = 850;
    final static int    DEFAULT_HEIGHT      = 600;
    final static String BACKGROUND_IMAGE    = "images/background.png";

    private ImageIcon     bgImage     = null;
    private JLabel        bg          = null;
    private JPanel        navPanel    = null;
    private JTextField    addressBar  = null;
    private JTabbedPane   displayPane = null;
    private JPopupMenu    popupMenu   = null;
    private JButton       btnBack, btnForwards, btnRefresh, btnStop,
                          btnHome, btnFavourite, btnGo, btnNewTab = null;

    // Public components
    public StatusBar statusBar = null;
    public JMenuBar  menuBar   = null;

    public BrowserWindow() {
        
        this.setTitle(Main.BROWSER_TITLE);
        this.setName(Main.BROWSER_TITLE);

        // Set window behavious
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.getContentPane().setLayout(new BorderLayout());

        // Set application icon
        this.setIconImage(Main.icon);
        
        // Set up components
        createMenuBar();
        createNavPanel();
        createAddressBar();
        createDisplayPane();
        createStatusBar();
        createLeftNavButtons();
        createRightNavButtons();
        createPopupMenu();

        // Pack UI so that the contentPane dimensions are set.
        this.pack();
        setBackgroundImage();

        this.addComponentListener(Main.controller);

        // Set the default size of the browser
        this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

        // Set focus to the address bar
        setABFocus();
        
    } // end constructor

    private void createMenuBar() {
        JMenu menu;
        JMenuItem menuItem;
        JCheckBoxMenuItem cbMenuItem;

        //Create the objects
        menuBar = new JMenuBar();

        // Build the file menu
        menu = new JMenu("File");
        menuBar.add(menu);

        // Add items to file menu
        menuItem = new JMenuItem("New Tab", new ImageIcon("images/new-tab-menu.png"));
        // Keyboard shortcut
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("tab");
        menuItem.addActionListener(Main.controller);
        menu.add(menuItem);

        menuItem = new JMenuItem("Close Tab", new ImageIcon("images/close-tab-menu.png"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("cltab");
        menuItem.addActionListener(Main.controller);
        menu.add(menuItem);

        menu.addSeparator();

        menuItem = new JMenuItem("Exit", new ImageIcon("images/close.png"));
        menuItem.setActionCommand("exit");
        menuItem.addActionListener(Main.controller);
        menu.add(menuItem);

        // Build edit menu
        menu = new JMenu("Edit");
        menuBar.add(menu);

        // Add items to edit menu
        menuItem = new JMenuItem("Cut", new ImageIcon("images/cut.png"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("cut");
        menuItem.addActionListener(Main.controller);
        menu.add(menuItem);

        menuItem = new JMenuItem("Copy", new ImageIcon("images/copy.png"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("copy");
        menuItem.addActionListener(Main.controller);
        menu.add(menuItem);

        menuItem = new JMenuItem("Paste", new ImageIcon("images/paste.png"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("paste");
        menuItem.addActionListener(Main.controller);
        menu.add(menuItem);

        // Build view menu
        menu = new JMenu("View");
        menuBar.add(menu);

        // Add items to view menu
        cbMenuItem = new JCheckBoxMenuItem("Menu Bar");
        cbMenuItem.setSelected(true);
        cbMenuItem.setActionCommand("menubar");
        cbMenuItem.addActionListener(Main.controller);
        menu.add(cbMenuItem);

        cbMenuItem = new JCheckBoxMenuItem("Status Bar");
        cbMenuItem.setSelected(true);
        cbMenuItem.setActionCommand("stbar");
        cbMenuItem.addActionListener(Main.controller);
        menu.add(cbMenuItem);

        menu.addSeparator();

        menuItem = new JMenuItem("Stop", new ImageIcon("images/stop-menu.png"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_ESCAPE, 0));
        menuItem.setActionCommand("stop");
        menuItem.addActionListener(Main.controller);
        menu.add(menuItem);

        menuItem = new JMenuItem("Reload", new ImageIcon("images/refresh-menu.png"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_R, ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("refresh");
        menuItem.addActionListener(Main.controller);
        menu.add(menuItem);

        // Build history menu
        menu = new JMenu("History");
        menuBar.add(menu);

        // Add items to history menu
        menuItem = new JMenuItem("Back", new ImageIcon("images/back-menu.png"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_LEFT, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("back");
        menuItem.addActionListener(Main.controller);
        menu.add(menuItem);

        menuItem = new JMenuItem("Forward", new ImageIcon("images/forwards-menu.png"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_RIGHT, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("forwards");
        menuItem.addActionListener(Main.controller);
        menu.add(menuItem);

        menu.addSeparator();

        menuItem = new JMenuItem("Home", new ImageIcon("images/home-menu.png"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_HOME, ActionEvent.ALT_MASK));
        menuItem.setActionCommand("home");
        menuItem.addActionListener(Main.controller);
        menu.add(menuItem);

        // Build bookmarks menu
        menu = new JMenu("Bookmarks");
        menuBar.add(menu);

        // Add items to history menu
        menuItem = new JMenuItem("Bookmark this page", new ImageIcon("images/favourite-menu.png"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_D, ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("fav");
        menuItem.addActionListener(Main.controller);
        menu.add(menuItem);

        // Build tools menu
        menu = new JMenu("Tools");
        menuBar.add(menu);

        // Add items to tools menu
        menuItem = new JMenuItem("Options...", new ImageIcon("images/options.png"));
        menuItem.setActionCommand("options");
        menuItem.addActionListener(Main.controller);
        menu.add(menuItem);

        // Build help menu
        menu = new JMenu("Help");
        menuBar.add(menu);

        // Add items to help menu
        menuItem = new JMenuItem("About WinterWolf", new ImageIcon("images/logo-menu.png"));
        menuItem.setActionCommand("about");
        menuItem.addActionListener(Main.controller);
        menu.add(menuItem);

        menuBar.setBackground(Color.white);
        menuBar.setBorderPainted(false);
        menuBar.setOpaque(false);
        
        this.setJMenuBar(menuBar);
    } // end createMenuBar

    private void createNavPanel() {
        navPanel = new JPanel();
        navPanel.setLayout(new BorderLayout(10, 0));
        navPanel.setOpaque(false);
        this.getContentPane().add(navPanel, BorderLayout.NORTH);
    }

    private void createAddressBar() {
        addressBar = new JTextField();
        // Set up action listener
        addressBar.setActionCommand("ab");
        addressBar.addActionListener(Main.controller);

        // Add padding for aesthetic purposes
        addressBar.setMargin(new Insets(0, 10, 0, 10));
        addressBar.setOpaque(false);
        
        navPanel.add(addressBar, BorderLayout.CENTER);
    }

    private void createDisplayPane() {
        displayPane = new JTabbedPane();
        displayPane.setOpaque(false);
        // Do not paint borders
        displayPane.setUI(new CustomTabbedPaneUI());

        this.getContentPane().add(displayPane, BorderLayout.CENTER);
    }

    private void createStatusBar() {
        statusBar = new StatusBar();
        this.getContentPane().add(statusBar, java.awt.BorderLayout.SOUTH);
    }

    private void createLeftNavButtons() {
        JPanel container = new JPanel();
        container.setLayout(new GridLayout(1, 5));

        btnBack     = new JButton();
        btnForwards = new JButton();
        btnRefresh  = new JButton();
        btnHome     = new JButton();
        btnNewTab   = new JButton();

        // Load icons
        Icon backIcon       = new ImageIcon("images/backwards.png");
        Icon forwardsIcon   = new ImageIcon("images/forwards.png");
        Icon refreshIcon    = new ImageIcon("images/refresh.png");
        Icon homeIcon       = new ImageIcon("images/home.png");
        Icon newTabIcon     = new ImageIcon("images/new-tab.png");

        // Set icons
        btnBack.setIcon(backIcon);
        btnForwards.setIcon(forwardsIcon);
        btnRefresh.setIcon(refreshIcon);
        btnHome.setIcon(homeIcon);
        btnNewTab.setIcon(newTabIcon);

        // Remove borders
        btnBack.setBorderPainted(false);
        btnForwards.setBorderPainted(false);
        btnRefresh.setBorderPainted(false);
        btnHome.setBorderPainted(false);
        btnNewTab.setBorderPainted(false);

        // Set background to white
        btnBack.setBackground(Color.white);
        btnForwards.setBackground(Color.white);
        btnRefresh.setBackground(Color.white);
        btnHome.setBackground(Color.white);
        btnNewTab.setBackground(Color.white);

        // Set action commands
        btnBack.setActionCommand("back");
        btnForwards.setActionCommand("forwards");
        btnRefresh.setActionCommand("refresh");
        btnHome.setActionCommand("home");
        btnNewTab.setActionCommand("tab");

        // Add action listeners
        btnBack.addActionListener(Main.controller);
        btnForwards.addActionListener(Main.controller);
        btnRefresh.addActionListener(Main.controller);
        btnHome.addActionListener(Main.controller);
        btnNewTab.addActionListener(Main.controller);

        // Set to transparent
        btnBack.setOpaque(false);
        btnForwards.setOpaque(false);
        btnRefresh.setOpaque(false);
        btnHome.setOpaque(false);
        btnNewTab.setOpaque(false);
        container.setOpaque(false);

        container.add(btnBack);
        container.add(btnForwards);
        container.add(btnRefresh);
        container.add(btnHome);
        container.add(btnNewTab);

        navPanel.add(container, BorderLayout.LINE_START);
    } // end createLeftNavButtons

    private void createRightNavButtons() {
        JPanel container = new JPanel();
        container.setLayout(new GridLayout(1, 3));

        btnGo        = new JButton();
        btnFavourite = new JButton();
        btnStop      = new JButton();

        // Load icons
        Icon goIcon        = new ImageIcon("images/go.png");
        Icon favouriteIcon = new ImageIcon("images/favourite.png");
        Icon stopIcon      = new ImageIcon("images/stop.png");

        // Set icons
        btnGo.setIcon(goIcon);
        btnFavourite.setIcon(favouriteIcon);
        btnStop.setIcon(stopIcon);

        // Remove borders
        btnGo.setBorderPainted(false);
        btnFavourite.setBorderPainted(false);
        btnStop.setBorderPainted(false);

        // Set background to white
        btnGo.setBackground(Color.white);
        btnFavourite.setBackground(Color.white);
        btnStop.setBackground(Color.white);

        // Set action commands
        btnGo.setActionCommand("go");
        btnFavourite.setActionCommand("fav");
        btnStop.setActionCommand("stop");

        // Add action listeners
        btnGo.addActionListener(Main.controller);
        btnFavourite.addActionListener(Main.controller);
        btnStop.addActionListener(Main.controller);

        // Set to transparent
        btnGo.setOpaque(false);
        btnFavourite.setOpaque(false);
        btnStop.setOpaque(false);
        container.setOpaque(false);

        container.add(btnGo);
        container.add(btnFavourite);
        container.add(btnStop);

        navPanel.add(container, BorderLayout.LINE_END);
    } // end createRightNavButtons

    private void createPopupMenu() {
        popupMenu = new JPopupMenu();
        JMenuItem menuItem;
        JCheckBoxMenuItem cbMenuItem;

        // Build menu items
        menuItem = new JMenuItem("New Tab", new ImageIcon("images/new-tab-menu.png"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_T, ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("tab");
        menuItem.addActionListener(Main.controller);
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Close Tab", new ImageIcon("images/close-tab-menu.png"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_W, ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("cltab");
        menuItem.addActionListener(Main.controller);
        popupMenu.add(menuItem);

        popupMenu.addSeparator();

        menuItem = new JMenuItem("Cut", new ImageIcon("images/cut.png"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_X, ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("cut");
        menuItem.addActionListener(Main.controller);
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Copy", new ImageIcon("images/copy.png"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_C, ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("copy");
        menuItem.addActionListener(Main.controller);
        popupMenu.add(menuItem);

        menuItem = new JMenuItem("Paste", new ImageIcon("images/paste.png"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_V, ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("paste");
        menuItem.addActionListener(Main.controller);
        popupMenu.add(menuItem);

        popupMenu.addSeparator();

        cbMenuItem = new JCheckBoxMenuItem("Menu Bar");
        cbMenuItem.setSelected(true);
        cbMenuItem.setActionCommand("menubar");
        cbMenuItem.addActionListener(Main.controller);
        popupMenu.add(cbMenuItem);

        cbMenuItem = new JCheckBoxMenuItem("Status Bar");
        cbMenuItem.setSelected(true);
        cbMenuItem.setActionCommand("stbar");
        cbMenuItem.addActionListener(Main.controller);
        popupMenu.add(cbMenuItem);

        popupMenu.addSeparator();

        menuItem = new JMenuItem("Bookmark this page", new ImageIcon("images/favourite-menu.png"));
        menuItem.setAccelerator(KeyStroke.getKeyStroke(
            KeyEvent.VK_D, ActionEvent.CTRL_MASK));
        menuItem.setActionCommand("fav");
        menuItem.addActionListener(Main.controller);
        popupMenu.add(menuItem);

        //Add listener to components that can bring up popup menus.
        menuBar.addMouseListener(Main.controller);
        addressBar.addMouseListener(Main.controller);
        displayPane.addMouseListener(Main.controller);
    } // end createPopupMenu

    private void setBackgroundImage() {
        ((JPanel)getContentPane()).setOpaque(false);

        bgImage = new ImageIcon(BACKGROUND_IMAGE);
        bg = new JLabel(bgImage);
        this.getLayeredPane().add(bg, new Integer(Integer.MIN_VALUE));
    } // end setBackgroundImage

    private void setABFocus() {
        this.addressBar.requestFocus();
    }


    // Public methods

    public String getABText() {
        return this.addressBar.getText();
    }

    public void setABText(String text) {
        this.addressBar.setText(text);
    }

    public void addTab(String title, JScrollPane pane) {
        this.displayPane.addTab(title, pane);
        // Focus new tab
        this.displayPane.setSelectedComponent(pane);
    }

    public void closeTab(int tabID) {
        this.displayPane.remove(tabID);
    }

    public int getCurrentTabSelectedID() {
        // Returns the ID of the selected tab
        return this.displayPane.getSelectedIndex();
    }
    public int getCurrentTabID() {
        /**
         * Returns the instance tab ID as defined in the Main class by grabbing
         * it from the Name variable
         */
        String name = this.displayPane.getSelectedComponent().getName();
        return (int)Integer.parseInt(name.split("Tab")[1]);
    }

    public void setTabTitle(String title, int index) {
        this.displayPane.setTitleAt(index, title);
        this.setTitle(title + " - " + Main.BROWSER_TITLE);
    }

    public String getTabTitle(int index) {
        try {
            return this.displayPane.getTitleAt(index);
        } catch (Exception e) {
            // If no tabs are open
            return null;
        }
    }

    public boolean isMenuBarVisible() {
        return this.menuBar.isVisible();
    }

    public void setMenuBarVisibility(boolean visibility) {
        this.menuBar.setVisible(visibility);
    }

    public boolean isStatusBarVisible() {
        return this.statusBar.isVisible();
    }

    public void setStatusBarVisibility(boolean visibility) {
        this.statusBar.setVisible(visibility);
    }

    public void updateBGImageLocation() {
        // Move the background image back to the top right of the frame
        int left = this.getContentPane().getWidth() - bgImage.getIconWidth();
        bg.setBounds(left, 0, bgImage.getIconWidth(), bgImage.getIconHeight());
    }

    public void showPopup(Component c, int x, int y) {
        // Show the context menu at the mouse location
        popupMenu.show(c, x, y);
    }

} // and class WWWindow