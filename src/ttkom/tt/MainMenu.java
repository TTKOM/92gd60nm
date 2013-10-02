package ttkom.tt;

import ttkom.tt.windows.MainWindow;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 27.11.12
 * Time: 12:19
 * To change this template use File | Settings | File Templates.
 */
public class MainMenu {
    private JMenuBar mBar;

    MainMenu(MainWindow mainWindow) {


        Integer rules = Integer.parseInt(mainWindow.getOperator()[3]);
        mBar = new JMenuBar();


        JMenu file = new JMenu("Фаил");
        JMenuItem exit = new JMenuItem("Выход");
        file.add(exit);  //exit.addActionListener( new JMenuAction.pressMenu());
        file.add(exit);
        exit.addActionListener(new ActionExit());
        mBar.add(file);
        if (Integer.parseInt(mainWindow.getOperator()[3]) >= 20) {
            JMenu add = new JMenu("Settings");
            JMenuItem comm = new JMenuItem("Commutators");
            comm.addActionListener(new OpenListComm(mainWindow));
            if (Integer.parseInt(mainWindow.getOperator()[3]) == 99) {
                JMenuItem module = new JMenuItem("Modules");
                add.add(module);
            }


            add.add(comm);
            mBar.add(add);
        }

        JMenu help = new JMenu("Помощь");
        JMenuItem about = new JMenuItem("О програме");
        help.add(about);
        about.addActionListener(new ActionExit());
        mBar.add(help);
    }

    public JMenuBar getMBar() {
        return mBar;
    }

    class ActionExit implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(1);
        }
    }

    class OpenListComm implements ActionListener {

        MainWindow frame;

        OpenListComm(MainWindow frame) {
            this.frame = frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                new ListCommutator(frame);
            } catch (SQLException e1) {
                e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }
}
