package ttkom.tt.menu.context;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 16.01.13
 * Time: 9:46
 * To change this template use File | Settings | File Templates.
 */
public class TableMenu {

    public TableMenu(int row, int colum, int x, int y, JTable table) {
        String ipAddres = (String) table.getValueAt(row, 8);
        JPopupMenu pmenu = new JPopupMenu();
        JMenuItem edit = new JMenuItem("Копировать");
        edit.addActionListener(new CopyIPAddres(ipAddres));
        table.setRowSelectionInterval(row, row);
        pmenu.add(edit);
        pmenu.show(table, x, y);
    }

    class CopyIPAddres implements ActionListener {
        String str;

        CopyIPAddres(String str) {
            if (str.trim().length() > 0) {
                this.str = str;
            } else {
                this.str = "Абонент не подключен";
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            StringSelection ss = new StringSelection(str);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(ss, null);

        }
    }

}
