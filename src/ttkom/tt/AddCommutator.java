package ttkom.tt;

import net.miginfocom.swing.MigLayout;
import ttkom.tt.dbase.mysql.ConnectDB;
import ttkom.tt.windows.MainWindow;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * User: G1yyk
 * Date: 07.12.12
 * Time: 17:35
 */
public class AddCommutator {
    //JTextField nameComm;

    private JComboBox streets, houses;
    private ArrayList<String[]> arrStreet = new ArrayList<String[]>();  // first elemetn is id street, second element is name street
    private JDialog window;
    private EditUser editUser;

    public AddCommutator(MainWindow mainWindow, Object win, Integer stat) throws ParseException, SQLException {
        /*
        Initilization variable
         */
        JTextField porch, ports;
        JFormattedTextField ipAddr1, ipAddr2, ipAddr3, ipAddr4;
        JTextField ipaddres;
        JTextArea descrComm;
        Integer countPort;
        JComboBox street, house;
        JScrollPane scrollPane;
        MaskFormatter ipmask, namemask;
        String[] comInfo = new String[10];
        String[] ipadd = new String[4];
        this.editUser = editUser;
        System.out.print(stat);
        if (stat > 0) {
            String query = "SELECT * FROM commutator WHERE id=" + stat;
            System.out.println(query);
            ResultSet result = mainWindow.getConnectDB().getConnect().createStatement().executeQuery(query);

            if (result.next()) {
                comInfo[0] = result.getString("id");
                comInfo[1] = result.getString("name");
                comInfo[2] = result.getString("ipaddres");
                comInfo[3] = result.getString("countPort");
                comInfo[4] = result.getString("porch");
                comInfo[5] = result.getString("descr");
                comInfo[6] = result.getString("idAddres");

                query = "SELECT  str.name,str.id as strid,hs.id,hs.house FROM houses hs, streets str WHERE hs.id=" + comInfo[6] + " AND str.id=hs.id_street";
                System.out.println(query);
                result = mainWindow.getConnectDB().getConnect().createStatement().executeQuery(query);
                if (result.next()) {
                    comInfo[7] = result.getString("house");  //7
                    comInfo[8] = result.getString("name");   //8
                    comInfo[9] = result.getString("strid");  //9
                }
            }
        }

        JButton save, close, update;

        window = new JDialog((Window) win, "Добавить коммутатор");
        MigLayout mlayer = new MigLayout("wrap 3", "[150]5[grow,fill]");
        MigLayout blayer = new MigLayout("wrap 4", "grow,fill");
        MigLayout iplayer = new MigLayout("wrap 7", "[]2[]2[]2[]2[]2[]2[]");
        JPanel mainPanel = new JPanel(mlayer);
        JPanel ipPanel = new JPanel(iplayer);
        JPanel butPanel = new JPanel(blayer);

        JTextField nameComm = new JTextField();
        if (stat > 0) {
            nameComm.setText(comInfo[1]);
        }

        streets = new JComboBox(setArrStreet(mainWindow.getConnectDB()));
        streets.addActionListener(new SetHouse(mainWindow.getConnectDB()));
        houses = new JComboBox();
        setArrHouse(mainWindow.getConnectDB());

        if (stat > 0) {
            for (int i = 0; i < arrStreet.size(); i++)
                if (Integer.parseInt(comInfo[9]) == Integer.parseInt(arrStreet.get(i)[0])) streets.setSelectedIndex(i);
            if (comInfo[0] != null) for (int i = 0; i < houses.getItemCount(); i++)
                if (comInfo[7].equals(houses.getItemAt(i))) houses.setSelectedIndex(i);
        }

        ipmask = new MaskFormatter("###");
        ipmask.setPlaceholderCharacter('0');
        save = new JButton("Сохранить");
        close = new JButton("Закрыть");
        update = new JButton("Обновить");
        ipaddres = new JTextField(comInfo[2]);
        ipAddr1 = new JFormattedTextField(ipmask);
        if (stat > 0) {
            ipAddr1.setText(comInfo[2].split("\\.")[0]);
        }
        ipAddr2 = new JFormattedTextField(ipmask);
        if (stat > 0) {
            ipAddr2.setText(comInfo[2].split("\\.")[1]);
        }
        ipAddr3 = new JFormattedTextField(ipmask);
        if (stat > 0) {
            ipAddr3.setText(comInfo[2].split("\\.")[2]);
        }
        ipAddr4 = new JFormattedTextField(ipmask);
        if (stat > 0) {
            ipAddr4.setText(comInfo[2].split("\\.")[3]);
        }
        porch = new JTextField();
        if (stat > 0) {
            porch.setText(comInfo[4]);
        }
        ports = new JTextField();
        if (stat > 0) {
            ports.setText(comInfo[3]);
        }
        descrComm = new JTextArea();
        descrComm.setLineWrap(true);
        descrComm.setRows(4);
        if (stat > 0) {
            descrComm.setText(comInfo[5]);
        }
        scrollPane = new JScrollPane(descrComm);

        mainPanel.add(new JLabel("Имя коммутатора"));
        mainPanel.add(nameComm, "span 2");
        mainPanel.add(new JLabel("IP адрес"));
        mainPanel.add(ipaddres, "w 150!,span 2,wrap");
        // mainPanel.add(ipPanel,"span 2");
        mainPanel.add(new JLabel("Кол-во портов"));
        mainPanel.add(ports, "w 40!,span 2");
        mainPanel.add(new JLabel("Улица"));
        mainPanel.add(streets, "span 2");
        mainPanel.add(new JLabel("Номер дома"));
        mainPanel.add(houses, "span 2");
        mainPanel.add(new JLabel("Подьезд"));
        mainPanel.add(porch, "w 40!,span 2");
        mainPanel.add(new JLabel("Описание"));
        mainPanel.add(scrollPane, "span 2");

        butPanel.add(save);
        butPanel.add(update);
        butPanel.add(close);
        if (stat > 0) {
            JButton delete = new JButton("Удалить");
            delete.addActionListener(new DeleteComm(stat, mainWindow.getConnectDB()));
            butPanel.add(delete);
        }
        mainPanel.add(butPanel, "span 3");

        save.addActionListener(new ActionSaveComm(nameComm, ipaddres, ports, porch, descrComm, window, mainWindow.getConnectDB(), stat));

        window.add(mainPanel);
        window.setSize(420, 320);
        window.setModal(true);
        window.setVisible(true);
        window.validate();
        window.repaint();

    }


    class ActionSaveComm implements ActionListener {
        private JTextField nameComm, ip1, ip2, ip3, ip4, ports, porch, ipaddres;
        private JTextArea descr;
        private Window win;
        private ConnectDB connectDB;
        private Integer stat;

        //private
        ActionSaveComm(JTextField nCom, JTextField ipaddres, JTextField port, JTextField porch, JTextArea descr, Window window, ConnectDB conn, Integer stat) {
            nameComm = nCom;
            this.ipaddres = ipaddres;
            ports = port;
            this.porch = porch;
            this.descr = descr;
            win = window;
            connectDB = conn;
            this.stat = stat;

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (nameComm.getText().trim().length() > 0) {
                if (ipaddres.getText().trim().length() > 0) {
                    String ipAddr = ipaddres.getText().trim();
                    try {
                        if (Integer.parseInt(ports.getText()) > 0) {
                            String query = "";
                            if (stat > 0) {
                                query = "UPDATE commutator SET name='" + nameComm.getText().trim() + "',ipaddres='" + ipAddr + "',countPort=" + Integer.parseInt(ports.getText().trim()) + ",porch='" + porch.getText().trim() + "',descr='" + descr.getText().trim() + "'," +
                                        "idAddres=(SELECT id FROM houses WHERE id_street=" + arrStreet.get(streets.getSelectedIndex())[0] + " AND house='" + houses.getSelectedItem() + "') WHERE id=" + stat;
                            } else {
                                query = "INSERT INTO commutator (name,ipaddres,countPort,porch,descr,idAddres) VALUES ('" + nameComm.getText().trim() + "','" + ipAddr + "'," + Integer.parseInt(ports.getText().trim()) + ",'" + porch.getText().trim() + "','" + descr.getText().trim() + "',(SELECT id FROM houses WHERE id_street=" + arrStreet.get(streets.getSelectedIndex())[0] + " AND house='" + houses.getSelectedItem() + "'))";
                            }
                            try {
                                connectDB.getConnect().createStatement().executeUpdate(query);
                                win.dispose();
                                //editUser.updateListComm(editUser.getIdAbon());
                            } catch (SQLException e1) {
                                if (e1.getErrorCode() == 1062) {
                                    JOptionPane.showMessageDialog(win, "Такой коммутатор уже существует", "Ошибка", JOptionPane.WARNING_MESSAGE);
                                } else {
                                    e1.printStackTrace();
                                }
                            }

                        } else {
                            JOptionPane.showMessageDialog(win, "Не указано кол-во портов", "Ошибка", JOptionPane.WARNING_MESSAGE);
                        }
                    } catch (NumberFormatException num) {
                        JOptionPane.showMessageDialog(win, "Не указано кол-во портов", "Ошибка", JOptionPane.WARNING_MESSAGE);
                    }

                } else {
                    JOptionPane.showMessageDialog(win, "Неверный формат IP адреса", "Ошибка", JOptionPane.WARNING_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(win, "Слишком короткое название коммутатора", "Ошибка", JOptionPane.WARNING_MESSAGE);
            }
        }


    }

    String[] setArrStreet(ConnectDB connectDB) throws SQLException {
        String query = "SELECT * FROM streets ORDER BY name ASC";
        Integer count = 0;
        ResultSet resultSet = connectDB.getConnect().createStatement().executeQuery(query);

        if (streets != null) streets.removeAllItems();
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

    void setArrHouse(ConnectDB connectDB) throws SQLException {
        String query = "SELECT * FROM houses WHERE id_street=" + arrStreet.get(streets.getSelectedIndex())[0] + " ORDER BY house ASC";

        System.out.println(query);
        Integer count = 0;

        ResultSet resultSet = connectDB.getConnect().createStatement().executeQuery(query);
        if (houses != null) houses.removeAllItems();

        while (resultSet.next()) {
            houses.addItem(resultSet.getString("house"));
        }
    }

    class SetHouse implements ActionListener {
        ConnectDB connectDB;

        SetHouse(ConnectDB con) {
            connectDB = con;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                setArrHouse(connectDB);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    class DeleteComm implements ActionListener {
        Integer stats;
        ConnectDB connectDB;

        DeleteComm(Integer stat, ConnectDB conn) {
            stats = stat;
            connectDB = conn;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String query = "DELETE FROM commutator WHERE id=" + stats;
            try {
                connectDB.getConnect().createStatement().executeUpdate(query);
                window.dispose();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

        }
    }

}
