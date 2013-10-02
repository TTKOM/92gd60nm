package ttkom.tt.windows.work;

import net.miginfocom.swing.MigLayout;
import sun.security.krb5.internal.KDCReqBody;
import ttkom.tt.EditUser;
import ttkom.tt.loging.LogAction;
import ttkom.tt.windows.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/*
I have 5 tables for list subscriber on status

 */

public class EventWindow extends AbWorkWindow {
    private final JDialog dialog;
    private final JTabbedPane mainTabs;
    private MainWindow mWin;
    private Object boolka;

    public EventWindow(final MainWindow mainWindow) {
        mWin = mainWindow;
        final JPanel mainPanel, buttonPanel, tabPanel;
        final String title = "Управление абонентами";
        final JButton save, close, createReport, clear, select;
        final Object[][] data;

        JTable[] listTable = new JTable[7];
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        dialog = new JDialog(mainWindow.getFrame(), title);
        dialog.setDefaultCloseOperation(dialog.DISPOSE_ON_CLOSE);
        dialog.addWindowListener(new MainCloseOper(this, dialog));
        mainTabs = new JTabbedPane();
        mainPanel = new JPanel(new MigLayout("wrap 1", "[]5", "al top"));

        final JScrollPane scrollPanel = new JScrollPane();

        final JTable toFreeze = createTable(setHeader(), getBody(mainWindow, 0));
        listTable[0] = toFreeze;
        toFreeze.setName("0");
        JScrollPane scFreeze = createScroll(toFreeze);
        scFreeze.setPreferredSize(new Dimension(screenSize.width - 114, screenSize.height - 110));

        final JTable toUnFreeze = createTable(setHeader(), getBody(mainWindow, 1));
        listTable[1] = toUnFreeze;
        toUnFreeze.setName("1");
        JScrollPane scUnFreeze = createScroll(toUnFreeze);
        scUnFreeze.setPreferredSize(new Dimension(screenSize.width - 114, screenSize.height - 110));

        final JTable toDisconnect = createTable(setHeader(), getBody(mainWindow, 2));
        listTable[2] = toDisconnect;
        toDisconnect.setName("2");
        JScrollPane scDisconnect = createScroll(toDisconnect);
        scDisconnect.setPreferredSize(new Dimension(screenSize.width - 114, screenSize.height - 110));

        final JTable toConnect = createTable(setHeader(), getBody(mainWindow, 3));
        toConnect.addMouseListener(new ActionOpenAbon(toConnect, dialog, mainWindow));
        listTable[3] = toConnect;
        toConnect.setName("3");
        JScrollPane scConnect = createScroll(toConnect);
        scConnect.setPreferredSize(new Dimension(screenSize.width - 114, screenSize.height - 110));

        final JTable toCall = createTable(setHeader(), getBody(mainWindow, 4));
        listTable[4] = toCall;
        toCall.setName("4");
        JScrollPane scCall = createScroll(toCall);
        scCall.setPreferredSize(new Dimension(screenSize.width - 114, screenSize.height - 110));

        final JTable toRDisconnect = createTable(setHeader(), getBody(mainWindow, 5));
        toRDisconnect.addMouseListener(new ActionOpenAbon(toRDisconnect,dialog,mainWindow));
        listTable[5] = toRDisconnect;
        toRDisconnect.setName("5");
        JScrollPane scRDisconnect = createScroll(toRDisconnect);
        scRDisconnect.setPreferredSize(new Dimension(screenSize.width - 114, screenSize.height - 110));

        final JTable toSendConnect = createTable(setHeader(), getBody(mainWindow, 6));
        toSendConnect.addMouseListener(new ActionOpenAbon(toSendConnect,dialog,mainWindow));
        listTable[6] = toSendConnect;
        toSendConnect.setName("6");
        JScrollPane scSendConnect = createScroll(toSendConnect);
        scSendConnect.setPreferredSize(new Dimension(screenSize.width - 114, screenSize.height - 110));

        save = setButton(null, "save", new AcSaveRows(listTable, mainWindow));
        save.setToolTipText("Сохранить выделенные элементы в фаил");
        clear = setButton(null, "clear", new AcClearSelected(listTable, mainTabs));
        clear.setToolTipText("Снять выделение со всех лементов");
        select = setButton(null, "select", new AcSelectAll(listTable, mainTabs));
        select.setToolTipText("Выделить всех абонентов");

        mainPanel.add(save, "w 30!,cell 0 0,al left");
        mainPanel.add(select, "w 30!,cell 0 0,al left");
        mainPanel.add(clear, "w 30!,cell 0 0,al left,wrap");

        mainTabs.addTab("Заморозить", scFreeze);
        mainTabs.addTab("Разморозить", scUnFreeze);
        mainTabs.addTab("Отключить", scDisconnect);
        mainTabs.addTab("Подключить", scConnect);
        mainTabs.addTab("Обзвонить", scCall);
        mainTabs.addTab("Отключить(порт)", scRDisconnect);
        mainTabs.addTab("На подключение", scSendConnect);


        mainPanel.add(mainTabs, "grow,w " + (screenSize.width - 112) + "!");
        mainPanel.add(new JLabel("Получение выписки по абонентам"), "grow");


        dialog.add(mainPanel);
        dialog.setSize(screenSize.width - 100, screenSize.height - 100);
        scrollPanel.setSize(screenSize.width - 150, screenSize.height - 300);

        dialog.setLocation(screenSize.width / 2 - dialog.getSize().width / 2, screenSize.height / 2 - dialog.getSize().height / 2);
        dialog.setModal(true);
        dialog.setVisible(true);
        dialog.validate();
        dialog.repaint();
    }


    private final class CloseWindow implements ActionListener {
        JTabbedPane tabs;

        CloseWindow(JTabbedPane tabs) {
            this.tabs = tabs;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            closeWindow(dialog);

        }
    }

    @Override
    public void selectRow() {

    }

    @Override
    public void unselectRow() {

    }

    @Override
    public void createReport() {

    }

    @Override
    public String[] setHeader() {
        return new String[]{"id", "№", "имя", "выбор", "баланс", "ipadress","port","дата откл", "дата заморозки", "улица", "дом", "квартира", "тариф", "статус", "подключение"};
    }

    @Override
    public final Object[][] getBody(MainWindow mainWindow, Integer arg) {
        final ArrayList<Object[]> arrayList = new ArrayList<Object[]>();
        String query = "";

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String strDate = formatDate.format(date);

        switch (arg) {
            case 0:
                query = "SELECT abn.id,abn.port,abn.idComm,abn.idDog,abn.name as fio,abn.balans,abn.dateDisconnect,abn.dateFreeze,str.name as street,hs.house,abn.flat,tf.name as tariff, st.name as stat,sc.name as stconn " +
                        "FROM abonents abn,tariff tf,streets str,houses hs,status st,conn_value sc " +
                        "WHERE abn.idAdres = hs.id AND abn.idTariff = tf.id AND hs.id_street = str.id AND sc.id = abn.statconn AND st.id = abn.status AND abn.status=4 AND abn.statconn=6 ORDER BY idDog ";
                break;
            case 1:
                query = "SELECT abn.id,abn.port,abn.idComm,abn.idDog,abn.name as fio,abn.balans,abn.dateDisconnect,abn.dateFreeze,str.name as street,hs.house,abn.flat,tf.name as tariff, st.name as stat,sc.name as stconn " +
                        "FROM abonents abn,tariff tf,streets str,houses hs,status st,conn_value sc " +
                        "WHERE abn.idAdres = hs.id AND abn.idTariff = tf.id AND hs.id_street = str.id AND sc.id = abn.statconn AND st.id = abn.status AND (abn.status=4 OR abn.status=1) AND abn.statconn=5 AND abn.balans >= 117 ORDER BY idDog";
                System.out.println();
                break;
            case 2:
                query = "SELECT abn.id,abn.port,abn.idComm,abn.idDog,abn.name as fio,abn.balans,abn.dateDisconnect,abn.dateFreeze,str.name as street,hs.house,abn.flat,tf.name as tariff, st.name as stat,sc.name as stconn " +
                        "FROM abonents abn,tariff tf,streets str,houses hs,status st,conn_value sc " +
                        "WHERE abn.idAdres = hs.id AND abn.idTariff = tf.id AND hs.id_street = str.id AND sc.id = abn.statconn AND st.id = abn.status AND abn.status=4 AND abn.statconn=5 AND abn.balans < 117 ORDER BY idDog";
                break;
            case 3:
                /*query = "SELECT abn.id,abn.port,abn.idComm,abn.idDog,abn.name as fio,abn.balans,abn.dateDisconnect,abn.dateFreeze,str.name as street,hs.house,abn.flat,tf.name as tariff, st.name as stat,sc.name as stconn " +
                        "FROM abonents abn,tariff tf,streets str,houses hs,status st,conn_value sc,payment pay " +
                        "WHERE abn.idAdres = hs.id AND abn.idTariff = tf.id AND hs.id_street = str.id AND sc.id = abn.statconn AND st.id = abn.status AND (abn.statconn=1 or abn.statconn=3) AND abn.status = 4 AND ((abn.statconn=1 AND pay.date>abn.dateDisconnect) OR (abn.statconn=3 AND pay.date>abn.dateFreeze) ) AND pay.idAbonent = abn.id ORDER BY idDog";
                        */
                query = "SELECT abn.id,abn.port,abn.idComm,abn.idDog,abn.name as fio,abn.balans,abn.dateDisconnect,abn.dateFreeze,str.name as street,hs.house,abn.flat,tf.name as tariff, st.name as stat,sc.name as stconn " +
                        "FROM abonents abn,tariff tf,streets str,houses hs,status st,conn_value sc,payment pay " +
                        "WHERE abn.idAdres = hs.id AND abn.idTariff = tf.id AND hs.id_street = str.id AND sc.id = abn.statconn AND st.id = abn.status AND ((abn.statconn=1 AND pay.date>abn.dateDisconnect) or (abn.statconn=3 AND pay.date>abn.dateFreeze)) AND abn.status = 4 AND pay.idAbonent = abn.id ORDER BY idDog";
                break;
            case 4:
                query = "SELECT abn.id,abn.port,abn.idComm,abn.idDog,abn.name as fio,abn.balans,abn.dateDisconnect,abn.dateFreeze,str.name as street,hs.house,abn.flat,tf.name as tariff, st.name as stat,sc.name as stconn " +
                        "FROM abonents abn,tariff tf,streets str,houses hs,status st,conn_value sc " +
                        "WHERE abn.idAdres = hs.id AND abn.idTariff = tf.id AND hs.id_street = str.id AND sc.id = abn.statconn AND st.id = abn.status AND abn.status=1 AND abn.statconn=2 AND abn.balans < 500 ORDER BY idDog";
                break;
            case 5:
                query = "SELECT abn.id,abn.port,abn.idComm,abn.idDog,abn.name as fio,abn.balans,abn.dateDisconnect,abn.dateFreeze,str.name as street,hs.house,abn.flat,tf.name as tariff, st.name as stat,sc.name as stconn " +
                        "FROM abonents abn,tariff tf,streets str,houses hs,status st,conn_value sc " +
                        "WHERE abn.idAdres = hs.id AND abn.idTariff = tf.id AND hs.id_street = str.id AND sc.id = abn.statconn AND st.id = abn.status AND abn.statconn=3 AND abn.status = 4 ORDER BY idDog";
                break;
            case 6:
                query = "SELECT abn.id,abn.port,abn.idComm,abn.idDog,abn.name as fio,abn.balans,abn.dateDisconnect,abn.dateFreeze,str.name as street,hs.house,abn.flat,tf.name as tariff, st.name as stat,sc.name as stconn " +
                        "FROM abonents abn,tariff tf,streets str,houses hs,status st,conn_value sc " +
                        "WHERE abn.idAdres = hs.id AND abn.idTariff = tf.id AND hs.id_street = str.id AND sc.id = abn.statconn AND st.id = abn.status AND abn.statconn=4 AND abn.status = 4 ORDER BY idDog";
                break;
        }

        try {
            //System.out.println(query);

            if (arg == 4) {
                System.out.println(query);

            }

            //System.out.print("\n"+query);
            ResultSet resultSet = mainWindow.getConnectDB().getConnect().createStatement().executeQuery(query);
                while (resultSet.next()) {
                        arrayList.add(new Object[]{
                                new Integer(resultSet.getString("id")),
                                new Integer(resultSet.getString("idDog")),
                                new String(resultSet.getString("fio")),
                                new Boolean(false),
                                new Double(resultSet.getString("balans")),
                                getIpAddres(resultSet.getString("idComm"), mainWindow.getConnectDB().getConnect()),
                                new String(resultSet.getString("port")),
                                new String(checkDate(resultSet.getString("dateDisconnect"))),
                                new String(checkDate(resultSet.getString("dateFreeze"))),
                                new String(resultSet.getString("street")),
                                new String(resultSet.getString("house")),
                                new String(resultSet.getString("flat")),
                                new String(resultSet.getString("tariff")),
                                new String(resultSet.getString("stat")),
                                new String(resultSet.getString("stconn"))
                        });


                }


            System.out.println("Размер масива "+arrayList.size());
            Object[][] objects = new Object[arrayList.size()][];
            for (int i = 0; i < arrayList.size(); i++) {
                objects[i] = arrayList.get(i);
            }

            return objects;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


    class AcClearSelected implements ActionListener {
        JTable[] table;
        JTabbedPane tabs;

        AcClearSelected(JTable[] table, JTabbedPane tabs) {
            this.table = table;
            this.tabs = tabs;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < table[tabs.getSelectedIndex()].getRowCount(); i++) {
                table[tabs.getSelectedIndex()].setValueAt(new Boolean(false), i, 3);
            }
        }
    }

    class AcSelectAll implements ActionListener {

        JTable[] table;
        JTabbedPane tabs;

        AcSelectAll(JTable[] table, JTabbedPane tabs) {
            this.table = table;
            this.tabs = tabs;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for (int i = 0; i < table[tabs.getSelectedIndex()].getRowCount(); i++) {
                table[tabs.getSelectedIndex()].setValueAt(new Boolean(true), i, 3);
            }
        }
    }

    class AcSaveRows implements ActionListener {
        final JTable[] table;
        final MainWindow mainwin;

        AcSaveRows(JTable[] table, MainWindow mainwin) {
            this.table = table;
            this.mainwin = mainwin;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                saveRows(table[mainTabs.getSelectedIndex()], mainwin);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    void saveRows(JTable table, MainWindow mainwin) throws IOException {

        Integer type = Integer.parseInt(table.getName());
        String fileName = "";
        String sufix = ".csv";

        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String strDate = formatDate.format(date);
        switch (type) {
            case 0://"toFreeze":
                fileName = strDate + "Заморозка-" + date.getTime() / 1000 + sufix;
                break;
            case 1://"toUnFreeze":
                fileName = strDate + "-Разморозка-" + date.getTime() / 1000 + sufix;
                break;
            case 2://"toDisconnect":
                fileName = strDate + "-Отключение-" + date.getTime() / 1000 + sufix;
                break;
            case 3://"toConnect":
                fileName = strDate + "-Подключение-" + date.getTime() / 1000 + sufix;
                break;
            case 4://"toCall":
                fileName = strDate + "-Обзвон-" + date.getTime() / 1000 + sufix;
                break;
            case 5://"toCall":
                fileName = strDate + "Отключение порта-" + date.getTime() / 1000 + sufix;
                break;
        }

        if (table.getRowCount() > 0) {
            FileDialog fd = new FileDialog(dialog, "Сформировать отчет", FileDialog.SAVE);
            JFileChooser fc = new JFileChooser();

            fd.setFile(fileName);
            fd.setMultipleMode(true);

            fd.setFilenameFilter(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".csv");
                }
            });


            fd.setLocation(50, 50);
            fd.show();
            ArrayList<Integer> ids = new ArrayList<Integer>();

            fd.setFile(fileName);

            if (fileName != null && fileName.trim().length() > 0) {

                File f = new File(fd.getDirectory() + fd.getFile());

                FileWriter wrt = new FileWriter(f);

                String flash = "";
                String min = "";
                if (!f.exists()) {
                    f.createNewFile();
                }
                //return new String[]{"id", "№", "имя", "выбор", "баланс", "ipadress","port","дата откл", "дата заморозки", "улица", "дом", "квартира", "тариф", "статус", "подключение"};
                int columCount = table.getModel().getColumnCount();
                int rowCount = table.getModel().getRowCount();
                for (int i = 0; i < columCount; i++) {
                    /*if (i != 0 && i != 3 && i != 4 && i != 5 && i != 6 && i != 7 && i != 8 && i != 12 && i != 13) {
                        flash += table.getColumnName(i) + ";";
                    } */
                    if (i == 1 || i == 2 || i ==9 || i == 10 || i == 11) {
                        flash += table.getColumnName(i) + ";";
                    }
                }
                flash += "\n";
                wrt.append(flash);
                for (int i = 0; i < rowCount; i++) {
                    flash = "";
                    Boolean ff = (Boolean) table.getValueAt(i, 3);
                    if (ff) {
                        ids.add((Integer) table.getValueAt(i, 0));
                        for (int j = 0; j < columCount; j++) {
                            //if (j != 0 && j != 3 && j != 5 && j != 6 &&  j != 12 && j != 13) {
                            if (j == 1 || j == 2 || j == 9 || j == 10 || j == 11) {

                                if (table.getModel().getValueAt(i, j) != null) {
                                    min = table.getModel().getValueAt(i, j).toString();
                                    if (min != null) {
                                        flash += min.replace("\n", ".") + ";";
                                    } else {
                                        flash += table.getModel().getValueAt(i, j) + ";";
                                    }
                                } else {
                                    flash += table.getModel().getValueAt(i, j) + ";";
                                }
                            }
                        }
                        flash += "\n";
                        wrt.append(flash);
                    }
                }
                wrt.flush();

                updateSubscribers(ids, mainwin.getConnectDB().getConnect(), table.getName());
                wrt.close();
                //ids = null;
                dialog.dispose();

                closeWindow(dialog);
            } else {
                JOptionPane.showMessageDialog(dialog, "Не выбран фаил для сохранения отчета", "Предупреждение", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(dialog, "Данные не выбранн", "Предупреждение", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void updateSubscribers(ArrayList<Integer> abs, Connection connection, String tableName) {
        /*
        mainTabs.addTab("Заморозить", scFreeze);
        mainTabs.addTab("Разморозить", scUnFreeze);
        mainTabs.addTab("Отключить", scDisconnect);
        mainTabs.addTab("Подключить", scConnect);
        mainTabs.addTab("Обзвонить",scCall);
         */

        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String strDate = formatDate.format(date);

        String query = "";
        Boolean toSQL = true;
        Integer type = Integer.parseInt(tableName);

        if (abs != null && abs.size() > 0) {
            switch (type) {
                case 0://"toFreeze":
                    query = "UPDATE abonents SET statconn = 5,dateFreeze='" + strDate + "' WHERE id = ";
                    break;
                case 1://"toUnFreeze":
                    query = "UPDATE abonents SET statconn = 2,status=1,dateFreeze=NULL WHERE id = ";
                    break;
                case 2://"toDisconnect":
                    query = "UPDATE abonents SET statconn = 3 WHERE id = ";
                    break;
                case 3://"toConnect":
                    query = "UPDATE abonents SET statconn = 4 WHERE id = ";
                    break;
                case 4://"toCall":
                    toSQL = false;
                    break;
                case 5://"to real Disconnect":
                    toSQL = false;
                    break;
            }


            //if (toSQL) {
            if (toSQL) {
                for (int i = 0; i < abs.size(); i++) {
                    try {
                        //System.out.println(query + abs.get(i));
                        LogAction.insertEvent(connection, abs.get(i), type, Integer.parseInt(mWin.getOperator()[4]));
                        connection.createStatement().executeUpdate(query + abs.get(i));

                    } catch (SQLException e) {
                        System.out.println("Код ошибки: " + e.getErrorCode());
                        e.printStackTrace();
                    }
                }
            }

            /*} else {
                for (int i = 0; i < abs.size(); i++) {
                    try {
                                     ddwd
                        LogAction.insertEvent(connection, abs.get(i), type, Integer.parseInt(mWin.getOperator()[4]));
                        connection.createStatement().executeUpdate(query + abs.get(i));

                    } catch (SQLException e) {
                        System.out.println("Код ошибки: " + e.getErrorCode());
                        e.printStackTrace();
                    }
                }
            }*/
        }
    }

    class ActionOpenAbon implements MouseListener {
        JTable table;

        MainWindow mWin;

        ActionOpenAbon(JTable table, Dialog frame, MainWindow mWin) {
            this.table = table;

            this.mWin = mWin;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Point point = e.getPoint();
                int row = table.rowAtPoint(point);
                //System.out.println(table.getValueAt(row, 0));
                String str = String.valueOf(table.getValueAt(row, 0));

                try {
                    new EditUser(mWin.getFrame(), mWin.getConnectDB(), str, mWin);
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

    private String getIpAddres(String idComm,Connection connection) throws SQLException {
        String query = "SELECT ipaddres FROm commutator WHERE id="+idComm;
        ResultSet result = connection.createStatement().executeQuery(query);

        if (result.next()) {
            return result.getString("ipaddres");
        } else { return ""; }
    }


}
