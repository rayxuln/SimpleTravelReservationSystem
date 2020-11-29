package com.raiix.travelreservationsystem.window;

import com.raiix.travelreservationsystem.App;
import com.raiix.travelreservationsystem.model.ReservationListModel;
import org.omg.CORBA.ARG_IN;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.*;

public class ReservationInspectPanel extends JPanel {
    App app;

    JTextField customerIDTextField;
    JLabel customerNameLabel;

    JTextArea infoTextArea;
    JTextField fromTextField;
    JTextField toTextField;
    JButton checkButton;

    JList reservationList;
    ReservationListModel reservationListModel;

    public void update(){
        customerIDTextField.setText("");
        ((ReservationListModel)reservationList.getModel()).setCustomerID(-1);
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

        //添加路线检查GUI
        panel1 = new JPanel();
        gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = GridBagConstraints.LINE_START;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        panel.add(panel1, gridBagConstraints);
        boxLayout = new BoxLayout(panel1, BoxLayout.X_AXIS);
        panel1.setLayout(boxLayout);

        panel1.add(new JLabel("从"));
        panel1.add(Box.createHorizontalStrut(3));
        fromTextField = new JTextField();
        fromTextField.setPreferredSize(new Dimension(150, 10));
        panel1.add(fromTextField);
        panel1.add(Box.createHorizontalStrut(3));
        panel1.add(new JLabel("到"));
        panel1.add(Box.createHorizontalStrut(3));
        toTextField = new JTextField();
        toTextField.setPreferredSize(new Dimension(150, 10));
        panel1.add(toTextField);
        panel1.add(Box.createHorizontalStrut(3));
        checkButton = new JButton("检查路线完整性");
        panel1.add(checkButton);

        //添加信息显示文本框
        gridBagConstraints.fill = GridBagConstraints.BOTH;
        gridBagConstraints.gridwidth = GridBagConstraints.REMAINDER;
        gridBagConstraints.weightx = 1;
        gridBagConstraints.weighty = 1;
        infoTextArea = new JTextArea();
        infoTextArea.setEditable(false);
        panel.add(infoTextArea, gridBagConstraints);


        splitPane.setLeftComponent(panel);

        //添加预定列表
        panel = new JPanel();
        boxLayout = new BoxLayout(panel, BoxLayout.Y_AXIS);
        panel.setLayout(boxLayout);
        panel.setBorder(new LineBorder(Color.BLACK));
        label = new JLabel("已预定");
        panel.add(label);
        reservationList = new JList(reservationListModel);
        panel.add(new JScrollPane(reservationList));

        splitPane.setRightComponent(panel);
    }

    public ReservationInspectPanel(App _app)
    {
        app = _app;
        reservationListModel = new ReservationListModel(app, -1);
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

        reservationListModel.addListDataListener(new ListDataListener() {
            private void update(){
                infoTextArea.setText("");
            }

            @Override
            public void intervalAdded(ListDataEvent e) {
                update();
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                update();
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                update();
            }
        });

        checkButton.addActionListener(new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String from = fromTextField.getText();
                String to = toTextField.getText();

                String path = generatePath(from, to);

                infoTextArea.setText(path);
            }
        });
    }

    private String generatePath(String fromCityName, String toCityName){
        if(reservationListModel.getSize() <= 0) return "";

        ArrayList<Integer> type_list = new ArrayList<Integer>();
        ArrayList<String> key_list = new ArrayList<String>();
        reservationListModel.getTypeKeyList(type_list, key_list);

        //获取相关航班数据
        ArrayList<String> flights_list = new ArrayList<String>();
        ArrayList<String> from_list = new ArrayList<String>();
        ArrayList<String> to_list = new ArrayList<String>();

        //获取城市名集合
        HashSet<String> city_names = new HashSet<String>();
        for(int i=0; i<key_list.size(); ++i)
        {
            if(type_list.get(i) == ReservationListModel.BUS || type_list.get(i) == ReservationListModel.HOTEL)
            {
                city_names.add(key_list.get(i));
            }else if(type_list.get(i) == ReservationListModel.FLIGHT)
            {
                flights_list.add(key_list.get(i));
                from_list.add(app.flightTableModel.getFromCity(key_list.get(i)));
                to_list.add(app.flightTableModel.getArivCity(key_list.get(i)));

                city_names.add(from_list.get(from_list.size()-1));
                city_names.add(to_list.get(to_list.size()-1));
            }

        }

        HashMap<String, Integer> idMap = new HashMap<String, Integer>();
        ArrayList<CityGraphNode> graph = new ArrayList<CityGraphNode>();
        for(String city:city_names)
        {
            idMap.put(city, graph.size());
            graph.add(new CityGraphNode(city));
        }

        //计算flights
        for(int i=0; i<from_list.size(); ++i)
        {
            int fromCity = idMap.get(from_list.get(i));
            int toCity = idMap.get(to_list.get(i));

            CityGraphNode node = graph.get(fromCity);
            node.flights.add(toCity);
        }

        //计算bus和hotel
        for(int i=0; i<key_list.size(); ++i)
        {
            if(type_list.get(i) == ReservationListModel.BUS || type_list.get(i) == ReservationListModel.HOTEL)
            {
                CityGraphNode node = graph.get(idMap.get(key_list.get(i)));
                if(type_list.get(i) == ReservationListModel.BUS)
                    node.bus = true;
                if(type_list.get(i) == ReservationListModel.HOTEL)
                    node.hotel = true;
            }
        }

        ArrayList<String> paths = new ArrayList<String>();
        ArrayList<CityGraphNode> path = new ArrayList<CityGraphNode>();
        ArrayList<Boolean> visited = new ArrayList<Boolean>();
        for(CityGraphNode g:graph) visited.add(false);

        Integer start = idMap.get(fromCityName);
        Integer target = idMap.get(toCityName);

        if(start == null)
        {
            JOptionPane.showMessageDialog(null,
                    "没有关于"+fromCityName+"的相关预订信息",
                    "错误",
                    JOptionPane.ERROR_MESSAGE
                    );
            return null;
        }
        if(target == null)
        {
            JOptionPane.showMessageDialog(null,
                    "没有关于"+toCityName+"的相关预订信息",
                    "错误",
                    JOptionPane.ERROR_MESSAGE
            );
            return null;
        }

        String res = CityGraphNode.walk(start, target, graph, path, visited);

        if(res == null)
            return "从"+fromCityName+"到"+toCityName+"的路线不完整";
        return res;
    }

    private static class CityGraphNode {

        public String name;
        public ArrayList<Integer> flights;
        public boolean bus;
        public boolean hotel;

        public CityGraphNode(String cityName){
            name = cityName;
            flights = new ArrayList<Integer>();
            bus = false;
            hotel = false;
        }

        @Override
        public String toString() {
            ArrayList<String> arr = new ArrayList<String>();
            if(bus) arr.add("大巴");
            if(hotel) arr.add("宾馆");

            StringBuilder sb = new StringBuilder();
            sb.append(name);
            if(arr.size() > 0)
            {
                sb.append("(");
                for (int i=0; i<arr.size(); ++i)
                {
                    sb.append(arr.get(i));
                    if(i < arr.size()-1)
                        sb.append(",");
                }
                sb.append(")");
            }

            return sb.toString();
        }

        public static String walk(int current,
                                int target,
                                ArrayList<CityGraphNode> graph,
                                ArrayList<CityGraphNode> path,
                                ArrayList<Boolean> visited)
        {
            visited.set(current, true);
            CityGraphNode node = graph.get(current);
            path.add(node);
            if(current == target)
            {
                StringBuilder sb = new StringBuilder();
                for(int i=0; i<path.size(); ++i)
                {
                    sb.append(path.get(i).toString());
                    if(i < path.size()-1)
                        sb.append("->");
                }
                return sb.toString();
            }

            for(Integer f:node.flights)
            {
                if(!visited.get(f)){
                    String res = walk(f, target, graph, path, visited);
                    if(res != null) return res;
                }
            }

            path.remove(path.size()-1);
            return null;
        }
    }
}
