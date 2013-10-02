package ttkom.tt;

import net.miginfocom.swing.MigLayout;
import ttkom.tt.dbase.mysql.ConnectDB;
import ttkom.tt.windows.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 27.11.12
 * Time: 15:46
 * To change this template use File | Settings | File Templates.
 */
public class AddUser {
    private JPanel mainPanel, formPanel, buttonPanel, leftPanel, rightPanel;
    private MigLayout migMainPanel, migFormPanel, migButPanel;
    private JTextField fio, flat, phone, phoneM, phoneW, accaunt, dognum, payment;
    private JScrollPane scrollBar;
    private JComboBox street, house, tariff;
    private JButton addIdle, saveAbon, clearForm, exitForm;
    private JTextArea descr;
    private ConnectDB connectDB;
    private JDialog window, win;

    private ArrayList<String[]> arrStreet = new ArrayList<String[]>();  // first elemetn is id street, second element is name street
    private ArrayList<String[]> arrHouse = new ArrayList<String[]>();
    private ArrayList<String[]> arrTariff = new ArrayList<String[]>();
    private MainWindow mainWindow;
    private ClassSpiner spin;
    private JTextField addFiled;
    private JLabel strDate;

    AddUser(JFrame frame, ConnectDB conn, MainWindow mwin) throws SQLException {
        String lastID;
        mainWindow = mwin;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JLabel addLHouse = new JLabel(), addLStreet = new JLabel();
        addLHouse.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/fedit.png")));
        addLStreet.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/fedit.png")));
        addLHouse.setName("house");
        addLStreet.setName("street");
        addLHouse.addMouseListener(new addAddr());
        addLStreet.addMouseListener(new addAddr());

        window = new JDialog(frame);

        connectDB = conn;
        window.setTitle("Add abonents");

        migMainPanel = new MigLayout("wrap 2", "grow, fill");
        migFormPanel = new MigLayout("wrap 3", "[]5[300]5[20]", "[grow, fill][grow,fill]");
        migButPanel = new MigLayout("wrap 2", "grow, fill");

        mainPanel = new JPanel(migMainPanel);
        formPanel = new JPanel(migFormPanel);
        buttonPanel = new JPanel(migButPanel);
        leftPanel = new JPanel(new MigLayout("wrap 1", "grow, fill"));
        rightPanel = new JPanel(new MigLayout("wrap 1", "grow, fill"));


        fio = new JTextField();
        flat = new JTextField();
        phone = new JTextField();
        phoneM = new JTextField();
        phoneW = new JTextField();
        accaunt = new JTextField();
        dognum = new JTextField();
        dognum.setText(getLiteID(conn));
        payment = new JTextField();
        descr = new JTextArea();
        descr.setLineWrap(true);

        scrollBar = new JScrollPane(descr);
        scrollBar.setPreferredSize(new Dimension(300, 100));


        addIdle = new JButton("Добавить пустого");
        saveAbon = new JButton("Добавить абонента");
        clearForm = new JButton("Очистить форму");
        exitForm = new JButton("Выход");

        street = new JComboBox(setArrStreet(conn));
        street.addActionListener(new setHouse());
        house = new JComboBox();
        setArrHouse(conn);
        tariff = new JComboBox(setArrTariff(conn));
        tariff.addActionListener(new ActionSelectTariff());


        SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        strDate = new JLabel(formatDate.format(date));
        strDate.addMouseListener(new ActionEditDate());

        // ===form_panel===
        formPanel.add(new JLabel("Номер договра"), "grow");
        formPanel.add(dognum, "grow,wrap");
        formPanel.add(new JLabel("ФИО"), "grow");
        formPanel.add(fio, "grow,wrap");
        formPanel.add(new JLabel("Тариф"), "grow");
        formPanel.add(tariff, "grow,wrap");
        formPanel.add(new JLabel("Улица"), "grow");
        formPanel.add(street, "grow");
        formPanel.add(addLStreet);
        formPanel.add(new JLabel("Дом"), "grow");
        formPanel.add(house, "grow");
        formPanel.add(addLHouse);
        formPanel.add(new JLabel("Квартира"), "grow");
        formPanel.add(flat, "grow,wrap");
        formPanel.add(new JLabel("Телефон домашний"), "grow");
        formPanel.add(phone, "grow,wrap");
        formPanel.add(new JLabel("Телефон мобильный"), "grow");
        formPanel.add(phoneM, "grow,wrap");
        formPanel.add(new JLabel("Телефон рабочий"), "grow");
        formPanel.add(phoneW, "grow,wrap");
        formPanel.add(new JLabel("Дата договра"), "grow");
        formPanel.add(strDate, "grow,wrap");
        formPanel.add(new JLabel("Платеж"), "grow");
        formPanel.add(payment, "grow,wrap");
        formPanel.add(new JLabel("Примечание"), "grow");
        formPanel.add(scrollBar, "span 2,wrap");
        // ===form_panel===

        // ===butt_panel===
        buttonPanel.add(saveAbon);
        saveAbon.addActionListener(new ActionAddAbonent());
        buttonPanel.add(clearForm);
        buttonPanel.add(addIdle);
        addIdle.addActionListener(new ActionAddIdle());
        buttonPanel.add(exitForm);
        exitForm.addActionListener(new ActionClose());
        // ===butt_panel===


        leftPanel.add(formPanel);
        leftPanel.add(buttonPanel);

        mainPanel.add(leftPanel);
        mainPanel.add(rightPanel);
        window.add(mainPanel);

        window.setSize(500, 500);
        window.setLocation(screenSize.width / 2 - 250, screenSize.height / 2 - 250);
        window.setModal(true);
        window.setVisible(true);
        window.validate();
        window.repaint();
    }

    class ActionEditDate implements MouseListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            editDate();
        }

        @Override
        public void mousePressed(MouseEvent e) {
        }

        @Override
        public void mouseReleased(MouseEvent e) {
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }
    }

    void editDate() {
        win = new JDialog(window);
        MigLayout layer = new MigLayout("wrap 1");
        JPanel mainpanel = new JPanel(layer);
        spin = new ClassSpiner();
        JButton save = new JButton("save");
        save.addActionListener(new SaveDate());
        mainpanel.add(spin.getSpiner());
        mainpanel.add(save);
        win.add(mainpanel);
        win.setSize(230, 100);
        win.setModal(true);
        win.setVisible(true);
        win.validate();
        win.repaint();
    }

    class SaveDate implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Date dt = (Date) spin.getSpiner().getValue();
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDatemain = formatDate.format(dt);
            strDate.setText(strDatemain);
            win.dispose();
            window.validate();
            window.repaint();
            window.validate();
        }
    }


    class ClassSpiner {
        private JSpinner spin;
        private SpinnerModel dateModel;

        ClassSpiner() {
            Calendar calendar = Calendar.getInstance();
            Date initDate = calendar.getTime();

            calendar.add(Calendar.YEAR, -7);
            Date earliestDate = calendar.getTime();

            calendar.add(Calendar.YEAR, +20);
            Date latestDate = calendar.getTime();

            dateModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.YEAR);//ignored for user input2

            spin = new JSpinner();
            spin.setModel(dateModel);
            spin.setPreferredSize(new Dimension(200, 25));
            spin.setMaximumSize(new Dimension(200, 25));

            spin.setEditor(new JSpinner.DateEditor(spin, "dd-MM-yyyy HH:mm:00"));
        }

        JSpinner getSpiner() {
            return spin;
        }
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

    void setArrHouse(ConnectDB connectDB) throws SQLException {
        String query = "SELECT * FROM houses WHERE id_street=" + arrStreet.get(street.getSelectedIndex())[0] + " ORDER BY house ASC";
        System.out.println(query);
        Integer count = 0;

        ResultSet resultSet = connectDB.getConnect().createStatement().executeQuery(query);
        if (house != null) house.removeAllItems();

        while (resultSet.next()) {
            house.addItem(resultSet.getString("house"));
        }
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

    void addHouseW() {
        win = new JDialog(window, "Добавить адрес");
        MigLayout layout = new MigLayout("wrap 3", "grow,fill");
        JPanel mpanel = new JPanel(layout);
        JComboBox street = new JComboBox();
        JTextField house;
        JButton save = new JButton("Сохранить"), clear = new JButton("Очистить"), exit = new JButton("Закрыть");
        save.addActionListener(new ActionAddHouse());
        exit.addActionListener(new ActionCloseWind());
        for (int i = 0; i < arrStreet.size(); i++) {
            street.addItem(arrStreet.get(i)[1]);
        }
        addFiled = new JTextField();
        addFiled.setToolTipText("Введите номер дома для добавления");

        mpanel.add(street, "wrap,span 3");
        mpanel.add(addFiled, "wrap,span 3");
        mpanel.add(save);
        mpanel.add(clear);
        mpanel.add(exit);


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


        win.add(mpanel);
        win.setSize(300, 150);
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

    class ActionSelectTariff implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String strDate = formatDate.format(date);


            String query = "SELECT * FROM ext_tariff WHERE datebegin<='" + strDate + "' AND dateend>='" + strDate + "' AND idtarif=" + arrTariff.get(tariff.getSelectedIndex())[0];
            System.out.println(query);
            try {
                ResultSet resultSet = connectDB.getConnect().createStatement().executeQuery(query);
                if (resultSet.next()) {
                    payment.setText(resultSet.getString("sum"));
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    void addSqlIdle() throws SQLException {
        String query = "INSERT INTO abonents (idDog,status) VALUES (" + dognum.getText().trim() + ",0)";
        connectDB.getConnect().createStatement().executeUpdate(query);
        window.dispose();
    }

    void addSqlAbonent() throws SQLException {
        Integer idStreet;
        String housNumb;
        SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (house.getItemCount() > 0 && fio.getText().trim().length() > 0 && flat.getText().trim().length() > 0 && payment.getText().trim().length() > 0) {
            try {
                if (Integer.parseInt(payment.getText().trim()) > 0) {
                    Integer pay = Integer.parseInt(payment.getText().trim());
                    housNumb = house.getSelectedItem().toString();

                    Integer idAddres = getIDAddr(Integer.parseInt(arrStreet.get(street.getSelectedIndex())[0]), house.getSelectedItem().toString());
                    Integer idTariff = Integer.parseInt(arrTariff.get(tariff.getSelectedIndex())[0]);

                    String strDate = this.strDate.getText();
                    System.out.println(strDate);

                    String query = "INSERT INTO abonents (idDog,name,idAdres,flat,idTariff,phone_h,phone_m,phone_w,dateDogovor,descr) " +
                            "VALUES (" + dognum.getText().trim() + ",'" + fio.getText().trim() + "'," + idAddres + ",'" + flat.getText().trim() + "'," + idTariff + ",'" + phone.getText().trim() + "','" + phoneM.getText().trim() + "','" + phoneW.getText().trim() + "','" + strDate + "','" + descr.getText().trim() + "')";

                    System.out.println(query);
                    // statAction.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
                    // ResultSet result = statAction.getGeneratedKeys();
                    Statement statement = connectDB.getConnect().createStatement();
                    statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
                    ResultSet result = statement.getGeneratedKeys();

                    if (result.next()) {
                        Integer idAbon = result.getInt(1);

                        query = "INSERT INTO payment (date,cost,idAbonent,idOperator) VALUES ('" + formater.format(new Date()) + "'," + pay + "," + idAbon + "," + mainWindow.getOperator()[4] + ")";
                        System.out.println(query);
                        connectDB.getConnect().createStatement().executeUpdate(query);
                    }

                    window.dispose();
                } else {
                    JOptionPane.showMessageDialog(win, "Cумму нельзя писать буквами", "Ошибка", JOptionPane.WARNING_MESSAGE);
                }
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
            try {
                if (Integer.parseInt(payment.getText().trim()) > 0) {

                } else {
                    JOptionPane.showMessageDialog(win, "Cумму должна быть больше нуля", "Ошибка", JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException nm) {
                JOptionPane.showMessageDialog(win, "Cумму нельзя писать буквами", "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
        }

    }

    Integer getIDAddr(Integer idStreet, String house) throws SQLException {
        String query = "SELECT * FROM houses WHERE id_street=" + idStreet + " AND house='" + house + "'";
        System.out.println(query);
        ResultSet resultSet = connectDB.getConnect().createStatement().executeQuery(query);

        if (resultSet.next() && resultSet.getInt("id") > 0) {
            return resultSet.getInt("id");
        } else {
            return 0;
        }
    }
}
