package ttkom.tt;

import net.miginfocom.swing.MigLayout;
import ttkom.tt.windows.MainWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 10.12.12
 * Time: 15:39
 */
public class ListCommutator {
    JTable table;
    TableModel model;
    MainWindow mainWindow;
    JComboBox types;
    JDialog window;
    Dimension screenSize;

    public ListCommutator(MainWindow mWin) throws SQLException {
        //screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        window = new JDialog(mWin.getFrame());
        screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        /*
        Initialize varibale
         */
        mainWindow = mWin;

        MigLayout mLayer = new MigLayout("wrap 3", "[150]5[]5[]");
        JPanel mainpanel = new JPanel(mLayer);

        types = new JComboBox(new String[]{"By Name", "By IPaddres", "By addres"});
        JTextField search = new JTextField();
        search.addKeyListener(new FindCommutator());
        JLabel icon = new JLabel();
        icon.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/find.png")));
        JButton impComm = new JButton("Импорт комм");
        JButton expComm = new JButton("Экспорт");
        expComm.addActionListener(new ExportComm());
        //impComm.addActionListener(new ImportCom());
        types.addActionListener(new SelectType(search));
        mainpanel.add(types, "grow");
        mainpanel.add(icon);
        mainpanel.add(search, "grow");

        JScrollPane scroll = setTable();

        mainpanel.add(scroll, "span 3");

        //mainpanel.add(impComm, "span 3");
        mainpanel.add(expComm, "span 3");


        window.add(mainpanel);
        firstList();
        window.setSize(810, 550);
        window.setModal(true);
        window.setVisible(true);
        window.validate();
        window.repaint();
    }

    class TableModel extends DefaultTableModel {
        public TableModel(Object rowData[][], Object columnNames[]) {
            super(rowData, columnNames);
        }

        public boolean isCellEditable(int row, int col) {
            return false;
        }
    }

    class ColorTableCellRenderer extends JLabel implements TableCellRenderer {
        public ColorTableCellRenderer() {
            setOpaque(true);
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel pan = new JPanel(new BorderLayout());
            JLabel lbl = new JLabel((String) value);
            if (table.getSelectedRow() == row) {
                pan.setBackground(new Color(253, 255, 225));
            } else {
                if ((row % 2) == 0) {
                    pan.setBackground(new Color(200, 200, 255));
                } else {
                    pan.setBackground(new Color(253, 253, 253));
                }
            }
            pan.add(lbl, BorderLayout.CENTER);
            return pan;
        }
    }

    JScrollPane setTable() {
        model = new TableModel(new Object[][]{}, new Object[]{"Номер", "Имя", "IP адрес", "Улица", "Дом", "Подьезд", "Портов"});
        table = new JTable(model);
        table.setDefaultRenderer(Object.class, new ColorTableCellRenderer());

        table.setAutoResizeMode(table.AUTO_RESIZE_ALL_COLUMNS);

        table.getColumnModel().getColumn(0).setMaxWidth(50);
        table.getColumnModel().getColumn(1).setMaxWidth(200);
        table.getColumnModel().getColumn(2).setMaxWidth(200);
        table.getColumnModel().getColumn(3).setMaxWidth(200);
        table.getColumnModel().getColumn(4).setMaxWidth(100);
        table.getColumnModel().getColumn(5).setMaxWidth(100);

        table.getColumnModel().getColumn(0).setMinWidth(50);
        table.getColumnModel().getColumn(1).setMinWidth(100);
        table.getColumnModel().getColumn(2).setMinWidth(150);
        table.getColumnModel().getColumn(3).setMinWidth(100);
        table.getColumnModel().getColumn(4).setMinWidth(100);
        table.getColumnModel().getColumn(5).setMinWidth(100);
        table.addMouseListener(new SelectCommutator());
        JScrollPane scTab = new JScrollPane(table);
        Integer w, h;
        w = (int) screenSize.getWidth() - 20;
        h = (int) screenSize.getHeight() - 200;
        scTab.setMaximumSize(new Dimension(w, h));
        scTab.setMinimumSize(new Dimension(w, h));
        return scTab;
    }

    void searchByAddres() {
        String query = "SELECT * FROM commutator com,houses hs,street str WHERE com.";
    }

    void firstList() throws SQLException {
        String query = "SELECT com.id,com.name,com.ipaddres,com.countPort,com.porch,hs.house,str.name as street FROM commutator com,houses hs, streets str" +
                " WHERE com.idAddres=hs.id AND hs.id_street=str.id  ORDER BY id ASC LIMIT 100";
        System.out.println(query);
        ResultSet result = mainWindow.getConnectDB().getConnect().createStatement().executeQuery(query);

        if (model != null) while (model.getRowCount() > 0) model.removeRow(0);

        while (result.next()) {
            model.addRow(new Object[]{
                    result.getString("id"),
                    result.getString("name"),
                    result.getString("ipaddres"),
                    result.getString("street"),
                    result.getString("house"),
                    result.getString("porch"),
                    result.getString("countPort")
            });
        }
    }

    class FindCommutator implements KeyListener {


        @Override
        public void keyTyped(KeyEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void keyPressed(KeyEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void keyReleased(KeyEvent e) {
            Integer type = types.getSelectedIndex();

            JTextField text = (JTextField) e.getSource();
            String query = "";
            System.out.println("type = " + type);
            if (type == 2) {
                String[] addr = text.getText().trim().split(" ");
                String strAddres = "";
                for (int i = 0; i < addr.length && i < 3; i++) {
                    if (i == 0) {
                        strAddres += "str.name LIKE '" + addr[i] + "%'  ";
                    }
                    if (i == 1) {
                        strAddres += "AND hs.house LIKE '" + addr[i] + "%'  ";
                    }
                    if (i == 2) {
                        strAddres += "AND com.porch LIKE '" + addr[i] + "%'  ";
                    }

                }
                query = "SELECT com.id,com.name,com.ipaddres,com.countPort,com.porch,hs.house,str.name as street FROM commutator com,houses hs,streets str WHERE " + strAddres + " AND str.id=hs.id_street AND com.idAddres=hs.id";


            } else if (type == 1) {
                query = "SELECT com.id,com.name,com.ipaddres,com.countPort,com.porch,hs.house,str.name as street FROM commutator com,houses hs,streets str WHERE com.ipaddres LIKE '" + text.getText().trim() + "%' AND str.id=hs.id_street AND com.idAddres=hs.id";
            } else if (type == 0) {
                query = "SELECT com.id,com.name,com.ipaddres,com.countPort,com.porch,hs.house,str.name as street FROM commutator com,houses hs,streets str WHERE com.name LIKE '" + text.getText().trim() + "%' AND str.id=hs.id_street AND com.idAddres=hs.id";
            }

            if (type >= 0 && type < 3) {
                try {
                    System.out.println(query);
                    ResultSet result = mainWindow.getConnectDB().getConnect().createStatement().executeQuery(query);
                    while (model.getRowCount() > 0) model.removeRow(0);
                    while (result.next()) {
                        model.addRow(new Object[]{
                                result.getString("id"),
                                result.getString("name"),
                                result.getString("ipaddres"),
                                result.getString("street"),
                                result.getString("house"),
                                result.getString("porch"),
                                result.getString("countPort")
                        });
                    }

                } catch (SQLException e1) {
                    e1.printStackTrace();
                }
            }
            System.out.println(e.getKeyChar());
        }
    }

    class SelectType implements ActionListener {
        JTextField textField;

        SelectType(JTextField ftld) {
            textField = ftld;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                textField.setText("");
                firstList();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    class ImportCom implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            new ImportComm(mainWindow, window);
        }
    }

    class SelectCommutator implements MouseListener {


        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                Point point = e.getPoint();
                int row = table.rowAtPoint(point);

                String str = (String) model.getValueAt(row, 0);
                try {
                    new AddCommutator(mainWindow, window, Integer.parseInt(str));
                } catch (ParseException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                } catch (SQLException e1) {
                    e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void mouseExited(MouseEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    class ExportComm implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            saveFile();
        }
    }

    private void saveFile() {
        System.out.println("-------------------");
        String query = "SELECT com.name,hs.house,str.name as street FROM commutator com,houses hs, streets str" +
                " WHERE com.idAddres=hs.id AND hs.id_street=str.id  ORDER BY str.name ";
        ArrayList<String[]> listComm = new ArrayList<String[]>();
        try {
            ResultSet result = mainWindow.getConnectDB().getConnect().createStatement().executeQuery(query);

            while (result.next()) {
                listComm.add(new String[]{result.getString("street"), result.getString("house"), result.getString("name")});
            }

            if (listComm.size() > 0) {
                FileDialog fd = new FileDialog(window, "Сформировать отчет", FileDialog.SAVE);
                JFileChooser fc = new JFileChooser();

                fd.setFile(fd.getName());
                fd.setMultipleMode(true);

                fd.setFilenameFilter(new FilenameFilter() {
                    public boolean accept(File dir, String name) {
                        return name.endsWith(".csv");
                    }
                });


                fd.setLocation(50, 50);
                fd.show();
                ArrayList<Integer> ids = new ArrayList<Integer>();

                fd.setFile(fd.getName());


                File f = new File(fd.getDirectory() + fd.getFile());

                FileWriter wrt = new FileWriter(f);

                String flash = "";
                //String min = "";
                if (!f.exists()) {
                    f.createNewFile();
                    System.out.println(f.getAbsolutePath());

                } else {
                    System.out.println(f.getAbsolutePath());
                }

                for (int i = 0; i < listComm.size(); i++) {
                    flash = listComm.get(i)[0] + ";" + listComm.get(i)[1] + ";" + listComm.get(i)[2] + ";\n";
                    wrt.append(flash);
                }

                wrt.flush();

            }
        } catch (SQLException e) {
            System.out.println("Ошибка номер " + e.getErrorCode());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }




        /*
        if (table.getRowCount() > 0) {
            FileDialog fd = new FileDialog(dialog, "Сформировать отчет", FileDialog.SAVE);
            JFileChooser fc = new JFileChooser();

            fd.setFile(fileName);
            fd.setMultipleMode(true);

            fd.setFilenameFilter(new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    return name.endsWith(".csv");
                }
            });


            fd.setLocation(50, 50);
            fd.show();
            ArrayList<Integer> ids = new ArrayList<Integer>();

            fd.setFile(fileName);

            if (fileName != null && fileName.trim().length() > 0) {

                File f = new File(fd.getDirectory() + fd.getFile());

                FileWriter wrt = new FileWriter(f);

                String flash = "";
                String min = "";
                if (!f.exists()) {
                    f.createNewFile();
                }
                int columCount = table.getModel().getColumnCount();
                int rowCount = table.getModel().getRowCount();
                for (int i = 0; i < columCount; i++) {
                    if (i != 0 && i != 3 && i != 5 && i != 11 && i != 12) {
                        flash += table.getColumnName(i) + ";";
                    }
                }
                flash += "\n";
                wrt.append(flash);
                for (int i = 0; i < rowCount; i++) {
                    flash = "";
                    Boolean ff = (Boolean) table.getValueAt(i, 3);
                    if (ff) {
                        ids.add((Integer) table.getValueAt(i, 0));
                        for (int j = 0; j < columCount; j++) {
                            if (j != 0 && j != 3 && j != 5 && j != 11 && j != 12) {

                                if (table.getModel().getValueAt(i, j) != null) {
                                    min = table.getModel().getValueAt(i, j).toString();
                                    if (min != null) {
                                        flash += min.replace("\n", ".") + ";";
                                    } else {
                                        flash += table.getModel().getValueAt(i, j) + ";";
                                    }
                                } else {
                                    flash += table.getModel().getValueAt(i, j) + ";";
                                }
                            }
                        }
                        flash += "\n";
                        wrt.append(flash);
                    }
                }
                wrt.flush();

                updateSubscribers(ids, mainwin.getConnectDB().getConnect(), table.getName());
                wrt.close();
                ids = null;
                dialog.dispose();

                closeWindow(dialog);
            } else {
                JOptionPane.showMessageDialog(dialog, "Не выбран фаил для сохранения отчета", "Предупреждение", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(dialog, "Данные не выбранн", "Предупреждение", JOptionPane.WARNING_MESSAGE);
        }*/
    }
}
