package ttkom.tt;

import net.miginfocom.swing.MigLayout;
import ttkom.tt.dbase.mysql.ConnectDB;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 28.11.12
 * Time: 12:08
 * To change this template use File | Settings | File Templates.
 */
public class AddTariff {
    JPanel mainPanel, formPanel, buttonPanel, infoPanel;
    JTextField nameTariff, costTariff;
    JTextArea descrTariff;
    JButton saveButt, clearButt, closeButt;
    JDialog window, win;
    ClassSpiner spin;
    ConnectDB connectDB;
    ArrayList<JLabel[]> arrayList = new ArrayList<JLabel[]>();    // first elemnt is dtBegin, second element is dtEnd

    public AddTariff(Frame frame, ConnectDB connect) {
        MigLayout migMainPanel, migFormPanel, migButtPanel, migInfoPanel;
        JScrollPane scrollPane, textPane = null;
        Date date;
        JLabel dtBegin, dtEnd;
        connectDB = connect;

        window = new JDialog(frame);
        window.setTitle("Добавление траифного плана");

        migMainPanel = new MigLayout("wrap 1", "grow, fill");
        migFormPanel = new MigLayout("wrap 2", "[50]5[300]", "[grow, fill][grow,fill][grow,fill]");
        migButtPanel = new MigLayout("wrap 2", "grow,fill");
        migInfoPanel = new MigLayout("wrap 3", "grow,fill");

        mainPanel = new JPanel(migMainPanel);
        formPanel = new JPanel(migFormPanel);
        buttonPanel = new JPanel(migButtPanel);
        infoPanel = new JPanel(migInfoPanel);

        nameTariff = new JTextField();
        costTariff = new JTextField();
        descrTariff = new JTextArea();


        saveButt = new JButton("Сохранить");
        saveButt.addActionListener(new ActionPeriod());
        saveButt.setActionCommand("save");
        clearButt = new JButton("Очистить форму");
        clearButt.addActionListener(new ActionPeriod());
        clearButt.setActionCommand("clear");
        closeButt = new JButton("Закрыть");
        closeButt.addActionListener(new ActionPeriod());
        closeButt.setActionCommand("close");


        date = new Date();
        scrollPane = new JScrollPane(mainPanel);
        scrollPane.setMaximumSize(new Dimension(450, 400));
        scrollPane.setOpaque(true);

        textPane = new JScrollPane(descrTariff);
        textPane.setMaximumSize(new Dimension(300, 80));
        textPane.setMinimumSize(new Dimension(300, 70));


        mainPanel.add(formPanel);
        mainPanel.add(buttonPanel);

        formPanel.add(new JLabel("Название тарифа"), "grow");
        formPanel.add(nameTariff, "grow,wrap");
        formPanel.add(new JLabel("Описание тарифа"), "grow");
        formPanel.add(textPane, "grow,wrap");
        formPanel.add(new JLabel("Стоимость тарифа"), "grow");
        formPanel.add(costTariff, "grow,wrap");
        formPanel.add(infoPanel, "span 2");


        //=====
        arrayList.add(new JLabel[]{new JLabel(date.getDate() + "-" + (date.getMonth() + 1) + "-" + (date.getYear() + 1900)), new JLabel("Not initialized")});

        arrayList.get(0)[0].setName("0");
        arrayList.get(0)[1].setName("1");

        arrayList.get(0)[0].addMouseListener(new EditPeriod());
        arrayList.get(0)[1].addMouseListener(new EditPeriod());

        JLabel dateLabel = new JLabel("Начало периода: ");
        JLabel dateLabelEnd = new JLabel("Конец периода: ");

        infoPanel.add(dateLabel);
        infoPanel.add(arrayList.get(0)[0], "wrap");
        arrayList.get(0)[0].setForeground(new Color(0, 153, 0));
        infoPanel.add(dateLabelEnd);
        infoPanel.add(arrayList.get(0)[1], "wrap");
        arrayList.get(0)[1].setForeground(new Color(0, 153, 0));

        JSeparator separator = new JSeparator();
        separator.setSize(380, 10);

        buttonPanel.add(saveButt, "w 190!");
        buttonPanel.add(clearButt, "w 190!");
        buttonPanel.add(closeButt, "w 190!");

        window.add(scrollPane);

        window.setSize(482, 350);
        window.setModal(true);
        window.setVisible(true);
        window.validate();
        window.repaint();
    }

    class ClassSpiner {
        private JSpinner spin;
        private SpinnerModel dateModel;

        ClassSpiner() {
            Calendar calendar = Calendar.getInstance();
            Date initDate = calendar.getTime();

            calendar.add(Calendar.YEAR, -7);
            Date earliestDate = calendar.getTime();

            calendar.add(Calendar.YEAR, +20);
            Date latestDate = calendar.getTime();

            dateModel = new SpinnerDateModel(initDate, earliestDate, latestDate, Calendar.YEAR);//ignored for user input2

            spin = new JSpinner();
            spin.setModel(dateModel);
            spin.setPreferredSize(new Dimension(220, 25));
            spin.setMaximumSize(new Dimension(220, 25));

            spin.setEditor(new JSpinner.DateEditor(spin, "dd/MM/yyyy"));
        }

        JSpinner getSpiner() {
            return spin;
        }
    }

    class ActionPeriod implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy");
            if (e.getActionCommand().equals("save")) {
                String nTariff = null, dTariff = null, cTariff = null;
                if (nameTariff.getText().trim().length() > 0) {
                    nTariff = nameTariff.getText();
                } else {
                    JOptionPane.showMessageDialog(window, "Введите название тарифа", "Ошибка", JOptionPane.WARNING_MESSAGE);
                    nameTariff.requestFocus();
                }

                if (costTariff.getText().trim().length() > 0) {
                    cTariff = costTariff.getText().trim();
                } else {
                    JOptionPane.showMessageDialog(window, "Введите стоимость тарифа для периода", "Ошибка", JOptionPane.WARNING_MESSAGE);
                    costTariff.requestFocus();
                }

                if (nTariff != null && cTariff != null) {
                    if (nTariff.length() > 0 && cTariff.length() > 0) {
                        String query = "Insert into tariff (name,descr) Values ('" + nameTariff.getText() + "','" + descrTariff.getText() + "')";
                        try {
                            Statement statement = connectDB.getConnect().createStatement();
                            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
                            ResultSet resultSet = statement.getGeneratedKeys();
                            if (resultSet.next()) {
                                Integer indexTariff = resultSet.getInt(1);
                                String dtBeg = null;
                                String dtEnd = null;
                                Date date = null;
                                if (arrayList.get(0)[0].getText().trim().length() > 0) {
                                    date = formatDate.parse(arrayList.get(0)[0].getText().trim());
                                    dtBeg = (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                                }
                                if (arrayList.get(0)[1].getText().trim().equals("Not initialized")) {
                                    date = formatDate.parse(arrayList.get(0)[0].getText().trim());
                                    dtEnd = (date.getYear() + 1920) + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                                } else if (arrayList.get(0)[1].getText().trim().length() > 0) {
                                    date = formatDate.parse(arrayList.get(0)[0].getText().trim());
                                    dtEnd = (date.getYear() + 1900) + "-" + (date.getMonth() + 1) + "-" + date.getDate();
                                }

                                if (Integer.parseInt(cTariff) > 0 && dtBeg.length() > 0 && dtEnd.length() > 0) {
                                    query = "INSERT INTO ext_tariff(idtarif,sum,datebegin,dateend) " +
                                            "VALUES (" + indexTariff + ",'" + cTariff + "','" + dtBeg + "','" + dtEnd + "')";
                                    System.out.println(query);
                                    statement.executeUpdate(query);
                                }
                            }
                        } catch (SQLException e1) {
                            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (ParseException e1) {
                            e1.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                        } catch (NumberFormatException num) {
                            JOptionPane.showMessageDialog(window, "Введите стоимость тарифа для периода", "Ошибка", JOptionPane.WARNING_MESSAGE);
                            costTariff.requestFocus();
                            costTariff.setText("");
                        }
                    }
                }
            } else if (e.getActionCommand().equals("clear")) {
                nameTariff.setText("");
                costTariff.setText("");
                descrTariff.setText("");
                Date dt = new Date();
                arrayList.get(0)[0].setText(dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + (dt.getYear() + 1900));
                arrayList.get(0)[1].setText("Not initialized");
            } else if (e.getActionCommand().equals("close")) {
                window.dispose();
            }
        }
    }

    class ClearAll implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {

        }
    }

    class EditPeriod implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount() == 2) {
                editDate(e.getComponent().getName());
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

    void editDate(String title) {
        win = new JDialog(window, title);
        MigLayout layer = new MigLayout("wrap 1");
        JPanel mainpanel = new JPanel(layer);
        spin = new ClassSpiner();
        JButton save = new JButton("save");
        save.setActionCommand(title);
        save.addActionListener(new SaveDate());
        mainpanel.add(spin.getSpiner());
        mainpanel.add(save);
        win.add(mainpanel);
        win.setSize(150, 100);
        win.setModal(true);
        win.setVisible(true);
        win.validate();
        win.repaint();
    }

    class SaveDate implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            Date dt = (Date) spin.getSpiner().getValue();
            String date = dt.getDate() + "-" + (dt.getMonth() + 1) + "-" + (dt.getYear() + 1900);
            setSpin(date, e.getActionCommand());
            win.dispose();
            window.validate();
            window.repaint();
            window.validate();
        }
    }

    void setSpin(String date, String type) {
        if (type.equals("0")) {
            arrayList.get(0)[0].setText(date);
        } else if (type.equals("1")) {
            arrayList.get(0)[1].setText(date);
        }
    }
}
