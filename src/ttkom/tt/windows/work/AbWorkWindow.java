package ttkom.tt.windows.work;

import com.toedter.calendar.JDateChooser;
import ttkom.tt.windows.MainWindow;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 18.01.13
 * Time: 14:12
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbWorkWindow {

    public JButton setButton(String title, String namePNG, ActionListener actionListener) {
        JButton button = new JButton(title);
        if (namePNG != null)
            button.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/" + namePNG + ".png")));
        button.addActionListener(actionListener);
        return button;
    }

    private class MyTableModel extends DefaultTableModel {
        public MyTableModel(Object rowData[][], Object columnNames[]) {
            super(rowData, columnNames);
        }

        public boolean isCellEditable(int row, int col) {
            if (getColumnClass(col).getName().equals("java.lang.Boolean")) {
                return true;
            } else if (getColumnClass(col).getName().equals("javax.swing.JButton")) {
                return true;
            }else {
                return false;
            }
        }

        public Class getColumnClass(int c) {
            if (getValueAt(0, c) != null) {
                return getValueAt(0, c).getClass();
            } else {
                return new String().getClass();
            }
        }

    }

    public JTable createTable(String[] header, Object[][] rowData) {
        MyTableModel model = new MyTableModel(rowData, header);
        JTable table = new JTable(model);
        //System.out.println("Model of table1 " + table.getModel().getClass().getCanonicalName());
        table.setDefaultRenderer(Object.class, new CheckBoxHeader());

        return table;
    }

    public JTable createTable(String[] header, Object[][] rowData,Integer ar) {
        ActionPanelEditorRenderer er = new ActionPanelEditorRenderer();
        MyTableModel model = new MyTableModel(rowData, header);
        JTable table = new JTable(model);

        //System.out.println("Model of table1 " + table.getModel().getClass().getCanonicalName());
        table.setDefaultRenderer(Object.class, new CheckBoxHeader());
        TableColumn column = table.getColumnModel().getColumn(3);
        column.setCellRenderer(er);
        column.setCellEditor(er);
        return table;
    }

    public JScrollPane createScroll(JTable table) {
        return new JScrollPane(table, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS, ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
    }


    public void closeWindow(Window win) {
        win.dispose();
    }

    public abstract void selectRow();

    public abstract void unselectRow();

    public abstract void createReport();

    public abstract String[] setHeader();

    public abstract Object[][] getBody(MainWindow mainWindow, Integer arg);

    class CheckBoxHeader extends JCheckBox implements TableCellRenderer {
        protected JCheckBox checkBox;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel pan = new JPanel(new BorderLayout());
            JLabel lbl = new JLabel(String.valueOf(value));

            if (column == 6) {
                try {
                    if (getCountDays(String.valueOf(value))) {
                        if (table.getSelectedRow() == row) {
                            pan.setBackground(new Color(255, 69, 0));
                        } else {
                            if ((row % 2) == 0) {
                                pan.setBackground(new Color(233, 150, 122));
                            } else {
                                pan.setBackground(new Color(255, 160, 122));
                            }
                        }
                    } else {
                        if (table.getSelectedRow() == row) {
                            pan.setBackground(new Color(253, 255, 225));
                        } else {
                            if ((row % 2) == 0) {
                                pan.setBackground(new Color(200, 200, 255));
                            } else {
                                pan.setBackground(new Color(253, 253, 253));
                            }
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

            } else {
                if (table.getSelectedRow() == row) {
                    pan.setBackground(new Color(253, 255, 225));
                } else {
                    if ((row % 2) == 0) {
                        pan.setBackground(new Color(200, 200, 255));
                    } else {
                        pan.setBackground(new Color(253, 253, 253));
                    }
                }
            }

            if (column == 3) {
                pan.add(lbl,BorderLayout.CENTER);
            } else {
                pan.add(lbl, BorderLayout.CENTER);
            }

            return pan;
        }
    }

    public Boolean getCountDays(String arg) throws ParseException {

        if (arg != null && arg.length() > 18) {
            SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH");
            String now = formater.format(new Date());
            Date dtNow = formater.parse(now);
            Date dtArg = formater.parse(arg.replace(".0", ""));

            long res = (dtNow.getTime() / 1000 - dtArg.getTime() / 1000) / 84200;
            if (res <= 2) {
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public JDateChooser getDate() {
        JDateChooser calendar = new JDateChooser("yyyy-MM-dd HH:mm:ss", "####-##-## ##:##:##", '0');
        return calendar;
    }

    public String checkDate(String args) {
        if (args != null && args.trim().length() > 0) {
            return args;
        } else {
            return "";
        }
    }

    public void addTable(Object[][] objects, JTable table) {
        System.out.println("\n\n\n" + objects[0][0]);
        System.out.println("Model of table2 " + table.getModel().getClass().getCanonicalName() + "---" + table.getModel().getRowCount() + " == " + objects.length);
        System.out.println("Обновляем таблицу");

        while (table.getRowCount() > 0) ((MyTableModel) table.getModel()).removeRow(0);

        for (int i = 0; i < objects.length; i++) {
            System.out.println(objects[i][1]);
            ((MyTableModel) table.getModel()).insertRow(table.getModel().getRowCount(), objects[i]);
        }
        table.revalidate();
    }

    class MainCloseOper implements WindowListener {
        AbWorkWindow obj;
        Window dialog;

        MainCloseOper(AbWorkWindow object, Window dialog) {
            obj = object;
            this.dialog = dialog;
        }

        @Override
        public void windowOpened(WindowEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void windowClosing(WindowEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void windowClosed(WindowEvent e) {
            dialog.dispose();
            obj = null;
        }

        @Override
        public void windowIconified(WindowEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void windowDeiconified(WindowEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void windowActivated(WindowEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void windowDeactivated(WindowEvent e) {
            //To change body of implemented methods use File | Settings | File Templates.
        }
    }

    class ActionPanelEditorRenderer extends AbstractCellEditor implements TableCellRenderer, TableCellEditor {

        JPanel panel1 = new JPanel();
        JPanel panel2 = new JPanel();
        public ActionPanelEditorRenderer() {
            super();
            JButton viewButton2 = new JButton(new AbstractAction("view2") {
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "Viewing");
                }
            });
            JButton editButton2 = new JButton(new AbstractAction("edit2") {;
                @Override
                public void actionPerformed(ActionEvent e) {
                    JOptionPane.showMessageDialog(null, "Editing");
                }
            });

            //panel1.setOpaque(true);
            panel1.add(viewButton2);
            //panel1.add(new JButton("edit1"));
           // panel2.setOpaque(true);
            //panel2.add(viewButton2);
            //panel2.add(editButton2);
        }


        @Override
        public Object getCellEditorValue() {
            return null;  //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            panel1.setBackground(isSelected?table.getSelectionBackground():table.getBackground());
            return null;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            panel1.setBackground(table.getSelectionBackground());
            return null;
        }
    }

}
