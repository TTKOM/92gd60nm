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
 * Time: 14:07
 * To change this template use File | Settings | File Templates.
 */
public class ChangeTariff extends AbModalWindow {
    JDialog dialog;


    public ChangeTariff(JFrame frame, MainWindow mainWindow, String[] userinfo) {
        JPanel mainPanel;
        String title;
        JDateChooser calendar;
        JButton save, close;
        JComboBox comboBox;
        Integer idtype = null;
        Integer idAbonent = Integer.parseInt(userinfo[0]);
        Integer idTariff = Integer.parseInt(userinfo[5]);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();


        this.dialog = super.setDialog("Подключение абонента", frame);
        mainPanel = new JPanel(new MigLayout("wrap 4", "grow,fill"));
        dialog.add(mainPanel);
        title = "Для подключения абонента выберите тарифный план\n и укажите дату подключения";
        calendar = super.setCalendar("yyyy-MM-dd HH:mm:ss", "####-##-## ##:##:##");
        save = super.setButton("Изменить", "update", new UpdateAbon(dialog, calendar, idtype, this, idAbonent, mainWindow, idTariff));
        close = super.setButton("Закрыть", "cancel", new CloseAbon());
        comboBox = super.setList(getList(mainWindow.getConnectDB().getConnect(), "SELECT * FROM tariff"));

        mainPanel.add(new JLabel(title), "span 4,wrap");

        mainPanel.add(comboBox);
        mainPanel.add(calendar);

        mainPanel.add(save);
        mainPanel.add(close);

        dialog.setSize(600, 100);
        dialog.setLocation(screenSize.width / 2 - dialog.getSize().width / 2, screenSize.height / 2 - dialog.getSize().height / 2);
        dialog.setModal(true);
        dialog.setVisible(true);
        dialog.validate();
        dialog.repaint();
    }

    class UpdateAbon implements ActionListener {
        Window frame;
        JDateChooser calendar;
        ArrayList<String[]> arrayList;
        ChangeTariff window;
        Integer idAbonent;
        Integer idTariff;
        MainWindow mainWindow;

        UpdateAbon(Window frame, JDateChooser calendar, Integer arList, AbModalWindow window, Integer idAbonent, MainWindow mainWindow, Integer idTariff) {
            this.frame = frame;
            this.calendar = calendar;
            this.window = (ChangeTariff) window;
            this.idAbonent = idAbonent;
            this.mainWindow = mainWindow;
            this.idTariff = idTariff;


        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int n, type = 0, status = 0;
            int idStatus;
            String query, strDate;
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            if ((n = JOptionPane.showConfirmDialog(this.frame, "Вы действительно хотите подключить абонента", "Предупреждение", JOptionPane.YES_NO_OPTION)) == 0) {

                idStatus = window.getIdItem((String) window.getListComboBox().getSelectedItem());
                strDate = formatDate.format(calendar.getDate());

                try {

                    if (Integer.parseInt(mainWindow.getOperator()[3]) >= 10 && Integer.parseInt(mainWindow.getOperator()[3]) < 20) {
                        type = 4;
                        status = idStatus;
                    } else if (Integer.parseInt(mainWindow.getOperator()[3]) >= 20) {
                        type = 2;
                        status = 1;
                    }

                    query = "UPDATE connection SET dtDisconn = '" + strDate + "',status=0 WHERE idAbonent=" + idAbonent + " AND status=1";
                    mainWindow.getConnectDB().getConnect().createStatement().executeUpdate(query);
                    query = "UPDATE abonents SET idTariff=" + idStatus + ",status=" + status + ",statconn=" + type + " WHERE id = " + idAbonent;
                    mainWindow.getConnectDB().getConnect().createStatement().executeUpdate(query);
                    query = "INSERT INTO connection (dtConn,idTariff,idAbonent) VALUES ('" + strDate + "'," + idStatus + "," + idAbonent + ")";
                    mainWindow.getConnectDB().getConnect().createStatement().executeUpdate(query);
                    query = "INSERT INTO stat_conn (id_abon,id_oper,id_status,dt_status) VALUES (" + this.idAbonent + "," + mainWindow.getOperator()[4] + "," + type + ",'" + strDate + "')";
                    mainWindow.getConnectDB().getConnect().createStatement().executeUpdate(query);

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

            }

            this.frame.dispose();

        }
    }

    class CloseAbon implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            dialog.dispose();
        }
    }


}
