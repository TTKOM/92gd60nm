package ttkom.tt;

import net.miginfocom.swing.MigLayout;
import ttkom.tt.dbase.mysql.ConnectDB;
import ttkom.tt.menu.context.TableMenu;
import ttkom.tt.windows.MainWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 27.11.12
 * Time: 16:19
 * To change this template use File | Settings | File Templates.
 */
public class SearchUser {
    private JPanel tablePanel, formPanel, mainPanel;
    private JComboBox comboTariff, searchType;
    private JTable table;
    private TableModel model;
    private JScrollPane scTab;
    private JDialog window;
    private ConnectDB connectDB;
    private ArrayList<String[]> arrTariff = new ArrayList<String[]>();
    private JTextField searchField;
    private JFrame frame;
    private MainWindow mWin;

    public SearchUser(JFrame frame, ConnectDB connect, MainWindow mainMenu) throws SQLException {
        this.frame = frame;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        window = new JDialog(frame);
        connectDB = connect;
        mWin = mainMenu;

        MigLayout mLayer = new MigLayout("wrap 1", "grow,fill");
        MigLayout tLayer = new MigLayout("wrap 1", "grow,fill");
        MigLayout fLayer = new MigLayout("wrap 2", "[150]5[grow,fill]", "[]5[]");

        mainPanel = new JPanel(mLayer);
        formPanel = new JPanel(fLayer);
        tablePanel = new JPanel(tLayer);

        mainPanel.add(tablePanel);
        mainPanel.add(formPanel);

        tablePanel.add(setTable());
        window.add(mainPanel);

        //comboTariff     =   new JComboBox(getTariff(connect));  comboTariff.addActionListener(new ActionClear());
        searchType = new JComboBox(new String[]{"По адресу", "По договору", "По имени", "По IP адресу"});
        searchType.addActionListener(new ActionClear());
        searchField = new JTextField();
        JButton search  =   new JButton("Искать"); search.addActionListener(new ActionSearchUser());

        searchField.addKeyListener(new ActionSearchPress());

//        formPanel.add(comboTariff,"w 150!");
        formPanel.add(searchType, "w 150!");
        //formPanel.add(search,"w 150!");
        formPanel.add(searchField);

        window.setLocation(screenSize.width / 2 - 550, screenSize.height / 2 - 250);
        window.setSize(1150, 550);
        window.setModal(true);
        window.setVisible(true);
        window.validate();
        window.repaint();
    }

    class ColorTableCellRenderer extends JLabel implements TableCellRenderer {
        public ColorTableCellRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel pan = new JPanel(new BorderLayout());
            JLabel lbl = new JLabel((String) value);
            if (table.getSelectedRow() == row) {
                pan.setBackground(new Color(219, 68, 68));
            } else {
                if ((row % 2) == 0) {
                    pan.setBackground(new Color(200, 200, 255));
                } else {
                    pan.setBackground(new Color(253, 253, 253));
                }
            }
            pan.add(lbl, BorderLayout.CENTER);
            return pan;
        }
    }

    JScrollPane setTable() {
        model = new TableModel(new Object[][]{}, new Object[]{"№", "договор", "ФИО", "Улица", "Дом", "Квартира", "Тариф", "Коммутатор", "ip addres", "порт", "Подключение", "Статус", "Дата договора","Дата подключения","Дата отключения" });
        table = new JTable(model);

        TableRowSorter<TableModel> sorter = new TableRowSorter(model) {
            @Override
            public Comparator<?> getComparator(int column) {
                // для нулевой строки
                if (column == 1) {
                    return new Comparator<String>() {
                        @Override
                        public int compare(String s1, String s2) {
                            return Integer.parseInt(s1) - Integer.parseInt(s2);
                        }
                    };
                }
                // для всех остальных
                return super.getComparator(column);
            }
        };



        sorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(1, SortOrder.ASCENDING)));

        table.setDefaultRenderer(Object.class, new ColorTableCellRenderer());
        table.setAutoResizeMode(0);
        table.setRowSorter(sorter);
        sorter.setSortable(1, true);

        table.setAutoResizeMode(table.AUTO_RESIZE_OFF);
        table.getColumnModel().getColumn(0).setMinWidth(50);
        table.getColumnModel().getColumn(1).setMinWidth(50);
        table.getColumnModel().getColumn(1).setMaxWidth(70);
        table.getColumnModel().getColumn(2).setMinWidth(250);
        table.getColumnModel().getColumn(1).setMinWidth(200);
        table.getColumnModel().getColumn(3).setMinWidth(100);
        table.getColumnModel().getColumn(4).setMinWidth(100);
        table.getColumnModel().getColumn(5).setMinWidth(50);
        table.getColumnModel().getColumn(6).setMinWidth(100);
        table.getColumnModel().getColumn(7).setMinWidth(100);
        table.getColumnModel().getColumn(8).setMinWidth(100);
        table.getColumnModel().getColumn(9).setMinWidth(30);
        table.getColumnModel().getColumn(10).setMinWidth(30);
        table.getColumnModel().getColumn(11).setMinWidth(30);
        table.getColumnModel().getColumn(12).setMinWidth(130);
        table.getColumnModel().getColumn(13).setMinWidth(130);
        table.getColumnModel().getColumn(14).setMinWidth(130);
        if (Integer.parseInt(mWin.getOperator()[3]) > 0) {
            table.addMouseListener(new ActionClickRow());
        }

        table.addMouseListener(new RightClickToTable());

        JScrollPane scTab = new JScrollPane(table);
        scTab.setMinimumSize(new Dimension(1120, 450));

        return scTab;
    }

    class TableModel extends DefaultTableModel {
        public TableModel(Object rowData[][], Object columnNames[]) {
            super(rowData, columnNames);
        }

        public boolean isCellEditable(int row, int col) {
            return false;
        }
    }

    String[] getTariff(ConnectDB conn) throws SQLException {
        String query = "SELECT * FROM tariff ORDER BY name ASC";
        ResultSet resultSet = conn.getConnect().createStatement().executeQuery(query);
        Integer count = 0;
        if (comboTariff != null) comboTariff.removeAllItems();
        while (arrTariff.size() > 0) arrTariff.remove(0);

        while (resultSet.next()) {
            arrTariff.add(new String[]{resultSet.getString("id"), resultSet.getString("name")});
            count++;
        }
        String[] obj = new String[count];
        for (int i = 0; i < arrTariff.size(); i++) {
            obj[i] = arrTariff.get(i)[1];
        }
        return obj;
    }

    class ActionSearchUser implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String[] textField = null;
            String query = null;
            String text = searchField.getText().trim();
            if (searchType.getSelectedIndex() == 0) {
                textField = text.split(" ");
                if (textField != null) {
                    if (textField.length > 0) {
                        if (textField.length == 1) {
                            query =
                                    "SELECT abs.id,str.name as street,hs.house,abs.name as fio,abs.idDog,abs.flat,abs.balans,tf.name as tariff,st.name as status,stconn.name as statusconnect " +
                                    "FROM  streets str,houses hs,abonents abs,tariff tf,status st,conn_value stconn " +
                                    "WHERE abs.idAdres=hs.id AND abs.id_service=1 AND str.id=hs.id_street AND st.id=abs.status AND stconn.id=abs.statconn AND tf.id=abs.idTariff AND str.name LIKE '" + textField[0] + "%'";
                        } else if (textField.length == 2) {
                            query =
                                    "SELECT abs.id,str.name as street,hs.house,abs.name as fio,abs.idDog,abs.flat,abs.balans,tf.name as tariff,st.name as status,stconn.name as statusconnect " +
                                    "FROM  streets str,houses hs,abonents abs,tariff tf,status st,conn_value stconn " +
                                    "WHERE abs.idAdres=hs.id AND abs.id_service=1 AND str.id=hs.id_street AND st.id=abs.status AND stconn.id=abs.statconn AND tf.id=abs.idTariff AND str.name LIKE '" + textField[0] + "%' AND hs.house = '" + textField[1] + "'";
                        } else if (textField.length == 3) {
                            query =
                                    "SELECT abs.id,str.name as street,hs.house,abs.name as fio,abs.idDog,abs.flat,abs.balans,tf.name as tariff,st.name as status,stconn.name as statusconnect " +
                                            "FROM  streets str,houses hs,abonents abs,tariff tf,status st,conn_value stconn " +
                                            "WHERE abs.idAdres=hs.id AND abs.id_service=1 AND str.id=hs.id_street AND st.id=abs.status AND stconn.id=abs.statconn AND tf.id=abs.idTariff AND str.name LIKE '" + textField[0] + "%' AND hs.house = '" + textField[1] + "' AND abs.flat LIKE '" + textField[2] + "'";
                        }


                    }
                }
            } else if (searchType.getSelectedIndex() == 1) {

            } else if (searchType.getSelectedIndex() == 2) {

            } else if (searchType.getSelectedIndex() == 3) {
                textField = searchField.getText().split(" ");
                if (text != null) {
                    if (text.length() > 0) {
                        query =
                                "SELECT abs.id,str.name as street,hs.house,abs.name as fio,abs.idDog,abs.flat,abs.balans,tf.name as tariff,st.name as status,stconn.name as statusconnect " +
                                "FROM  streets str,houses hs,abonents abs,tariff tf,commutator com,conn_value stconn " +
                                "WHERE abs.idAdres=hs.id AND abs.id_service=1 AND str.id=hs.id_street AND st.id=abs.status AND stconn.id=abs.statconn AND tf.id=abs.idTariff AND com.id=abs.idComm AND com.name LIKE '" + text + "$'";
                    }

                }
            }

            if (query.length() > 0) {
                try {
                    ResultSet resultSet = connectDB.getConnect().createStatement().executeQuery(query);
                    //"№","договор","ФИО","Улица","Дом","Квартира","Баланс","Тариф"
                    while (resultSet.next()) {
                        model.addRow(new Object[]{
                                resultSet.getString("id"),
                                resultSet.getString("idDog"),
                                resultSet.getString("fio"),
                                resultSet.getString("street"),
                                resultSet.getString("house"),
                                resultSet.getString("flat"),
                                resultSet.getString("balans"),
                                resultSet.getString("tariff"),
                                resultSet.getString("status")
                        });
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }

    class ActionSearchPress implements KeyListener {

        @Override
        public void keyTyped(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void keyReleased(KeyEvent e) {

            String[] textField = null;
            String query = null;
            //Integer tar         = Integer.valueOf(arrTariff.get(comboTariff.getSelectedIndex())[0]);

            while (model.getRowCount() > 0) {
                model.removeRow(0);
            }


            String text = searchField.getText();
            if (searchType.getSelectedIndex() == 0) {
                textField = text.split(" ");
                if (textField != null) {
                    if (textField.length > 0) {
                        if (textField.length == 1) {
                            query =
                                    "SELECT abs.id,str.name as street,hs.house,abs.name as fio,abs.idDog,abs.flat,abs.balans,st.name as status,stconn.name as statusconnect,tf.name as tariff,abs.idComm,abs.port,abs.dateDogovor,abs.dateConnect,dateDisconnect " +
                                            "FROM  streets str,houses hs,abonents abs,tariff tf,status st,conn_value stconn " +
                                            "WHERE abs.idAdres=hs.id AND abs.id_service=1 AND str.id=hs.id_street AND st.id=abs.status AND stconn.id=abs.statconn AND tf.id=abs.idTariff  AND str.name LIKE '" + textField[0] + "%'";
                        } else if (textField.length == 2) {
                            query =
                                    "SELECT abs.id,str.name as street,hs.house,abs.name as fio,abs.idDog,abs.flat,abs.balans,st.name as status,stconn.name as statusconnect,tf.name as tariff,abs.idComm,abs.port,abs.dateDogovor,abs.dateConnect,dateDisconnect " +
                                            "FROM  streets str,houses hs,abonents abs,tariff tf,status st,conn_value stconn " +
                                            "WHERE abs.idAdres=hs.id AND abs.id_service=1 AND str.id=hs.id_street AND st.id=abs.status AND stconn.id=abs.statconn AND tf.id=abs.idTariff AND  str.name LIKE '" + textField[0] + "%' AND hs.house = '" + textField[1] + "'";
                        } else if (textField.length == 3) {
                            query =
                                    "SELECT abs.id,str.name as street,hs.house,abs.name as fio,abs.idDog,abs.flat,abs.balans,st.name as status,stconn.name as statusconnect,tf.name as tariff,abs.idComm,abs.port,abs.dateDogovor,abs.dateConnect,dateDisconnect " +
                                            "FROM  streets str,houses hs,abonents abs,tariff tf,status st,conn_value stconn " +
                                            "WHERE abs.idAdres=hs.id AND abs.id_service=1 AND str.id=hs.id_street AND st.id=abs.status AND stconn.id=abs.statconn AND tf.id=abs.idTariff AND str.name LIKE '" + textField[0] + "%' AND hs.house = '" + textField[1] + "' AND abs.flat LIKE '" + textField[2] + "%'";
                        }
                    }
                }
            } else if (searchType.getSelectedIndex() == 1) {
                query =
                        "SELECT abs.id,str.name as street,hs.house,abs.name as fio,abs.idDog,abs.flat,abs.balans,st.name as status,stconn.name as statusconnect,tf.name as tariff,abs.idComm,abs.port,abs.dateDogovor,abs.dateConnect,dateDisconnect " +
                                "FROM  streets str,houses hs,abonents abs,tariff tf,status st,conn_value stconn  " +
                                "WHERE abs.idAdres=hs.id AND abs.id_service=1 AND str.id=hs.id_street AND st.id=abs.status AND stconn.id=abs.statconn AND tf.id=abs.idTariff AND abs.idDog LIKE '" + searchField.getText().trim() + "%'";
            } else if (searchType.getSelectedIndex() == 2) {
                query =
                        "SELECT abs.id,str.name as street,hs.house,abs.name as fio,abs.idDog,abs.flat,abs.balans,st.name as status,stconn.name as statusconnect,tf.name as tariff,abs.idComm,abs.port,abs.dateDogovor,abs.dateConnect,dateDisconnect " +
                                "FROM  streets str,houses hs,abonents abs,tariff tf ,status st,conn_value stconn " +
                                "WHERE  abs.idAdres=hs.id  AND abs.id_service=1 AND str.id=hs.id_street AND st.id=abs.status AND stconn.id=abs.statconn AND tf.id=abs.idTariff  AND abs.name LIKE '" + searchField.getText().trim() + "%'";
            } else if (searchType.getSelectedIndex() == 3) {

                query =
                        "SELECT abs.id,str.name as street,hs.house,abs.name as fio,abs.idDog,abs.flat,abs.balans,st.name as status,stconn.name as statusconnect,tf.name as tariff,abs.idComm,abs.port,abs.dateDogovor,abs.dateConnect,dateDisconnect " +
                                "FROM  streets str,houses hs,abonents abs,tariff tf,commutator com,status st,conn_value stconn  " +
                                "WHERE abs.idAdres=hs.id AND abs.id_service=1 AND str.id=hs.id_street AND st.id=abs.status AND stconn.id=abs.statconn AND tf.id=abs.idTariff AND com.id = abs.idComm AND com.ipaddres LIKE '" + text + "%'";

            }

            if (model != null) while (model.getRowCount() > 0) model.removeRow(0);

            try {
                ResultSet resultSet = mWin.getConnectDB().getConnect().createStatement().executeQuery(query);
                while (resultSet.next()) {
                    String dtConn = "", dtDog = "",dtDisconn="";
                    String ipAddr = "";
                    String ipName = "";
                    if (resultSet.getString("dateConnect") != null)
                        dtConn = resultSet.getString("dateConnect").replace(".0", "");
                    if (resultSet.getString("dateDogovor") != null)
                        dtDog = resultSet.getString("dateDogovor").replace(".0", "");
                    if (resultSet.getString("dateDisconnect") != null)
                        dtDisconn = resultSet.getString("dateDisconnect").replace(".0", "");
                    if (resultSet.getString("idComm") != null) {
                        query = "SELECT ipaddres,name FROM commutator WHERE id=" + resultSet.getString("idComm");
                        ResultSet result = mWin.getConnectDB().getConnect().createStatement().executeQuery(query);
                        if (result.next()) {
                            ipAddr = result.getString("ipaddres");
                            ipName = result.getString("name");
                        }
                    }
                    model.addRow(new Object[]{
                            resultSet.getString("id"),
                            resultSet.getString("idDog"),
                            resultSet.getString("fio"),
                            resultSet.getString("street"),
                            resultSet.getString("house"),
                            resultSet.getString("flat"),
                            resultSet.getString("tariff"),
                            ipName,
                            ipAddr,
                            resultSet.getString("port"),
                            resultSet.getString("status"),
                            resultSet.getString("statusconnect"),
                            dtDog,
                            dtConn,
                            dtDisconn
                    });
                }


            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        }
    }

    class ActionClear implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            while (model.getRowCount() > 0) model.removeRow(0);
            searchField.setText("");
        }
    }

    class ActionClickRow implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Point point = e.getPoint();
                int row = table.rowAtPoint(point);
                String str = (String) table.getValueAt(row, 0);
                window.dispose();

                try {
                    /*
                    DEBUG */


                    new EditUser(frame, connectDB, str, mWin);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    class RightClickToTable implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getButton() == 3) {
                Point point = e.getPoint();
                int colum = table.columnAtPoint(point);
                int row = table.rowAtPoint(point);
                int x = (int) point.getX();
                int y = (int) point.getY();
                new TableMenu(row, colum, x, y, table);
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
