package com.raiix.travelreservationsystem.window;

import com.raiix.travelreservationsystem.App;
import com.raiix.travelreservationsystem.model.*;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Vector;

public class MainWindow extends JFrame implements WindowListener, TableModelListener {

    JPanel mainPanel;
    private JTabbedPane tabbedPane;
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

    public MainWindow(App _app) {
        setTitle("简单旅行预定系统");
        setSize(800, 600);
        app = _app;

        models = new BasicTableModel[]{app.flightTableModel, app.busTableModel, app.hotelsTableModel, app.customersTableModel};

        currentDataManageTableIndex = 0;

        addWindowListener(this);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
//        setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH);

        add(mainPanel);

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

        MouseListener dataTableMouseListener = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    deleteMenuItem.setEnabled(dataTable.getSelectedRowCount() > 0);
                    dataTableMenu.show(getMostRecentFocusOwner(), e.getX(), e.getY());
                }
            }
        };

        dataTable.addMouseListener(dataTableMouseListener);
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

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(0, 0));
        tabbedPane = new JTabbedPane();
        tabbedPane.setAutoscrolls(false);
        tabbedPane.setEnabled(true);
        tabbedPane.setMinimumSize(new Dimension(800, 600));
        tabbedPane.setPreferredSize(new Dimension(800, 600));
        tabbedPane.setTabLayoutPolicy(1);
        tabbedPane.setTabPlacement(1);
        mainPanel.add(tabbedPane, BorderLayout.CENTER);
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new BorderLayout(0, 0));
        tabbedPane.addTab("数据管理", panel1);
        panel1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        final JToolBar toolBar1 = new JToolBar();
        toolBar1.setBorderPainted(true);
        toolBar1.setFloatable(false);
        panel1.add(toolBar1, BorderLayout.NORTH);
        toolBar1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15), null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        flightsButton = new JButton();
        flightsButton.setText("航班");
        toolBar1.add(flightsButton);
        busButton = new JButton();
        busButton.setText("大巴");
        toolBar1.add(busButton);
        hotelsButton = new JButton();
        hotelsButton.setText("宾馆");
        toolBar1.add(hotelsButton);
        customersButton = new JButton();
        customersButton.setText("客户");
        toolBar1.add(customersButton);
        final JScrollPane scrollPane1 = new JScrollPane();
        panel1.add(scrollPane1, BorderLayout.CENTER);
        scrollPane1.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "数据表", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, null, null));
        dataTable = new JTable();
        dataTable.setAutoCreateRowSorter(true);
        scrollPane1.setViewportView(dataTable);
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        panel2.setName("");
        tabbedPane.addTab("预定", panel2);
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        tabbedPane.addTab("预定信息查询", panel3);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return mainPanel;
    }
}

