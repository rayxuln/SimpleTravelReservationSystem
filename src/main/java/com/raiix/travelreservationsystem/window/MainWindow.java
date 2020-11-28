package com.raiix.travelreservationsystem.window;

import com.raiix.travelreservationsystem.App;

import javax.swing.*;
import java.awt.event.*;

public class MainWindow extends JFrame implements WindowListener {

    JPanel mainPanel;
    private JTabbedPane tabbedPane;

    App app;

    private void generateGUI(){
        mainPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        mainPanel.setLayout(boxLayout);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("数据管理", new DataManagePanel(app));
        tabbedPane.addTab("预定", new JPanel());
        tabbedPane.addTab("预定查询", new JPanel());

        mainPanel.add(tabbedPane);
        add(mainPanel);
    }

    public MainWindow(App _app) {
        setTitle("简单旅行预定系统");
        setSize(800, 600);
        app = _app;
        generateGUI();

        addWindowListener(this);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);
    }

    public void windowOpened(WindowEvent e) {

    }

    public void windowClosing(WindowEvent e) {
        int n = JOptionPane.showConfirmDialog(
                this,
                "要退出本系统吗？",
                "提示",
                JOptionPane.YES_NO_OPTION);
        if (n == 0)
            app.quit();
    }

    public void windowClosed(WindowEvent e) {
        app.quit();
    }

    public void windowIconified(WindowEvent e) {

    }

    public void windowDeiconified(WindowEvent e) {

    }

    public void windowActivated(WindowEvent e) {

    }

    public void windowDeactivated(WindowEvent e) {

    }
}
