package ttkom.tt.windows.modal;

import com.toedter.calendar.JDateChooser;
import net.miginfocom.swing.MigLayout;
import ttkom.tt.windows.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 11.01.13
 * Time: 16:54
 * To change this template use File | Settings | File Templates.
 */
public class DisconnectAbon extends AbModalWindow {

    JDialog dialog;

    public DisconnectAbon(JFrame frame, MainWindow mainWindow, Integer idAbonent) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        JPanel mainPanel;
        JButton save, close;
        JComboBox comboBox;
        JDateChooser calendar = null;
        Integer idtype = null;


        this.dialog = super.setDialog("Отключение абонента", frame);
        mainPanel = new JPanel(new MigLayout("wrap 4", "grow,fill"));
        dialog.add(mainPanel);
        calendar = super.setCalendar("yyyy-MM-dd HH:mm:ss", "####-##-## ##:##:##");
        save = super.setButton("Отключить", "remove", new UpdateAbon(frame, calendar, idtype, this, idAbonent, mainWindow));
        close = super.setButton("Закрыть", "cancel", new CloseAbon());
        comboBox = super.setList(super.getList(mainWindow.getConnectDB().getConnect(), "SELECT * FROM status WHERE id>2"));


        mainPanel.add(comboBox);
        mainPanel.add(calendar);
        mainPanel.add(save);
        mainPanel.add(close);

        dialog.setLocation(screenSize.width / 2 - 250, screenSize.height / 2 - 50);
        dialog.setSize(550, 100);
        dialog.setModal(true);
        dialog.setVisible(true);
        dialog.validate();
        dialog.repaint();

    }

    class UpdateAbon implements ActionListener {

        JFrame frame;
        JDateChooser calendar;
        ArrayList<String[]> arrayList;
        DisconnectAbon window;
        Integer idAbonent;
        MainWindow mainWindow;

        UpdateAbon(JFrame frame, JDateChooser calendar, Integer arList, AbModalWindow window, Integer idAbonent, MainWindow mainWindow) {
            this.frame = frame;
            this.calendar = calendar;
            this.window = (DisconnectAbon) window;
            this.idAbonent = idAbonent;
            this.mainWindow = mainWindow;


        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int n, type = 0;
            int idStatus;
            String query, strDate;
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


            if ((n = JOptionPane.showConfirmDialog(this.frame, "Вы действительно хотите отключить абонента", "Предупреждение", JOptionPane.YES_NO_OPTION)) == 0) {
                idStatus = window.getIdItem((String) window.getListComboBox().getSelectedItem());
                strDate = formatDate.format(this.calendar.getDate());
                System.out.println(idStatus + " " + strDate);

                try {

                    /*
                    В зависимости от прав абонента statconn будет менятся. Если операторы будут делать отключение тогда число будет 3(сигнла что его необходимо отключить),
                    Если отключать абонента будет инженер тогда число будет 1 - что абонент действительно отключен.
                    Дополнительно будет создоваться запись в таблице с историей действий по абоненту. Кто и когда его отключал.
                     */

                    if (Integer.parseInt(mainWindow.getOperator()[3]) >= 10 && Integer.parseInt(mainWindow.getOperator()[3]) < 20) {
                        type = 3;
                    } else if (Integer.parseInt(mainWindow.getOperator()[3]) >= 20) {
                        type = 1;
                    }

                    query = "UPDATE connection SET dtDisconn = '" + strDate + "',status=0 WHERE idAbonent=" + this.idAbonent + " AND status=1";
                    this.mainWindow.getConnectDB().getConnect().createStatement().executeUpdate(query);
                    query = "UPDATE abonents SET dateDisconnect='" + strDate + "',status=" + idStatus + ",statconn=" + type + " WHERE id=" + this.idAbonent;
                    this.mainWindow.getConnectDB().getConnect().createStatement().executeUpdate(query);
                    query = "INSERT INTO log (idOperator,dtEdit,old,new,type) VALUES (" + mainWindow.getOperator()[4] + ",'" + strDate + "','status=" + idStatus + "','status=" + idStatus + "',1)";
                    this.mainWindow.getConnectDB().getConnect().createStatement().executeUpdate(query);
                    query = "INSERT INTO stat_conn (id_abon,id_oper,id_status,dt_status) VALUES (" + this.idAbonent + "," + mainWindow.getOperator()[4] + "," + type + ",'" + strDate + "')";
                    this.mainWindow.getConnectDB().getConnect().createStatement().executeUpdate(query);
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }

            window.dialog.dispose();
        }
    }

    class CloseAbon implements ActionListener {


        @Override
        public void actionPerformed(ActionEvent e) {
            dialog.dispose();
        }
    }
}
