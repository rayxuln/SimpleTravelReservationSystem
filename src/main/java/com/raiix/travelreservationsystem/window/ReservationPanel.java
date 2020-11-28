package com.raiix.travelreservationsystem.window;

import com.raiix.travelreservationsystem.App;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class ReservationPanel extends JPanel {

    App app;

    private void generateGUI(){
        setLayout(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        add(splitPane, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        GridBagLayout gridBagLayout = new GridBagLayout();
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        panel.setLayout(gridBagLayout);

        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        JLabel label = new JLabel("客户ID: ");
        gridBagLayout.addLayoutComponent(label, gridBagConstraints);
        panel.add(label);

        JPanel panel1 = new JPanel();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagLayout.addLayoutComponent(panel1, gridBagConstraints);
        panel.add(panel1);
        BoxLayout boxLayout = new BoxLayout(panel1, BoxLayout.X_AXIS);
        panel1.setLayout(boxLayout);
        JTextField textField = new JTextField();
        panel1.add(textField);
        panel1.add(Box.createHorizontalStrut(15));
        panel1.add(new JLabel("无效的客户ID"));
        panel1.add(Box.createGlue());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("航班", new JScrollPane(new JTable()));
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagLayout.addLayoutComponent(tabbedPane, gridBagConstraints);
        panel.add(tabbedPane);

        splitPane.setLeftComponent(panel);

        panel = new JPanel();
        boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);
        panel.setBorder(new LineBorder(Color.BLACK));
        label = new JLabel("已预定");
        panel.add(label);
        JList list = new JList(new String[]{"航班 xxx", "xxx的大巴", "xxx的宾馆"});
        panel.add(new JScrollPane(list));

        splitPane.setRightComponent(panel);
    }

    public ReservationPanel(App _app)
    {
        app = _app;
        generateGUI();


    }
}
