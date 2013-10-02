package ttkom.tt;

import net.miginfocom.swing.MigLayout;
import ttkom.tt.windows.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 11.12.12
 * Time: 11:51
 */
public class AddAddres {

    AddAddres(MainWindow mainWindow, Object win) {
        JDialog window = new JDialog((Window) win);


        MigLayout layout = new MigLayout("wrap 3", "grow,fill");
        JPanel mpanel = new JPanel(layout);
        JComboBox streets = new JComboBox();
        JTextField street = new JTextField("");

        JTextField house;
        JButton save = new JButton("Сохранить"), clear = new JButton("Очистить"), exit = new JButton("Закрыть");

        window.add(mpanel);
        window.setSize(300, 150);
        window.setModal(true);
        window.setVisible(true);
        window.validate();
        window.repaint();
    }

    class Check implements ActionListener {

        Check(JTextField string) {

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String query = "SELECT id FROM street str,houses hs WHERE str.id=hs.id_street AND str.name='" + 22 + "'";
        }
    }
}
