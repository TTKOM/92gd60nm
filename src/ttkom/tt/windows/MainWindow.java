package ttkom.tt.windows;

import ttkom.tt.LoginW;
import ttkom.tt.dbase.mysql.ConnectDB;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.HashMap;
//import ttkom.tt.dbase.mysql.ConnectDB;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 23.11.12
 * Time: 14:33
 * To change this template use File | Settings | File Templates.
 */
public class MainWindow {
    private String[] operator;
    private ConnectDB connectDB;
    private JFrame frame;
    private HashMap<String,Integer> rules = new HashMap<String, Integer>();

    public MainWindow() throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Image icon = Toolkit.getDefaultToolkit().getImage(getClass().getClassLoader().getResource("ttkom/tt/img/eye.png"));
        frame.setIconImage(icon);
        new LoginW(frame, this);
    }
    public void setOperator(String login, String pass, String name, String param, String id) {
        operator = new String[]{login, pass, name, param, id};
    }
    public String[] getOperator() { return operator; }
    public void setConnectDB(String dbase, String addr) { connectDB = new ConnectDB(dbase, addr, "franchise", "43202129472"); }
    public ConnectDB getConnectDB() { return connectDB; }
    public JFrame getFrame() { return frame; }
    public void closeClass(Object arg) { arg = null; }
    public void closeConnect() throws SQLException { connectDB.getConnect().close(); }
    public HashMap getRules() { return this.rules; }
    public void setRules(HashMap rules) { this.rules = rules; }



}
