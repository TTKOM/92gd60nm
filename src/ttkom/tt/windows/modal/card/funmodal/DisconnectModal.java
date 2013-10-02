package ttkom.tt.windows.modal.card.funmodal;

import com.toedter.calendar.JDateChooser;
import net.miginfocom.swing.MigLayout;
import ttkom.tt.windows.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 10.06.13
 * Time: 15:58
 * To change this template use File | Settings | File Templates.
 */
public class DisconnectModal extends AbModalWindows {


    ArrayList<String[]> reasons = new ArrayList<String[]>();
    JComboBox reason;    ;
    MainWindow  mainClass;
    Integer     id_sub;
    JDateChooser dateChooser;
    JButton disconn;
    JButton close;




    public DisconnectModal(Window frame, String title, MigLayout layout,MainWindow mainClass,Integer id_sub) throws SQLException {
        super(frame, title, layout);

        this.mainClass = mainClass;
        this.id_sub = id_sub;
        reason = setComboBox(setReason(mainClass.getConnectDB().getConnect()),"3");
        dateChooser = setDateChooser();
        disconn = setButton(3,"Отключить","remove.png"); disconn.addActionListener(new Disconnect(id_sub));
        close   = setButton(3,"Закрыть","cancel.png"); close.addActionListener(new CloseWindow());

        getWindow().setSize(500, 110);
        getWindow().setLocation((getSizeWindow().width-getWindow().getSize().width)/2,(getSizeWindow().height-getWindow().getSize().height)/2);
        getMainPanel().setLayout(new MigLayout("wrap 4","grow,fill"));

        setHeader();
        getMainPanel().add(reason);
        getMainPanel().add(dateChooser);
        getMainPanel().add(disconn);
        getMainPanel().add(close);

        getWindow().setModal(true);
        getWindow().setVisible(true);
        getWindow().validate();
        getWindow().repaint();


    }

    @Override
    public void setHeader() {
        getMainPanel().add(new JLabel("Причина:"));
        getMainPanel().add(new JLabel("Дата откл.:"),"wrap");
    }

    public ArrayList<String[]> setReason(Connection connect) throws SQLException {

        String query = "SELECT * FROM status WHERE id = 3";

        ResultSet result = connect.createStatement().executeQuery(query);

        while (result.next())
            reasons . add ( new String[] { result.getString("id"),result.getString("name"),result.getString("dscr")});

        return reasons;
    }

    private JDateChooser setDateChooser() {
        return new JDateChooser(new Date());
    }

    class Disconnect implements ActionListener {

        Integer id_sub;

        Disconnect(Integer id_sub) {
            this.id_sub = id_sub;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String strDate = dateFormat.format(new Date());


            String query = "";

            try {
                System.out.println("==="+reason.getSelectedIndex());
                if (reason.getSelectedIndex()>0) {
                    if (Integer.parseInt(reasons.get(reason.getSelectedIndex()-1)[0])>0) {
                        query = "UPDATE connection SET dtDisconn = '" + strDate + "',status=0 WHERE idAbonent=" + id_sub + " AND status=1";
                        mainClass.getConnectDB().getConnect().createStatement().executeUpdate(query);
                        query = "UPDATE abonents SET dateDisconnect='" + strDate + "',status=" + reasons.get(reason.getSelectedIndex()-1)[0] + ",statconn=1 WHERE id=" + id_sub;
                        mainClass.getConnectDB().getConnect().createStatement().executeUpdate(query);
                    } else {
                        JOptionPane.showMessageDialog(window, "Не указанна прчина отключения.\nУкажите причину и попробуйте снова", "Предупреждение", JOptionPane.WARNING_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(window, "Не указанна прчина отключения.\nУкажите причину и попробуйте снова", "Предупреждение", JOptionPane.WARNING_MESSAGE);
                }
            } catch (SQLException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }

        }
    }




}
