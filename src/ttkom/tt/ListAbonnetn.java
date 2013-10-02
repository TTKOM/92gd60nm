package ttkom.tt;

import net.miginfocom.swing.MigLayout;
import ttkom.tt.menu.context.TableMenu;
import ttkom.tt.windows.MainWindow;
import ttkom.tt.windows.modal.card.search.SearchUTM;
import ttkom.tt.windows.work.EventWindow;
import ttkom.tt.windows.work.ReportPay;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 27.11.12
 * Time: 14:17
 * To change this template use File | Settings | File Templates.
 */
public class ListAbonnetn {
    JFrame frame;
    MainWindow mWin;

    private JTable table;
    private TableModel model;
    private JScrollPane scTab;
    private Dimension screenSize;

    public ListAbonnetn(MainWindow mainWindow) {
        mWin = mainWindow;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JPanel mainpanel, buttonpanel, tabelpanel;
        MigLayout ml_findpanel, ml_tpanel, ml_mapanel;
        JButton search, add, reloadList, stat, eventList,utm_abon;
        JButton addTariff;
        JScrollPane scrollPane = setTable();

        //=====================================
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setTitle(String.valueOf(mainWindow.getConnectDB().getUrl()));
        frame.addWindowListener(new MainClose());
        ml_mapanel = new MigLayout("wrap 1", "grow, fill");
        ml_findpanel = new MigLayout("wrap 5");
        ml_tpanel = new MigLayout("wrap 1", "grow, fill");
        mainpanel = new JPanel(ml_mapanel);
        buttonpanel = new JPanel(ml_findpanel);
        tabelpanel = new JPanel(ml_tpanel);
        //search = new JButton("Поиск");
        search = new JButton();


        search.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/find.png")));
        search.setText("IDNET");
        search.setToolTipText("Поиск абонента IDNET");
        search.addActionListener(new SearchAction());


        add = new JButton();
        add.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/plus.png")));
        add.setText("Добавить");

        if (mainWindow.getOperator()[0].hashCode()=="g1yyk".hashCode())  {
            stat = new JButton("");
        } else {stat = new JButton("Статистика");}


        stat.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/statistick.png")));

        if (Integer.parseInt(mWin.getOperator()[3]) > 5) {
            add.addActionListener(new AddAction());
        }
        mainpanel.add(buttonpanel);
        mainpanel.add(tabelpanel);
        buttonpanel.add(search);
        buttonpanel.add(add);
        if (Integer.valueOf(mainWindow.getOperator()[3]) >= 999) {
            addTariff = new JButton("Добавить Тариф");
            addTariff.addActionListener(new AddTariffAction());
            buttonpanel.add(addTariff, "w 150!");
        }
        if (Integer.valueOf(mainWindow.getOperator()[3]) == 10 || mainWindow.getOperator()[0].hashCode()=="report".hashCode()) {
            buttonpanel.add(stat, "w 150!");
            stat.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new ReportPay(mWin);
                }
            });
        }

        utm_abon = new JButton();
        utm_abon.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/find.png")));
        utm_abon.setText("TL");

        utm_abon.addActionListener(new SearchUTM5());

        buttonpanel.add(utm_abon);


        //eventList = new JButton("События");
        eventList = new JButton();
        eventList.addActionListener(new ActionEventWindow(mainWindow));
        eventList.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/status.png")));
        eventList.setText("События");
        eventList.setToolTipText("События");
        if (Integer.valueOf(mainWindow.getOperator()[3]) == 10 || Integer.valueOf(mainWindow.getOperator()[3]) == 99) {
            buttonpanel.add(eventList);
        }

        //reloadList = new JButton("Обновить список");        //update.png
        reloadList = new JButton();        //update.png
        reloadList.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/update.png")));
        reloadList.setToolTipText("Обновить список");
        //reloadList.setPreferredSize(new Dimension(20,20));
        //reloadList.setMinimumSize(new Dimension(20,20));
        reloadList.addActionListener(new ActtionReloadList());
        buttonpanel.add(reloadList, "w 24!,wrap");


        tabelpanel.add(scrollPane);
        showabon();

        frame.add(mainpanel);
        frame.setJMenuBar(new MainMenu(mainWindow).getMBar());
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.add(mainpanel);
        frame.pack();
        frame.setVisible(true);
    }

    class SearchAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new SearchUser(frame, mWin.getConnectDB(), mWin);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        }
    }

    class SearchUTM5 implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new SearchUTM(frame,mWin.getConnectDB(),mWin);
        }
    }

    class AddAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new AddUser(frame, mWin.getConnectDB(), mWin);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    class FindTemirline implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    class AddTariffAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            new AddTariff(frame, mWin.getConnectDB());
        }
    }

    class ActtionReloadList implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            showabon();
        }
    }

    class ActionEventWindow implements ActionListener {
        MainWindow mainwin;
        ActionEventWindow(MainWindow mainWindow) {
            this.mainwin = mainWindow;
        }

        @Override
        public void actionPerformed(java.awt.event.ActionEvent e) {
            new EventWindow(mainwin);
        }
    }

    JScrollPane setTable() {
        model = new TableModel(new Object[][]{}, new Object[]{"№", "договор", "ФИО", "Улица", "Дом", "Квартира", "Тариф", "Баланс", "ip addres", "порт", "Подключение", "Статус", "Дата подключения", "Дата отключения", "Дата договора"});
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
        if (Integer.parseInt(mWin.getOperator()[3]) > 0) {
            table.addMouseListener(new ActionOpenAbon());
        }
        table.setAutoResizeMode(0);
        table.setRowSorter(sorter);
        sorter.setSortable(1, true);
        table.getColumnModel().getColumn(0).setMinWidth(40);
        table.getColumnModel().getColumn(1).setMinWidth(50);
        table.getColumnModel().getColumn(2).setMinWidth(250);
        table.getColumnModel().getColumn(3).setMinWidth(100);
        table.getColumnModel().getColumn(4).setMinWidth(50);
        table.getColumnModel().getColumn(5).setMinWidth(75);
        table.getColumnModel().getColumn(6).setMinWidth(130);
        table.getColumnModel().getColumn(7).setMinWidth(80);
        table.getColumnModel().getColumn(7).setMaxWidth(80);
        table.getColumnModel().getColumn(8).setMinWidth(100);
        table.getColumnModel().getColumn(9).setMinWidth(50);
        table.getColumnModel().getColumn(10).setMinWidth(75);
        table.getColumnModel().getColumn(11).setMinWidth(75);
        table.getColumnModel().getColumn(12).setMinWidth(150);
        table.getColumnModel().getColumn(13).setMinWidth(150);
        table.getColumnModel().getColumn(14).setMinWidth(150);

        Integer w, h;
        w = (int) screenSize.getWidth() - 20;
        h = (int) screenSize.getHeight() - 150;

        JScrollPane scTab = new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        table.addMouseListener(new RightClickToTable());

        scTab.setMaximumSize(new Dimension(w, h));
        scTab.setMinimumSize(new Dimension(w, h));

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

    void showabon() {
        String query =
                "SELECT abs.id,str.name as street,hs.house,abs.name as fio,abs.idDog,abs.flat,abs.dateDogovor,abs.dateConnect,abs.dateDisconnect,tf.name as tariff,stconn.name as statusconnect," +
                "abs.port,abs.idComm,abs.balans,st.name as status " +
                "FROM  streets str,houses hs,abonents abs,tariff tf,status st,conn_value stconn " +
                "WHERE abs.idAdres=hs.id AND str.id=hs.id_street AND tf.id=abs.idTariff AND st.id=abs.status AND stconn.id=abs.statconn AND abs.id_service=1 ORDER BY abs.idDog ASC ";

        if (model != null) while (model.getRowCount() > 0) model.removeRow(0);
        try {
            ResultSet resultSet = mWin.getConnectDB().getConnect().createStatement().executeQuery(query);

            while (resultSet.next()) {
                String dtConn = "", dtDog = "", dtDisconn = "";
                String ipAddr = "";
                if (resultSet.getString("dateConnect") != null)
                    dtConn = resultSet.getString("dateConnect").replace(".0", "");
                if (resultSet.getString("dateDisconnect") != null)
                    dtDisconn = resultSet.getString("dateDisconnect").replace(".0", "");
                if (resultSet.getString("dateDogovor") != null)
                    dtDog = resultSet.getString("dateDogovor").replace(".0", "");
                if (resultSet.getString("idComm") != null) {
                    query = "SELECT ipaddres FROM commutator WHERE id=" + resultSet.getString("idComm");
                    ResultSet result = mWin.getConnectDB().getConnect().createStatement().executeQuery(query);
                    if (result.next()) { ipAddr = result.getString("ipaddres"); }
                }
                model.addRow(new Object[]{
                        resultSet.getString("id"),
                        resultSet.getString("idDog"),
                        resultSet.getString("fio"),
                        resultSet.getString("street"),
                        resultSet.getString("house"),
                        resultSet.getString("flat"),
                        resultSet.getString("tariff"),
                        resultSet.getString("balans"),
                        ipAddr,
                        resultSet.getString("port"),
                        resultSet.getString("status"),
                        resultSet.getString("statusconnect"),
                        dtConn,
                        dtDisconn,
                        dtDog
                });
            }


        } catch (SQLException e) {
            System.out.println("Код ошибки: " + e.getErrorCode());
            e.printStackTrace();
        }
    }

    class ActionOpenAbon implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Point point = e.getPoint();
                int row = table.rowAtPoint(point);
                String str = (String) table.getValueAt(row, 0);
                try {
                    new EditUser(frame, mWin.getConnectDB(), str, mWin);
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

    class MainClose implements WindowListener {

        @Override
        public void windowOpened(WindowEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void windowClosing(WindowEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void windowClosed(WindowEvent e) {
            try {
                mWin.closeConnect();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            System.exit(1);
        }

        @Override
        public void windowIconified(WindowEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void windowActivated(WindowEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }
}
