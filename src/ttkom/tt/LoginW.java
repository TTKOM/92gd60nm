package ttkom.tt;

import net.miginfocom.swing.MigLayout;
import ttkom.tt.dbase.mysql.ConnectDB;
import ttkom.tt.windows.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;


/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 27.11.12
 * Time: 11:10
 * To change this template use File | Settings | File Templates.
 */
public class LoginW {
    private JFrame frame;
    private JTextField tlogin;
    private JTextField tpass;
    private JTextField addEdit;
    private MainWindow mainWindow;
    private JPanel panel;


    public LoginW(JFrame frames, MainWindow mainWindow) throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        Dimension screenSize;
        MigLayout mC, fC, bC;
        JPanel form, butt, addr, editAddr, imgPanel, sepPanel;
        JButton ok, fail;
        JLabel pngLabel, lpass, llogin;
        JSeparator addrSeparator;

        frame = frames;

        this.mainWindow = mainWindow;
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation(screenSize.width / 2 - 100, screenSize.height / 2 - 100);

        panel = new JPanel(mC = new MigLayout("wrap 1", "grow, fill"));
        panel.add(form = new JPanel(fC = new MigLayout("wrap 2", "grow,fill")));
        panel.add(butt = new JPanel(bC = new MigLayout("wrap 4", "grow,fill")));
        panel.add(addr = new JPanel(new MigLayout("wrap 2", "[]10 ", "[grow,fill]rel")));
        panel.add(editAddr = new JPanel(new MigLayout("wrap 2", "[]10 ", "[grow,fill]rel")));

        addr.add(new JLabel("Enter IP addres DB"), "span,wrap");
        addr.add(addEdit = new JTextField(), "span,wrap");

        //addEdit.setText("10.254.252.22/test");
        addEdit.setText("10.254.252.22/megaline");
        addEdit.setColumns(20);
        addEdit.addKeyListener(new EnterToFran());

        llogin = new JLabel("Логин:");
        tlogin = new JTextField();
        tlogin.setColumns(10);
        //tlogin.setText("g1yyk");
        tlogin.setText("");
        tlogin.addKeyListener(new EnterToFran());
        lpass = new JLabel("Пароль:");
        tpass = new JPasswordField();
        tpass.setColumns(10);
        tpass.setText("");
        //tpass.setText("2435768");
        tpass.addKeyListener(new EnterToFran());
        ok = new JButton("Вход");
        ok.addActionListener(new LoginAction());
        fail = new JButton("Выход");
        form.add(llogin);
        form.add(tlogin);
        form.add(lpass);
        form.add(tpass);
        butt.add(ok);
        butt.add(fail);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }

    class LoginAction implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            loginAction();
        }
    }

    void loginAction() {
        String login = tlogin.getText().trim();
        String pass = tpass.getText().trim();
        String query = "SELECT * FROM operator WHERE login='" + login + "' AND pass='" + pass + "'";
        String[] dbpar = addEdit.getText().split("/");

        mainWindow.setConnectDB(dbpar[1], dbpar[0]);
        ConnectDB connect = mainWindow.getConnectDB();
        try {
            ResultSet result = connect.getConnect().createStatement().executeQuery(query);
            if (result.next()) {
                if (login.equals(result.getString("login")) && pass.equals(result.getString("pass"))) {

                    query = "SELECT * FROM `version`";

                    ResultSet row = connect.getConnect().createStatement().executeQuery(query);

                    if (row.next())
                        if (row.getString("value").equals(MainClass.version)) {
                            mainWindow.setOperator(result.getString("login"), result.getString("pass"), result.getString("name"), result.getString("type"), result.getString("id"));
                            getRules(result.getInt("id"),mainWindow.getConnectDB().getConnect());
                            frame.dispose();
                            frame.remove(panel);
                            SwingUtilities.invokeLater(new Runnable() {
                                public void run() {
                                    new ListAbonnetn(mainWindow);
                                }
                            });
                        } else {
                            JOptionPane.showMessageDialog(frame, "Версия программы "+MainClass.version+" и версия БД "+row.getString("value")+" различны.\nОбновите программу.", "Ошибка", JOptionPane.WARNING_MESSAGE);
                            System.exit(1);
                        }
                }
            } else {
                JOptionPane.showMessageDialog(frame, "Неверный логин или пароль\nПовторите свою попытку", "J", JOptionPane.WARNING_MESSAGE);
                tlogin.setText("");
                tpass.setText("");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    class EnterToFran implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void keyPressed(KeyEvent e) {

        }

        @Override
        public void keyReleased(KeyEvent e) {
            if (e.getKeyCode() == 10) { loginAction(); }
        }
    }

    private void getRules(Integer idOper,Connection conn) throws SQLException {
        HashMap<String,Integer> map = new HashMap<String, Integer>();
        String query = "SELECT rp.id_rules,rs.name FROM rules_operator rp,rules rs WHERE rp.id_oper="+idOper+" AND rp.id_rules = rs.id";
        ResultSet resultSet = conn.createStatement().executeQuery(query);

        while (resultSet.next())
            map.put(resultSet.getString("name"),resultSet.getInt("id_rules"));
        mainWindow.setRules(map);
    }

}
