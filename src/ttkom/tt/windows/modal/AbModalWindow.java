package ttkom.tt.windows.modal;

import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

//import ttkom.tt.dbase.mysql.ConnectDB;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 11.01.13
 * Time: 10:02
 * To change this template use File | Settings | File Templates.
 */
public class AbModalWindow {
    private JDateChooser calendar;
    private JComboBox list;
    private ArrayList<String[]> arList = new ArrayList<String[]>();
    private JDialog dialog;


    public JDateChooser setCalendar(String formatDate, String returnDate) {
        calendar = new JDateChooser(formatDate, returnDate, '0');
        calendar.setDate(new Date());
        return calendar;
    }

    public JDateChooser getCalendar() {
        calendar.setDate(new Date());
        return calendar;
    }

    public JComboBox setList(Object[] objects) {
        list = new JComboBox(objects);
        return list;
    }

    public JComboBox getListComboBox() {
        return list;
    }

    public ArrayList<String[]> getArList() {
        return arList;
    }

    public JDialog setDialog(String title, JFrame frame) {
        JDialog dialog = new JDialog(frame, title, Dialog.ModalityType.APPLICATION_MODAL);
        return dialog;
    }

    public JDialog getDialog() {
        return dialog;
    }

    public JButton setButton(String title, String namePNG, ActionListener actionListener) {
        JButton button = new JButton(title);
        button.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/" + namePNG + ".png")));
        button.addActionListener(actionListener);
        return button;
    }

    Object[] getList(Connection connection, String query) {
        ArrayList<String[]> arg = new ArrayList<String[]>();
        Object[] turn = new Object[0];
        try {
            ResultSet result = connection.createStatement().executeQuery(query);
            while (result.next()) {
                arg.add(new String[]{result.getString("id"), result.getString("name")});
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (arg.size() > 0) {
            turn = new Object[arg.size()];

            for (int i = 0; i < arg.size(); i++) {
                turn[i] = arg.get(i)[1];
            }
        }
        arList = arg;
        return turn;
    }

    Integer getIdItem(String name) {
        for (int i = 0; i < arList.size(); i++) {
            if (arList.get(i)[1].equals(name)) {
                return Integer.parseInt(arList.get(i)[0]);
            }
        }

        return 0;
    }
}
