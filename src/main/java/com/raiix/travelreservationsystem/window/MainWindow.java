package com.raiix.travelreservationsystem.window;

import com.raiix.travelreservationsystem.App;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainWindow extends JFrame {
    App app;

    private void generateGUI(){
        JPanel mainPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(mainPanel, BoxLayout.Y_AXIS);
        mainPanel.setLayout(boxLayout);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("数据管理", new DataManagePanel(app));
        tabbedPane.addTab("预定", new ReservationPanel(app));
        tabbedPane.addTab("预定查询", new JPanel());

        mainPanel.add(tabbedPane);
        add(mainPanel);
    }

    public MainWindow(App _app) {
        setTitle("简单旅行预定系统");
        setSize(800, 600);
        app = _app;
        generateGUI();

        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        if(e.getID() == WindowEvent.WINDOW_CLOSING)
        {
            int n = JOptionPane.showConfirmDialog(
                    this,
                    "要退出本系统吗？",
                    "提示",
                    JOptionPane.YES_NO_OPTION);
            if (n != 0)
                return;
        }
        super.processWindowEvent(e);
    }
}
