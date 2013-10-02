package ttkom.tt.windows.modal.card.search;
import net.miginfocom.swing.MigLayout;
import ttkom.tt.dbase.mysql.ConnectDB;
import ttkom.tt.windows.MainWindow;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 25.04.13
 * Time: 11:27
 * Абстрактный класс для создания окна поиска абонента
 */
public abstract class AbSearch implements ISearch {

    private Dimension screenSize;
    private TableModel model;
    private JTable table;
    private JScrollPane scTab;
    private ConnectDB connectDB;
    private MainWindow mainWindow;
    private JTextField searchField;
    private JComboBox searchType;
    private JDialog dialog;


    AbSearch(JFrame frame, ConnectDB connect, MainWindow mainMenu) {
        setConnectDB(connect);
        setMainWindow(mainMenu);
        setScreenSize();
        dialog = new JDialog(frame);

        MigLayout mLayer = new MigLayout("wrap 1", "grow,fill");
        MigLayout tLayer = new MigLayout("wrap 1", "grow,fill");
        MigLayout fLayer = new MigLayout("wrap 2", "[100]5[grow,fill]", "[]5[]");

        JPanel mainPanel = new JPanel(mLayer);
        JPanel formPanel = new JPanel(fLayer);
        JPanel tablePanel = new JPanel(tLayer);

        mainPanel.add(tablePanel);
        mainPanel.add(formPanel);

        setModel(new Object[]{"№","Dog","FIO","street","houes","flat","commutator","ip addres","port"});
        setTable();
        setScTab(getTable(),1120, 450);
        tablePanel.add(getScTab());

        searchType = new JComboBox(new String[]{"По договору","По адресу","По имени","По IP адресу"});
        searchField = new JTextField();

        formPanel.add(searchType);
        formPanel.add(searchField);

        dialog.add(mainPanel);


    }



    @Override
    public void setTable() {

        table = new JTable(getModel());
        TableRowSorter<TableModel> sorter = new TableRowSorter(model) {

            @Override
            public Comparator<?> getComparator(int column) {
                // для нулевой строки
                if (column == 1) {
                    return new Comparator<String>() {
                        @Override
                        public int compare(String s1, String s2) {
                            return Integer.parseInt(s1) - Integer.parseInt(s2);
                        }
                    };
                }
                // для всех остальных
                return super.getComparator(column);
            }
        };
        sorter.setSortKeys(Arrays.asList(new RowSorter.SortKey(1, SortOrder.ASCENDING)));
        table.setDefaultRenderer(Object.class, new ColorTableCellRenderer(new Color(253, 253, 253),new Color(200, 200, 255),new Color(219, 68, 68)));
        table.setAutoResizeMode(0);
        table.setRowSorter(sorter);
        sorter.setSortable(1, true);
    }

    public JTable getTable() {  return table;  }


    protected void setScTab(JTable table,Integer width,Integer height) {
        scTab  = new JScrollPane(table);
        scTab.setMinimumSize(new Dimension(width, height));
    }
    protected JScrollPane getScTab() { return scTab; }


    @Override
    public JTextField setSearchForm() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public JScrollPane setScrollTable(JTable table) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    protected void setScreenSize() { screenSize = Toolkit.getDefaultToolkit().getScreenSize(); }
    protected Dimension getScreenSize() { return screenSize; }

    protected void setModel(Object[] objects) { model = new TableModel(new Object[][]{},objects); }
    protected TableModel getModel() { return model; }

    public class TableModel extends DefaultTableModel {
        public TableModel(Object rowData[][], Object columnNames[]) { super(rowData, columnNames); }
        public boolean isCellEditable(int row, int col) { return false; }
    }

    class ColorTableCellRenderer extends JLabel implements TableCellRenderer {
        private Color activ,noactiv,select;
        public ColorTableCellRenderer(Color activ,Color noactiv,Color select) {
            setOpaque(true);
            this.activ = activ;
            this.noactiv = noactiv;
            this.select = select;
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JPanel pan = new JPanel(new BorderLayout());

            JLabel lbl = new JLabel((String) value);

            if (table.getSelectedRow() == row)
                pan.setBackground(select);
            else {
                if ((row % 2) == 0)
                    pan.setBackground(activ);
                else
                    pan.setBackground(noactiv);
            }

            pan.add(lbl, BorderLayout.CENTER);

            return pan;
        }
    }

    protected void setConnectDB(ConnectDB connectDB) { this.connectDB = connectDB; }
    protected ConnectDB getConnectDB() { return this.connectDB;     }

    protected void setMainWindow(MainWindow mainWindow) { this.mainWindow = mainWindow; }
    protected MainWindow getMainWindow() { return this.mainWindow; }

    //protected void setSearchField() { searchField = new JTextField(); }
    protected JTextField getSearchField() { return searchField; }

    //protected void setSearchType(String[] obj) { searchType = new JComboBox(obj); }
    protected JComboBox getSearchType(){ return  searchType; }

    protected JDialog getDialog() { return dialog; }

    protected abstract void activateDialog(Dialog dialog);
    protected abstract void searchUser();
    protected abstract Integer getIdtype();
    protected void clearTable() {  while (model.getRowCount()>0) model.removeRow(0);   }

    protected String getStreet(String arg) {
        if (arg != null && arg.length()>0) {
            String[] splitString = arg.split(" ");
            if (splitString.length>0) { return splitString[0].trim(); }
        }
        return "";
    }
    protected String getHouse(String arg) {
        if (arg != null && arg.length()>0) {
            String[] splitString = arg.split(" ");
            if (splitString.length>1) { return  splitString[1].trim(); }
        }
        return "";
    }
    protected String getFlat(String arg) {
        if (arg != null && arg.length()>0) {
            String[] splitString = arg.split(" ");
            if (splitString.length>2) { return  splitString[2].trim(); }
        }
        return "";
    }


}
