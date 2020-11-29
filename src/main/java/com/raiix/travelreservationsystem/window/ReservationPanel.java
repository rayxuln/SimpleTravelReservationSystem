package com.raiix.travelreservationsystem.window;

import com.raiix.travelreservationsystem.App;
import com.raiix.travelreservationsystem.model.*;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class ReservationPanel extends JPanel {

    JTextField customerIDTextField;
    JLabel customerNameLabel;
    JTable flightsDataTable;
    JTable busDataTable;
    JTable hotelsDataTable;

    JList reservationList;

    JTable currentTable;

    App app;

    public void update(){
        customerIDTextField.setText("");
        ((BasicTableModel)flightsDataTable.getModel()).refresh();
        ((BasicTableModel)busDataTable.getModel()).refresh();
        ((BasicTableModel)hotelsDataTable.getModel()).refresh();
    }

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
        customerIDTextField = new JTextField();
        panel1.add(customerIDTextField);
        panel1.add(Box.createHorizontalStrut(15));
        customerNameLabel = new JLabel("");
        panel1.add(customerNameLabel);
        panel1.add(Box.createGlue());

        //添加表格
        JTabbedPane tabbedPane = new JTabbedPane();

        flightsDataTable = new JTable(new FlightTableModel(app, false));
        flightsDataTable.setAutoCreateRowSorter(true);
        flightsDataTable.setDragEnabled(false);
        tabbedPane.addTab("航班", new JScrollPane(flightsDataTable));

        busDataTable = new JTable(new BusTableModel(app, false));
        busDataTable.setAutoCreateRowSorter(true);
        busDataTable.setDragEnabled(false);
        tabbedPane.addTab("大巴", new JScrollPane(busDataTable));

        hotelsDataTable = new JTable(new HotelsTableModel(app, false));
        busDataTable.setAutoCreateRowSorter(true);
        busDataTable.setDragEnabled(false);
        tabbedPane.addTab("宾馆", new JScrollPane(hotelsDataTable));

        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                update();
            }
        });

        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        gridBagLayout.addLayoutComponent(tabbedPane, gridBagConstraints);
        panel.add(tabbedPane);

        splitPane.setLeftComponent(panel);

        //添加预定列表
        panel = new JPanel();
        boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);
        panel.setBorder(new LineBorder(Color.BLACK));
        label = new JLabel("已预定");
        panel.add(label);
        reservationList = new JList(app.reservationListModel);
        panel.add(new JScrollPane(reservationList));

        splitPane.setRightComponent(panel);
    }

    public ReservationPanel(App _app) {
        app = _app;
        generateGUI();

        customerIDTextField.getDocument().addDocumentListener(new DocumentListener() {
            private void update(){
                String id_str = customerIDTextField.getText();
                int id = -1;
                try{
                    id = Integer.parseInt(id_str);
                }catch (NumberFormatException ignore) { }

                String name = app.customersTableModel.getName(id);
                if(name == null)
                {
                    customerNameLabel.setText("无效的客户ID");
                    customerNameLabel.setForeground(Color.red);
                }else
                {
                    customerNameLabel.setText(name);
                    customerNameLabel.setForeground(Color.BLACK);
                }

                ReservationListModel reservationListModel = (ReservationListModel) reservationList.getModel();
                reservationListModel.setCustomerID(id);
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
            }
        });

        final JPopupMenu bookPopupMenu = new JPopupMenu("reservation");
        JMenuItem menuItem = new JMenuItem("预定");
        menuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                BasicTableModel m = (BasicTableModel) currentTable.getModel();
                int row = currentTable.getSelectedRow();
                int type = 0;
                if(m instanceof FlightTableModel)
                {
                    type = ReservationListModel.FLIGHT;
                }else if(m instanceof BusTableModel)
                {
                    type = ReservationListModel.BUS;
                }else if(m instanceof HotelsTableModel)
                {
                    type = ReservationListModel.HOTEL;
                }

                if(type == 0)
                {
                    System.err.println("数据模型不支持！");
                    return;
                }

                try{
                    app.reservationListModel.book(type, (String) m.getValueAt(row, 0));
                } catch (BasicTableModel.InvalidAvailDeltaException invalidAvailDeltaException) {
                    JOptionPane.showMessageDialog(
                            null,
                            "剩余数量为空，预定失败！",
                            "错误",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });
        bookPopupMenu.add(menuItem);

        MouseAdapter mouseAdapter = new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if(e.getButton() == MouseEvent.BUTTON3)
                {
                    String id_str = customerIDTextField.getText();
                    int id = -1;
                    try{
                        id = Integer.parseInt(id_str);
                    }catch (NumberFormatException ignore) { }
                    String name = app.customersTableModel.getName(id);
                    if(name == null) return;

                    JTable table = (JTable)e.getComponent();
                    if(table.getSelectedRowCount() > 0){
                        currentTable = table;
                        bookPopupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        };

        flightsDataTable.addMouseListener(mouseAdapter);
        busDataTable.addMouseListener(mouseAdapter);
        hotelsDataTable.addMouseListener(mouseAdapter);
    }
}
