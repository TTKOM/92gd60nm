package ttkom.tt;

import com.toedter.calendar.JDateChooser;
import net.miginfocom.swing.MigLayout;
import ttkom.tt.dbase.mysql.ConnectDB;
import ttkom.tt.loging.LogAction;
import ttkom.tt.windows.MainWindow;
import ttkom.tt.windows.modal.ChangeTariff;
import ttkom.tt.windows.modal.DisconnectAbon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 27.11.12
 * Time: 15:46
 * To change this template use File | Settings | File Templates.
 */
public class EditUser {
    private JPanel mainPanel, formPanel, buttonPanel, leftPanel, rightPanel;
    private MigLayout migMainPanel, migFormPanel, migButPanel;
    private JTextField fio, flat, phone, phoneM, phoneW, accaunt, dognum, abonPort;
    private JTextArea descr;
    private JScrollPane scrollBar, scrolDescr;
    private JComboBox street, house, tariff, comm;
    private JButton addIdle, saveAbon, clearForm, exitForm, getConnectInfo;
    private JButton addPay, updatePay, discon, status, connect;
    private ConnectDB connectDB;
    private JDialog window, win;

    private ArrayList<String[]> arrStreet = new ArrayList<String[]>();  // first elemetn is id street, second element is name street
    private ArrayList<String[]> arrHouse = new ArrayList<String[]>();
    private ArrayList<String[]> arrTariff = new ArrayList<String[]>();
    private ArrayList<String[]> arrComm = new ArrayList<String[]>();
    private MainWindow mainWindow;

    private JTextField addFiled;


    private JTable table;
    private TableModel model;
    private JScrollPane scTab;

    private Integer idAbonent;

    private JComboBox countMonth, intMonth;

    private JTextField cost;
    private JCheckBox activation;
    private Integer idTariff;
    private JDateChooser calendar;

    private JLabel addConn;
    private Integer selCom;

    //Integer.parseInt(mWin.getOperator()[3])>5


    public EditUser(JFrame frame, ConnectDB conn, String idAbonen, MainWindow mWin) throws SQLException {
        Date tdNow = new Date();
        Date tdBefore = tdNow;
        tdBefore.setHours(tdBefore.getHours() - 1);
        mainWindow = mWin;
        connectDB = conn;
        idAbonent = Integer.valueOf(idAbonen);
        selCom = 0;
        String lastID;

        String[] userInfo = getAbonInfo(idAbonen);

        idTariff = Integer.parseInt(userInfo[5]);
        JLabel addLHouse = new JLabel("!"), addLStreet = new JLabel("!"), genAccount = new JLabel("#");

        addLHouse.setName("house");
        addLStreet.setName("street");
        addLHouse.addMouseListener(new addAddr());
        addLStreet.addMouseListener(new addAddr());
        JButton upName, upTariff, upAddr, upHouse, upFlat, upPhone, upPhoneM, upPhoneW, upComm, upPort, upDescr;
        JButton addComm, addAddres;
        upName = new JButton();
        upName.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/update.png")));
        upName.addActionListener(new UpdateName(userInfo));
        upTariff = new JButton();
        upTariff.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/update.png")));
        upTariff.addActionListener(new UpdateTariff(userInfo));
        upAddr = new JButton();
        upAddr.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/update.png")));
        upAddr.addActionListener(new UpdateAddr(userInfo));
        upHouse = new JButton();
        upHouse.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/update.png")));
        upFlat = new JButton();
        upFlat.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/update.png")));
        upPhone = new JButton();
        upPhone.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/update.png")));
        upPhone.addActionListener(new UpdateStr(userInfo, 1));
        upPhoneM = new JButton();
        upPhoneM.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/update.png")));
        upPhoneM.addActionListener(new UpdateStr(userInfo, 2));
        upPhoneW = new JButton();
        upPhoneW.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/update.png")));
        upPhoneW.addActionListener(new UpdateStr(userInfo, 3));
        upComm = new JButton();
        upComm.addActionListener(new updateComm());
        upComm.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/update.png")));
        upPort = new JButton();
        upPort.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/update.png")));
        upDescr = new JButton();
        upDescr.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/update.png")));

        addComm = new JButton();
        addComm.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/plus.png")));
        addComm.addActionListener(new ActionAddComm(mWin, userInfo));
        addAddres = new JButton();
        addAddres.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/plus.png")));

        //addConn = new JLabel("+");
        //addConn.addMouseListener(new ActionAddConnect());
        //addConn.setEnabled(false);


        window = new JDialog(frame);


        migMainPanel = new MigLayout("wrap 2", "[450]5[600]", "[grow, fill][grow,fill]");
        migFormPanel = new MigLayout("wrap 3", "[]5[300]5[20]", "grow,fill");
        migButPanel = new MigLayout("wrap 2", "grow, fill");
        mainPanel = new JPanel(migMainPanel);
        formPanel = new JPanel(migFormPanel);
        formPanel.setMinimumSize(new Dimension(450, 450));
        buttonPanel = new JPanel(migButPanel);

        leftPanel = new JPanel(new MigLayout("wrap 1", "grow, fill"));
        rightPanel = new JPanel(new MigLayout("wrap 8", "[100]5[100]5[100]5[100]5[100]5[100]5[100]", "grow,fill"));

        fio = new JTextField();
        if (Integer.valueOf(mWin.getOperator()[3]) >= 10) {
            fio.setEditable(true);
        } else {
            fio.setEditable(false);
            upName.setEnabled(false);
        }
        flat = new JTextField();
        if (Integer.valueOf(mWin.getOperator()[3]) >= 10) {
            flat.setEditable(true);
        } else {
            flat.setEditable(false);
            upFlat.setEnabled(false);
        }
        phone = new JTextField();
        if (Integer.valueOf(mWin.getOperator()[3]) >= 10) {
            phone.setEditable(true);
        } else {
            phone.setEditable(false);
            upPhone.setEnabled(false);
        }
        phoneM = new JTextField();
        if (Integer.valueOf(mWin.getOperator()[3]) >= 10) {
            phoneM.setEditable(true);
        } else {
            phoneM.setEditable(false);
            upPhoneM.setEnabled(false);
        }
        phoneW = new JTextField();
        if (Integer.valueOf(mWin.getOperator()[3]) >= 10) {
            phoneW.setEditable(true);
        } else {
            phoneW.setEditable(false);
            upPhoneW.setEnabled(false);
        }
        accaunt = new JTextField();
        if (Integer.valueOf(mWin.getOperator()[3]) >= 10) {
            accaunt.setEditable(true);
        } else {
            accaunt.setEditable(false);
        }
        dognum = new JTextField();
        dognum.setText(getLiteID(conn));
        dognum.setEditable(false);
        dognum.setEnabled(false);
        abonPort = new JTextField();
        if (Integer.valueOf(mWin.getOperator()[3]) >= 20) {
            abonPort.setEditable(true);
        } else {
            abonPort.setEditable(false);
            upPort.setEnabled(false);
        }

        descr = new JTextArea();
        scrolDescr = new JScrollPane(descr);
        scrolDescr.setPreferredSize(new Dimension(300, 100));
        descr.setLineWrap(true);

        window.setTitle("Абонент номер договора " + userInfo[1]);

        saveAbon = new JButton("Обновить");
        saveAbon.addActionListener(new UpdateAll(mWin, userInfo));
        exitForm = new JButton("Закрыть");
        exitForm.addActionListener(new CloseWindow());

        addPay = new JButton("Добавить");
        addPay.addActionListener(new OpenWindowForPay());
        addPay.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/plus.png")));
        if (Integer.parseInt(mWin.getOperator()[3]) > 5) {
            addPay.setEnabled(true);
        } else {
            addPay.setEnabled(false);
        }
        updatePay = new JButton("Обновить");
        updatePay.addActionListener(new UpdatePays());
        updatePay.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/update.png")));
        if (Integer.parseInt(mWin.getOperator()[3]) > 5) {
            updatePay.setEnabled(true);
        } else {
            updatePay.setEnabled(false);
        }
        discon = new JButton("Отключить");
        discon.addActionListener(new ActionDisconnect(userInfo, frame));
        discon.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/remove.png")));
        if (Integer.parseInt(mWin.getOperator()[3]) > 5) {
            discon.setEnabled(true);
        } else {
            discon.setEnabled(false);
        }
        status = new JButton("Выполнено");
        status.addActionListener(new Disconnect(userInfo));
        status.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/status.png")));
        if (Integer.parseInt(mWin.getOperator()[3]) > 5) {

            if (Integer.parseInt(userInfo[16]) != 6 && Integer.parseInt(userInfo[16]) != 1) {

                status.setEnabled(true);
                status.setVisible(true);
            } else {
                status.setEnabled(false);
                status.setVisible(false);
            }
        } else {
            status.setEnabled(false);
            status.setVisible(false);
        }
        connect = new JButton("Подключить");
        connect.addActionListener(new ActionConnect(frame, userInfo));
        connect.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/connect.png")));

        getConnectInfo = new JButton("События");
        getConnectInfo.addActionListener(new getHistory());
        getConnectInfo.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/find.png")));

            street = new JComboBox(setArrStreet(conn));
        if (Integer.valueOf(mWin.getOperator()[3]) >= 10) {
            street.setEnabled(true);
        } else {
            street.setEnabled(false);
            upAddr.setEnabled(false);
        }
        street.addActionListener(new setHouse());

        house = new JComboBox();
        setArrHouse(conn);
        if (Integer.valueOf(mWin.getOperator()[3]) >= 10) {
            house.setEnabled(true);
        } else {
            house.setEnabled(false);
            upHouse.setEnabled(false);
        }
        comm = new JComboBox(setArrComm(conn, userInfo));
        if (Integer.valueOf(mWin.getOperator()[3]) >= 20) {
            comm.setEnabled(true);
        } else {
            comm.setEnabled(false);
            upComm.setEnabled(false);
            addComm.setEnabled(false);
        }


        if (selCom > 0) {
            comm.setSelectedIndex(selCom);
        }


        tariff = new JComboBox(setArrTariff(conn));
        if (Integer.valueOf(mWin.getOperator()[3]) >= 20) {
            tariff.setEnabled(true);
        } else {
            tariff.setEnabled(false);
            upTariff.setEnabled(false);
        }

        for (int i = 0; i < arrTariff.size(); i++)
            if (Integer.parseInt(arrTariff.get(i)[0]) == idTariff) tariff.setSelectedIndex(i);
        for (int i = 0; i < arrStreet.size(); i++)
            if (Integer.parseInt(userInfo[13]) == Integer.parseInt(arrStreet.get(i)[0])) street.setSelectedIndex(i);
        for (int i = 0; i < house.getItemCount(); i++)
            if (userInfo[11].equals(house.getItemAt(i))) house.setSelectedIndex(i);

        scrollBar = setTable();
        calendar = new JDateChooser("yyyy-MM-dd HH:mm:ss", "####-##-##", '0');
        calendar.setEnabled(false);

        activation = new JCheckBox();
        activation.addActionListener(new ActionSelect());
        if (Integer.valueOf(mWin.getOperator()[3]) >= 20 || Integer.valueOf(mWin.getOperator()[3]) >= 1) {
            activation.setEnabled(true);
        } else {
            activation.setEnabled(false);
        }
        formPanel.add(new JLabel("Номер договра"), "grow");
        formPanel.add(dognum, "grow,span 2,wrap");
        dognum.setText(userInfo[1]);
        formPanel.add(new JLabel("ФИО"), "grow");
        formPanel.add(fio, "grow,span 2,wrap");
        fio.setText(userInfo[2]);   //formPanel.add(upName,"wrap");
        formPanel.add(new JLabel("Тариф"), "grow");
        formPanel.add(tariff, "grow,span 2,wrap");                               //formPanel.add(upTariff,"wrap");
        formPanel.add(new JLabel("Улица"), "grow");
        formPanel.add(street, "grow");
        formPanel.add(addAddres, "growy,push,span 1 3");   // formPanel.add(upAddr,"push,growy, span 1 3");
        formPanel.add(new JLabel("Дом"), "grow");
        formPanel.add(house, "grow");                               //formPanel.add(upHouse,"push");
        formPanel.add(new JLabel("Квартира"), "grow");
        formPanel.add(flat, "grow");
        flat.setText(userInfo[4]);  //formPanel.add(upFlat,"wrap");
        formPanel.add(new JLabel("Телефон домашний"), "grow");
        formPanel.add(phone, "grow,span 2,wrap");
        phone.setText(userInfo[6]); //formPanel.add(upPhone,"wrap");
        formPanel.add(new JLabel("Телефон мобильный"), "grow");
        formPanel.add(phoneM, "grow,span 2,wrap");
        phoneM.setText(userInfo[7]);//formPanel.add(upPhoneM,"wrap");
        formPanel.add(new JLabel("Телефон рабочий"), "grow");
        formPanel.add(phoneW, "grow,span 2,wrap");
        phoneW.setText(userInfo[8]);//formPanel.add(upPhoneW,"wrap");

        formPanel.add(new JLabel("Подключение"), "grow");
        if (userInfo[12] == null) {
            formPanel.add(activation, "split 4");
            formPanel.add(calendar);
            //formPanel.add(addConn);
        } else {
            formPanel.add(new JLabel(userInfo[12].replace(".0", "")), "split 2");
        }

        formPanel.add(getConnectInfo, "wrap");

        formPanel.add(new JLabel("Имя Комм"), "grow");
        formPanel.add(comm, "grow,split 2");
        formPanel.add(addComm);
        formPanel.add(upComm);
        formPanel.add(new JLabel("Порт"), "grow");
        formPanel.add(abonPort, "grow");
        formPanel.add(upPort);
        abonPort.setText(userInfo[15]);
        formPanel.add(new JLabel("Примечание"), "grow");
        formPanel.add(scrolDescr, "grow,wrap");
        descr.setText(userInfo[17]);

        buttonPanel.add(saveAbon);  // saveAbon.addActionListener  (new ActionAddAbonent());
        buttonPanel.add(exitForm);  //exitForm.addActionListener  (new ActionClose());
        JLabel titlePanel = new JLabel("Платежи абонента");
        titlePanel.setFont(new Font(titlePanel.getFont().getFontName(), Font.PLAIN, 20));
        setAddPay(idAbonent);

        rightPanel.add(titlePanel, "alignx center,wrap,span 8");
        rightPanel.add(addPay);

        if (Integer.valueOf(mainWindow.getOperator()[3]) > 10) {
            if ((Integer.parseInt(userInfo[18]) == 1 || Integer.parseInt(userInfo[18]) == 3) && (Integer.parseInt(userInfo[16])==4 || Integer.parseInt(userInfo[16])==6) && userInfo[12] != null && userInfo[12].trim().length() > 0)
                rightPanel.add(connect,"hidemode 3");
            if ((Integer.parseInt(userInfo[18]) == 2 || Integer.parseInt(userInfo[18]) == 4 || Integer.parseInt(userInfo[18]) == 1 || Integer.parseInt(userInfo[18]) == 3 || Integer.parseInt(userInfo[18]) == 5) && Integer.parseInt(userInfo[16]) > 0)
                rightPanel.add(discon, "hidemode 3");
        }

        rightPanel.add(updatePay, "wrap");
        rightPanel.add(scrollBar, "span 8");
        leftPanel.add(formPanel);
        leftPanel.add(buttonPanel);
        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel, "aligny top,alignx left");
        window.add(mainPanel);
        window.setSize(950, 550);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        window.setLocation(screenSize.width / 2 - window.getSize().width / 2, screenSize.height / 2 - window.getSize().height / 2);
        window.setModal(true);
        window.setVisible(true);
        window.validate();
        window.repaint();
    }

    class setHouse implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                setArrHouse(connectDB);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    class getHistory implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            showEvents();
        }
    }

    void showEvents() {
        win = new JDialog(window, "События абонента");
        MigLayout layout = new MigLayout("wrap 2", "grow,fill");
        JPanel mainPanel = new JPanel(new MigLayout("wrap 1","grow,fill"));
        TableModel modelPay = new TableModel(new Object[][]{},new Object[]{"Дата","Статус","Оператор"});
        JTable table = new JTable(modelPay);

        //String query = "SELECT * FROM payment WHERE idAbonent="+ infoAbon.getIdAbon() + " ORDER BY id DESC";
        String query = "SELECT evs.name,ev.dtEvent,op.name as oper FROM events_status evs,events ev,operator op WHERE evs.code = ev.idEvent AND ev.idOper = op.id AND ev.idAbon = "+idAbonent;
        try {

            ResultSet resultSet = mainWindow.getConnectDB().getConnect().createStatement().executeQuery(query);

            while (resultSet.next()) {
                modelPay.addRow(new Object[]{
                        resultSet.getString("dtEvent"),
                        resultSet.getString("name"),
                        resultSet.getString("oper")
                });
            }
        } catch (SQLException e1) {
            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        Dimension screnSize = Toolkit.getDefaultToolkit().getScreenSize();
        JScrollPane scrollPane = new JScrollPane(table);
        mainPanel.add(scrollPane);
        win.add(mainPanel);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        win.setLocation(screenSize.width / 2 - win.getSize().width / 2, screenSize.height / 2 - win.getSize().height / 2);
        win.add(mainPanel);
        win.setSize(300, 150);
        win.setModal(true);
        win.setVisible(true);
        win.validate();
        win.repaint();
    }

    class ActionConnect implements ActionListener {
        private JFrame frame;
        private String[] userinfos;

        ActionConnect(JFrame frame, String[] userinfo) {
            this.frame = frame;
            this.userinfos = userinfo;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            new ChangeTariff(frame, mainWindow, userinfos);
        }
    }

    class ActionSelect implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (activation.isSelected()) {
                calendar.setEnabled(true);
                //addConn.setEnabled(true);
            } else {
                calendar.setEnabled(false);
                //addConn.setEnabled(false);
            }
        }
    }

    class UpdateTariff implements ActionListener {
        private String[] userinfo;

        UpdateTariff(String[] userInfo) {
            userinfo = userInfo;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            updateTariff(userinfo);
        }
    }

    class Disconnect implements ActionListener {
        String[] userinfo;
        String strDate = "";

        Disconnect(String[] user) {
            this.userinfo = user;
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            strDate = formatDate.format(new Date());
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            String query = "UPDATE abonents SET status=6 WHERE id=" + idAbonent;
            try {
                connectDB.getConnect().createStatement().executeUpdate(query);
            } catch (SQLException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            query = "INSERT INTO log (idOperator,dtEdit,old,new,type) VALUES (" + mainWindow.getOperator()[4] + ",'" + strDate + "','status=" + userinfo[16] + ";','status=6',1)";
            try {
                connectDB.getConnect().createStatement().executeUpdate(query);
            } catch (SQLException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            status.setVisible(false);
        }
    }

    void updateTariff(String[] userinfo) {
        Integer idTariff = Integer.parseInt(arrTariff.get(tariff.getSelectedIndex())[0]);
        String query = "";
        int n;

        if (idTariff != this.idTariff) {
            if ((n = JOptionPane.showConfirmDialog(window, "Вы действительно хотите изменить тарифный план", "Предупреждение", JOptionPane.YES_NO_OPTION)) == 0) {
                if (idTariff != Integer.parseInt(userinfo[5])) {
                    Date date = new Date();
                    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String strDate = formatDate.format(date);

                    try {
                        if (userinfo[12] != null && userinfo[12].length() > 5) {
                            query = "UPDATE connection SET dtDisconn = '" + strDate + "',status=0 WHERE idAbonent=" + userinfo[0] + " AND status=1";
                            LogAction.insertEvent(connectDB.getConnect(), Integer.parseInt(userinfo[0]), 3, Integer.parseInt(mainWindow.getOperator()[4]));
                            connectDB.getConnect().createStatement().executeUpdate(query);
                            query = "INSERT INTO connection (dtConn,idTariff,idAbonent) VALUES ('" + strDate + "'," + idTariff + "," + userinfo[0] + ")";
                            LogAction.insertEvent(connectDB.getConnect(), Integer.parseInt(userinfo[0]), 4, Integer.parseInt(mainWindow.getOperator()[4]));
                            connectDB.getConnect().createStatement().executeUpdate(query);
                        } else {
                            //query = "INSERT INTO connection (dtConn,dtDisconn,idTariff,idAbonent) VALUES ('" + strDate + "','" + strDate + "'," + idTariff + "," + userinfo[0] + ")";
                            //LogAction.insertEvent(connectDB.getConnect(), Integer.parseInt(userinfo[0]), 4, Integer.parseInt(mainWindow.getOperator()[4]));
                            //connectDB.getConnect().createStatement().executeUpdate(query);
                        }
                        query = "UPDATE abonents SET idTariff=" + idTariff + " WHERE id = " + userinfo[0];
                        connectDB.getConnect().createStatement().executeUpdate(query);
                        for (int i = 0; i < arrTariff.size(); i++) {
                            if (idTariff == Integer.parseInt(arrTariff.get(i)[0])) {
                                tariff.setSelectedIndex(i);
                            }
                        }
                        connectDB.getConnect().createStatement().executeUpdate(query);
                    } catch (SQLException e1) {
                        e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }

                }
            }
        }


    }

    class UpdateName implements ActionListener {
        private String[] userinfo;

        UpdateName(String[] uInfo) {
            userinfo = uInfo;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            updateName(userinfo);
        }
    }


    void updateName(String[] userinfo) {
        String query = "";
        if (!fio.getText().trim().equals(userinfo[2])) {
            query = "UPDATE abonents SET name='" + fio.getText().trim() + "' WHERE id=" + userinfo[0];
            try {
                connectDB.getConnect().createStatement().executeUpdate(query);
            } catch (SQLException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    class UpdateAddr implements ActionListener {
        private String[] userinfo;

        UpdateAddr(String[] uInfo) {
            userinfo = uInfo;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            updateAddr(userinfo);
        }
    }

    void updateAddr(String[] userinfo) {
        String query;
        try {
            query = "UPDATE abonents SET idAdres=(SELECT id FROM houses WHERE house='" + house.getItemAt(house.getSelectedIndex()) + "' AND id_street=" + arrStreet.get(street.getSelectedIndex())[0] + " ),flat='" + flat.getText().trim() + "' WHERE id=" + userinfo[0];
            connectDB.getConnect().createStatement().executeUpdate(query);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    class UpdateStr implements ActionListener {
        private String[] userinfo;
        private Integer type;

        UpdateStr(String[] uInfo, Integer t) {
            userinfo = uInfo;
            type = t;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            updateStr(userinfo, type);
        }
    }

    void updateStr(String[] userinfo, Integer type) {
        String query = "";
        switch (type) {
            case 1:
                query = "UPDATE abonents SET phone_h='" + phone.getText().trim() + "'  WHERE id=" + userinfo[0];
                break;
            case 2:
                query = "UPDATE abonents SET phone_m='" + phoneM.getText().trim() + "' WHERE id=" + userinfo[0];
                break;
            case 3:
                query = "UPDATE abonents SET phone_w='" + phoneW.getText().trim() + "' WHERE id=" + userinfo[0];
                break;
        }
        try {
            connectDB.getConnect().createStatement().executeUpdate(query);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    class UpdateAll implements ActionListener {
        private MainWindow mainWindow;
        private String[] userinfo;

        UpdateAll(MainWindow mWin, String[] uInfo) {
            mainWindow = mWin;
            userinfo = uInfo;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String query = "";
            try {
                updateDescr();
            } catch (SQLException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            if (Integer.parseInt(mainWindow.getOperator()[3]) >= 10) {
                updateAddr(userinfo);
                updateName(userinfo);
                updateStr(userinfo, 1);
                updateStr(userinfo, 2);
                updateStr(userinfo, 3);
                //updateDescr();
            }

            if (Integer.parseInt(mainWindow.getOperator()[3]) >= 20 || Integer.parseInt(mainWindow.getOperator()[3]) == 1) {


                updateTariff(userinfo);
                if (activation.isSelected() && comm.getSelectedIndex() == 0) {
                    JOptionPane.showMessageDialog(window, "Невозможно подключить \nабонента без оборудования", "Предупреждение", JOptionPane.WARNING_MESSAGE);

                /*
                    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = calendar.getDate();
                    String strDate = formatDate.format(date);
                    query = "UPDATE abonents SET idComm=" + arrComm.get(comm.getSelectedIndex())[0] + ",port=" + abonPort.getText().trim() + " WHERE id=" + idAbonent;
                    try {
                        connectDB.getConnect().createStatement().executeUpdate(query);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    query = "UPDATE abonents SET dateConnect='" + strDate + "',status=1 WHERE id=" + idAbonent;
                    try {
                        connectDB.getConnect().createStatement().executeUpdate(query);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    query = "INSERT INTO connection (dtConn,dtDisconn,idTariff,idAbonent,status) VALUES ('" + strDate + "','2020-01-01'," + idTariff + "," + idAbonent + ",1)";
                    try {
                        connectDB.getConnect().createStatement().executeUpdate(query);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }*/
                } else if (activation.isSelected() && comm.getSelectedIndex() > 0) {
                    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = calendar.getDate();
                    String strDate = formatDate.format(date);
                    query = "UPDATE abonents SET dateConnect='" + strDate + "',status=1,statconn=2 WHERE id=" + idAbonent;
                    try {
                        LogAction.insertEvent(connectDB.getConnect(), Integer.parseInt(userinfo[0]), 4, Integer.parseInt(mainWindow.getOperator()[4]));
                        connectDB.getConnect().createStatement().executeUpdate(query);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                    query = "UPDATE abonents SET idComm=" + arrComm.get(comm.getSelectedIndex())[0] + ",port=" + abonPort.getText().trim() + " WHERE id=" + idAbonent;
                    try {
                        connectDB.getConnect().createStatement().executeUpdate(query);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                    query = "INSERT INTO connection (dtConn,dtDisconn,idTariff,idAbonent,status) VALUES ('" + strDate + "','2020-01-01'," + idTariff + "," + idAbonent + ",1)";
                    try {
                        connectDB.getConnect().createStatement().executeUpdate(query);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }

                } else if (comm.getSelectedIndex() > 0 && Integer.parseInt(mainWindow.getOperator()[3]) >= 20) {
                    query = "UPDATE abonents SET idComm=" + arrComm.get(comm.getSelectedIndex())[0] + ",port=" + abonPort.getText().trim() + " WHERE id=" + idAbonent;
                    try {
                        connectDB.getConnect().createStatement().executeUpdate(query);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                } else if (comm.getSelectedIndex() == 0 && Integer.parseInt(mainWindow.getOperator()[3]) >= 20) {
                    query = "UPDATE abonents SET idComm=null,port=null WHERE id=" + idAbonent;
                    try {
                        connectDB.getConnect().createStatement().executeUpdate(query);
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }

            if (Integer.parseInt(mainWindow.getOperator()[3]) >= 30) {
                query = "UPDATE abonents SET idComm=" + arrComm.get(comm.getSelectedIndex())[0] + " WHERE id=" + idAbonent;
                try {
                    connectDB.getConnect().createStatement().executeUpdate(query);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }

            window.dispose();
        }
    }

    class ActionAddStreet implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            String nameStreet = addFiled.getText();
            try {
                addSqlStreet(nameStreet);
                street = null;
                street = new JComboBox(setArrStreet(connectDB));
                setArrHouse(connectDB);
                win.dispose();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    class ActionAddHouse implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int idStreet = Integer.parseInt(arrStreet.get(street.getSelectedIndex())[0]);
            String numHouse = addFiled.getText();
            try {
                addSqlHouse(idStreet, numHouse);
                setArrHouse(connectDB);
                win.dispose();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    class ActionCloseWind implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            win.dispose();
        }
    }

    class ActionAddAbonent implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                addSqlAbonent();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    class ActionAddIdle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                addSqlIdle();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    class ActionClose implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            window.dispose();
        }
    }

    class OpenWindowForPay implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                openWindowAddPay(idAbonent, idTariff);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    class SelectCountMonth implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox comb = (JComboBox) e.getSource();

            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String strDate = formatDate.format(date);
            Integer idTariff = Integer.parseInt(arrTariff.get(tariff.getSelectedIndex())[0]);
            String query = "SELECT * FROM ext_tariff WHERE datebegin<='" + strDate + "' AND dateend>='" + strDate + "' AND idtarif=" + idTariff;
            try {
                ResultSet result = connectDB.getConnect().createStatement().executeQuery(query);
                if (result.next()) {
                    Integer summ = Integer.parseInt(String.valueOf(comb.getSelectedItem()));
                    cost.setText((summ * result.getInt("sum")) + "");
                }
            } catch (SQLException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    class SelectTariff implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Integer idTariff = Integer.parseInt(arrTariff.get(tariff.getSelectedIndex())[0]);
            Date date = new Date();
            String strDate = formatDate.format(date);

            String query = "SELECT * FROM ext_tariff WHERE datebegin<='" + strDate + "' AND dateend>='" + strDate + "' AND idtarif=" + idTariff;

            try {
                ResultSet result = connectDB.getConnect().createStatement().executeQuery(query);
                if (result.next()) {
                    cost.setText(result.getString("sum"));
                    intMonth.setSelectedIndex(0);
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }


        }
    }

    class CloseWindowForPay implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            win.dispose();
        }
    }

    class CloseWindow implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            window.dispose();
        }
    }

    class ActionAddPay implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                if (Float.parseFloat(cost.getText()) != 0 && Integer.parseInt(arrTariff.get(tariff.getSelectedIndex())[0]) > 0) {
                    SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    String strDate = formatDate.format(date);


                    String query = "INSERT INTO payment (`date`,`cost`,`idAbonent`,`idOperator`) " +
                            "VALUES ('" + strDate + "'," + cost.getText() + "," + idAbonent + "," + mainWindow.getOperator()[4] + ")";
                    try {
                        connectDB.getConnect().createStatement().executeUpdate(query);
                        win.dispose();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            } catch (NumberFormatException num) {

            }

        }
    }

    class UpdatePays implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                while (model.getRowCount() > 0) model.removeRow(0);
                setAddPay(idAbonent);
            } catch (SQLException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    private void updateDescr() throws SQLException {
        String query = "UPDATE abonents SET descr='" + descr.getText().trim() + "' WHERE id=" + idAbonent;
        connectDB.getConnect().createStatement().executeUpdate(query);
    }

    class ActionAddComm implements ActionListener {
        MainWindow mainWindow;
        String[] userInfo;
        EditUser edit;

        ActionAddComm(MainWindow mWin, String[] user) {
            mainWindow = mWin;
            userInfo = user;

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new AddCommutator(mainWindow, window, 0);
                //setArrComm(mainWindow.getConnectDB(),userInfo);
                //comm    =   new JComboBox(setArrComm(mainWindow.getConnectDB(),userInfo));
            } catch (ParseException e1) {
                e1.printStackTrace();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }


    class updateComm implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            updateListComm(String.valueOf(idAbonent));
        }
    }

    public void updateListComm(String user) {
        String[] userinfo = new String[15];
        userinfo[14] = user;
        try {
            // comm.addItem(arrComm.get(i)[1]);
            //comm = new JComboBox(setArrComm(mainWindow.getConnectDB(),userinfo));
            Object[] arg = setArrComm(mainWindow.getConnectDB(), userinfo);
            for (int i = 0; i < arg.length; i++) {
                comm.addItem(arg[i]);
            }

            //if (selCom >= 0) { comm.setSelectedIndex(selCom); }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getIdAbon() {

        return String.valueOf(idAbonent);
    }

    class ActionDisconnect implements ActionListener {
        private String[] userinfo;
        private JFrame frame;

        ActionDisconnect(String[] userinfo, JFrame frame) {
            this.frame = frame;
            this.userinfo = userinfo;


        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int n;
            String query = "";
            String idStatus = null;
            new DisconnectAbon(frame, mainWindow, Integer.parseInt(userinfo[0]));
        }
    }


    class addAddr implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                if (e.getComponent().getName().equals("house")) {
                    addHouseW();
                } else if (e.getComponent().getName().equals("street")) {
                    addStreetW();
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

    class ActionAddConnect implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2 && addConn.isEnabled()) {
                SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = calendar.getDate();

                if (date != null) {
                    if (date.getYear() > 0) {
                        String strDate = formatDate.format(date);
                        if (JOptionPane.showConfirmDialog(null, "Вы уверены что хотите обновить дату подключения абонента?", "Добавляем подключение", JOptionPane.YES_NO_OPTION) == 0) {
                            String query = "UPDATE abonents SET dateConnect='" + strDate + "' WHERE id=" + idAbonent;
                            //id 	dtConn 	dtDisconn 	idTariff 	idAbonent 	status
                            String insQuery = "INSERT INTO connection (dtConn,idTariff,idAbonent) VALUES ('" + strDate + "'," + idTariff + "," + idAbonent + ")";
                            try {
                                LogAction.insertEvent(connectDB.getConnect(), idAbonent, 3, Integer.parseInt(mainWindow.getOperator()[4]));
                                connectDB.getConnect().createStatement().executeUpdate(query);
                                connectDB.getConnect().createStatement().executeUpdate(insQuery);
                            } catch (SQLException e1) {
                                e1.printStackTrace();
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(win, "Укажите дату", "Ошибка", JOptionPane.WARNING_MESSAGE);
                        calendar.setFocusable(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(win, "Укажите дату", "Ошибка", JOptionPane.WARNING_MESSAGE);
                    calendar.setFocusable(true);
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

    String[] getAbonInfo(String idAbon) throws SQLException {
        String query = "SELECT * FROM abonents WHERE id = " + idAbon;
        ResultSet result = connectDB.getConnect().createStatement().executeQuery(query);
        if (result.next()) {
            //id 	idDog 	name 	idAdres 	flat 	idTariff 	phone_h 	phone_m 	phone_w 	dateConnect 	dateDogovor 	dateZayavka 	balans 	dt_balance 	status
            String[] userinfo = new String[]{
                    result.getString("id"),     //0
                    result.getString("idDog"),  //1
                    result.getString("name"),
                    result.getString("idAdres"),
                    result.getString("flat"),
                    result.getString("idTariff"),  //5
                    result.getString("phone_h"),
                    result.getString("phone_m"),
                    result.getString("phone_w"),
                    "",//nametariff                   //9
                    "",//name street                  //10
                    "",//house 11
                    result.getString("dateConnect"),//12
                    "",//dtConn; 13
                    result.getString("idComm"), //14
                    result.getString("port"),   //15
                    result.getString("status"), //16
                    result.getString("descr"),   //17
                    result.getString("statconn")      //18
            };

            query = "SELECT name FROM tariff WHERE id=" + userinfo[5];
            result = connectDB.getConnect().createStatement().executeQuery(query);
            if (result.next()) {
                userinfo[9] = result.getString("name");
            }
            query = "SELECT str.name as street,hs.house,str.id as idstr FROM houses hs, streets str WHERE str.id=hs.id_street AND hs.id=" + userinfo[3];
            result = connectDB.getConnect().createStatement().executeQuery(query);
            if (result.next()) {
                userinfo[10] = result.getString("street");
                userinfo[11] = result.getString("house");
                userinfo[13] = result.getString("idstr");
            }
            return userinfo;
        }
        return new String[]{};
    }

    String[] setArrStreet(ConnectDB connectDB) throws SQLException {
        String query = "SELECT * FROM streets ORDER BY name ASC";
        Integer count = 0;
        ResultSet resultSet = connectDB.getConnect().createStatement().executeQuery(query);

        if (street != null) street.removeAllItems();
        while (arrStreet.size() > 0) {
            arrStreet.remove(0);
        }

        while (resultSet.next()) {
            arrStreet.add(new String[]{resultSet.getString("id"), resultSet.getString("name")});
            count++;
        }
        String[] obj = new String[count];
        for (int i = 0; i < arrStreet.size(); i++) {
            obj[i] = arrStreet.get(i)[1];
        }
        return obj;
    }

    String[] setArrComm(ConnectDB connectDB, String[] userinfo) throws SQLException {
        String query = "SELECT * FROM commutator ORDER BY name ASC";
        if (comm != null) comm.removeAllItems();
        while (arrComm.size() > 0) arrComm.remove(0);

        ResultSet result = connectDB.getConnect().createStatement().executeQuery(query);
        Integer count = 1;

        arrComm.add(new String[]{"0", "Нет коммутатора", "Нет коммутатора"});
        while (result.next()) {
            arrComm.add(new String[]{result.getString("id"), result.getString("name"), result.getString("ipaddres")});
            count++;
        }

        String[] obj = new String[count];
        Integer sel = 01;
        for (int i = 0; i < arrComm.size(); i++) {
            obj[i] = arrComm.get(i)[1];
            if (userinfo[14] != null) {
                if (Integer.parseInt(userinfo[14]) == Integer.parseInt(arrComm.get(i)[0])) {
                    selCom = i;

                }
            }

        }

        return obj;
    }

    String[] setArrTariff(ConnectDB connectDB) throws SQLException {
        String query = "SELECT * FROM tariff ORDER BY name ASC";
        Integer count = 0;
        ResultSet resultSet = connectDB.getConnect().createStatement().executeQuery(query);
        if (tariff != null) tariff.removeAllItems();

        while (resultSet.next()) {
            arrTariff.add(new String[]{resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("descr")});
            count++;
        }

        String[] obj = new String[count];
        for (int i = 0; i < arrTariff.size(); i++) obj[i] = arrTariff.get(i)[1];
        return obj;
    }

    String getLiteID(ConnectDB connectDB) throws SQLException {
        String query = "SELECT * FROM abonents ORDER BY id DESC LIMIT 1";
        ResultSet resultSet = connectDB.getConnect().createStatement().executeQuery(query);

        if (resultSet.next()) {
            return String.valueOf(resultSet.getInt("id") + 1);
        } else {
            return "0";
        }
    }

    void setArrHouse(ConnectDB connectDB) throws SQLException {
        String query = "SELECT * FROM houses WHERE id_street=" + arrStreet.get(street.getSelectedIndex())[0] + " ORDER BY house ASC";
        Integer count = 0;
        ResultSet resultSet = connectDB.getConnect().createStatement().executeQuery(query);
        if (house != null) house.removeAllItems();
        while (resultSet.next()) house.addItem(resultSet.getString("house"));
    }

    void addHouseW() {
        win = new JDialog(window, "Добавить адрес");
        MigLayout layout = new MigLayout("wrap 3", "grow,fill");
        JPanel mpanel = new JPanel(layout);
        JComboBox street = new JComboBox();
        JTextField house;
        JButton save = new JButton("Сохранить"), clear = new JButton("Очистить"), exit = new JButton("Закрыть");
        save.addActionListener(new ActionAddHouse());
        exit.addActionListener(new ActionCloseWind());
        for (int i = 0; i < arrStreet.size(); i++)
            street.addItem(arrStreet.get(i)[1]);

        addFiled = new JTextField();
        addFiled.setToolTipText("Введите номер дома для добавления");

        mpanel.add(street, "wrap,span 3");
        mpanel.add(addFiled, "wrap,span 3");
        mpanel.add(save);
        mpanel.add(clear);
        mpanel.add(exit);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        win.setLocation(screenSize.width / 2 - win.getSize().width / 2, screenSize.height / 2 - win.getSize().height / 2);
        win.add(mpanel);
        win.setSize(300, 150);
        win.setModal(true);
        win.setVisible(true);
        win.validate();
        win.repaint();
    }

    void addStreetW() {
        win = new JDialog(window, "Добавить адрес");
        MigLayout layout = new MigLayout("wrap 3", "grow,fill");
        JPanel mpanel = new JPanel(layout);
        JComboBox street = new JComboBox();

        JButton save = new JButton("Сохранить"), clear = new JButton("Очистить"), exit = new JButton("Закрыть");
        save.addActionListener(new ActionAddStreet());
        exit.addActionListener(new ActionCloseWind());
        for (int i = 0; i < arrStreet.size(); i++) {
            street.addItem(arrStreet.get(i)[1]);
        }
        addFiled = new JTextField();
        addFiled.setToolTipText("Введите название улицы для добавления");

        mpanel.add(street, "wrap,span 3");
        mpanel.add(addFiled, "wrap,span 3");
        mpanel.add(save);
        mpanel.add(clear);
        mpanel.add(exit);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        win.setLocation(screenSize.width / 2 - win.getSize().width / 2, screenSize.height / 2 - win.getSize().height / 2);
        win.add(mpanel);
        win.setSize(300, 150);
        win.setModal(true);
        win.setVisible(true);
        win.validate();
        win.repaint();
    }

    void addSqlIdle() throws SQLException {
        String query = "INSERT INTO abonents (id,idDog,status) VALUES (" + dognum.getText().trim() + "," + dognum.getText().trim() + ",0)";
        connectDB.getConnect().createStatement().executeUpdate(query);
        window.dispose();
    }

    void addSqlAbonent() throws SQLException {
        Integer idStreet;
        String housNumb;
        if (house.getItemCount() > 0 && fio.getText().trim().length() > 0 && flat.getText().trim().length() > 0) {
            try {


                housNumb = house.getSelectedItem().toString();

                Integer idAddres = getIDAddr(Integer.parseInt(arrStreet.get(street.getSelectedIndex())[0]), house.getSelectedItem().toString());
                Integer idTariff = Integer.parseInt(arrTariff.get(tariff.getSelectedIndex())[0]);
                SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String strDate = formatDate.format(date);

                String query = "INSERT INTO abonents (id,idDog,name,idAdres,flat,idTariff,phone_h,phone_m,phone_w,dateDogovor,idComm) " +
                        "VALUES (" + dognum.getText().trim() + "," + dognum.getText().trim() + ",'" + fio.getText().trim() + "'," + idAddres + ",'" + flat.getText().trim() + "'," + idTariff + ",'" + phone.getText().trim() + "','" + phoneM.getText().trim() + "','" + phoneW.getText().trim() + "','" + strDate + "'," + arrComm.get(comm.getSelectedIndex())[0] + ")";

                Statement statement = connectDB.getConnect().createStatement();
                statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
                ResultSet result = statement.getGeneratedKeys();

                if (result.next()) {
                    Integer idAbon = result.getInt(1);
                    query = "";
                    connectDB.getConnect().createStatement().executeUpdate(query);
                }
                window.dispose();

            } catch (NumberFormatException num) {
                JOptionPane.showMessageDialog(win, "Cумму нельзя писать буквами", "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            if (fio.getText().trim().length() <= 0)
                JOptionPane.showMessageDialog(win, "Укажите имя абонента", "Ошибка", JOptionPane.WARNING_MESSAGE);
            if (flat.getText().trim().length() <= 0)
                JOptionPane.showMessageDialog(win, "Укажите номер квартиры", "Ошибка", JOptionPane.WARNING_MESSAGE);
            if (house.getItemCount() <= 0)
                JOptionPane.showMessageDialog(win, "Укажите номер дома или добавтье его", "Ошибка", JOptionPane.WARNING_MESSAGE);
        }

    }

    void setAddPay(Integer idAbon) throws SQLException {

        while (model.getRowCount() > 0) model.removeRow(0);
        String query = "SELECT pm.cost,pm.date,op.name FROM payment pm,operator op WHERE pm.idAbonent=" + idAbon + " AND pm.idOperator=op.id";

        ResultSet result = connectDB.getConnect().createStatement().executeQuery(query);

        while (result.next()) {
            String[] obj = new String[]{
                    result.getString("cost"),
                    result.getString("date").replace(".0", ""),
                    result.getString("name")
            };
            model.addRow(obj);
        }
    }

    void openWindowAddPay(Integer idAbon, Integer tariff) throws SQLException {
        win = new JDialog(window, "Добавить адрес");

        MigLayout layout = new MigLayout("wrap 3", "grow,fill");

        JPanel mpanel = new JPanel(layout);
        JComboBox listtarif = this.tariff;
        listtarif.addActionListener(new SelectTariff());
        listtarif.setEnabled(true);


        intMonth = new JComboBox(new Object[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});
        intMonth.addActionListener(new SelectCountMonth());
        cost = new JTextField();
        cost.setToolTipText("Введите сумму платежа");
        JButton save = new JButton("Добавить платеж");
        save.addActionListener(new ActionAddPay());
        JButton close = new JButton("Закрыть");
        close.addActionListener(new CloseWindowForPay());
        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String strDate = formatDate.format(date);

        mpanel.add(new JLabel("Сумма"));
        mpanel.add(new JLabel("Тариф"));
        mpanel.add(new JLabel("Кол-во"));
        mpanel.add(cost);
        mpanel.add(listtarif);
        for (int i = 0; i < arrTariff.size(); i++) {
            if (Integer.parseInt(arrTariff.get(i)[0]) == tariff) {
                listtarif.setSelectedIndex(i);
                String query = "SELECT * FROM ext_tariff WHERE datebegin<='" + strDate + "' AND dateend>='" + strDate + "' AND idtarif=" + tariff;
                ResultSet result = connectDB.getConnect().createStatement().executeQuery(query);
                if (result.next()) {
                    cost.setText(result.getString("sum"));
                }
                break;
            }
        }

        mpanel.add(intMonth);

        mpanel.add(save);
        mpanel.add(close);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        win.setLocation(screenSize.width / 2 - win.getSize().width / 2, screenSize.height / 2 - win.getSize().height / 2);
        win.add(mpanel);
        win.setSize(420, 120);
        win.setModal(true);
        win.setVisible(true);
        win.validate();
        win.repaint();


    }

    boolean addSqlHouse(Integer idStreet, String numHouse) throws SQLException {

        if (numHouse.trim() != "") {
            String query = "INSERT INTO houses(id_street,house) VALUES (" + idStreet + ",'" + numHouse + "')";
            String chQuery = "SELECT * FROM houses WHERE id_street=" + idStreet + " AND house='" + numHouse + "'";
            ResultSet resultSet = connectDB.getConnect().createStatement().executeQuery(chQuery);
            if (resultSet.next() && resultSet.getInt("id") > 0) {
                JOptionPane.showMessageDialog(win, "Такой адрес уже существует", "Ошибка", JOptionPane.WARNING_MESSAGE);
                return false;
            } else {
                if (connectDB.getConnect().createStatement().executeUpdate(query) > 0) {
                    return true;
                } else {
                    return false;
                }
            }
        } else {
            return false;
        }

    }

    boolean addSqlStreet(String nameStreet) throws SQLException {
        if (nameStreet.trim() != "") {
            String chQuery = "SELECT * FROM streets WHERE name = '" + nameStreet.trim() + "'";

            ResultSet resultSet = connectDB.getConnect().createStatement().executeQuery(chQuery);
            if (resultSet.next() && resultSet.getInt("id") > 0) {
                JOptionPane.showMessageDialog(win, "Такая улица уже есть", "Ошибка", JOptionPane.WARNING_MESSAGE);
                return false;
            } else {
                String query = "INSERT INTO streets (name) VALUES ('" + nameStreet.trim() + "')";
                connectDB.getConnect().createStatement().executeUpdate(query);
                return true;
            }

        } else {
            return false;
        }

    }

    Integer getIDAddr(Integer idStreet, String house) throws SQLException {
        String query = "SELECT * FROM houses WHERE id_street=" + idStreet + " AND house='" + house + "'";
        ResultSet resultSet = connectDB.getConnect().createStatement().executeQuery(query);

        if (resultSet.next() && resultSet.getInt("id") > 0) {
            return resultSet.getInt("id");
        } else {
            return 0;
        }
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
                pan.setBackground(new Color(253, 255, 225));
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
        model = new TableModel(new Object[][]{}, new Object[]{"Сумма", "Дата приема", "Оператор"});
        table = new JTable(model);
        table.setDefaultRenderer(Object.class, new ColorTableCellRenderer());

        table.setAutoResizeMode(table.AUTO_RESIZE_OFF);

        table.getColumnModel().getColumn(0).setMaxWidth(100);
        table.getColumnModel().getColumn(1).setMaxWidth(200);
        table.getColumnModel().getColumn(2).setMaxWidth(150);

        table.getColumnModel().getColumn(0).setMinWidth(100);
        table.getColumnModel().getColumn(1).setMinWidth(200);
        table.getColumnModel().getColumn(2).setMinWidth(200);
        //table.addMouseListener(new ActionClickRow());
        JScrollPane scTab = new JScrollPane(table);
        scTab.setMaximumSize(new Dimension(500, 600));
        return scTab;
    }

}


