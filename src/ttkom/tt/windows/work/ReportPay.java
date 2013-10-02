package ttkom.tt.windows.work;

import com.toedter.calendar.JDateChooser;
import net.miginfocom.swing.MigLayout;
import ttkom.tt.windows.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 06.02.13
 * Time: 9:40
 * To change this template use File | Settings | File Templates.
 */
public class ReportPay extends AbWorkWindow {
    private final JDialog dialog;
    private final JDateChooser toDate, endDate;
    private JLabel itogInfo;
    private String summa;

    public ReportPay(final MainWindow mainWindow) {


        final JPanel mainPanel;//,buttonPanel,reportPanel;
        final String title = "Отчеты по платежам";
        final JButton save, createReport;//, clear, select;

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        mainPanel = new JPanel(new MigLayout("wrap 7", "[]5[]5[]5[]5[]5[]5[]5", "al top"));
        dialog = new JDialog(mainWindow.getFrame(), title);
        dialog.setDefaultCloseOperation(dialog.DISPOSE_ON_CLOSE);
        toDate = getDate();
        toDate.setDate(new Date());
        endDate = getDate();
        endDate.setDate(new Date());
        itogInfo = new JLabel();
        final JTable report = createTable(setHeader(), getBody(mainWindow, 0));
        getItogInfo(report);
        final JScrollPane scReport = createScroll(report);

        save = setButton("Сохранить", null, new AcSaveRows(report));
        save.setToolTipText("Сохранить отчет в файл");

        createReport = setButton("Создать", null, new AcGenerateReport(report, mainWindow));
        createReport.setToolTipText("Сгенерировать отчет по выбранным датам");

        mainPanel.add(save);
        mainPanel.add(new JLabel("от"));
        mainPanel.add(toDate, "w 145!");
        mainPanel.add(new JLabel("до"));
        mainPanel.add(endDate, "w 145!");
        mainPanel.add(createReport, "wrap");

        scReport.setPreferredSize(new Dimension(screenSize.width - 114, screenSize.height - 110));

        mainPanel.add(scReport, "grow,span 7");
        mainPanel.add(itogInfo);

        dialog.add(mainPanel);
        dialog.setSize(screenSize.width - 100, screenSize.height - 100);
        dialog.setLocation(screenSize.width / 2 - dialog.getSize().width / 2, screenSize.height / 2 - dialog.getSize().height / 2);
        dialog.setModal(true);
        dialog.setVisible(true);
        dialog.validate();
        dialog.repaint();
    }

    class AcSaveRows implements ActionListener {
        final JTable table;
        AcSaveRows(JTable table) {
            this.table = table;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                saveRow(table);
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }

    class AcGenerateReport implements ActionListener {
        final JTable table;
        final MainWindow mwin;

        AcGenerateReport(JTable table, MainWindow mwin) {
            this.table = table;
            this.mwin = mwin;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            addTable(getBody(mwin, 1), table);
            getItogInfo(table);
        }
    }

    @Override
    public void selectRow() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void unselectRow() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void createReport() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String[] setHeader() {
        return new String[]{"дог", "ФИО", "сумма","оператор","дата", "баланс", "улица", "дом", "кв"};
    }

    @Override
    public Object[][] getBody(MainWindow mainWindow, Integer arg) {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        final SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd 08:00:00");

        ArrayList<Object[]> arrayList = new ArrayList<Object[]>();

        String query = "";

        if (arg == 0) {
            query =
                    "SELECT abn.idDog,abn.name as fio,abn.balans,pay.date as paydate,pay.cost,str.name as street,hs.house,abn.flat,op.name as operator " +
                            "FROM abonents abn,payment pay,houses hs,streets str,operator op " +
                            "WHERE pay.idOperator=op.id AND pay.idAbonent = abn.id AND abn.idAdres = hs.id AND hs.id_street = str.id AND pay.date>'" + format2.format(toDate.getDate()) + "' AND pay.date<'" + format.format(endDate.getDate()) + "' ORDER BY pay.date";
        } else if (arg == 1) {
            query =
                    "SELECT abn.idDog,abn.name as fio,abn.balans,pay.date as paydate,pay.cost,str.name as street,hs.house,abn.flat,op.name as operator " +
                            "FROM abonents abn,payment pay,houses hs,streets str,operator op " +
                            "WHERE pay.idOperator=op.id AND pay.idAbonent = abn.id AND abn.idAdres = hs.id AND hs.id_street = str.id AND pay.date>'" + format.format(toDate.getDate()) + "' AND pay.date<'" + format.format(endDate.getDate()) + "' ORDER BY pay.date";
        }

        try {


            ResultSet resultSet = mainWindow.getConnectDB().getConnect().createStatement().executeQuery(query);
            while (resultSet.next()) {

                arrayList.add(new Object[]{
                        new Integer(resultSet.getString("idDog")),
                        new String(resultSet.getString("fio")),
                        new Integer(resultSet.getInt("cost")),
                        new String(resultSet.getString("operator")),
                        new String(checkDate(resultSet.getString("paydate"))),
                        resultSet.getString("balans"),
                        new String(resultSet.getString("street")),
                        new String(resultSet.getString("house")),
                        new String(resultSet.getString("flat"))
                });
            }
            Object[][] objects = new Object[arrayList.size()][];
            for (int i = 0; i < arrayList.size(); i++) {
                objects[i] = arrayList.get(i);
            }
            return objects;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    private void getItogInfo(JTable table) {
        summa = String.valueOf(getItogCheck(table));
        itogInfo.setText("Всего платежей: " + table.getRowCount() + " Итого: " + summa);
    }

    private Integer getItogCheck(JTable table) {
        Integer summ = 0;
        int jj = 0;
        for (int i = 0; i < table.getRowCount(); i++,jj++) {
            summ += Integer.valueOf((Integer) table.getValueAt(i, 2));
        }
        return summ;
    }

    private void saveRow(JTable table) throws IOException {

        String fileName = "";
        String sufix = ".csv";

        SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        String strDate = formatDate.format(date);

        fileName = strDate + "-Платежи-" + date.getTime() / 1000 + sufix;


        if (table.getRowCount() > 0) {
            final FileDialog fd = new FileDialog(dialog, "Сформировать отчет", FileDialog.SAVE);
            final JFileChooser fc = new JFileChooser();

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
                final File f = new File(fd.getDirectory() + fd.getFile());
                final FileWriter wrt = new FileWriter(f);
                String flash = "";
                String min = "";
                if (!f.exists()) {
                    f.createNewFile();
                }

                flash +=";Платежи за: "+(toDate.getDate().getDate()+1)+"-"+(toDate.getDate().getMonth()+1)+"-"+(toDate.getDate().getYear()+1900)+" -- "+
                        (endDate.getDate().getDate()+1)+"-"+(endDate.getDate().getMonth()+1)+"-"+(endDate.getDate().getYear()+1900)+";\n";
                int columCount = table.getModel().getColumnCount();
                int rowCount = table.getModel().getRowCount();
                for (int i = 0; i < columCount; i++) {
                    flash += table.getColumnName(i) + ";";
                }

                flash += "\n";
                wrt.append(flash);
                for (int i = 0; i < rowCount; i++) {
                    flash = "";
                    ids.add((Integer) table.getValueAt(i, 0));

                    for (int j = 0; j < columCount; j++) {
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
                    flash += "\n";
                    wrt.append(flash);
                }
                flash = ";Итого:;"+summa+";";
                wrt.append(flash);
                wrt.flush();
                wrt.close();
            } else {
                JOptionPane.showMessageDialog(dialog, "Не выбран фаил для сохранения отчета", "Предупреждение", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(dialog, "Данные не выбранн", "Предупреждение", JOptionPane.WARNING_MESSAGE);
        }
    }
}