package ttkom.tt.windows.modal.card.funmodal;

import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 10.06.13
 * Time: 9:28
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbModalWindows {

    JDialog window = null;
    Dimension sizeWindow = null;
    JPanel mainPanel = null;

    public AbModalWindows(Window frame,String title,MigLayout layout) {
        setWindow(frame,title);
        setSizeWindow();
        setMainPanel(layout);

        getWindow().add(getMainPanel());

    }

    public abstract void setHeader();

    public JDialog getWindow() { return window; }
    public void setWindow(Window win,String title) { window = new JDialog(win); window.setTitle(title);}

    public Dimension getSizeWindow() { return sizeWindow; }
    public void setSizeWindow() { sizeWindow = Toolkit.getDefaultToolkit().getScreenSize(); }

    public JPanel getMainPanel() { return mainPanel; }
    public void setMainPanel(MigLayout layout) { mainPanel = new JPanel(layout);}

    public JButton setButton(Integer type,String text,String url) {
        JButton button = new JButton();
        switch (type) {
            case 1:
                if (text!=null && text.length()>0)
                    button.setText(text);
                break;
            case 2:
                if (url!=null && url.length()>0) button.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/"+url)));
                break;
            case 3:
                if (text!=null && text.length()>0) button.setText(text);
                if (url!=null && url.length()>0) button.setIcon(new ImageIcon(getClass().getClassLoader().getResource("ttkom/tt/img/"+url)));
                break;
        }
        return button;
    }

    public JTextField setTextField() {
        return new JTextField();
    }

    public JComboBox setComboBox(ArrayList<String[]> variants,String select) {
        ArrayList<String> frame = new ArrayList<String>();
        Integer numSelect = 0;
        JComboBox comboBox = null;
        frame.add("");
        for (int i =0;i<variants.size();i++) {
            if (variants.get(i)[0].equals(select)) numSelect = i + 1;
            frame.add((String) variants.get(i)[1]);
        }

        Object[] objects = new Object[frame.size()];

        for (int i = 0; i<frame.size();i++) objects[i] = frame.get(i);

        comboBox = new JComboBox(objects);
        comboBox.setSelectedIndex(numSelect);

        return comboBox;
    }
    public void logString(String old,String id_sub,String id_oper,Connection conn,Integer type) throws SQLException {
        SimpleDateFormat simDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String query = "INSERT INTO events (idAbon,idEvent,value,dtEvent,idOper) VALUES ("+id_sub+","+type+",'"+old+"','"+simDate.format(new Date())+"',"+id_oper+")";
        System.out.println(query);
        conn.createStatement().executeUpdate(query);
    }

    class CloseWindow implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            getWindow().dispose();
        }
    }

}
