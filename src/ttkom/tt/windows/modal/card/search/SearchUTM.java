package ttkom.tt.windows.modal.card.search;

import ttkom.tt.dbase.mysql.ConnectDB;
import ttkom.tt.userinfo.UserInfoKabTV;
import ttkom.tt.userinfo.UserInfoUTM;
import ttkom.tt.windows.MainWindow;
import ttkom.tt.windows.modal.card.subscriber.AbCardSubscriber;
import ttkom.tt.windows.modal.card.subscriber.CardSubscriberTL;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created with IntelliJ IDEA.
 * User: g1yyk
 * Date: 25.04.13
 * Time: 15:22
 * To change this template use File | Settings | File Templates.
 */
public class SearchUTM extends AbSearch {
    private ConnectDB connectLocal;
    public SearchUTM(JFrame frame, ConnectDB connect, MainWindow mainMenu) {
        super(frame, connect, mainMenu);
        getSearchField().addKeyListener(new SearchUser());
        setSizeColum();
        getTable().addMouseListener(new ClickTable(frame));
        activateDialog(getDialog());

    }

    @Override
    protected void activateDialog(Dialog dialog) {
        dialog.setLocation(getScreenSize().width / 2 - 550, getScreenSize().height / 2 - 250);
        dialog.setSize(1150, 550);
        dialog.setModal(true);
        dialog.setVisible(true);
        dialog.validate();
        dialog.repaint();
    }

    @Override
    protected void searchUser() {
        String query = getSQLForSearch(getSearchField().getText().trim(),getIdtype());
        clearTable();
        try {
            ResultSet result = getConnectDB().getConnect().createStatement().executeQuery(query);
            while (result.next()) {
                String commutator = "";
                String ipaddres   = "";


                if (result.getString("idComm")!=null) {
                    query = "SELECT name AS commutator, ipaddres FROM commutator WHERE id ="+result.getString("idComm");
                    ResultSet resComm = getConnectDB().getConnect().createStatement().executeQuery(query);
                    if (resComm.next()) {
                        commutator = resComm.getString("commutator");
                        ipaddres   = resComm.getString("ipaddres");
                    }
                }
               getModel().addRow(new Object[]{
                       result.getString("id"),
                       result.getString("idDog"),
                       result.getString("fio"),
                       result.getString("street"),
                       result.getString("house"),
                       result.getString("flat"),
                       commutator,
                       ipaddres,
                       result.getString("port")
               });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected Integer getIdtype() {
        return getSearchType().getSelectedIndex();  //To change body of implemented methods use File | Settings | File Templates.
    }

    class SearchUser implements KeyListener {
        @Override
        public void keyTyped(KeyEvent e) { }
        @Override
        public void keyPressed(KeyEvent e) { }
        @Override
        public void keyReleased(KeyEvent e) { searchUser(); }
    }

    private void setConnectLocal()      { connectLocal = new ConnectDB("utm", "10.254.252.17", "admin", "admin"); }
    private ConnectDB getConnectLocal() { return connectLocal; }

    private String getSQLForSearch(String arg,Integer type) {

        String query = "SELECT abn.id, abn.idDog, abn.name AS fio, hs.house, str.name AS street, abn.flat, abn.port, abn.idComm " +
                        "FROM abonents abn,  streets str, houses hs " +
                        "WHERE hs.id = abn.idAdres AND hs.id_street = str.id ";//AND abn.id_service=2 ";

        switch (type) {
            case 0: //По договору
                if (arg.trim().length()>0)
                    query += "AND abn.idDog LIKE '"+arg.replaceAll(" ","")+"%'";
                else
                    query += " LIMIT 30";
                break;
            case 1: //По адресу
                String flat,house,street;
                if ((street=getStreet(arg.trim())).length()>0)
                    query += "AND str.name LIKE '"+street+"%' ";
                if ((house=getHouse(arg.trim())).length()>0)
                    query += "AND hs.house LIKE '"+house+"%' ";
                if ((flat = getFlat(arg.trim())).length()>0)
                    query += "AND abn.flat LIKE '"+flat+"%'";
                break;
            case 2:   //По фио
                query += "AND abn.name LIKE '"+arg.trim()+"%'";
                break;
            case 3: // По ip адресу
                query = "SELECT abn.id, abn.idDog, abn.name AS fio, hs.house, str.name AS street, abn.flat, abn.port, abn.idComm " +
                        "FROM abonents abn,  streets str, houses hs, commutator com " +
                        "WHERE hs.id = abn.idAdres AND hs.id_street = str.id AND abn.idComm = com.id ";//AND abn.id_service=2 ";
                query += "AND com.ipaddres LIKE '"+arg.trim().replaceAll(",",".").replaceAll(" ",".")+"%'";
                break;

        }
        return query;
    }

    private void setSizeColum() {
        getTable().getColumnModel().getColumn(2).setMinWidth(250);
        getTable().getColumnModel().getColumn(3).setMinWidth(150);
        getTable().getColumnModel().getColumn(6).setMinWidth(150);
    }

    class ClickTable implements MouseListener {

        JFrame frame;

        ClickTable(JFrame frame) {
            this.frame = frame;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getClickCount()==2) {

                Point point = e.getPoint();
                int row = getTable().rowAtPoint(point);
                String str = (String) getTable().getValueAt(row, 0);

                try { AbCardSubscriber cardSubscriber = new CardSubscriberTL(frame,getMainWindow(),str); }
                catch (SQLException e1) { e1.printStackTrace(); }
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

}
