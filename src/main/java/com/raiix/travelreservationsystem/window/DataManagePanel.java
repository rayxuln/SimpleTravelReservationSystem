package com.raiix.travelreservationsystem.window;

import com.raiix.travelreservationsystem.App;
import com.raiix.travelreservationsystem.model.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.*;

public class DataManagePanel extends JPanel implements TableModelListener {

    private JButton flightsButton;
    private JButton busButton;
    private JButton hotelsButton;
    private JButton customersButton;

    private JTable dataTable;
    private JPopupMenu dataTableMenu;
    private JMenuItem deleteMenuItem;
    private JMenuItem addMenuItem;

    private final BasicTableModel[] models;

    private int currentDataManageTableIndex;

    App app;

    private void generateGUI(){
        //数据管理
        BoxLayout boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        setLayout(boxLayout);

        JToolBar toolBar = new JToolBar("切换数据表");
        flightsButton = new JButton("航班");
        busButton = new JButton("大巴");
        hotelsButton = new JButton("宾馆");
        customersButton = new JButton("客户");
        toolBar.add(flightsButton);
        toolBar.add(busButton);
        toolBar.add(hotelsButton);
        toolBar.add(customersButton);
        toolBar.setFloatable(false);

        dataTable = new JTable();
        dataTable.setAutoCreateRowSorter(true);
        dataTable.setDragEnabled(false);

        JScrollPane scrollPane = new JScrollPane(dataTable);

        add(toolBar);
        add(scrollPane);
    }

    public DataManagePanel(App _app) {
        setSize(800, 600);
        app = _app;

        generateGUI();

        models = new BasicTableModel[]{app.flightTableModel, app.busTableModel, app.hotelsTableModel, app.customersTableModel};

        currentDataManageTableIndex = 0;

        flightsButton.addActionListener(makeDataManageButtonActionListener(0));
        busButton.addActionListener(makeDataManageButtonActionListener(1));
        hotelsButton.addActionListener(makeDataManageButtonActionListener(2));
        customersButton.addActionListener(makeDataManageButtonActionListener(3));

        dataTable.setModel(models[0]);
        app.flightTableModel.addTableModelListener(this);
        app.busTableModel.addTableModelListener(this);
        app.hotelsTableModel.addTableModelListener(this);
        app.customersTableModel.addTableModelListener(this);

        dataTableMenu = new JPopupMenu("菜单");

        deleteMenuItem = new JMenuItem("删除");
        deleteMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int t = currentDataManageTableIndex;
                BasicTableModel m = models[t];
                m.remove((String) m.getValueAt(dataTable.getSelectedRow(), 0));
            }
        });
        dataTableMenu.add(deleteMenuItem);

        dataTableMenu.add(new JPopupMenu.Separator());

        addMenuItem = new JMenuItem("添加");
        addMenuItem.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int t = currentDataManageTableIndex;
                if (t == 0) {
                    FlightTableModel m = (FlightTableModel) models[t];
                    m.add(
                            JOptionPane.showInputDialog(m.getColumnsName(0)),
                            Double.parseDouble(JOptionPane.showInputDialog(m.getColumnsName(1))),
                            Integer.parseInt(JOptionPane.showInputDialog(m.getColumnsName(2))),
                            JOptionPane.showInputDialog(m.getColumnsName(4)),
                            JOptionPane.showInputDialog(m.getColumnsName(5))
                    );
                } else if (t == 1) {
                    BusTableModel m = (BusTableModel) models[t];
                    m.add(
                            JOptionPane.showInputDialog(m.getColumnsName(0)),
                            Double.parseDouble(JOptionPane.showInputDialog(m.getColumnsName(1))),
                            Integer.parseInt(JOptionPane.showInputDialog(m.getColumnsName(2)))
                    );
                } else if (t == 2) {
                    HotelsTableModel m = (HotelsTableModel) models[t];
                    m.add(
                            JOptionPane.showInputDialog(m.getColumnsName(0)),
                            Double.parseDouble(JOptionPane.showInputDialog(m.getColumnsName(1))),
                            Integer.parseInt(JOptionPane.showInputDialog(m.getColumnsName(2)))
                    );
                } else if (t == 3) {
                    CustomersTableModel m = (CustomersTableModel) models[t];
                    m.add(
                            JOptionPane.showInputDialog(m.getColumnsName(1))
                    );
                }
            }
        });
        dataTableMenu.add(addMenuItem);


        dataTable.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    deleteMenuItem.setEnabled(dataTable.getSelectedRowCount() > 0);
                    dataTableMenu.show(dataTable, e.getX(), e.getY());
                }
            }
        });
    }

    public ActionListener makeDataManageButtonActionListener(final int id) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                currentDataManageTableIndex = id;
                dataTable.setModel(models[currentDataManageTableIndex]);
            }
        };
    }

    @Override
    public void tableChanged(TableModelEvent e) {
        if (e instanceof BasicTableModel.InvalidChangedKeyTableModelEvent) {
            JOptionPane.showMessageDialog(dataTable, "该主键值与其他值冲突!", "错误", JOptionPane.ERROR_MESSAGE);
        } else if (e instanceof BasicTableModel.InvalidAddKeyTableModelEvent) {
            JOptionPane.showMessageDialog(dataTable, "该主键值与其他值冲突!", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }
}
