package ttkom.tt.windows.modal.card.funmodal;

import net.miginfocom.swing.MigLayout;
import ttkom.tt.dbase.mysql.ConnectDB;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 04.06.13
 * Time: 9:41
 * To change this template use File | Settings | File Templates.
 */
public class AbFunModal {

    JDialog win;
    JPanel mainPanel;
    ArrayList<String[]> arrTariff = new ArrayList<String[]>();
    JButton save,close;
    JComboBox tariff,countMonth;
    JTextField cost;

    AbFunModal(Integer id_subscriber,Integer id_tariff,String title,Window window,Connection conn) throws SQLException {
        win = new JDialog(window,title);
        mainPanel = setMainPanel(new MigLayout("wrap 3","grow,fill"));
        win.add(mainPanel);
        tariff = new JComboBox(setArrTariff(conn));
        countMonth = new JComboBox(new Object[]{"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"});
        JTextField cost = new JTextField();
        save = new JButton("Добавить платеж");
        close = new JButton("Закрыть");

        mainPanel.add(tariff);
        mainPanel.add(countMonth);
        mainPanel.add(cost);

    }

    private JPanel setMainPanel(MigLayout layout) {
        JPanel arg = new JPanel(layout);
        return arg;
    }

    public JButton getSave()  { return  save;  }
    public JButton getClose() { return  close; }
    public JComboBox getTariff() { return  tariff;}
    public JComboBox getCountMonth() { return countMonth; }
    public ArrayList<String[]> getArrTariff() { return getArrTariff();}
    public JTextField getCost() { return  cost; }
    public void setCost(String arg) { cost.setText(arg);}



    String[] setArrTariff(Connection conn) throws SQLException {
        String query = "SELECT * FROM tariff ORDER BY name ASC";
        Integer count = 0;
        ResultSet resultSet = conn.createStatement().executeQuery(query);
        while (resultSet.next()) {
            arrTariff.add(new String[]{resultSet.getString("id"), resultSet.getString("name"), resultSet.getString("descr")});
            count++;
        }
        String[] obj = new String[count];
        for (int i = 0; i < arrTariff.size(); i++) obj[i] = arrTariff.get(i)[1];
        return obj;
    }

    JDialog getWin() {return win;}

}
