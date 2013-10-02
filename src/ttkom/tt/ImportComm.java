package ttkom.tt;

import net.miginfocom.swing.MigLayout;
import ttkom.tt.windows.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 15.12.12
 * Time: 11:28
 */
public class ImportComm {
    MainWindow main;

    public ImportComm(MainWindow mainWindow, Object win) {

        Dialog window = new JDialog((Window) win);
        main = mainWindow;


        MigLayout layout = new MigLayout("wrap 3", "grow,fill");
        JPanel mpanel = new JPanel(layout);

        JButton importBut = new JButton("Импорт");
        importBut.addActionListener(new ImportSelectFile());

        mpanel.add(importBut);


        window.add(mpanel);
        window.setSize(300, 150);
        window.setModal(true);
        window.setVisible(true);
        window.validate();
        window.repaint();

    }

    class ImportSelectFile implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileopen = new JFileChooser();
            int ret = fileopen.showDialog(null, "Открыть файл");
            if (ret == JFileChooser.APPROVE_OPTION) {
                File file = fileopen.getSelectedFile();
                if (file.getName().equals("release.csv")) {


                    try {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                        String buffer = "";
                        String query = "";
                        while ((buffer = reader.readLine()) != null) {
                            String[] args = buffer.split(",");
                            if (args.length > 4) {
                                if (args[0].trim() != "" && args[2].trim() != "" && args[3].trim() != "" && args[4].trim() != "" && args[2].trim() != "10.250.1.56") {
                                    query = "SELECT id FROM commutator WHERE ipaddres='" + args[2].trim() + "'";
                                    System.out.println(query);
                                    ResultSet res = main.getConnectDB().getConnect().createStatement().executeQuery(query);
                                    if (!res.next()) {
                                        System.out.println(args.length);
                                        System.out.println("0 - " + args[0]);
                                        System.out.println("1 - " + args[1]);
                                        System.out.println("2 - " + args[2]);
                                        System.out.println("3 - " + args[3]);
                                        query = "SELECT hs.id FROM houses hs, streets str WHERE hs.id_street = str.id AND hs.house='" + args[4].trim() + "' AND str.name='" + args[3].trim() + "'";
                                        System.out.println(query);
                                        res = main.getConnectDB().getConnect().createStatement().executeQuery(query);
                                        if (res.next()) {
                                            query = "INSERT INTO commutator (name,ipaddres,idAddres,type) VALUES ('" + args[0] + "','" + args[2] + "'," + res.getString(1) + ",'" + args[1] + "')";
                                            main.getConnectDB().getConnect().createStatement().executeUpdate(query);
                                        }

                                    }
                                }
                            }

                        }
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e3) {
                        e3.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    } catch (SQLException e2) {
                        e2.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }


                }
            }
        }
    }
}
